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
package org.seasar.extension.unit;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataReader;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;
import org.seasar.extension.dataset.states.RowStates;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

public class BeanReader implements DataReader {

    private DataSet dataSet_ = new DataSetImpl();

    private DataTable table_ = dataSet_.addTable("Bean");

    protected BeanReader() {
    }

    public BeanReader(Object bean) {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(bean.getClass());
        setupColumns(beanDesc);
        setupRow(beanDesc, bean);
    }

    protected void setupColumns(BeanDesc beanDesc) {
        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            PropertyDesc pd = beanDesc.getPropertyDesc(i);
            Class propertyType = pd.getPropertyType();
            table_.addColumn(pd.getPropertyName(), ColumnTypes
                    .getColumnType(propertyType));
        }
    }

    protected void setupRow(BeanDesc beanDesc, Object bean) {
        DataRow row = table_.addRow();
        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            PropertyDesc pd = beanDesc.getPropertyDesc(i);
            Object value = pd.getValue(bean);
            ColumnType ct = ColumnTypes.getColumnType(pd.getPropertyType());
            row.setValue(pd.getPropertyName(), ct.convert(value, null));
        }
        row.setState(RowStates.UNCHANGED);
    }

    public DataSet read() {
        return dataSet_;
    }

}