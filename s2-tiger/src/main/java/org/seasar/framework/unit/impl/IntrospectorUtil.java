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
import java.util.ArrayList;
import java.util.List;

/**
 * @author taedium
 * 
 */
public class IntrospectorUtil {

    /**
     * アノテーションが付与されたメソッドのリストを返します。
     * 
     * @param clazz
     *            テストクラス
     * @param annotationClass
     *            アノテーションクラス
     * @return アノテーションが付与されたメソッドのリスト
     */
    public static List<Method> getAnnotatedMethods(Class<?> clazz,
            Class<? extends Annotation> annotationClass) {
        List<Method> results = new ArrayList<Method>();
        for (Class<?> eachClass : getSuperClasses(clazz)) {
            Method[] methods = eachClass.getDeclaredMethods();
            for (Method eachMethod : methods) {
                Annotation annotation = eachMethod
                        .getAnnotation(annotationClass);
                if (annotation != null && !isShadowed(eachMethod, results))
                    results.add(eachMethod);
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
    public static List<Class<?>> getSuperClasses(final Class<?> clazz) {
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
    public static boolean isShadowed(final Method method,
            final List<Method> results) {
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
    protected static boolean isShadowed(final Method current,
            final Method previous) {
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

}
