/*
 * Copyright (c) 2008, SQL Power Group Inc.
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

package ca.sqlpower.wabit.report;

import java.text.Format;
import java.util.Collections;
import java.util.List;

import ca.sqlpower.query.Item;
import ca.sqlpower.query.SQLObjectItem;
import ca.sqlpower.query.StringItem;
import ca.sqlpower.wabit.AbstractWabitObject;
import ca.sqlpower.wabit.WabitObject;

public class ColumnInfo extends AbstractWabitObject{
    
    /**
     * Defines if the column is a grouping, break
     * or neither.
     */
    public enum GroupAndBreak {
        GROUP,
        BREAK,
        NONE
    }

	/**
	 * The item this column information is describing.
	 */
    private Item columnInfoItem;

	/**
	 * Column width in Graphics2D units (screen pixels or 1/72 of an inch when printed).
	 */
	private static final int DEFAULT_COL_WIDTH = 72;
	public static final String FORMAT_CHANGED = "format";
	public static final String DATATYPE_CHANGED = "dataType";
	public static final String HORIZONAL_ALIGNMENT_CHANGED = "horizontalAlignment";
	public static final String COLUMN_INFO_ITEM_CHANGED = "columnInfoItem";
	public static final String WIDTH_CHANGED = "width";
	private static final String WILL_GROUP_OR_BREAK_CHANGED = "willGroupOrBreak";
	private static final String WILL_SUBTOTAL_CHANGED = "willSubtotal";
	private static final String COLUMN_ALIAS = "columnAlias";
	
	private int width = DEFAULT_COL_WIDTH;

	private HorizontalAlignment hAlign = HorizontalAlignment.LEFT;

	private DataType dataType = null;

	private Format format = null;
	
	/**
	 * Defines if the column described by this information
	 * should have a break or group after every value change.
	 */
	private GroupAndBreak willGroupOrBreak = GroupAndBreak.NONE;
	
	/**
	 * defines if this column should be totaled before each new break. Only
	 * numeric columns should allow subtotals.
	 */
	private boolean willSubtotal = false;
	
	/**
	 * This is the alias of the column this information is for. If an Item
	 * does not exist for the column because the query has been user modified
	 * then the ColumnInfo will use the alias instead.
	 * Using the alias instead of the Item can result in lost column information
	 * if two or more columns have the same name. 
	 */
	private String columnAlias;
	
	public ColumnInfo(Item item, String label) {
		setColumnInfoItem(item);
		setName(label);
		
	}
	
	public ColumnInfo(String label) {
		setColumnAlias(label);
		setName(label);
	}
	
	public ColumnInfo(String alias, String label) {
		setColumnAlias(alias);
		setName(label);
	}
	
	public ColumnInfo(ColumnInfo columnInfo) {
		this.columnInfoItem = columnInfo.columnInfoItem.createCopy();
		this.columnAlias = columnInfo.columnAlias;
		this.dataType = columnInfo.dataType;
		this.hAlign = columnInfo.hAlign;
		this.width = columnInfo.width;
		this.willGroupOrBreak = columnInfo.willGroupOrBreak;
		this.willSubtotal = columnInfo.willSubtotal;
	}

	/**
	 * This value can be null. There is no Item defined for columns that 
	 * are generated from users modifying the SQL script manually.
	 */
	public Item getColumnInfoItem() {
		return columnInfoItem;
	}
	public void setColumnInfoItem(Item item) {
		firePropertyChange(COLUMN_INFO_ITEM_CHANGED, this.columnInfoItem, item);
		this.columnInfoItem = item;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		firePropertyChange(WIDTH_CHANGED, this.width, width);
		this.width = width;
	}
	public HorizontalAlignment getHorizontalAlignment() {
		return hAlign;
	}
	public void setHorizontalAlignment(HorizontalAlignment align) {
		firePropertyChange(HORIZONAL_ALIGNMENT_CHANGED, hAlign, align);
		hAlign = align;
	}
	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType type) {
		firePropertyChange(DATATYPE_CHANGED, this.dataType, type);
		dataType = type;
	}
	public Format getFormat() {
		return format;
	}
	public void setFormat(Format format) {
		firePropertyChange(FORMAT_CHANGED, this.format, format);
		this.format = format;
	}

	public boolean allowsChildren() {
		return false;
	}

	public int childPositionOffset(Class<? extends WabitObject> childType) {
		throw new UnsupportedOperationException("should not have Children");
	}

	public List<? extends WabitObject> getChildren() {
		return Collections.emptyList();
	}

	public GroupAndBreak getWillGroupOrBreak() {
		return willGroupOrBreak;
	}

	public void setWillGroupOrBreak(GroupAndBreak willGroupOrBreak) {
		firePropertyChange(WILL_GROUP_OR_BREAK_CHANGED, this.willGroupOrBreak, willGroupOrBreak);
		this.willGroupOrBreak = willGroupOrBreak;
	}

	public boolean getWillSubtotal() {
		return willSubtotal;
	}

	public void setWillSubtotal(boolean subtotal) {
		firePropertyChange(WILL_SUBTOTAL_CHANGED, this.willSubtotal, subtotal);
		this.willSubtotal = subtotal;
	}

	public void setColumnAlias(String columnAlias) {
		firePropertyChange(COLUMN_ALIAS, this.columnAlias, columnAlias);
		this.columnAlias = columnAlias;
	}

	public String getColumnAlias() {
		return columnAlias;
	}

    public List<WabitObject> getDependencies() {
        return Collections.emptyList();
    }

}
