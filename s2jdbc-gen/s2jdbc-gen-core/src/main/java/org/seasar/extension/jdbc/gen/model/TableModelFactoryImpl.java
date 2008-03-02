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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.seasar.extension.jdbc.gen.TableModelFactory;

/**
 * @author taedium
 * 
 */
public class TableModelFactoryImpl implements TableModelFactory {

    protected ConcurrentMap<String, TableModel> tableModelCache = new ConcurrentHashMap<String, TableModel>();

    public TableModel getTableModel(String schemaName, String tableName) {
        if (tableName == null) {
            throw new NullPointerException("tableName");
        }
        String key = createKey(schemaName, tableName);
        TableModel tableModel = tableModelCache.get(key);
        if (tableModel != null) {
            return tableModel;
        }
        tableModel = new TableModel();
        TableModel tableModel2 = tableModelCache.putIfAbsent(key, tableModel);
        return tableModel2 != null ? tableModel2 : tableModel;
    }

    protected String createKey(String schemaName, String tableName) {
        if (schemaName == null) {
            return tableName.toLowerCase();
        }
        return schemaName.toLowerCase() + "." + tableName.toLowerCase();
    }

}
