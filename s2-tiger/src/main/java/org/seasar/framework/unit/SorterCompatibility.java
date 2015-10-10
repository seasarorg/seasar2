/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import java.lang.reflect.Method;

import org.junit.runner.Runner;
import org.junit.runner.manipulation.Sorter;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.MethodUtil;

/**
 * org.junit.runner.manipulation.Sorter クラスのシグニチャ変更に対応するクラスです。
 *
 * JUnit 4.4: public void apply(Runner)
 * ↓
 * JUnit 4.5: public void apply(Object)
 *
 *
 * @author manhole
 */
public class SorterCompatibility {

    @SuppressWarnings("unchecked")
    private static final Class[] APPLY_PARAM_TYPES = new Class[] { Object.class };

    /**
     * Sorter#apply(sortable)メソッドを実行します。
     * 
     * @param sorter
     *            ソーター
     * @param sortable
     *            ソート対象
     */
    public static void apply(final Sorter sorter, final Runner sortable) {
        try {
            sorter.apply(sortable);
        } catch (final NoSuchMethodError ignore) {
            // JUnit 4.5 or higher
            apply45(sorter, sortable);
        }
    }

    private static void apply45(final Sorter sorter, final Object sortable) {
        final BeanDesc beanDesc = BeanDescFactory
                .getBeanDesc(sorter.getClass());
        final Method method = beanDesc.getMethod("apply", APPLY_PARAM_TYPES);
        MethodUtil.invoke(method, sorter, new Object[] { sortable });
    }

}
