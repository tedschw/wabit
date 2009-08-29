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

import ca.sqlpower.wabit.AbstractWabitObject;
import ca.sqlpower.wabit.WabitObject;

public class ColumnInfo extends AbstractWabitObject{

	/**
	 * The columnInfoKey is used as the key for matching this columninfo to
	 * column alias in the query. This is distinct from the name (see
	 * {@link #getName()}), which should be rendered as this column's header.
	 */
    private String columnInfoKey;

	/**
	 * Column width in Graphics2D units (screen pixels or 1/72 of an inch when printed).
	 */
	private static final int DEFAULT_COL_WIDTH = 72;
	public static final String FORMAT_CHANGED = "format";
	public static final String DATATYPE_CHANGED = "dataType";
	public static final String HORIZONAL_ALIGNMENT_CHANGED = "horizontalAlignment";
	public static final String COLUMN_INFO_KEY_CHANGED = "columnInfoKey";
	public static final String WIDTH_CHANGED = "width";
	
	private int width = DEFAULT_COL_WIDTH;

	private HorizontalAlignment hAlign = HorizontalAlignment.LEFT;

	private DataType dataType = null;

	private Format format = null;

	public ColumnInfo(String label) {
		setColumnInfoKey(label);
		setName(label);
	}

	public String getColumnInfoKey() {
		return columnInfoKey;
	}
	public void setColumnInfoKey(String label) {
		firePropertyChange(COLUMN_INFO_KEY_CHANGED, this.columnInfoKey, label);
		this.columnInfoKey = label;
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
}
