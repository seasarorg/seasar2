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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * 命名規約を解釈してテストクラスを分析するイントロスペクターです。
 * 
 * @author taedium
 */
public class ConventionTestIntrospector extends AnnotationTestIntrospector {

    /** テストメソッドとして解釈すべきでないメソッドに付与されるアノテーションのセット */
    protected final Set<Class<? extends Annotation>> nonTestAnnotations = CollectionsUtil
            .newHashSet();

    /** テストメソッドとして解釈すべきでないメソッド名の正規表現 */
    protected final Set<Pattern> nonTestMethodNamePatterns = new HashSet<Pattern>();

    /** テストクラスの初期化メソッドの名前 */
    protected String beforeClassMethodName = "beforeClass";

    /** テストクラスの解放メソッドの名前 */
    protected String afterClassMethodName = "afterClass";

    /** 全テストケースの初期化メソッドの名前 */
    protected String beforeMethodName = "before";

    /** 全テストケースの解放メソッドを名前 */
    protected String afterMethodName = "after";

    /** テストケース個別にモックの振る舞いを記録するメソッドの名前のプレフィックス */
    protected String recordMethodName = "record";

    /**
     * 初期化メソッド
     */
    @InitMethod
    public void init() {
        addNonTestAnnotation(beforeAnnotation);
        addNonTestAnnotation(afterAnnotation);
        if (beforeMethodName != null) {
            addNonTestMethodNamePattern(beforeMethodName + ".*");
        }
        if (afterMethodName != null) {
            addNonTestMethodNamePattern(afterMethodName + ".*");
        }
        if (recordMethodName != null) {
            addNonTestMethodNamePattern(recordMethodName + ".+");
        }
    }

    /**
     * テストクラスの初期化メソッドの名前を設定します。
     * 
     * @param beforeClassMethodName
     *            初期化メソッドの名前
     */
    public void setBeforeClassMethodName(final String beforeClassMethodName) {
        this.beforeClassMethodName = beforeClassMethodName;
    }

    /**
     * テストクラスの解放メソッドの名前を設定します。
     * 
     * @param afterClassMethodName
     *            解放メソッドの名前
     */
    public void setAfterClassMethodName(final String afterClassMethodName) {
        this.afterClassMethodName = afterClassMethodName;
    }

    /**
     * 全テストケースの初期化メソッドの名前を設定します。
     * 
     * @param beforeMethodName
     *            初期化メソッドの名前
     */
    public void setBeforeMethodName(final String beforeMethodName) {
        this.beforeMethodName = beforeMethodName;
    }

    /**
     * 全テストケースの解放メソッドの名前を設定します。
     * 
     * @param afterMethodName
     *            解放メソッドの名前
     */
    public void setAfterMethodName(final String afterMethodName) {
        this.afterMethodName = afterMethodName;
    }

    /**
     * テストケース個別にモックの振る舞いを記録するメソッドの名前のプレフィックスを設定します。
     * 
     * @param recordMethodName
     *            記録メソッド名のプレフィックス
     */
    public void setRecordMethodName(final String recordMethodName) {
        this.recordMethodName = recordMethodName;
    }

    /**
     * テストメソッドと解釈すべきでないメソッドに付与するアノテーションを登録します。
     * 
     * @param annotation
     *            テストメソッドと解釈すべきでないメソッドに付与するアノテーション
     */
    public void addNonTestAnnotation(
            final Class<? extends Annotation> annotation) {

        nonTestAnnotations.add(annotation);
    }

    /**
     * テストメソッドとして解釈すべきでないメソッド名の正規表現を登録します。
     * 
     * @param pattern
     *            テストメソッドとして解釈すべきでないメソッド名の正規表現
     */
    public void addNonTestMethodNamePattern(final String pattern) {
        nonTestMethodNamePatterns.add(Pattern.compile(pattern));
    }

    @Override
    public List<Method> getBeforeClassMethods(final Class<?> clazz) {
        final List<Method> methods = super.getBeforeClassMethods(clazz);
        final Method method = getMethod(clazz, beforeClassMethodName);
        if (method != null) {
            if (hasValidStaticSignature(method) && !methods.contains(method)) {
                methods.add(method);
            }
        }
        return methods;
    }

    @Override
    public List<Method> getAfterClassMethods(final Class<?> clazz) {
        final List<Method> methods = super.getAfterClassMethods(clazz);
        final Method method = getMethod(clazz, afterClassMethodName);
        if (method != null) {
            if (hasValidStaticSignature(method) && !methods.contains(method)) {
                methods.add(method);
            }
        }
        return methods;
    }

    @Override
    public List<Method> getBeforeMethods(final Class<?> clazz) {
        final List<Method> methods = super.getBeforeMethods(clazz);
        final Method method = getMethod(clazz, beforeMethodName);
        if (method != null) {
            if (hasValidNonStaticSignature(method) && !methods.contains(method)) {
                methods.add(method);
            }
        }
        return methods;
    }

    @Override
    public List<Method> getAfterMethods(final Class<?> clazz) {
        final List<Method> methods = super.getAfterMethods(clazz);
        final Method method = getMethod(clazz, afterMethodName);
        if (method != null) {
            if (hasValidNonStaticSignature(method) && !methods.contains(method)) {
                methods.add(method);
            }
        }
        return methods;
    }

    @Override
    public Method getEachBeforeMethod(final Class<?> clazz, final Method method) {
        if (beforeMethodName == null) {
            return null;
        }
        final String methodName = beforeMethodName
                + StringUtil.capitalize(method.getName());
        return getMethod(clazz, methodName);
    }

    @Override
    public Method getEachAfterMethod(final Class<?> clazz, final Method method) {
        if (afterMethodName == null) {
            return null;
        }
        final String methodName = afterMethodName
                + StringUtil.capitalize(method.getName());
        return getMethod(clazz, methodName);
    }

    @Override
    public Method getEachRecordMethod(final Class<?> clazz, final Method method) {
        if (recordMethodName == null) {
            return null;
        }
        final String methodName = recordMethodName
                + StringUtil.capitalize(method.getName());
        return getMethod(clazz, methodName);
    }

    @Override
    public List<Method> getTestMethods(final Class<?> clazz) {
        final List<Method> results = new ArrayList<Method>();
        for (Class<?> eachClass : getSuperClasses(clazz)) {
            final Method[] methods = eachClass.getDeclaredMethods();
            for (final Method eachMethod : methods) {
                if (isTestMethod(eachMethod)
                        && !isShadowed(eachMethod, results)) {
                    results.add(eachMethod);
                }
            }
        }
        return results;
    }

    /**
     * スーパークラスのリストを返します。
     * 
     * @param clazz
     *            基点となるクラス
     * @return スーパークラスのリスト
     */
    protected List<Class<?>> getSuperClasses(final Class<?> clazz) {
        final ArrayList<Class<?>> results = new ArrayList<Class<?>>();
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            results.add(current);
            current = current.getSuperclass();
        }
        return results;
    }

    /**
     * <code>method</code>が<code>result</code>内のメソッドに隠蔽されるならば<code>true</code>を返します。
     * 
     * @param method
     *            検査の対象のメソッド
     * @param results
     *            隠蔽されていないメソッドのリスト
     * @return <code>result</code>内のメソッドに<code>method</code>が隠蔽される場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean isShadowed(final Method method, final List<Method> results) {
        for (final Method each : results) {
            if (isShadowed(method, each))
                return true;
        }
        return false;
    }

    /**
     * <code>current</code>が<code>previous</code>に隠蔽される場合<code>true</code>を返します。
     * 
     * @param current
     *            検査の対象のメソッド
     * @param previous
     *            隠蔽されていないメソッド
     * @return <<code>current</code>が<code>previous</code>に隠蔽される場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean isShadowed(final Method current, final Method previous) {
        if (!previous.getName().equals(current.getName())) {
            return false;
        }
        if (previous.getParameterTypes().length != current.getParameterTypes().length) {
            return false;
        }
        for (int i = 0; i < previous.getParameterTypes().length; i++) {
            if (!previous.getParameterTypes()[i].equals(current
                    .getParameterTypes()[i]))
                return false;
        }
        return true;
    }

    /**
     * 指定されたメソッドがテストメソッドの場合<code>true</code>を返します。
     * 
     * @param method
     *            メソッド
     * @return 指定されたメソッドがテストメソッドの場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean isTestMethod(final Method method) {
        if (!hasNonTestAnnotation(method)) {
            if (!hasNonTestMethodName(method)) {
                if (hasValidNonStaticSignature(method)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定されたメソッドにテストメソッドとして解釈すべきでないアノテーションが付与されている場合<code>true</code>
     * 
     * @param method
     *            メソッド
     * @return 指定されたメソッドにテストメソッドとして解釈すべきでないアノテーションが付与されている場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean hasNonTestAnnotation(final Method method) {
        for (final Annotation each : method.getAnnotations()) {
            if (nonTestAnnotations.contains(each.annotationType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 指定されたメソッドの名前がテストメソッドとして解釈すべきでない名前の場合<code>true</code>を返します。
     * 
     * @param method
     *            メソッド
     * @return 指定されたメソッドの名前がテストメソッドとして解釈すべきでない名前の場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean hasNonTestMethodName(final Method method) {
        for (final Pattern each : nonTestMethodNamePatterns) {
            final Matcher matcher = each.matcher(method.getName());
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * メソッドがstaticかつ適切なシグネチャを持つ場合<code>true</code>を返します。
     * 
     * @param method
     * @return メソッドがstaticかつ適切なシグネチャを持つ場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean hasValidStaticSignature(final Method method) {
        if (!Modifier.isStatic(method.getModifiers())) {
            return false;
        }
        return hasValidSignature(method);
    }

    /**
     * メソッドが非staticかつ適切なシグネチャを持つ場合<code>true</code>を返します。
     * 
     * @param method
     *            メソッド
     * @return メソッドが非staticかつ適切なシグネチャを持つ場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean hasValidNonStaticSignature(final Method method) {
        if (Modifier.isStatic(method.getModifiers())) {
            return false;
        }
        return hasValidSignature(method);
    }

    /**
     * メソッドが適切なシグネチャを持つ場合<code>true</code>を返します。
     * <p>
     * 適切なシグネチャをもつメソッドとは以下の条件を満たしているシグネチャを意味します。
     * <ul>
     * <li>アクセス修飾子がpublicである</li>
     * <li>戻り値がvoidである</li>
     * <li>パラメータの数が0である</li>
     * </ul>
     * </p>
     * 
     * @param method
     *            メソッド
     * @return メソッドが適切なシグネチャを持つ場合<code>true</code>、そうでない場合<code>false</code>
     */
    protected boolean hasValidSignature(final Method method) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }
        if (method.getReturnType() != Void.TYPE) {
            return false;
        }
        if (method.getParameterTypes().length != 0) {
            return false;
        }
        return true;
    }

    /**
     * 指定されたクラスを基点にスーパークラスを辿り指定されたメソッドを返します。
     * 
     * @param clazz
     *            基点となるクラス
     * @param methodName
     *            メソッド名
     * @return 指定されたメソッド、メソッドが見つからない場合<code>null</code>
     */
    protected Method getMethod(final Class<?> clazz, final String methodName) {
        for (Class<?> eachClass : getSuperClasses(clazz)) {
            final Method[] methods = eachClass.getDeclaredMethods();
            for (final Method eachMethod : methods) {
                if (eachMethod.getName().equals(methodName)) {
                    return eachMethod;
                }
            }
        }
        return null;
    }

}
