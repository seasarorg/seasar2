/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.unit.impl.ConventionTestIntrospector;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * テストクラスに定義されたすべてのテストメソッドを扱うランナーです。
 * 
 * @author taedium
 */
public class S2TestClassMethodsRunner extends Runner implements Filterable,
        Sortable {

    private static class FailedBefore extends Exception {

        private static final long serialVersionUID = 1L;
    }

    /** {@link S2TestClassMethodsRunner}の振る舞いを提供するプロバイダ */
    protected static Provider provider;

    /** テストメソッドのリスト */
    protected final List<Method> testMethods;

    /** テストクラス */
    protected final Class<?> testClass;

    /**
     * インスタンスを構築します。
     * 
     * @param clazz
     *            テストクラス
     */
    public S2TestClassMethodsRunner(final Class<?> clazz) {
        testClass = clazz;
        testMethods = getTestMethods();
    }

    /**
     * このクラスを破棄します。
     */
    public static void dispose() {
        provider = null;
    }

    @Override
    public void run(final RunNotifier notifier) {
        try {
            runBefores(notifier);
            runMethods(notifier);
        } catch (final FailedBefore e) {
        } finally {
            runAfters(notifier);
        }
    }

    /**
     * テストメソッド群を実行します。
     *
     * @param notifier
     *            ノティファイアー
     */
    protected void runMethods(final RunNotifier notifier) {
        if (testMethods.isEmpty()) {
            RunNotifierCompatibility.testAborted(notifier,
                    getDescription(), new Exception("No runnable methods"));
        }
        for (final Method method : testMethods) {
            invokeTestMethod(method, notifier);
        }
    }

    /**
     * テストクラスの初期化メソッドを実行します。
     * 
     * @param notifier
     *            ノティファイアー
     * @throws FailedBefore
     *             何らかの例外が発生した場合
     */
    protected void runBefores(final RunNotifier notifier) throws FailedBefore {
        try {
            final List<Method> befores = getBeforeClassMethods();
            for (final Method before : befores)
                before.invoke(null);
        } catch (final InvocationTargetException e) {
            addFailure(e.getTargetException(), notifier);
            throw new FailedBefore();
        } catch (final Throwable e) {
            addFailure(e, notifier);
            throw new FailedBefore();
        }
    }

    /**
     * テストクラスの解放メソッドを実行します。
     * 
     * @param notifier
     *            ノティフィアー
     */
    protected void runAfters(final RunNotifier notifier) {
        final List<Method> afters = getAfterClassMethods();
        for (final Method after : afters)
            try {
                after.invoke(null);
            } catch (final InvocationTargetException e) {
                addFailure(e.getTargetException(), notifier);
            } catch (final Throwable e) {
                addFailure(e, notifier);
            }
    }

    /**
     * テストの失敗を登録します。
     * 
     * @param targetException
     *            例外
     * @param notifier
     *            ノティフィアー
     */
    protected void addFailure(final Throwable targetException,
            final RunNotifier notifier) {

        final Failure failure = new Failure(getDescription(), targetException);
        notifier.fireTestFailure(failure);
    }

    /**
     * テストメソッドのリストを返します。
     * 
     * @return テストメソッドのリスト
     */
    protected List<Method> getTestMethods() {
        return getProvider().getTestMethods(testClass);
    }

    /**
     * テストクラスの初期化メソッドのリストを返します。
     * 
     * @return 初期化メソッドのリスト
     */
    protected List<Method> getBeforeClassMethods() {
        return getProvider().getBeforeClassMethods(testClass);
    }

    /**
     * テストクラスの解放メソッドのリストを返します。
     * 
     * @return 解放メソッドのリスト
     */
    protected List<Method> getAfterClassMethods() {
        return getProvider().getAfterClassMethods(testClass);
    }

    @Override
    public Description getDescription() {
        final Description spec = Description.createSuiteDescription(getName());
        for (final Method method : testMethods)
            spec.addChild(methodDescription(method));
        return spec;
    }

    /**
     * テストクラスの名前を返します。
     * 
     * @return テストクラスの名前
     */
    protected String getName() {
        return getTestClass().getName();
    }

    /**
     * テストクラスのインスタンスを作成します。
     * 
     * @return テストクラスのインスタンス
     * @throws Exception
     *             何らかの例外が発生した場合
     */
    protected Object createTest() throws Exception {
        return getTestClass().getConstructor().newInstance();
    }

    /**
     * テストメソッドを実行します。
     * 
     * @param method
     *            テストメソッド
     * @param notifier
     *            ノティファイアー
     */
    protected void invokeTestMethod(final Method method,
            final RunNotifier notifier) {
        Object test = null;
        try {
            test = createTest();
        } catch (final InvocationTargetException e) {
            RunNotifierCompatibility.testAborted(notifier,
                    methodDescription(method), e.getCause());
            return;
        } catch (final Exception e) {
            RunNotifierCompatibility.testAborted(notifier,
                    methodDescription(method), e);
            return;
        }
        createMethodRunner(test, method, notifier).run();
    }

    /**
     * テストメソッドランナーを作成します。
     * 
     * @param test
     *            テスト
     * @param method
     *            テストメソッド
     * @param notifier
     *            ノティファイアー
     * @return テストメソッドランナー
     */
    protected S2TestMethodRunner createMethodRunner(final Object test,
            final Method method, RunNotifier notifier) {

        return getProvider().createMethodRunner(test, method, notifier,
                methodDescription(method));
    }

    /**
     * テストメソッドの名前を返します。
     * 
     * @param method
     *            テストメソッド
     * @return テストメソッドの名前
     */
    protected String testName(final Method method) {
        return method.getName();
    }

    /**
     * テストのディスクリプションを返します。
     * 
     * @param method
     *            テストメソッド
     * @return ディスクリプション
     */
    protected Description methodDescription(final Method method) {
        return Description.createTestDescription(getTestClass(),
                testName(method));
    }

    public void filter(final Filter filter) throws NoTestsRemainException {
        for (final Iterator<Method> iter = testMethods.iterator(); iter
                .hasNext();) {
            final Method method = iter.next();
            if (!filter.shouldRun(methodDescription(method))) {
                iter.remove();
            }
        }
        if (testMethods.isEmpty()) {
            throw new NoTestsRemainException();
        }
    }

    public void sort(final Sorter sorter) {
        Collections.sort(testMethods, new Comparator<Method>() {

            public int compare(final Method o1, final Method o2) {
                return sorter.compare(methodDescription(o1),
                        methodDescription(o2));
            }
        });
    }

    /**
     * テストクラスを返します。
     * 
     * @return テストクラス
     */
    protected Class<?> getTestClass() {
        return testClass;
    }

    /**
     * {@link S2TestClassMethodsRunner}の振る舞いを提供するプロバイダを返します。
     * 
     * @return 振る舞いを提供するプロバイダ
     */
    protected static Provider getProvider() {
        if (provider == null) {
            provider = new DefaultProvider();
        }
        return provider;
    }

    /**
     * {@link S2TestClassMethodsRunner}の振る舞いを提供するプロバイダを設定します。
     * 
     * @param p
     *            振る舞いを提供するプロバイダ
     */
    protected static void setProvider(final Provider p) {
        provider = p;
    }

    /**
     * {@link S2TestClassMethodsRunner}の振る舞いを提供します。
     * 
     * @author taedium
     */
    public interface Provider {

        /**
         * テストメソッドのリストを返します。
         * 
         * @param clazz
         *            テストクラス
         * @return テストメソッドのリスト
         */
        List<Method> getTestMethods(Class<?> clazz);

        /**
         * テストクラスの初期化メソッドのリストを返します。
         * 
         * @param clazz
         *            テストクラス
         * @return 初期化メソッド
         */
        List<Method> getBeforeClassMethods(Class<?> clazz);

        /**
         * テストクラスの解放メソッドのリストを返します。
         * 
         * @param clazz
         *            テストクラス
         * @return 解放メソッドのリスト
         */
        List<Method> getAfterClassMethods(Class<?> clazz);

        /**
         * テストメソッドランナーを作成します。
         * 
         * @param test
         *            テストクラスのインスタンス
         * @param method
         *            テストメソッド
         * @param notifier
         *            ノティファイアー
         * @param description
         *            ディスクリプション
         * @return テストメソッドランナー
         */
        S2TestMethodRunner createMethodRunner(Object test, Method method,
                RunNotifier notifier, Description description);
    }

    /**
     * {@link S2TestClassMethodsRunner}の振る舞いを提供するデフォルトの実装クラスです。
     * 
     * @author taedium
     */
    public static class DefaultProvider implements Provider {

        /** テストクラスのイントロスペクター */
        protected S2TestIntrospector introspector;

        /** メソッドランナー */
        protected Class<? extends S2TestMethodRunner> methodRunnerClass;

        /** メソッドランナーのコンストラクタ */
        protected Constructor<? extends S2TestMethodRunner> constructor;

        /**
         * インスタンスを構築します。
         */
        public DefaultProvider() {
            final ConventionTestIntrospector conventionIntrospector = new ConventionTestIntrospector();
            conventionIntrospector.init();
            this.introspector = conventionIntrospector;
            setTestMethodRunnerClass(S2TestMethodRunner.class);
        }

        /**
         * テストクラスのイントロスペクターを設定します。
         * 
         * @param introspector
         *            イントロスペクター
         */
        @Binding(bindingType = BindingType.MAY)
        public void setTestIntrospector(final S2TestIntrospector introspector) {
            this.introspector = introspector;
        }

        /**
         * メソッドランナーのクラスを設定します。
         * 
         * @param methodRunnerClass
         *            メソッドランナーのクラス
         */
        @Binding(bindingType = BindingType.MAY)
        public void setTestMethodRunnerClass(
                final Class<? extends S2TestMethodRunner> methodRunnerClass) {

            this.methodRunnerClass = methodRunnerClass;
            this.constructor = ReflectionUtil.getConstructor(methodRunnerClass,
                    Object.class, Method.class, RunNotifier.class,
                    Description.class, S2TestIntrospector.class);
        }

        public List<Method> getTestMethods(final Class<?> clazz) {
            return introspector.getTestMethods(clazz);
        }

        public List<Method> getBeforeClassMethods(final Class<?> clazz) {
            return introspector.getBeforeClassMethods(clazz);
        }

        public List<Method> getAfterClassMethods(final Class<?> clazz) {
            return introspector.getAfterClassMethods(clazz);
        }

        public S2TestMethodRunner createMethodRunner(final Object test,
                final Method method, final RunNotifier notifier,
                final Description description) {

            return ReflectionUtil.newInstance(constructor, test, method,
                    notifier, description, introspector);
        }

    }
}
