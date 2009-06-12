/*
 * Copyright (c) 2009, SQL Power Group Inc.
 *
 * This file is part of Wabit.
 *
 * Wabit is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Wabit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */

package ca.sqlpower.wabit.olap;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Member;
import org.olap4j.query.Query;
import org.olap4j.query.QueryAxis;
import org.olap4j.query.QueryDimension;
import org.olap4j.query.Selection;

/**
 * This is a class full of static helper methods to assist with the execution of
 * olap queries
 * 
 * @author thomas
 * 
 */
public class OlapUtils {
    
    private OlapUtils() {
        //Do nothing
    }
    
    /**
     * Tests whether or not the given parent member has the other member as one
     * of its descendants--either a direct child, or a child of a child, and so
     * on. Does not consider parent to be a descendant of itself, so in the case
     * both arguments are equal, this method returns false.
     * 
     * @param parent
     *            The parent member
     * @param testForDescendituitivitiness
     *            The member to check if it has parent as an ancestor
     */
    public static boolean isDescendant(Member parent, Member testForDescendituitivitiness) {
        if (testForDescendituitivitiness.equals(parent)) return false;
        while (testForDescendituitivitiness != null) {
            if (testForDescendituitivitiness.equals(parent)) return true;
            testForDescendituitivitiness = testForDescendituitivitiness.getParentMember();
        }
        return false;
    }
    
    
    /**
     * This method returns a deep copy of an MDX Query because there is no such
     * method in the API.
     * 
     * @param query
     *            This is the {@link Query} that is being copied
     * @return This returns the copied Query, it is a new copy that only shares
     *         {@link Dimension}s and {@link Member}
     *         
     * @throws SQLException
     */
    public static Query copyMDXQuery(Query query) throws SQLException {
        if (query == null) return null;
        Query modifiedMDXQuery = new Query(query.getName(), query.getCube());
        for (Map.Entry<Axis, QueryAxis> axisEntry : query.getAxes().entrySet()) {
            QueryAxis copiedAxis = new QueryAxis(modifiedMDXQuery, axisEntry.getValue().getLocation());
            for (QueryDimension oldDimension : axisEntry.getValue().getDimensions()) {
                QueryDimension copiedDimension = new QueryDimension(modifiedMDXQuery, oldDimension.getDimension());
                for (Selection selection : oldDimension.getSelections()) {
                    Selection copiedSelection = copiedDimension.createSelection(selection.getMember(), selection.getOperator());
                    copiedDimension.getSelections().add(copiedSelection);
                }
                copiedAxis.getDimensions().add(copiedDimension);
            }
            modifiedMDXQuery.getAxes().put(axisEntry.getKey(), copiedAxis);
        }
        return modifiedMDXQuery;
    }

    /**
     * This method will modify the {@link Query} object passed in to expand or
     * collapse the member passed in. To decide if the member should be expanded
     * or collapsed the displayed members will be checked to see if the member's
     * children are shown.
     */
    public static void expandOrCollapseMDX(Member member, Query queryToModify) {
        boolean isLeaf = true;
        QueryDimension containingDimension = null;
        member.getHierarchy();
        final QueryAxis colQueryAxis = queryToModify.getAxes().get(Axis.COLUMNS);
        if (!colQueryAxis.getDimensions().contains(member.getDimension())) {
            for (QueryDimension dim : colQueryAxis.getDimensions()) {
                for (Selection sel : dim.getSelections()) {
                    if (isDescendant(member, sel.getMember())) {
                        isLeaf = false;
                    }
                    if (sel.getMember().equals(member)) {
                        containingDimension = dim;
                    }
                }
            }
        }
        final QueryAxis rowQueryAxis = queryToModify.getAxes().get(Axis.ROWS);
        if (!rowQueryAxis.getDimensions().contains(member.getDimension())) {
            for (QueryDimension dim : rowQueryAxis.getDimensions()) {
                for (Selection sel : dim.getSelections()) {
                    if (isDescendant(member, sel.getMember())) {
                        isLeaf = false;
                    }
                    if (sel.getMember().equals(member)) {
                        containingDimension = dim;
                    }
                }
            }
        }
        try {
            if (isLeaf) {
                for (Member child : member.getChildMembers()) {
                    Selection newSelection = containingDimension.createSelection(child);
                    containingDimension.getSelections().add(newSelection);
                }
                Collections.sort(containingDimension.getSelections(), new Comparator<Selection>() {
                    private final MemberHierarchyComparator memberComparator = new MemberHierarchyComparator();
                    
                    public int compare(Selection o1, Selection o2) {
                        return memberComparator.compare(o1.getMember(), o2.getMember());
                    }
                });
            } else {
                List<Selection> selectionsToRemove = new ArrayList<Selection>();
                for (Selection sel : containingDimension.getSelections()) {
                    if (isDescendant(member, sel.getMember())) {
                        selectionsToRemove.add(sel);
                    }
                }
                containingDimension.getSelections().removeAll(selectionsToRemove);
            }
        } catch (OlapException e) {
            throw new RuntimeException(e);
        }
    }
}
