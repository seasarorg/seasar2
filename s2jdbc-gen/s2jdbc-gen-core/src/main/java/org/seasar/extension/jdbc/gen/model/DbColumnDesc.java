/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.model;

/**
 * @author taedium
 * 
 */
public class DbColumnDesc {

    protected String name;

    protected int sqlType;

    protected String typeName;

    protected int length;

    protected int scale;

    protected boolean nullable;

    protected boolean primaryKey;

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

    public int getSqlType() {
        return sqlType;
    }

    public void setSqlType(int sqlType) {
        this.sqlType = sqlType;
    }

    /**
     * @return Returns the typeName.
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * @param typeName
     *            The typeName to set.
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
     * @return Returns the primaryKey.
     */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey
     *            The primaryKey to set.
     */
    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

}
