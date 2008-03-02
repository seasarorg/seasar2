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
public class RowModel {

    protected PrimaryKeyModel primaryKeyModel;

    protected List<Object> valueList = new ArrayList<Object>();

    public RowModel(PrimaryKeyModel primaryKeyModel) {
        this.primaryKeyModel = primaryKeyModel;
    }

    public void addValue(Object value) {
        valueList.add(value);
    }

    public List<Object> getValueList() {
        return Collections.unmodifiableList(valueList);
    }

    public Key getKey() {
        int size = primaryKeyModel != null ? primaryKeyModel
                .getColumnNameList().size() : 0;
        Object[] keyValues = new Object[size];
        for (int i = 0; i < size; i++) {
            keyValues[i] = valueList.get(i);
        }
        return new Key(keyValues);
    }
}
