/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.extension.persistence;

/**
 * @author higa
 * 
 */
public class ColumnMeta {

	private String name;

	private ColumnType type;

	private boolean primary;

	private boolean unique;

	private boolean nullable;

	private boolean selectable;

	private boolean insertable;

	private boolean updatable;

	private int length;

	private int precision;

	private int scale;

	/**
	 * @return Returns the insertable.
	 */
	public boolean isInsertable() {
		return insertable;
	}

	/**
	 * @param insertable
	 *            The insertable to set.
	 */
	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}

	/**
	 * @return Returns the length.
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length
	 *            The length to set.
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the nullable.
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * @param nullable
	 *            The nullable to set.
	 */
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * @return Returns the precision.
	 */
	public int getPrecision() {
		return precision;
	}

	/**
	 * @param precision
	 *            The precision to set.
	 */
	public void setPrecision(int precision) {
		this.precision = precision;
	}

	/**
	 * @return Returns the primary.
	 */
	public boolean isPrimary() {
		return primary;
	}

	/**
	 * @param primary
	 *            The primary to set.
	 */
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	/**
	 * @return Returns the scale.
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * @param scale
	 *            The scale to set.
	 */
	public void setScale(int scale) {
		this.scale = scale;
	}

	/**
	 * @return Returns the selectable.
	 */
	public boolean isSelectable() {
		return selectable;
	}

	/**
	 * @param selectable
	 *            The selectable to set.
	 */
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}

	/**
	 * @return Returns the type.
	 */
	public ColumnType getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(ColumnType type) {
		this.type = type;
	}

	/**
	 * @return Returns the unique.
	 */
	public boolean isUnique() {
		return unique;
	}

	/**
	 * @param unique
	 *            The unique to set.
	 */
	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	/**
	 * @return Returns the updatable.
	 */
	public boolean isUpdatable() {
		return updatable;
	}

	/**
	 * @param updatable
	 *            The updatable to set.
	 */
	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
}