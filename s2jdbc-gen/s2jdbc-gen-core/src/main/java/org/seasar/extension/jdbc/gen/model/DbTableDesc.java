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
import java.util.List;

/**
 * @author taedium
 * 
 */
public class DbTableDesc {

    protected String name;

    protected String schema;

    protected List<DbColumnDesc> columnDescList = new ArrayList<DbColumnDesc>();

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
     * @return Returns the schema.
     */
    public String getSchema() {
        return schema;
    }

    /**
     * @param schema
     *            The schema to set.
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void addColumnDesc(DbColumnDesc columnDesc) {
        columnDescList.add(columnDesc);
    }

    public List<DbColumnDesc> getColumnDescList() {
        return Collections.unmodifiableList(columnDescList);
    }
}
