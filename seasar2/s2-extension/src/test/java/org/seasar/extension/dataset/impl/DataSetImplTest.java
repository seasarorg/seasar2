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
package org.seasar.extension.dataset.impl;

import junit.framework.TestCase;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.TableNotFoundRuntimeException;

/**
 * @author higa
 * 
 */
public class DataSetImplTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testRemoveTable() throws Exception {
        DataSet dataSet = new DataSetImpl();
        DataTable table = dataSet.addTable("hoge");
        assertEquals("1", table, dataSet.removeTable("hoge"));
        assertEquals("2", 0, dataSet.getTableSize());
        dataSet.addTable(table);
        assertEquals("3", table, dataSet.removeTable(table));
        assertEquals("4", 0, dataSet.getTableSize());
        dataSet.addTable(table);
        assertEquals("5", table, dataSet.removeTable(0));
        assertEquals("6", 0, dataSet.getTableSize());
        try {
            dataSet.removeTable("hoge");
            fail("7");
        } catch (TableNotFoundRuntimeException ex) {
            System.out.println(ex);
        }
    }
}