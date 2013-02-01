/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.impl.DataSetImpl;

import static org.seasar.framework.unit.S2Assert.*;

/**
 * 
 * @author taedium
 */
public class S2AssertTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void testAssertMapEquals_DataSet_Map() throws Exception {
        DataSet dataSet = new DataSetImpl();
        DataTable table = dataSet.addTable("hoge");
        table.addColumn("aaa");
        table.addColumn("bbb");
        DataRow row = table.addRow();
        row.setValue("aaa", 10);
        row.setValue("bbb", 20);

        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("aaa", 10);
        map.put("bbb", 20);

        assertMapEquals(dataSet, map);
    }

    /**
     * 
     * @throws Exception
     */
    public void testAssertMapEquals_DataSet_List() throws Exception {
        DataSet dataSet = new DataSetImpl();
        DataTable table = dataSet.addTable("hoge");
        table.addColumn("aaa");
        table.addColumn("bbb");
        DataRow row = table.addRow();
        row.setValue("aaa", 10);
        row.setValue("bbb", 20);
        row = table.addRow();
        row.setValue("aaa", 30);
        row.setValue("bbb", 40);

        List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("aaa", 10);
        map.put("bbb", 20);
        list.add(map);
        map = new HashMap<String, Integer>();
        map.put("aaa", 30);
        map.put("bbb", 40);
        list.add(map);

        assertMapEquals(dataSet, list);
    }

}
