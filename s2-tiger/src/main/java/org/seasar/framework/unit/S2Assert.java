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
 * テストで使用するアサートメソッドの集合です。
 * <p>
 * static importを宣言して利用してください。
 * 
 * <pre>
 * import org.seasar.framework.unit.S2Assert.*;
 *   ...
 *   assertEquals(...);
 * </pre>
 * 
 * </p>
 * 
 * @author taedium
 * 
 */
public class S2Assert extends Assert {

    /** {@link S2TestCase}のアダプタ */
    protected static final S2TestCaseAdapter adapter = new S2TestCaseAdapter();

    /**
     * インスタンスを構築します。
     * 
     */
    protected S2Assert() {
    }

    /**
     * 2つのデータセットが等しいことをアサートします。
     * 
     * @param expected
     *            期待値
     * @param actual
     *            実際値
     */
    public static void assertEquals(DataSet expected, DataSet actual) {
        adapter.assertEquals(null, expected, actual);
    }

    /**
     * 2つのデータセットが等しいことをアサートします。もし等しくない場合、エラー情報に指定したメッセージが含まれます。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param actual
     *            実際値
     */
    public static void assertEquals(String message, DataSet expected,
            DataSet actual) {

        adapter.assertEquals(message, expected, actual);
    }

    /**
     * 2つのデータテーブルが等しいことをアサートします。
     * 
     * @param expected
     *            期待値
     * @param actual
     *            実際値
     */
    public static void assertEquals(DataTable expected, DataTable actual) {
        adapter.assertEquals(null, expected, actual);
    }

    /**
     * 2つのデータテーブルが等しいことをアサートします。もし等しくない場合、エラー情報に指定したメッセージが含まれます。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param actual
     *            実際値
     */
    public static void assertEquals(String message, DataTable expected,
            DataTable actual) {

        adapter.assertEquals(message, expected, actual);
    }

    /**
     * データセットとオブジェクトが等しいことをアサートします。
     * 
     * @param expected
     *            期待値
     * @param actual
     *            実際値
     */
    public static void assertEquals(DataSet expected, Object actual) {
        adapter.assertEquals(null, expected, actual);
    }

    /**
     * データセットとオブジェクトが等しいことをアサートします。 もし等しくない場合、エラー情報に指定したメッセージが含まれます。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param actual
     *            実際値
     */
    public static void assertEquals(String message, DataSet expected,
            Object actual) {

        adapter.assertEquals(message, expected, actual);
    }

    /**
     * データセットとマップが等しいことをアサートします。
     * 
     * @param <V>
     *            マップの値の型
     * @param expected
     *            期待値
     * @param map
     *            実際値
     */
    public static <V> void assertMapEquals(DataSet expected, Map<String, V> map) {
        adapter.assertMapEquals(null, expected, map);
    }

    /**
     * データセットとマップが等しいことをアサートします。もし等しくない場合、エラー情報に指定したメッセージが含まれます。
     * 
     * @param <V>
     *            マップの値の型
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param map
     *            実際値
     */
    public static <V> void assertMapEquals(String message, DataSet expected,
            Map<String, V> map) {

        adapter.assertMapEquals(message, expected, map);
    }

    /**
     * データセットとマップのリストが等しいことをアサートします。
     * 
     * @param <V>
     *            マップの値の型
     * @param expected
     *            期待値
     * @param list
     *            実際値
     */
    public static <V> void assertMapEquals(DataSet expected,
            List<Map<String, V>> list) {
        adapter.assertMapListEquals(null, expected, list);
    }

    /**
     * データセットとマップのリストが等しいことをアサートします。もし等しくない場合、エラー情報に指定したメッセージが含まれます。
     * 
     * @param <V>
     *            マップの値の型
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param list
     *            実際値
     */
    public static <V> void assertMapEquals(String message, DataSet expected,
            List<Map<String, V>> list) {

        adapter.assertMapListEquals(message, expected, list);
    }

    /**
     * データセットとビーンが等しいことをアサートします。
     * 
     * @param expected
     *            期待値
     * @param bean
     *            実際値
     */
    public static void assertBeanEquals(DataSet expected, Object bean) {
        adapter.assertBeanEquals(null, expected, bean);
    }

    /**
     * データセットとビーンが等しいことをアサートします。もし等しくない場合、エラー情報に指定したメッセージが含まれます。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param bean
     *            実際値
     */
    public static void assertBeanEquals(String message, DataSet expected,
            Object bean) {

        adapter.assertBeanEquals(message, expected, bean);
    }

    /**
     * データセットとビーンのリストが等しいことをアサートします。
     * 
     * @param expected
     *            期待値
     * @param list
     *            実際値
     */
    public static void assertBeanEquals(DataSet expected, List<?> list) {

        adapter.assertBeanListEquals(null, expected, list);
    }

    /**
     * データセットとビーンのリストが等しいことをアサートします。もし等しくない場合、エラー情報に指定したメッセージが含まれます。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param list
     *            実際値
     */
    public static void assertBeanEquals(String message, DataSet expected,
            List<?> list) {

        adapter.assertBeanListEquals(message, expected, list);
    }

    /**
     * データセットとJava Persistence APIのエンティティが等しいことをアサートします。
     * 
     * @param expected
     *            期待値
     * @param entity
     *            実際値
     */
    public static void assertEntityEquals(DataSet expected, Object entity) {
        assertEntityEquals(null, expected, entity);
    }

    /**
     * データセットとJava Persistence
     * APIのエンティティが等しいことをアサートします。もし等しくない場合、エラー情報に指定したメッセージが含まれます。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param entity
     *            実際値
     */
    public static void assertEntityEquals(String message, DataSet expected,
            Object entity) {

        EntityReader reader = EntityReaderFactory.getEntityReader(entity);
        assertEqualsIgnoreTableOrder(message, expected, reader.read());
    }

    /**
     * データセットとJava Persistence APIのエンティティのコレクションが等しいことをアサートします。
     * 
     * @param expected
     *            期待値
     * @param entities
     *            実際値
     */
    public static void assertEntityEquals(DataSet expected,
            Collection<?> entities) {

        assertEntityEquals(null, expected, entities);
    }

    /**
     * データセットとJava Persistence
     * APIのエンティティのコレクションが等しいことをアサートします。もし等しくない場合、エラー情報に指定したメッセージが含まれます。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param entities
     *            実際値
     */
    public static void assertEntityEquals(String message, DataSet expected,
            Collection<?> entities) {

        EntityReader reader = EntityReaderFactory.getEntityReader(entities);
        assertEqualsIgnoreTableOrder(message, expected, reader.read());
    }

    /**
     * 2つのデータセットがデータテーブルの順番を無視して等しいことをアサートします。
     * 
     * @param expected
     *            期待値
     * @param actual
     *            実際値
     */
    public static void assertEqualsIgnoreTableOrder(DataSet expected,
            DataSet actual) {

        assertEqualsIgnoreTableOrder(null, expected, actual);
    }

    /**
     * 2つのデータセットがデータテーブルの順番を無視して等しいことをアサートします。もし等しくない場合、エラー情報に指定したメッセージが含まれます。
     * 
     * @param message
     *            メッセージ
     * @param expected
     *            期待値
     * @param actual
     *            実際値
     */
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

    /**
     * {@link S2TestCase}のアダプタです。
     * 
     * @author taedium
     */
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
