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
package org.seasar.framework.unit;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.jpa.unit.EntityReader;
import org.seasar.framework.jpa.unit.EntityReaderFactory;

/**
 * @author taedium
 * 
 */
public class S2Assert extends Assert {

    protected static S2TestCaseAdapter adapter = new S2TestCaseAdapter();

    protected S2Assert() {
    }

    public static void assertEquals(DataSet expected, DataSet actual) {
        adapter.assertEquals(null, expected, actual);
    }

    public static void assertEquals(String message, DataSet expected,
            DataSet actual) {

        adapter.assertEquals(message, expected, actual);
    }

    public static void assertEquals(DataTable expected, DataTable actual) {
        adapter.assertEquals(null, expected, actual);
    }

    public static void assertEquals(String message, DataTable expected,
            DataTable actual) {

        adapter.assertEquals(message, expected, actual);
    }

    public static void assertEquals(DataSet expected, Object actual) {
        adapter.assertEquals(null, expected, actual);
    }

    public static void assertEquals(String message, DataSet expected,
            Object actual) {

        adapter.assertEquals(message, expected, actual);
    }

    public static void assertMapEquals(DataSet expected, Map<?, ?> map) {
        adapter.assertMapEquals(null, expected, map);
    }

    public static void assertMapEquals(String message, DataSet expected,
            Map<?, ?> map) {

        adapter.assertMapEquals(message, expected, map);
    }

    public static void assertMapEquals(DataSet expected, List<Map<?, ?>> list) {
        adapter.assertMapListEquals(null, expected, list);
    }

    public static void assertMapEquals(String message, DataSet expected,
            List<Map<?, ?>> list) {

        adapter.assertMapListEquals(message, expected, list);
    }

    public static void assertBeanEquals(DataSet expected, Object bean) {
        adapter.assertBeanEquals(null, expected, bean);
    }

    public static void assertBeanEquals(String message, DataSet expected,
            Object bean) {

        adapter.assertBeanEquals(message, expected, bean);
    }

    public static void assertBeanEquals(DataSet expected, List<?> list) {

        adapter.assertBeanListEquals(null, expected, list);
    }

    public static void assertBeanEquals(String message, DataSet expected,
            List<?> list) {

        adapter.assertBeanListEquals(message, expected, list);
    }

    public static void assertEntityEquals(DataSet expected, Object entity) {
        assertEntityEquals(null, expected, entity);
    }

    public static void assertEntityEquals(String message, DataSet expected,
            Object entity) {

        EntityReader reader = EntityReaderFactory.getEntityReader(entity);
        assertEqualsIgnoreTableOrder(message, expected, reader.read());
    }

    public static void assertEntityEquals(DataSet expected,
            Collection<?> entities) {

        assertEntityEquals(null, expected, entities);
    }

    public static void assertEntityEquals(String message, DataSet expected,
            Collection<?> entities) {

        EntityReader reader = EntityReaderFactory.getEntityReader(entities);
        assertEqualsIgnoreTableOrder(message, expected, reader.read());
    }

    public static void assertEqualsIgnoreTableOrder(DataSet expected,
            DataSet actual) {

        assertEqualsIgnoreTableOrder(null, expected, actual);
    }

    public static void assertEqualsIgnoreTableOrder(String message,
            DataSet expected, DataSet actual) {

        message = message == null ? "" : message;
        for (int i = 0; i < expected.getTableSize(); ++i) {
            final String tableName = expected.getTable(i).getTableName();
            final String notFound = message + ":Table " + tableName
                    + " was not found.";
            assertTrue(notFound, actual.hasTable(tableName));
            assertEquals(message, expected.getTable(i), actual
                    .getTable(tableName));
        }
    }

    protected static class S2TestCaseAdapter extends S2TestCase {

        @SuppressWarnings("unchecked")
        @Override
        protected void assertMapEquals(String message, DataSet expected, Map map) {
            super.assertMapEquals(message, expected, map);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void assertMapListEquals(String message, DataSet expected,
                List list) {

            super.assertMapListEquals(message, expected, list);
        }

        @Override
        protected void assertBeanEquals(String message, DataSet expected,
                Object bean) {

            super.assertBeanEquals(message, expected, bean);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void assertBeanListEquals(String message, DataSet expected,
                List list) {

            super.assertBeanListEquals(message, expected, list);
        }
    }

}
