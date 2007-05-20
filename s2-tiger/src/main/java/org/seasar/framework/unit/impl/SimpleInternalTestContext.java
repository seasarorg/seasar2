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
package org.seasar.framework.unit.impl;

import java.lang.reflect.Method;
import java.util.List;

import org.seasar.extension.dataset.DataSet;
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
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.framework.message.MessageResourceBundleFactory;
import org.seasar.framework.unit.ConfigFileIncluder;
import org.seasar.framework.unit.ExpectedDataReader;
import org.seasar.framework.unit.InternalTestContext;
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

    /** EJB3を使用するかどうかを表すフラグ。デフォルトは<code>false</code> */
    protected boolean ejb3Enabled = false;

    /** JTAを使用するかどうかを表すフラグ。デフォルトは<code>false</code> */
    protected boolean jtaEnabled = false;

    /** S2コンテナが初期化されたかどうかを表すフラグ */
    protected boolean containerInitialized;

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

    public void setTestClass(final Class<?> testClass) {
        this.testClass = testClass;
    }

    public void setTestMethod(final Method testMethod) {
        this.testMethod = testMethod;
    }

    /**
     * このコンポーネントを初期化します。
     * 
     * @throws Throwable
     */
    @InitMethod
    public void init() throws Throwable {
        namingConvention = new NamingConventionImpl();
        container.register(namingConvention);
    }

    /**
     * このコンポーネントを破棄します。
     */
    @DestroyMethod
    public void destroy() {
        MessageResourceBundleFactory.clear();
        namingConvention = null;
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
        register(componentClass, namingConvention
                .fromClassNameToComponentName(componentClass.getName()));
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

}
