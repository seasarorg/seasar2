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
package org.seasar.framework.unit.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Test.None;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.interceptors.MockInterceptor;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.factory.AspectDefFactory;
import org.seasar.framework.env.Env;
import org.seasar.framework.unit.Expression;
import org.seasar.framework.unit.InternalTestContext;
import org.seasar.framework.unit.S2TestIntrospector;
import org.seasar.framework.unit.annotation.Mock;
import org.seasar.framework.unit.annotation.Mocks;
import org.seasar.framework.unit.annotation.PostBindFields;
import org.seasar.framework.unit.annotation.PreUnbindFields;
import org.seasar.framework.unit.annotation.Prerequisite;
import org.seasar.framework.unit.annotation.RegisterNamingConvention;
import org.seasar.framework.unit.annotation.RootDicon;
import org.seasar.framework.unit.annotation.TxBehavior;
import org.seasar.framework.unit.annotation.TxBehaviorType;
import org.seasar.framework.unit.annotation.WarmDeploy;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * アノテーションを解釈してテストクラスを分析するイントロスペクターです。
 * 
 * @author taedium
 */
public class AnnotationTestIntrospector implements S2TestIntrospector {

    /** テストクラスの初期化メソッドに注釈可能なアノテーションクラス */
    protected Class<? extends Annotation> beforeClassAnnotation = BeforeClass.class;

    /** テストクラスの解放メソッドに注釈可能なアノテーションクラス */
    protected Class<? extends Annotation> afterClassAnnotation = AfterClass.class;

    /** 全テストケースに共通の初期化メソッドに注釈可能なアノテーションクラス */
    protected Class<? extends Annotation> beforeAnnotation = Before.class;

    /** 全テストケースに共通の解放メソッドに注釈可能なアノテーションクラス */
    protected Class<? extends Annotation> afterAnnotation = After.class;

    /** テストクラスのバインドフィールド直後のメソッドに注釈可能なアノテーションクラス */
    protected Class<? extends Annotation> postBindFieldsAnnotation = PostBindFields.class;

    /** テストクラスのアンバインドフィールド直前のメソッドメソッドに注釈可能なアノテーションクラス */
    protected Class<? extends Annotation> preUnbindFieldsAnnotation = PreUnbindFields.class;

    /** テストケースを無視する処理が有効かどうかを表すフラグ。デフォルトは<code>true</code> */
    protected boolean enableIgnore = true;

    /** テストケースの事前条件チェック処理が有効かどうかを表すフラグ。デフォルトは<code>true</code> */
    protected boolean enablePrerequisite = true;

    /**
     * テストクラスの初期化メソッドに注釈可能なアノテーションクラスを設定します。
     * 
     * @param beforeClassAnnotation
     *            アノテーションクラス
     */
    public void setBeforeClassAnnotation(
            final Class<? extends Annotation> beforeClassAnnotation) {
        this.beforeClassAnnotation = beforeClassAnnotation;
    }

    /**
     * テストクラスの解放メソッドに注釈可能なアノテーションクラスを設定します。
     * 
     * @param afterClassAnnotation
     *            アノテーションクラス
     */
    public void setAfterClassAnnotation(
            final Class<? extends Annotation> afterClassAnnotation) {
        this.afterClassAnnotation = afterClassAnnotation;
    }

    /**
     * 全テストケースに共通の初期化メソッドに注釈可能なアノテーションクラスを設定します。
     * 
     * @param beforeAnnotation
     *            アノテーションクラス
     */
    public void setBeforeAnnotation(
            final Class<? extends Annotation> beforeAnnotation) {
        this.beforeAnnotation = beforeAnnotation;
    }

    /**
     * 全テストケースに共通の初期化メソッドに注釈可能なアノテーションクラスを設定します。
     * 
     * @param afterAnnotation
     *            アノテーションクラス
     */
    public void setAfterAnnotation(
            final Class<? extends Annotation> afterAnnotation) {
        this.afterAnnotation = afterAnnotation;
    }

    /**
     * テストクラスのバインドフィールド直後のメソッドに注釈可能なアノテーションクラスを設定します。
     * 
     * @param postBindFieldsAnnotation
     *            アノテーションクラス
     */
    public void setPostBindFieldsAnnotation(
            Class<? extends Annotation> postBindFieldsAnnotation) {
        this.postBindFieldsAnnotation = postBindFieldsAnnotation;
    }

    /**
     * テストクラスのアンバインドフィールド直前のメソッドに注釈可能なアノテーションクラスを設定します。
     * 
     * @param preUnbindFieldsAnnotation
     *            アノテーションクラス
     */
    public void setPreUnbindFieldsAnnotation(
            Class<? extends Annotation> preUnbindFieldsAnnotation) {
        this.preUnbindFieldsAnnotation = preUnbindFieldsAnnotation;
    }

    /**
     * テストケースを無視する処理を有効とするかどうかを設定します。
     * 
     * @param enableIgnore
     *            無視する処理が有効の場合<code>true</code>、そうでない場合<code>false</code>
     */
    public void setEnableIgnore(boolean enableIgnore) {
        this.enableIgnore = enableIgnore;
    }

    /**
     * テストケースの事前条件チェックの処理を有効とするかどうかを設定します。
     * 
     * @param enablePrerequisite
     *            事前条件チェックの処理が有効の場合<code>true</code>、そうでない場合<code>false</code>
     */
    public void setEnablePrerequisite(boolean enablePrerequisite) {
        this.enablePrerequisite = enablePrerequisite;
    }

    public List<Method> getBeforeClassMethods(final Class<?> clazz) {
        return getAnnotatedMethods(clazz, beforeClassAnnotation);
    }

    public List<Method> getAfterClassMethods(final Class<?> clazz) {
        return getAnnotatedMethods(clazz, afterClassAnnotation);
    }

    public List<Method> getPostBindFieldsMethods(final Class<?> clazz) {
        return getAnnotatedMethods(clazz, postBindFieldsAnnotation);
    }

    public List<Method> getPreUnbindFieldsMethods(final Class<?> clazz) {
        return getAnnotatedMethods(clazz, preUnbindFieldsAnnotation);
    }

    public List<Method> getBeforeMethods(final Class<?> clazz) {
        return getAnnotatedMethods(clazz, beforeAnnotation);
    }

    public List<Method> getAfterMethods(final Class<?> clazz) {
        return getAnnotatedMethods(clazz, afterAnnotation);
    }

    public List<Method> getTestMethods(final Class<?> clazz) {
        return getAnnotatedMethods(clazz, Test.class);
    }

    /**
     * アノテーションが付与されたメソッドのリストを返します。
     * 
     * @param clazz
     *            テストクラス
     * @param annotationClass
     *            アノテーションクラス
     * @return アノテーションが付与されたメソッドのリスト
     */
    protected List<Method> getAnnotatedMethods(Class<?> clazz,
            Class<? extends Annotation> annotationClass) {
        List<Method> results = IntrospectorUtil.getAnnotatedMethods(clazz,
                annotationClass);
        if (runsTopToBottom(annotationClass))
            Collections.reverse(results);
        return results;
    }

    /**
     * クラスの階層構造の上位から実行するならば<code>true</code>を返します。
     * 
     * @param annotation
     *            アノテーション
     * @return クラスの階層構造の上位から実行するならば<code>true</code>
     */
    protected boolean runsTopToBottom(Class<? extends Annotation> annotation) {
        return annotation.equals(Before.class)
                || annotation.equals(BeforeClass.class);
    }

    public Method getEachBeforeMethod(final Class<?> clazz, final Method method) {
        return null;
    }

    public Method getEachAfterMethod(final Class<?> clazz, final Method method) {
        return null;
    }

    public Method getEachRecordMethod(final Class<?> clazz, final Method method) {
        return null;
    }

    public Class<? extends Throwable> expectedException(final Method method) {
        final Test annotation = method.getAnnotation(Test.class);
        if (annotation == null || annotation.expected() == None.class) {
            return null;
        }
        return annotation.expected();
    }

    public long getTimeout(final Method method) {
        final Test annotation = method.getAnnotation(Test.class);
        if (annotation != null) {
            return annotation.timeout();
        }
        return 0;
    }

    public boolean isIgnored(final Method method) {
        if (enableIgnore) {
            return method.isAnnotationPresent(Ignore.class);
        }
        return false;
    }

    public boolean isFulfilled(final Class<?> clazz, final Method method,
            final Object test) {
        if (!enablePrerequisite) {
            return true;
        }
        if (clazz.isAnnotationPresent(Prerequisite.class)) {
            final String source = clazz.getAnnotation(Prerequisite.class)
                    .value();
            final Expression exp = createExpression(source, method, test);
            if (!isFulfilled(exp)) {
                return false;
            }
        }
        if (method.isAnnotationPresent(Prerequisite.class)) {
            final String source = method.getAnnotation(Prerequisite.class)
                    .value();
            final Expression exp = createExpression(source, method, test);
            if (!isFulfilled(exp)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 事前条件が満たされた場合<code>true</code>を返します。
     * 
     * @param expression
     *            事前条件を表す式
     * @return 事前条件が満たされた場合<code>true</code>、満たされない場合<code>false</code>
     */
    protected boolean isFulfilled(final Expression expression) {
        final Object result = expression.evaluateNoException();
        if (expression.isMethodFailed()) {
            System.err.println(expression.getException());
            return false;
        }
        expression.throwExceptionIfNecessary();
        if (result instanceof Boolean && Boolean.class.cast(result)) {
            return true;
        }
        return false;
    }

    public boolean needsTransaction(final Class<?> clazz, final Method method) {
        final TxBehaviorType type = getTxBehaviorType(clazz, method);
        return type == null || type != TxBehaviorType.NONE;
    }

    public boolean requiresTransactionCommitment(final Class<?> clazz,
            final Method method) {
        final TxBehaviorType type = getTxBehaviorType(clazz, method);
        return type != null && type == TxBehaviorType.COMMIT;
    }

    /**
     * トランザクションの振る舞いを返します。
     * 
     * @param clazz
     *            テストクラス
     * @param method
     *            テストメソッド
     * @return トランザクションの振る舞い
     */
    protected TxBehaviorType getTxBehaviorType(final Class<?> clazz,
            final Method method) {
        if (method.isAnnotationPresent(TxBehavior.class)) {
            return method.getAnnotation(TxBehavior.class).value();
        }
        if (clazz.isAnnotationPresent(TxBehavior.class)) {
            return clazz.getAnnotation(TxBehavior.class).value();
        }
        return null;
    }

    public boolean needsWarmDeploy(final Class<?> clazz, final Method method) {
        if (method.isAnnotationPresent(WarmDeploy.class)) {
            return method.getAnnotation(WarmDeploy.class).value();
        }
        if (clazz.isAnnotationPresent(WarmDeploy.class)) {
            return clazz.getAnnotation(WarmDeploy.class).value();
        }
        return true;
    }

    public boolean isRegisterNamingConvention(final Class<?> clazz,
            final Method method) {
        if (method.isAnnotationPresent(RegisterNamingConvention.class)) {
            return method.getAnnotation(RegisterNamingConvention.class).value();
        }
        if (clazz.isAnnotationPresent(RegisterNamingConvention.class)) {
            return clazz.getAnnotation(RegisterNamingConvention.class).value();
        }
        return true;
    }

    public void createMock(final Method method, final Object test,
            final InternalTestContext context) {
        final Mock mock = method.getAnnotation(Mock.class);
        if (mock != null) {
            createMock(mock, method, test, context);
        } else {
            final Mocks mocks = method.getAnnotation(Mocks.class);
            if (mocks != null) {
                for (final Mock each : mocks.value()) {
                    createMock(each, method, test, context);
                }
            }
        }
    }

    /**
     * <code>mock</code>から{@link MockInterceptor モックインターセプター}を作成し、<code>context</code>に登録します。
     * 
     * @param mock
     *            モックインターセプターの定義
     * @param method
     *            テストメソッド
     * @param test
     *            テストクラスのインスタンス
     * @param context
     *            S2JUnit4の内部的なテストコンテキスト
     */
    protected void createMock(final Mock mock, final Method method,
            final Object test, final InternalTestContext context) {
        final MockInterceptor mi = new MockInterceptor();
        if (!StringUtil.isEmpty(mock.returnValue())) {
            final Expression exp = createExpression(mock.returnValue(), method,
                    test);
            mi.setReturnValue(exp.evaluate());
        }
        if (!StringUtil.isEmpty(mock.throwable())) {
            final Expression exp = createExpression(mock.throwable(), method,
                    test);
            final Object result = exp.evaluate();
            mi.setThrowable(Throwable.class.cast(result));
        }
        Pointcut pc = null;
        if (StringUtil.isEmpty(mock.pointcut())) {
            pc = AspectDefFactory.createPointcut(mock.target());
        } else {
            pc = AspectDefFactory.createPointcut(mock.pointcut());
        }
        final Object componentKey = StringUtil.isEmpty(mock.targetName()) ? mock
                .target()
                : mock.targetName();
        final AspectDef aspectDef = AspectDefFactory.createAspectDef(mi, pc);
        context.addAspecDef(componentKey, aspectDef);
        context.addMockInterceptor(mi);
    }

    /**
     * 式を作成します。
     * 
     * @param source
     *            式の文字列表現
     * @param method
     *            テストメソッド
     * @param test
     *            テストクラスのインスタンス
     * @return 式
     */
    protected Expression createExpression(final String source,
            final Method method, final Object test) {
        final Map<String, Object> ctx = CollectionsUtil.newHashMap();
        ctx.put("ENV", Env.getValue());
        ctx.put("method", method);
        return new OgnlExpression(source, test, ctx);
    }

    public String getRootDicon(final Class<?> clazz, final Method method) {
        if (method.isAnnotationPresent(RootDicon.class)) {
            return method.getAnnotation(RootDicon.class).value();
        }
        if (clazz.isAnnotationPresent(RootDicon.class)) {
            return clazz.getAnnotation(RootDicon.class).value();
        }
        return null;
    }

}
