/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.unit.impl;

import java.lang.reflect.Method;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.TigerAnnotationHandler;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.message.MessageResourceBundleFactory;
import org.seasar.framework.unit.ConfigFileIncluder;
import org.seasar.framework.unit.ExpectedDataReader;
import org.seasar.framework.unit.InternalTestContext;
import org.seasar.framework.unit.PreparationType;
import org.seasar.framework.unit.TestDataPreparer;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * Servlet、JTA、EJB3のAPIに依存せずにS2JUnit4を実行可能にするシンプルなテストコンテキストです。
 * 
 * @author taedium
 */
public class SimpleInternalTestContext implements InternalTestContext {

    /** Tigerのアノテーションハンドラー */
    protected final TigerAnnotationHandler handler = new TigerAnnotationHandler();

    /** モックインターセプターのリスト */
    protected final List<MockInterceptor> mockInterceptors = CollectionsUtil
            .newArrayList();

    /** ルートのS2コンテナ */
    protected S2Container container;

    /** 命名規約 */
    protected NamingConvention namingConvention;

    /** テストクラス */
    protected Class<?> testClass;

    /** テストメソッド */
    protected Method testMethod;

    /** 自動インクルードをするかどうかを表すフラグ。デフォルトは<code>true</code> */
    protected boolean autoIncluding = true;

    /** テストデータを自動準備するかどうかを表すフラグ。デフォルトは<code>true</code> */
    protected boolean autoPreparing = true;

    /** テストデータの準備方法 */
    protected PreparationType preparationType = PreparationType.WRITE;

    /** テストデータの文字列に含まれる空白を取り除くかどうかを表すフラグ。デフォルトは<code>true</code> */
    protected boolean trimString = true;

    /** EJB3を使用するかどうかを表すフラグ。デフォルトは<code>false</code> */
    protected boolean ejb3Enabled = false;

    /** JTAを使用するかどうかを表すフラグ。デフォルトは<code>false</code> */
    protected boolean jtaEnabled = false;

    /** S2コンテナが初期化されたかどうかを表すフラグ */
    protected boolean containerInitialized;

    /** {@link ColumnTypes}から登録除去された{@link ColumnType カラムの型}をクラスをキーにして管理するマップ */
    protected Map<Class<?>, ColumnType> deregisteredColumnTypesByClass = new HashMap<Class<?>, ColumnType>();

    /** {@link ColumnTypes}から登録除去された{@link ColumnType カラムの型}をSQL型をキーにして管理するマップ */
    protected Map<Integer, ColumnType> deregisteredColumnTypesBySqlType = new HashMap<Integer, ColumnType>();

    /**
     * S2コンテナを設定します。
     * 
     * @param container
     *            S2コンテナ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setContainer(final S2Container container) {
        this.container = container.getRoot();
    }

    /**
     * EJB3を使用する場合<code>true</code>を設定します。
     * 
     * @param ejb3Enabled
     *            EJB3を使用する場合<code>true</code>、使用しない場合<code>false</code>
     */
    @Binding(bindingType = BindingType.MAY)
    public void setEjb3Enabled(boolean ejb3Enabled) {
        this.ejb3Enabled = ejb3Enabled;
    }

    /**
     * JTAを使用する場合<code>true</code>を設定します。
     * 
     * @param jtaEnabled
     *            JTAを使用する場合<code>true</code>、使用しない場合<code>false</code>
     */
    @Binding(bindingType = BindingType.MAY)
    public void setJtaEnabled(boolean jtaEnabled) {
        this.jtaEnabled = jtaEnabled;
    }

    public void setAutoIncluding(final boolean autoIncluding) {
        this.autoIncluding = autoIncluding;
    }

    public void setAutoPreparing(final boolean autoPreparing) {
        this.autoPreparing = autoPreparing;
    }

    public PreparationType getPreparationType() {
        return preparationType;
    }

    public void setPreparationType(PreparationType preparationType) {
        this.preparationType = preparationType;
    }

    public void setTrimString(final boolean trimString) {
        this.trimString = trimString;
    }

    public boolean isTrimString() {
        return trimString;
    }

    public void setTestClass(final Class<?> testClass) {
        this.testClass = testClass;
    }

    public void setTestMethod(final Method testMethod) {
        this.testMethod = testMethod;
    }

    @Binding(bindingType = BindingType.NONE)
    public void setNamingConvention(NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    /**
     * このコンポーネントを初期化します。
     * 
     * @throws Throwable
     */
    @InitMethod
    public void init() throws Throwable {
    }

    /**
     * このコンポーネントを破棄します。
     */
    @DestroyMethod
    public void destroy() {
        MessageResourceBundleFactory.clear();
    }

    public void initContainer() {
        container.init();
        containerInitialized = true;
    }

    public void destroyContainer() {
        container.destroy();
        container = null;
        containerInitialized = false;
    }

    public void include() {
        if (autoIncluding) {
            if (container.hasComponentDef(ConfigFileIncluder.class)) {
                final ConfigFileIncluder includer = (ConfigFileIncluder) container
                        .getComponent(ConfigFileIncluder.class);
                includer.include(this);
            }
        }
    }

    public void include(final String path) {
        final String convertedPath = ResourceUtil.convertPath(path, testClass);
        S2ContainerFactory.include(container, convertedPath);
    }

    public void register(final Class<?> componentClass,
            final String componentName) {
        final ComponentDef cd = handler.createComponentDef(componentClass,
                InstanceDefFactory.SINGLETON);
        cd.setComponentName(componentName);
        handler.appendDI(cd);
        handler.appendAspect(cd);
        handler.appendInterType(cd);
        handler.appendInitMethod(cd);
        handler.appendDestroyMethod(cd);
        container.register(cd);
    }

    public void register(final Class<?> componentClass) {
        if (namingConvention == null) {
            register(componentClass, null);
        } else {
            register(componentClass, namingConvention
                    .fromClassNameToComponentName(componentClass.getName()));
        }
    }

    public void register(final ComponentDef componentDef) {
        container.register(componentDef);
    }

    public void register(final Object component, String componentName) {
        container.register(component, componentName);
    }

    public void register(final Object component) {
        container.register(component);
    }

    public String getTestClassPackagePath() {
        return testClass.getName().replace('.', '/')
                .replaceFirst("/[^/]+$", "");
    }

    public String getTestClassShortName() {
        return ClassUtil.getShortClassName(testClass);
    }

    public String getTestMethodName() {
        return testMethod.getName();
    }

    public void prepareTestData() {
        if (autoPreparing) {
            if (hasComponentDef(TestDataPreparer.class)) {
                final TestDataPreparer preparer = getComponent(TestDataPreparer.class);
                preparer.prepare(this);
            }
        }
    }

    public DataSet getExpected() {
        if (hasComponentDef(ExpectedDataReader.class)) {
            final ExpectedDataReader reader = getComponent(ExpectedDataReader.class);
            final DataSet expected = reader.read(this);
            if (expected != null) {
                return expected;
            }
        }
        return null;
    }

    public MockInterceptor getMockInterceptor(final int index) {
        return mockInterceptors.get(index);
    }

    public S2Container getContainer() {
        return container;
    }

    public <T> T getComponent(final Class<? extends T> componentKey) {
        return componentKey.cast(container.getComponent(componentKey));
    }

    public Object getComponent(final Object componentKey) {
        return container.getComponent(componentKey);
    }

    public boolean hasComponentDef(final Object componentKey) {
        return container.hasComponentDef(componentKey);
    }

    public ComponentDef getComponentDef(final int index) {
        return container.getComponentDef(index);
    }

    public ComponentDef getComponentDef(final Object componentKey) {
        return container.getComponentDef(componentKey);
    }

    public void addMockInterceptor(final MockInterceptor mockInterceptor) {
        mockInterceptors.add(mockInterceptor);
    }

    public int getMockInterceptorSize() {
        return mockInterceptors.size();
    }

    public void addAspecDef(final Object componentKey, final AspectDef aspectDef) {
        if (containerInitialized) {
            throw new IllegalStateException();
        }
        container.getComponentDef(componentKey).addAspectDef(0, aspectDef);
    }

    public boolean isEjb3Enabled() {
        return ejb3Enabled;
    }

    public boolean isJtaEnabled() {
        return jtaEnabled;
    }

    public void registerColumnTypes() {
        if (!trimString) {
            registerColumnType(String.class, ColumnTypes.NOT_TRIM_STRING);
            registerColumnType(Types.CHAR, ColumnTypes.NOT_TRIM_STRING);
            registerColumnType(Types.LONGVARCHAR, ColumnTypes.NOT_TRIM_STRING);
            registerColumnType(Types.VARCHAR, ColumnTypes.NOT_TRIM_STRING);
        }
    }

    /**
     * カラムの型を登録します。
     * 
     * @param clazz
     *            クラス
     * @param columnType
     *            カラムの型
     */
    protected void registerColumnType(Class<?> clazz, ColumnType columnType) {
        ColumnType original = ColumnTypes.registerColumnType(clazz, columnType);
        deregisteredColumnTypesByClass.put(clazz, original);
    }

    /**
     * カラムの型を登録します。
     * 
     * @param sqlType
     *            SQL型
     * @param columnType
     *            カラムの型
     */
    protected void registerColumnType(int sqlType, ColumnType columnType) {
        ColumnType original = ColumnTypes.registerColumnType(sqlType,
                columnType);
        deregisteredColumnTypesBySqlType.put(sqlType, original);
    }

    public void revertColumnTypes() {
        for (Map.Entry<Class<?>, ColumnType> e : deregisteredColumnTypesByClass
                .entrySet()) {
            ColumnTypes.registerColumnType(e.getKey(), e.getValue());
        }
        for (Map.Entry<Integer, ColumnType> e : deregisteredColumnTypesBySqlType
                .entrySet()) {
            ColumnTypes.registerColumnType(e.getKey(), e.getValue());
        }
    }
}
