/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

/**
 * Bean用の {@link DataReader}です。
 * 
 * @author higa
 * 
 */
public class BeanReader implements DataReader {

    private DataSet dataSet = new DataSetImpl();

    private DataTable table = dataSet.addTable("Bean");

    /**
     * {@link BeanReader}を作成します。
     */
    protected BeanReader() {
    }

    /**
     * {@link BeanReader}を作成します。
     * 
     * @param bean
     *            Bean
     */
    public BeanReader(Object bean) {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(bean.getClass());
        setupColumns(beanDesc);
        setupRow(beanDesc, bean);
    }

    /**
     * カラムをセットアップします。
     * 
     * @param beanDesc
     *            Bean記述
     */
    protected void setupColumns(BeanDesc beanDesc) {
        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            PropertyDesc pd = beanDesc.getPropertyDesc(i);
            Class propertyType = pd.getPropertyType();
            table.addColumn(pd.getPropertyName(), ColumnTypes
                    .getColumnType(propertyType));
        }
    }

    /**
     * 行をセットアップします。
     * 
     * @param beanDesc
     *            Bean記述
     * @param bean
     *            Bean
     */
    protected void setupRow(BeanDesc beanDesc, Object bean) {
        DataRow row = table.addRow();
        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            PropertyDesc pd = beanDesc.getPropertyDesc(i);
            Object value = pd.getValue(bean);
            ColumnType ct = ColumnTypes.getColumnType(pd.getPropertyType());
            row.setValue(pd.getPropertyName(), ct.convert(value, null));
        }
        row.setState(RowStates.UNCHANGED);
    }

    public DataSet read() {
        return dataSet;
    }

}