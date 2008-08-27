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
package org.seasar.extension.jdbc.gen.internal.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.model.TestModel;
import org.seasar.extension.jdbc.gen.model.TestModelFactory;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.util.ClassUtil;

/**
 * {@link TestModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class TestModelFactoryImpl implements TestModelFactory {

    /** 設定ファイルのパス */
    protected String configPath;

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName;

    /** テストクラス名のサフィックス */
    protected String testClassNameSuffix;

    /** クラスモデルのサポート */
    protected ClassModelSupport classModelSupport = new ClassModelSupport();

    /**
     * インスタンスを構築します。
     * 
     * @param configPath
     *            設定ファイルのパス
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     * @param testClassNameSuffix
     *            テストクラス名のサフィックス
     */
    public TestModelFactoryImpl(String configPath, String jdbcManagerName,
            String testClassNameSuffix) {
        if (configPath == null) {
            throw new NullPointerException("configPath");
        }
        if (jdbcManagerName == null) {
            throw new NullPointerException("jdbcManagerName");
        }
        if (testClassNameSuffix == null) {
            throw new NullPointerException("testClassNameSuffix");
        }
        this.configPath = configPath;
        this.jdbcManagerName = jdbcManagerName;
        this.testClassNameSuffix = testClassNameSuffix;
    }

    public TestModel getEntityTestModel(EntityMeta entityMeta) {
        TestModel testModel = new TestModel();
        testModel.setConfigPath(configPath);
        testModel.setJdbcManagerName(jdbcManagerName);
        String packageName = ClassUtil.splitPackageAndShortClassName(entityMeta
                .getEntityClass().getName())[0];
        testModel.setPackageName(packageName);
        testModel.setShortClassName(entityMeta.getName() + testClassNameSuffix);
        testModel.setShortEntityClassName(entityMeta.getName());
        doImportName(testModel, entityMeta);
        doIdValue(testModel, entityMeta);
        return testModel;
    }

    /**
     * インポート名を処理します。
     * 
     * @param testModel
     *            テストモデル
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doImportName(TestModel testModel, EntityMeta entityMeta) {
        classModelSupport.addImportName(testModel, JdbcManager.class);
        classModelSupport.addImportName(testModel, S2TestCase.class);
        for (PropertyMeta propertyMeta : entityMeta.getIdPropertyMetaList()) {
            classModelSupport.addImportName(testModel, propertyMeta
                    .getPropertyClass());
        }
    }

    /**
     * 識別子の式を処理します。
     * 
     * @param testModel
     *            テストモデル
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doIdValue(TestModel testModel, EntityMeta entityMeta) {
        for (PropertyMeta propertyMeta : entityMeta.getIdPropertyMetaList()) {
            Class<?> propertyClass = propertyMeta.getPropertyClass();
            testModel.addIdExpression(getExpression(propertyClass));
        }
    }

    /**
     * プロパティのクラスの値を表す式を取得します。
     * 
     * @param propertyClass
     *            プロパティのクラス
     * @return 識別子の式
     */
    protected String getExpression(Class<?> propertyClass) {
        Class<?> clazz = ClassUtil.getPrimitiveClassIfWrapper(propertyClass);
        if (clazz.isPrimitive()) {
            if (clazz == boolean.class) {
                return "true";
            }
            if (clazz == char.class) {
                return "'a'";
            }
            if (clazz == byte.class) {
                return "(byte) 1";
            }
            if (clazz == short.class) {
                return "(short) 1";
            }
            if (clazz == int.class) {
                return "1";
            }
            if (clazz == long.class) {
                return "1L";
            }
            if (clazz == float.class) {
                return "1f";
            }
            if (clazz == double.class) {
                return "1d";
            }
        }
        if (clazz == String.class) {
            return "\"aaa\"";
        }
        if (clazz == BigDecimal.class) {
            return "new BigDecimal(1)";
        }
        if (clazz == BigDecimal.class || clazz == BigInteger.class) {
            return "new BigInteger(1)";
        }
        if (clazz == Date.class) {
            return "new Date()";
        }
        if (clazz == Calendar.class) {
            return "Calendar.getInstance()";
        }
        if (clazz == java.sql.Date.class) {
            return "Date.valueOf(\"2008-01-01\")";
        }
        if (clazz == Timestamp.class) {
            return "Timestamp.valueOf(\"2008-01-01 12:00:00\")";
        }
        if (clazz == Time.class) {
            return "Time.valueOf(\"12:00:00\")";
        }
        throw new IllegalArgumentException("propertyClass");
    }
}
