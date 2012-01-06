/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.runners.InitializationError;
import org.seasar.framework.unit.impl.IntrospectorUtil;

/**
 * メソッドを検証するクラスです。
 * 
 * @author taedium
 */
public class S2MethodValidator {

    /** テストクラス */
    protected Class<?> clazz;

    /** 検証に違反したときに発生するスロー可能オブジェクトのリスト */
    protected List<Throwable> errors = new ArrayList<Throwable>();

    /**
     * インスタンスを構築します。
     * 
     * @param clazz
     */
    public S2MethodValidator(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * インスタンスメソッドを検証します。
     */
    public void validateInstanceMethods() {
        validateTestMethods(After.class, false);
        validateTestMethods(Before.class, false);
        validateTestMethods(Test.class, false);
    }

    /**
     * スタティックメソッドを検証します。
     */
    public void validateStaticMethods() {
        validateTestMethods(BeforeClass.class, true);
        validateTestMethods(AfterClass.class, true);
    }

    /**
     * デフォルトのランナーに対し、メソッドを検証します。
     * 
     * @return スロー可能オブジェクトのリスト
     */
    public List<Throwable> validateMethodsForDefaultRunner() {
        validateNoArgConstructor();
        validateStaticMethods();
        validateInstanceMethods();
        return errors;
    }

    /**
     * 検証の結果エラーがないことをアサートします。
     * 
     * @throws InitializationError
     *             検証で何らかのエラーが発生した場合
     */
    public void assertValid() throws InitializationError {
        if (!errors.isEmpty())
            throw new InitializationError(errors);
    }

    /**
     * 引数なしのコンストラクタがあることを検証します。
     */
    public void validateNoArgConstructor() {
        try {
            clazz.getConstructor();
        } catch (Exception e) {
            errors.add(new Exception(
                    "Test class should have public zero-argument constructor",
                    e));
        }
    }

    /**
     * テストメソッドを検証します。
     * 
     * @param annotation
     *            アノテーション
     * @param isStatic
     *            スタティックなメソッドであれば<code>true</code>
     */
    protected void validateTestMethods(Class<? extends Annotation> annotation,
            boolean isStatic) {
        List<Method> methods = IntrospectorUtil.getAnnotatedMethods(clazz,
                annotation);
        for (Method each : methods) {
            if (Modifier.isStatic(each.getModifiers()) != isStatic) {
                String state = isStatic ? "should" : "should not";
                errors.add(new Exception("Method " + each.getName() + "() "
                        + state + " be static"));
            }
            if (!Modifier.isPublic(each.getDeclaringClass().getModifiers()))
                errors.add(new Exception("Class "
                        + each.getDeclaringClass().getName()
                        + " should be public"));
            if (!Modifier.isPublic(each.getModifiers()))
                errors.add(new Exception("Method " + each.getName()
                        + " should be public"));
            if (each.getReturnType() != Void.TYPE)
                errors.add(new Exception("Method " + each.getName()
                        + " should be void"));
            if (each.getParameterTypes().length != 0)
                errors.add(new Exception("Method " + each.getName()
                        + " should have no parameters"));
        }
    }
}
