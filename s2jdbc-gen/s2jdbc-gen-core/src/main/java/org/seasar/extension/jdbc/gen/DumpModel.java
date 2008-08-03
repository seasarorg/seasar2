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
package org.seasar.extension.jdbc.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author taedium
 * 
 */
public class DumpModel {

    protected String delimiter;

    protected List<String> columnNameList = new ArrayList<String>();

    protected List<List<String>> rowList = new ArrayList<List<String>>();

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public List<String> getColumnNameList() {
        return Collections.unmodifiableList(columnNameList);
    }

    public void addColumnName(String columnName) {
        columnNameList.add(columnName);
    }

    public void addRow(List<String> row) {
        this.rowList.add(row);
    }

    public List<List<String>> getRowList() {
        return Collections.unmodifiableList(rowList);
    }

}
