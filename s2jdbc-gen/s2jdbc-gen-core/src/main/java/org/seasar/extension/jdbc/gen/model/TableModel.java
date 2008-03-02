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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.util.CaseInsensitiveMap;

/**
 * @author taedium
 * 
 */
public class TableModel {

    protected String schema;

    protected String name;

    protected List<ColumnModel> columnModelList = new ArrayList<ColumnModel>();

    protected Map<String, ColumnModel> columnModelMap = new CaseInsensitiveMap();

    protected PrimaryKeyModel primaryKeyModel;

    protected List<ForeignKeyModel> foreignKeyModelList = new ArrayList<ForeignKeyModel>();

    protected Map<Key, ForeignKeyModel> foreignKeyModeMap = new HashMap<Key, ForeignKeyModel>();

    protected List<UniqueKeyModel> uniqueKeyModelList = new ArrayList<UniqueKeyModel>();

    protected Map<Key, UniqueKeyModel> uniqueKeyModelMap = new HashMap<Key, UniqueKeyModel>();

    protected List<RowModel> rowModelList = new ArrayList<RowModel>();

    protected Map<Key, RowModel> rowModelMap = new HashMap<Key, RowModel>();

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasColumnModel(String name) {
        return columnModelMap.containsKey(name);
    }

    public void addColumnModel(ColumnModel columnModel) {
        columnModelList.add(columnModel);
        columnModelMap.put(columnModel.getName(), columnModel);
    }

    public List<ColumnModel> getColumnModelList() {
        return Collections.unmodifiableList(columnModelList);
    }

    public boolean hasPrimaryKeyModel() {
        return primaryKeyModel != null;
    }

    public PrimaryKeyModel getPrimaryKeyModel() {
        return primaryKeyModel;
    }

    public void setPrimaryKeyModel(PrimaryKeyModel primaryKeyModel) {
        this.primaryKeyModel = primaryKeyModel;
    }

    public boolean hasForeignKeyModel(Key key) {
        return foreignKeyModeMap.containsKey(key);
    }

    public void addForeignKeyModel(ForeignKeyModel foreignKeyModel) {
        foreignKeyModelList.add(foreignKeyModel);
        foreignKeyModeMap.put(foreignKeyModel.getKey(), foreignKeyModel);
    }

    public List<ForeignKeyModel> getForeignKeyModelList() {
        return Collections.unmodifiableList(foreignKeyModelList);
    }

    public boolean hasUniqueKeyModel(Key key) {
        return uniqueKeyModelMap.containsKey(key);
    }

    public void addUniqueKeyModel(UniqueKeyModel uniqueKeyModel) {
        uniqueKeyModelList.add(uniqueKeyModel);
        uniqueKeyModelMap.put(uniqueKeyModel.getKey(), uniqueKeyModel);
    }

    public List<UniqueKeyModel> getUniqueKeyModelList() {
        return Collections.unmodifiableList(uniqueKeyModelList);
    }

    public boolean hasRowModel(Key key) {
        return rowModelMap.containsKey(key);
    }

    public void addRowModel(RowModel rowModel) {
        rowModelList.add(rowModel);
        rowModelMap.put(rowModel.getKey(), rowModel);
    }

    public List<RowModel> getRowModelList() {
        return Collections.unmodifiableList(rowModelList);
    }

}