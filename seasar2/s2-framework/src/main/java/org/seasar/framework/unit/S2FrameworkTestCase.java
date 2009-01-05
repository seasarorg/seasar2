/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.deployer.ComponentDeployerFactory;
import org.seasar.framework.container.deployer.ExternalComponentDeployerProvider;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.external.servlet.HttpServletExternalContext;
import org.seasar.framework.container.external.servlet.HttpServletExternalContextComponentDefRegister;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.AnnotationHandlerFactory;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.container.servlet.S2ContainerServlet;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.framework.env.Env;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.message.MessageResourceBundleFactory;
import org.seasar.framework.mock.servlet.MockHttpServletRequest;
import org.seasar.framework.mock.servlet.MockHttpServletResponse;
import org.seasar.framework.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.framework.mock.servlet.MockServletConfig;
import org.seasar.framework.mock.servlet.MockServletConfigImpl;
import org.seasar.framework.mock.servlet.MockServletContext;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

/**
 * Seasar2を使うテストを行なうための<code>TestCase</code>です。
 * 
 * @author higa
 */
public abstract class S2FrameworkTestCase extends TestCase {

    /**
     * 環境が設定されているファイルのパスです。
     */
    protected static final String ENV_PATH = "env_ut.txt";

    /**
     * 環境が設定されていない場合のデフォルト値です。
     */
    protected static final String ENV_VALUE = "ut";

    private S2Container container;

    private Servlet servlet;

    private MockServletConfig servletConfig;

    private MockServletContext servletContext;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private ClassLoader originalClassLoader;

    private UnitClassLoader unitClassLoader;

    private NamingConvention namingConvention;

    private List boundFields;

    private boolean warmDeploy = true;

    private boolean registerNamingConvention = true;

    private AnnotationHandler annotationHandler = AnnotationHandlerFactory
            .getAnnotationHandler();

    /**
     * {@link S2FrameworkTestCase}を作成します。
     */
    public S2FrameworkTestCase() {
    }

    /**
     * {@link S2FrameworkTestCase}を作成します。
     * 
     * @param name
     */
    public S2FrameworkTestCase(String name) {
        super(name);
    }

    /**
     * WARM deployかどうかを返します。
     * 
     * @return WARM deployかどうか
     */
    public boolean isWarmDeploy() {
        return warmDeploy && !ResourceUtil.isExist("s2container.dicon")
                && ResourceUtil.isExist("convention.dicon")
                && ResourceUtil.isExist("creator.dicon")
                && ResourceUtil.isExist("customizer.dicon");
    }

    /**
     * WARM deployかどうかを設定します。
     * 
     * @param warmDeploy
     */
    public void setWarmDeploy(boolean warmDeploy) {
        this.warmDeploy = warmDeploy;
    }

    /**
     * テスト用のS2コンテナを作成する際に{@link NamingConvention}を登録する場合は<code>true</code>を返します。
     * 
     * @return テスト用のS2コンテナを作成する際に{@link NamingConvention}を登録する場合は<code>true</code>
     */
    public boolean isRegisterNamingConvention() {
        return registerNamingConvention;
    }

    /**
     * テスト用のS2コンテナを作成する際に{@link NamingConvention}を登録する場合は<code>true</code>を設定します。
     * 
     * @param registerNamingConvention
     *            テスト用のS2コンテナを作成する際に{@link NamingConvention}を登録する場合は<code>true</code>
     */
    public void setRegisterNamingConvention(boolean registerNamingConvention) {
        this.registerNamingConvention = registerNamingConvention;
    }

    /**
     * {@link S2Container}を返します。
     * 
     * @return {@link S2Container}
     */
    public S2Container getContainer() {
        return container;
    }

    /**
     * コンポーネントを返します。
     * 
     * @param componentName
     * @return
     */
    public Object getComponent(String componentName) {
        return container.getComponent(componentName);
    }

    /**
     * コンポーネントを返します。
     * 
     * @param componentClass
     * @return
     */
    public Object getComponent(Class componentClass) {
        return container.getComponent(componentClass);
    }

    /**
     * {@link ComponentDef}を返します。
     * 
     * @param componentName
     * @return {@link ComponentDef}
     */
    public ComponentDef getComponentDef(String componentName) {
        return container.getComponentDef(componentName);
    }

    /**
     * {@link ComponentDef}を返します。
     * 
     * @param componentClass
     * @return {@link ComponentDef}
     */
    public ComponentDef getComponentDef(Class componentClass) {
        return container.getComponentDef(componentClass);
    }

    /**
     * コンポーネントを登録します。
     * 
     * @param componentClass
     * @see #register(Class, String)
     */
    public void register(Class componentClass) {
        if (namingConvention == null) {
            register(componentClass, null);
        } else {
            register(componentClass, namingConvention
                    .fromClassNameToComponentName(componentClass.getName()));
        }
    }

    /**
     * コンポーネントを登録します。
     * 
     * @param componentClass
     * @param componentName
     */
    public void register(Class componentClass, String componentName) {
        ComponentDef cd = annotationHandler.createComponentDef(componentClass,
                InstanceDefFactory.SINGLETON);
        cd.setComponentName(componentName);
        annotationHandler.appendDI(cd);
        annotationHandler.appendAspect(cd);
        annotationHandler.appendInterType(cd);
        annotationHandler.appendInitMethod(cd);
        annotationHandler.appendDestroyMethod(cd);
        container.register(cd);
    }

    /**
     * コンポーネントを登録します。
     * 
     * @param component
     */
    public void register(Object component) {
        container.register(component);
    }

    /**
     * コンポーネントを登録します。
     * 
     * @param component
     * @param componentName
     */
    public void register(Object component, String componentName) {
        container.register(component, componentName);
    }

    /**
     * コンポーネントを登録します。
     * 
     * @param componentDef
     */
    public void register(ComponentDef componentDef) {
        container.register(componentDef);
    }

    /**
     * diconファイルをインクルードします。
     * 
     * @param path
     */
    public void include(String path) {
        S2ContainerFactory.include(container, convertPath(path));
    }

    /**
     * パスを変換します。
     * 
     * @param path
     *            パス
     * @return 変換後のパス
     */
    protected String convertPath(String path) {
        return ResourceUtil.convertPath(path, getClass());
    }

    /**
     * @see junit.framework.TestCase#runBare()
     */
    public void runBare() throws Throwable {
        setUpContainer();
        try {
            setUp();
            try {
                setUpForEachTestMethod();
                try {
                    container.init();
                    try {
                        setUpAfterContainerInit();
                        try {
                            bindFields();
                            try {
                                setUpAfterBindFields();
                                try {
                                    doRunTest();
                                } finally {
                                    tearDownBeforeUnbindFields();
                                }
                            } finally {
                                unbindFields();
                            }
                        } finally {
                            tearDownBeforeContainerDestroy();
                        }
                    } finally {
                        container.destroy();
                    }
                } finally {
                    tearDownForEachTestMethod();
                }
            } finally {
                tearDown();
            }
        } finally {
            tearDownContainer();
        }
    }

    /**
     * ルートのdiconのパスを返します。
     * 
     * @return ルートのdiconのパス
     * @throws Throwable
     */
    protected String getRootDicon() throws Throwable {
        return null;
    }

    /**
     * S2コンテナをセットアップします。
     * 
     * @throws Throwable
     *             例外が発生した場合
     */
    protected void setUpContainer() throws Throwable {
        Env.setFilePath(ENV_PATH);
        Env.setValueIfAbsent(ENV_VALUE);
        originalClassLoader = getOriginalClassLoader();
        unitClassLoader = new UnitClassLoader(originalClassLoader);
        Thread.currentThread().setContextClassLoader(unitClassLoader);
        if (isWarmDeploy()) {
            S2ContainerFactory.configure("warmdeploy.dicon");
        }
        String rootDicon = resolveRootDicon();
        container = StringUtil.isEmpty(rootDicon) ? S2ContainerFactory.create()
                : S2ContainerFactory.create(rootDicon);
        SingletonS2ContainerFactory.setContainer(container);
        if (servletContext == null) {
            servletContext = new MockServletContextImpl("s2-example");
        }
        request = servletContext.createRequest("/hello.html");
        response = new MockHttpServletResponseImpl(request);
        servletConfig = new MockServletConfigImpl();
        servletConfig.setServletContext(servletContext);
        servlet = new S2ContainerServlet();
        servlet.init(servletConfig);
        ExternalContext externalContext = new HttpServletExternalContext();
        externalContext.setApplication(servletContext);
        externalContext.setRequest(request);
        externalContext.setResponse(response);
        container.setExternalContext(externalContext);
        container
                .setExternalContextComponentDefRegister(new HttpServletExternalContextComponentDefRegister());
        ComponentDeployerFactory
                .setProvider(new ExternalComponentDeployerProvider());
        if (!container.hasComponentDef(NamingConvention.class)
                && isRegisterNamingConvention()) {
            namingConvention = new NamingConventionImpl();
            container.register(namingConvention);
        }
    }

    /**
     * オリジナルのクラスローダを返します。
     * 
     * @return オリジナルのクラスローダ
     */
    protected ClassLoader getOriginalClassLoader() {
        S2Container configurationContainer = S2ContainerFactory
                .getConfigurationContainer();
        if (configurationContainer != null
                && configurationContainer.hasComponentDef(ClassLoader.class)) {
            return (ClassLoader) configurationContainer
                    .getComponent(ClassLoader.class);
        }
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * ルートのdiconを解決します。
     * 
     * @return ルートのdicon
     * @throws Throwable
     *             例外が発生した場合
     */
    protected String resolveRootDicon() throws Throwable {
        String targetName = getTargetName();
        if (targetName.length() > 0) {
            try {
                Method method = getClass().getMethod(
                        "getRootDicon" + targetName, null);
                return (String) method.invoke(this, null);
            } catch (Exception ignore) {
            }
        }
        return getRootDicon();
    }

    /**
     * S2コンテナの終了処理を行ないます。
     * 
     * @throws Throwable
     *             例外が発生した場合
     */
    protected void tearDownContainer() throws Throwable {
        ComponentDeployerFactory
                .setProvider(new ComponentDeployerFactory.DefaultProvider());
        SingletonS2ContainerFactory.setContainer(null);
        S2ContainerServlet.clearInstance();
        MessageResourceBundleFactory.clear();
        DisposableUtil.dispose();
        S2ContainerBehavior
                .setProvider(new S2ContainerBehavior.DefaultProvider());
        Thread.currentThread().setContextClassLoader(originalClassLoader);
        unitClassLoader = null;
        originalClassLoader = null;
        container = null;
        servletContext = null;
        request = null;
        response = null;
        servletConfig = null;
        servlet = null;
        namingConvention = null;
        Env.initialize();
    }

    /**
     * S2コンテナが初期化された後のセットアップを行ないます。
     * 
     * @throws Throwable
     *             例外が発生した場合
     */
    protected void setUpAfterContainerInit() throws Throwable {
    }

    /**
     * フィールドのバインディング後のセットアップを行ないます。
     * 
     * @throws Throwable
     *             例外が発生した場合
     */
    protected void setUpAfterBindFields() throws Throwable {
    }

    /**
     * フィールドがアンバインディングされる前に終了処理を行ないます。
     * 
     * @throws Throwable
     *             例外が発生した場合
     */
    protected void tearDownBeforeUnbindFields() throws Throwable {
    }

    /**
     * テストメソッドごとのセットアップを行ないます。
     * 
     * @throws Throwable
     *             例外が発生した場合
     */
    protected void setUpForEachTestMethod() throws Throwable {
        String targetName = getTargetName();
        if (targetName.length() > 0) {
            invoke("setUp" + targetName);
        }
    }

    /**
     * S2コンテナが破棄される前に終了処理を行ないます。
     * 
     * @throws Throwable
     *             例外が発生した場合
     */
    protected void tearDownBeforeContainerDestroy() throws Throwable {
    }

    /**
     * テストメソッドごとの終了処理を行ないます。
     * 
     * @throws Throwable
     *             例外が発生した場合
     */
    protected void tearDownForEachTestMethod() throws Throwable {
        String targetName = getTargetName();
        if (targetName.length() > 0) {
            invoke("tearDown" + getTargetName());
        }
    }

    /**
     * テストを実行します。
     * 
     * @throws Throwable
     *             例外が発生した場合
     */
    protected void doRunTest() throws Throwable {
        runTest();
    }

    /**
     * サーブレットを返します。
     * 
     * @return サーブレット
     */
    protected Servlet getServlet() {
        return servlet;
    }

    /**
     * サーブレットを設定します。
     * 
     * @param servlet
     *            サーブレット
     */
    protected void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

    /**
     * サーブレットの設定を返します。
     * 
     * @return サーブレットの設定
     */
    protected MockServletConfig getServletConfig() {
        return servletConfig;
    }

    /**
     * サーブレットの設定を設定します。
     * 
     * @param servletConfig
     *            サーブレットの設定
     */
    protected void setServletConfig(MockServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    /**
     * サーブレットコンテキストを返します。
     * 
     * @return サーブレットコンテキスト
     */
    protected MockServletContext getServletContext() {
        return servletContext;
    }

    /**
     * サーブレットコンテキストを設定します。
     * 
     * @param servletContext
     *            サーブレットコンテキスト
     */
    protected void setServletContext(MockServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * リクエストを返します。
     * 
     * @return リクエスト
     */
    protected MockHttpServletRequest getRequest() {
        return request;
    }

    /**
     * リクエストを設定します。
     * 
     * @param request
     *            リクエスト
     */
    protected void setRequest(MockHttpServletRequest request) {
        this.request = request;
    }

    /**
     * レスポンスを返します。
     * 
     * @return レスポンス
     */
    protected MockHttpServletResponse getResponse() {
        return response;
    }

    /**
     * @param response
     */
    protected void setResponse(MockHttpServletResponse response) {
        this.response = response;
    }

    /**
     * 命名規約を返します。
     * 
     * @return 命名規約
     */
    protected NamingConvention getNamingConvention() {
        return namingConvention;
    }

    /**
     * ターゲット名を返します。
     * 
     * @return ターゲット名
     */
    protected String getTargetName() {
        return getName().substring(4);
    }

    /**
     * ターゲットメソッドを返します。
     * 
     * @return ターゲットメソッド
     */
    protected Method getTargetMethod() {
        return ClassUtil.getMethod(getClass(), getName(), null);
    }

    /**
     * メソッドを実行します。
     * 
     * @param methodName
     *            メソッド名
     * @return 実行結果
     * @throws Throwable
     *             例外が発生した場合
     */
    protected Object invoke(String methodName) throws Throwable {
        try {
            Method method = ClassUtil.getMethod(getClass(), methodName, null);
            return MethodUtil.invoke(method, this, null);
        } catch (NoSuchMethodRuntimeException ignore) {
            return null;
        }
    }

    /**
     * フィールドにコンポーネントをバインドします。
     * 
     * @throws Throwable
     *             例外が発生した場合
     */
    protected void bindFields() throws Throwable {
        boundFields = new ArrayList();
        for (Class clazz = getClass(); clazz != S2FrameworkTestCase.class
                && clazz != null; clazz = clazz.getSuperclass()) {

            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                bindField(fields[i]);
            }
        }
    }

    /**
     * フィールドにコンポーネントをバインドします。
     * 
     * @param field
     *            フィールド
     */
    protected void bindField(Field field) {
        if (isAutoBindable(field)) {
            field.setAccessible(true);
            if (FieldUtil.get(field, this) != null) {
                return;
            }
            String name = normalizeName(field.getName());
            Object component = null;
            if (getContainer().hasComponentDef(name)) {
                Class componentClass = getComponentDef(name)
                        .getComponentClass();
                if (componentClass == null) {
                    component = getComponent(name);
                    if (component != null) {
                        componentClass = component.getClass();
                    }
                }
                if (componentClass != null
                        && field.getType().isAssignableFrom(componentClass)) {
                    if (component == null) {
                        component = getComponent(name);
                    }
                } else {
                    component = null;
                }
            }
            if (component == null
                    && getContainer().hasComponentDef(field.getType())) {
                component = getComponent(field.getType());
            }
            if (component != null) {
                FieldUtil.set(field, this, component);
                boundFields.add(field);
            }
        }
    }

    /**
     * 名前を正規化します。
     * 
     * @param name
     *            名前
     * @return 正規化された名前
     */
    protected String normalizeName(String name) {
        return StringUtil.replace(name, "_", "");
    }

    /**
     * 自動バインディング可能かどうか返します。
     * 
     * @param field
     *            フィールド
     * @return 自動バインディング可能かどうか
     */
    protected boolean isAutoBindable(Field field) {
        int modifiers = field.getModifiers();
        return !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)
                && !field.getType().isPrimitive();
    }

    /**
     * バインディングされた情報をクリアします。
     */
    protected void unbindFields() {
        for (int i = 0; i < boundFields.size(); ++i) {
            Field field = (Field) boundFields.get(i);
            try {
                field.set(this, null);
            } catch (IllegalArgumentException e) {
                System.err.println(e);
            } catch (IllegalAccessException e) {
                System.err.println(e);
            }
        }
        boundFields = null;
    }

}
