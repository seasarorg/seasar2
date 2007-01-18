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

import org.seasar.extension.persistence.exception.PropertyMetaNotFoundRuntimeException;
import org.seasar.framework.util.ArrayMap;

/**
 * @author higa
 * 
 */
public class EntityMeta {

    private String name;

    private TableMeta tableMeta;

    private ArrayMap propertyMetaMap = new ArrayMap();

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
     * @return Returns the tableMeta.
     */
    public TableMeta getTableMeta() {
        return tableMeta;
    }

    /**
     * @param tableMeta
     *            The tableMeta to set.
     */
    public void setTableMeta(TableMeta tableMeta) {
        this.tableMeta = tableMeta;
    }

    public PropertyMeta getPropertyMeta(String propertyName) {
        PropertyMeta meta = (PropertyMeta) propertyMetaMap.get(propertyName);
        if (meta == null) {
            throw new PropertyMetaNotFoundRuntimeException(name, propertyName);
        }
        return meta;
    }

    public PropertyMeta getPropertyMeta(int index) {
        return (PropertyMeta) propertyMetaMap.get(index);
    }

    public int getPropertyMetaSize() {
        return propertyMetaMap.size();
    }

    public void addPropertyMeta(PropertyMeta propertyMeta) {
        propertyMetaMap.put(propertyMeta.getName(), propertyMeta);
    }
}