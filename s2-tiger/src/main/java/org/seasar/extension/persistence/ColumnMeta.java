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
 * Columnのメタデータです。
 * 
 * @author higa
 * 
 */
public class ColumnMeta {

    private String name;

    private boolean unique;

    private boolean nullable;

    private boolean insertable;

    private boolean updatable;

    private String columnDefinition;

    private String table;

    private int length;

    private int precision;

    private int scale;

    /**
     * インサート可能かどうか返します。
     * 
     * @return insertable
     */
    public boolean isInsertable() {
        return insertable;
    }

    /**
     * インサート可能かどうか設定します。
     * 
     * @param insertable
     */
    public void setInsertable(boolean insertable) {
        this.insertable = insertable;
    }

    /**
     * 長さを返します。
     * 
     * @return length.
     */
    public int getLength() {
        return length;
    }

    /**
     * 長さを設定します。
     * 
     * @param length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * 名前を返します。
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定します。
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * null可能かどうか返します。
     * 
     * @return nullable
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * null可能かどうか設定します。
     * 
     * @param nullable
     */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    /**
     * 精度を返します。
     * 
     * @return precision
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * 精度を設定します。
     * 
     * @param precision
     */
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     * スケールを返します。
     * 
     * @return scale
     */
    public int getScale() {
        return scale;
    }

    /**
     * スケールを設定します。
     * 
     * @param scale
     */
    public void setScale(int scale) {
        this.scale = scale;
    }

    /**
     * ユニークかどうか返します。
     * 
     * @return unique.
     */
    public boolean isUnique() {
        return unique;
    }

    /**
     * ユニークかどうか設定します。
     * 
     * @param unique
     */
    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    /**
     * 更新可能かどうか返します。
     * 
     * @return updatable
     */
    public boolean isUpdatable() {
        return updatable;
    }

    /**
     * 更新可能かどうか設定します。
     * 
     * @param updatable
     */
    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    /**
     * カラム定義を返します。
     * 
     * @return columnDefinition
     */
    public String getColumnDefinition() {
        return columnDefinition;
    }

    /**
     * カラム定義を設定します。
     * 
     * @param columnDefinition
     */
    public void setColumnDefinition(String columnDefinition) {
        this.columnDefinition = columnDefinition;
    }

    /**
     * セカンダリテーブル名を返します。
     * 
     * @return table.
     */
    public String getTable() {
        return table;
    }

    /**
     * セカンダリテーブル名を設定します。
     * 
     * @param table
     */
    public void setTable(String table) {
        this.table = table;
    }
}