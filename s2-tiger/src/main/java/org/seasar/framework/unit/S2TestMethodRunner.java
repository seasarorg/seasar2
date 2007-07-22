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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ejb.EJB;
import javax.transaction.TransactionManager;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.convention.impl.NamingConventionImpl;
import org.seasar.framework.env.Env;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * テストメソッドを扱うランナーです。
 * 
 * @author taedium
 */
public class S2TestMethodRunner {

    private static class FailedBefore extends Exception {

        private static final long serialVersionUID = 1L;
    }

    /** S2JUnit4のデフォルトの設定ファイルのパス */
    protected static final String DEFAULT_S2JUNIT4_PATH = "s2junit4.dicon";

    /** このランナーで使用する環境名設定ファイルのパス */
    protected static final String ENV_PATH = "env_ut.txt";

    /** 環境名設定ファイルのパスにファイルが存在しない場合の環境名 */
    protected static final String ENV_VALUE = "ut";

    /** S2JUnit4の設定ファイルのパス */
    protected static String s2junit4Path = DEFAULT_S2JUNIT4_PATH;

    /** テストオブジェクト */
    protected final Object test;

    /** テストクラス */
    protected final Class<?> testClass;

    /** テストメソッド */
    protected final Method method;

    /** ノティファイアー */
    protected final RunNotifier notifier;

    /** テストのディスクリプション */
    protected final Description description;

    /** テストクラスのイントロスペクター */
    protected final S2TestIntrospector introspector;

    /** {@link #unitClassLoader テストで使用するクラスローダー}で置き換えられる前のオリジナルのクラスローダー */
    protected ClassLoader originalClassLoader;

    /** テストで使用するクラスローダー */
    protected UnitClassLoader unitClassLoader;

    /** S2JUnit4の内部的なテストコンテキスト */
    protected InternalTestContext testContext;

    /** バインディングが行われたフィールドのリスト */
    private List<Field> boundFields = CollectionsUtil.newArrayList();

    /** EasyMockとの対話をサポートするオブジェクト */
    protected EasyMockSupport easyMockSupport = new EasyMockSupport();

    /** テストが失敗したことを表すフラグ */
    protected boolean testFailed;

    /**
     * インスタンスを構築します。
     * 
     * @param test
     *            テストクラスのインスタンス
     * @param method
     *            テストメソッド
     * @param notifier
     *            ノティファイアー
     * @param description
     *            テストのディスクリプション
     * @param introspector
     *            テストクラスのイントロスペクター
     */
    public S2TestMethodRunner(final Object test, final Method method,
            final RunNotifier notifier, final Description description,
            final S2TestIntrospector introspector) {
        this.test = test;
        this.testClass = test.getClass();
        this.method = method;
        this.notifier = notifier;
        this.description = description;
        this.introspector = introspector;
        this.notifier.addListener(new RunListener() {

            @Override
            public void testFailure(Failure failure) throws Exception {
                testFailed = true;
            }
        });
    }

    /**
     * テストの失敗を登録します。
     * 
     * @param e
     *            失敗を表すスロー可能オブジェクト
     */
    protected void addFailure(final Throwable e) {
        final Failure failure = new Failure(description, e);
        notifier.fireTestFailure(failure);
    }

    /**
     * このランナーを起動します。
     */
    public void run() {
        try {
            Env.setFilePath(ENV_PATH);
            Env.setValueIfAbsent(ENV_VALUE);
            if (isIgnored() || !isFulfilled()) {
                notifier.fireTestIgnored(description);
                return;
            }
            notifier.fireTestStarted(description);
            try {
                final long timeout = introspector.getTimeout(method);
                if (timeout > 0) {
                    runWithTimeout(timeout);
                } else {
                    runMethod();
                }
            } finally {
                notifier.fireTestFinished(description);
            }
        } finally {
            Env.initialize();
        }
    }

    /**
     * 無視の対象の場合<code>true</code>を返します。
     * 
     * @return 無視の対象の場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean isIgnored() {
        return introspector.isIgnored(method);
    }

    /**
     * 事前条件が満たされる場合<code>true</code>を返します。
     * 
     * @return 事前条件が満たされる場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean isFulfilled() {
        return introspector.isFulfilled(testClass, method, test);
    }

    /**
     * タイムアウトのミリ秒を指定してテストを実行します。
     * 
     * @param timeout
     *            タイムアウトのミリ秒
     */
    protected void runWithTimeout(final long timeout) {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        final Callable<Object> callable = new Callable<Object>() {

            public Object call() throws Exception {
                runMethod();
                return null;
            }
        };
        final Future<Object> result = service.submit(callable);
        service.shutdown();
        try {
            final boolean terminated = service.awaitTermination(timeout,
                    TimeUnit.MILLISECONDS);
            if (!terminated) {
                service.shutdownNow();
            }
            result.get(0, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            addFailure(new Exception(String.format(
                    "test timed out after %d milliseconds", timeout)));
        } catch (Exception e) {
            addFailure(e);
        }
    }

    /**
     * テストケースを実行します。
     * <p>
     * テストの実行に必要な事前処理と事後処理を行います。
     * </p>
     */
    protected void runMethod() {
        easyMockSupport.clear();
        try {
            setUpTestContext();
            try {
                runBefores();
                try {
                    runEachBefore();
                    initContainer();
                    try {
                        bindFields();
                        try {
                            final boolean recorded = runEachRecord();
                            if (recorded) {
                                easyMockSupport.replay();
                            }
                            runTest();
                            if (recorded) {
                                easyMockSupport.verify();
                                easyMockSupport.reset();
                            }
                        } finally {
                            unbindFields();
                        }
                    } finally {
                        testContext.destroyContainer();
                    }
                } catch (final FailedBefore e) {
                } catch (final Throwable e) {
                    addFailure(e);
                } finally {
                    runEachAfter();
                }
            } catch (final FailedBefore e) {
            } finally {
                runAfters();
                tearDownTestContext();
            }
        } catch (final Throwable e) {
            addFailure(e);
        }
    }

    /**
     * テストコンテキストをセットアップします。
     * 
     * @throws Throwable
     *             何らかの例外またはエラーが起きた場合
     */
    protected void setUpTestContext() throws Throwable {
        originalClassLoader = getOriginalClassLoader();
        unitClassLoader = new UnitClassLoader(originalClassLoader);
        Thread.currentThread().setContextClassLoader(unitClassLoader);
        if (needsWarmDeploy()) {
            S2ContainerFactory.configure("warmdeploy.dicon");
        }
        final S2Container container = createRootContainer();
        SingletonS2ContainerFactory.setContainer(container);
        testContext = InternalTestContext.class.cast(container
                .getComponent(InternalTestContext.class));
        testContext.setTestClass(testClass);
        testContext.setTestMethod(method);
        if (!testContext.hasComponentDef(NamingConvention.class)
                && introspector.isRegisterNamingConvention(testClass, method)) {
            final NamingConvention namingConvention = new NamingConventionImpl();
            testContext.register(namingConvention);
            testContext.setNamingConvention(namingConvention);
        }

        for (Class<?> clazz = testClass; clazz != Object.class; clazz = clazz
                .getSuperclass()) {

            final Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                final Field field = fields[i];
                if (isAutoBindable(field)
                        && field.getType() == TestContext.class) {
                    field.setAccessible(true);
                    if (ReflectionUtil.getValue(field, test) != null) {
                        continue;
                    }
                    bindField(field, testContext);
                }
            }
        }
    }

    /**
     * オリジナルのクラスローダーを返します。
     * 
     * @return オリジナルのクラスローダー
     */
    protected ClassLoader getOriginalClassLoader() {
        S2Container configurationContainer = S2ContainerFactory
                .getConfigurationContainer();
        if (configurationContainer != null
                && configurationContainer.hasComponentDef(ClassLoader.class)) {
            return ClassLoader.class.cast(configurationContainer
                    .getComponent(ClassLoader.class));
        }
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * ルートのコンテナを返します。
     * 
     * @return ルートのコンテナ
     */
    protected S2Container createRootContainer() {
        final String rootDicon = introspector.getRootDicon(testClass, method);
        if (StringUtil.isEmpty(rootDicon)) {
            return S2ContainerFactory.create(s2junit4Path);
        }
        S2Container container = S2ContainerFactory.create(rootDicon);
        S2ContainerFactory.include(container, s2junit4Path);
        return container;

    }

    /**
     * テストコンテキストを解放します。
     * 
     * @throws Throwable
     *             何らかの例外またはエラーが起きた場合
     */
    protected void tearDownTestContext() throws Throwable {
        testContext = null;
        DisposableUtil.dispose();
        S2ContainerBehavior
                .setProvider(new S2ContainerBehavior.DefaultProvider());
        Thread.currentThread().setContextClassLoader(originalClassLoader);
        unitClassLoader = null;
        originalClassLoader = null;
    }

    /**
     * すべてのテストケース共通の初期化処理を実行します。
     * 
     * @throws FailedBefore
     *             何らかの例外またはエラーが発生した場合
     */
    protected void runBefores() throws FailedBefore {
        try {
            final List<Method> befores = introspector
                    .getBeforeMethods(testClass);
            for (final Method before : befores) {
                before.invoke(test);
            }
        } catch (final InvocationTargetException e) {
            addFailure(e.getTargetException());
            throw new FailedBefore();
        } catch (final Throwable e) {
            addFailure(e);
            throw new FailedBefore();
        }
    }

    /**
     * すべてのテストケース共通の解放処理を実行します。
     */
    protected void runAfters() {
        final List<Method> afters = introspector.getAfterMethods(testClass);
        for (final Method after : afters) {
            try {
                after.invoke(test);
            } catch (final InvocationTargetException e) {
                addFailure(e.getTargetException());
            } catch (final Throwable e) {
                addFailure(e);
            }
        }
    }

    /**
     * テストケース個別の初期化メソッドを実行します。
     * 
     * @throws FailedBefore
     *             何らかの例外またはエラーが発生した場合
     */
    protected void runEachBefore() throws FailedBefore {
        try {
            final Method eachBefore = introspector.getEachBeforeMethod(
                    testClass, method);
            if (eachBefore != null) {
                invokeMethod(eachBefore);
            }
        } catch (final Throwable e) {
            addFailure(e);
            throw new FailedBefore();
        }
        easyMockSupport.bindMockFields(test, testContext.getContainer());
    }

    /**
     * テストケース個別の解放メソッドを実行します。
     */
    protected void runEachAfter() {
        easyMockSupport.unbindMockFields(test);
        try {
            final Method eachAfter = introspector.getEachAfterMethod(testClass,
                    method);
            if (eachAfter != null) {
                invokeMethod(eachAfter);
            }
        } catch (final Throwable e) {
            addFailure(e);
        }
    }

    /**
     * コンテナを初期化します。
     */
    protected void initContainer() {
        testContext.include();
        introspector.createMock(method, test, testContext);
        testContext.initContainer();
    }

    /**
     * フィールドにコンポーネントをバインディングします。
     * 
     * @throws Throwable
     *             何らかの例外またはエラーが発生した場合
     */
    protected void bindFields() throws Throwable {
        for (Class<?> clazz = testClass; clazz != Object.class; clazz = clazz
                .getSuperclass()) {

            final Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; ++i) {
                bindField(fields[i]);
            }
        }
    }

    /**
     * 指定されたフィールドにコンポーネントをバインディングします。
     * 
     * @param field
     *            フィールド
     */
    protected void bindField(final Field field) {
        if (isAutoBindable(field)) {
            field.setAccessible(true);
            if (ReflectionUtil.getValue(field, test) != null) {
                return;
            }
            final String name = resolveComponentName(field);
            Object component = null;
            if (testContext.hasComponentDef(name)) {
                Class<?> componentClass = testContext.getComponentDef(name)
                        .getComponentClass();
                if (componentClass == null) {
                    component = testContext.getComponent(name);
                    if (component != null) {
                        componentClass = component.getClass();
                    }
                }
                if (componentClass != null
                        && field.getType().isAssignableFrom(componentClass)) {
                    if (component == null) {
                        component = testContext.getComponent(name);
                    }
                } else {
                    component = null;
                }
            }
            if (component == null
                    && testContext.hasComponentDef(field.getType())) {
                component = testContext.getComponent(field.getType());
            }
            if (component != null) {
                bindField(field, component);
            }
        }
    }

    /**
     * 指定されたフィールドに指定された値をバインディングします。
     * 
     * @param field
     *            フィールド
     * @param object
     *            値
     */
    protected void bindField(final Field field, final Object object) {
        ReflectionUtil.setValue(field, test, object);
        boundFields.add(field);
    }

    /**
     * 自動フィールドバインディングが可能な場合<code>true</code>を返します。
     * 
     * @param field
     *            フィールド
     * @return 自動フィールドバインディングが可能な場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean isAutoBindable(final Field field) {
        final int modifiers = field.getModifiers();
        return !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)
                && !field.getType().isPrimitive();
    }

    /**
     * フィールドからコンポーネントの名前を解決します。
     * 
     * @param filed
     *            フィールド
     * @return コンポーネント名
     */
    protected String resolveComponentName(final Field filed) {
        if (testContext.isEjb3Enabled()) {
            final EJB ejb = filed.getAnnotation(EJB.class);
            if (ejb != null) {
                if (!StringUtil.isEmpty(ejb.beanName())) {
                    return ejb.beanName();
                } else if (!StringUtil.isEmpty(ejb.name())) {
                    return ejb.name();
                }
            }
        }
        return normalizeName(filed.getName());
    }

    /**
     * コンポーネント名を正規化します。
     * 
     * @param name
     *            コンポーネント名
     * @return 正規化されたコンポーネント名
     */
    protected String normalizeName(final String name) {
        return StringUtil.replace(name, "_", "");
    }

    /**
     * テストケース個別の登録メソッド存在する場合、登録メソッドを実行し<code>true</code>を返します。
     * 
     * @return 登録メソッドが存在する場合<code>true</code>、存在しない場合<code>false</code>
     * @throws Throwable
     *             何らかの例外またはエラーが発生した場合
     */
    protected boolean runEachRecord() throws Throwable {
        final Method recordMethod = introspector.getEachRecordMethod(testClass,
                method);
        if (recordMethod != null) {
            invokeMethod(recordMethod);
            return true;
        }
        return false;
    }

    /**
     * テストを実行します。
     * <p>
     * JTAが利用可能な場合、トランザクションの制御とテストデータの準備を行います。
     * </p>
     * 
     * @throws Throwable
     *             何らかの例外またはエラーが発生した場合
     */
    protected void runTest() throws Throwable {
        if (!testContext.isJtaEnabled()) {
            executeMethod();
            return;
        }
        TransactionManager tm = null;
        if (introspector.needsTransaction(testClass, method)) {
            try {
                tm = testContext.getComponent(TransactionManager.class);
                tm.begin();
            } catch (Throwable t) {
                System.err.println(t);
            }
        }
        try {
            testContext.prepareTestData();
            executeMethod();
        } finally {
            if (tm != null) {
                if (requiresTransactionCommitment()) {
                    tm.commit();
                } else {
                    tm.rollback();
                }
            }
        }
    }

    /**
     * テストが失敗していない場合かつトランザクションをコミットするように設定されている場合に<code>true</code>を返します。
     * 
     * @return テストが失敗していない場合かつトランザクションをコミットするように設定されている場合に<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean requiresTransactionCommitment() {
        return !testFailed
                && introspector
                        .requiresTransactionCommitment(testClass, method);
    }

    /**
     * テストメソッドを実行します。
     * <p>
     * 期待される例外またはエラーが存在するか、存在する場合その例外またはエラーがスローされたかを確認します。
     * </p>
     * 
     * @throws Throwable
     *             何らかの例外またはエラーが発生した場合
     */
    protected void executeMethod() throws Throwable {
        try {
            executeMethodBody();
            if (expectsException()) {
                addFailure(new AssertionError("Expected exception: "
                        + expectedException().getName()));
            }
        } catch (final InvocationTargetException e) {
            final Throwable actual = e.getTargetException();
            if (!expectsException()) {
                addFailure(actual);
            } else if (isUnexpected(actual)) {
                String message = "Unexpected exception, expected<"
                        + expectedException().getName() + "> but was<"
                        + actual.getClass().getName() + ">";
                addFailure(new Exception(message, actual));
            }
        }
    }

    /**
     * テストメソッド本体を実行します。
     * 
     * @throws Throwable
     *             何らかの例外またはエラーが発生した場合
     */
    protected void executeMethodBody() throws Throwable {
        method.invoke(test);
    }

    /**
     * テストの実行で例外が発生することが期待されている場合<code>true</code>を返します。
     * 
     * @return テストの実行で例外が発生することが期待されている場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean expectsException() {
        return expectedException() != null;
    }

    /**
     * 期待していない例外もしくはエラーの場合<code>true</code>を返します。
     * 
     * @param exception
     *            例外もしくはエラー
     * @return 期待されていない例外の場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean isUnexpected(final Throwable exception) {
        return !expectedException().isAssignableFrom(exception.getClass());
    }

    /**
     * 発生すると期待されているエラーもしくは例外のクラスを返します。
     * 
     * @return 発生すると期待されているエラーもしくは例外のクラスがある場合そのクラス、ない場合<code>null</code>
     */
    protected Class<? extends Throwable> expectedException() {
        return introspector.expectedException(method);
    }

    /**
     * 指定されたメソッドを実行します。
     * 
     * @param method
     *            メソッド
     * @throws Throwable
     *             何らかの例外またはエラーが発生した場合
     */
    protected void invokeMethod(final Method method) throws Throwable {
        try {
            ReflectionUtil.invoke(method, test);
        } catch (NoSuchMethodRuntimeException ignore) {
        }
    }

    /**
     * フィールドとコンポーネントのバインディングを解除します。
     */
    protected void unbindFields() {
        for (final Field field : boundFields) {
            try {
                field.set(test, null);
            } catch (IllegalArgumentException e) {
                System.err.println(e);
            } catch (IllegalAccessException e) {
                System.err.println(e);
            }
        }
        boundFields = null;
    }

    /**
     * WARM deployが必要とされる場合<code>true</code>を返します。
     * 
     * @return WARM deployが必要とされる場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean needsWarmDeploy() {
        return introspector.needsWarmDeploy(testClass, method)
                && !ResourceUtil.isExist("s2container.dicon")
                && ResourceUtil.isExist("convention.dicon")
                && ResourceUtil.isExist("creator.dicon")
                && ResourceUtil.isExist("customizer.dicon");
    }
}
