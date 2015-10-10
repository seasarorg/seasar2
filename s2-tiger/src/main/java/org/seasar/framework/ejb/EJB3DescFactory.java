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
package org.seasar.framework.ejb;

import java.util.concurrent.ConcurrentMap;

import org.seasar.framework.ejb.impl.EJB3DescImpl;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * EJB3セッションビーン定義のインスタンスを生成するファクトリクラスです。
 * 
 * @author koichik
 */
public class EJB3DescFactory {

    private static boolean initialized;

    /** {@link EJB3Desc}のキャッシュ */
    protected static final ConcurrentMap<Class<?>, EJB3DescImpl> ejb3Descs = CollectionsUtil
            .newConcurrentHashMap();

    static {
        initialize();
    }

    /**
     * 初期化します。
     */
    public static void initialize() {
        if (initialized) {
            return;
        }
        DisposableUtil.add(new Disposable() {

            public void dispose() {
                EJB3DescFactory.dispose();
            }
        });
        initialized = true;
    }

    /**
     * {@link EJB3Desc}のキャッシュをクリアします。
     */
    public static void dispose() {
        ejb3Descs.clear();
        initialized = false;
    }

    /**
     * {@code beanClass}に対応する{@link EJB3Desc}のインスタンスを返します。
     * <p>
     * {@code beanClass}がEJB3セッションビーンでない場合は{@code null}を返します。
     * </p>
     * 
     * @param beanClass
     *            セッションビーンクラス
     * @return {@code beanClass}に対応する{@link EJB3Desc}のインスタンス
     */
    public static EJB3Desc getEJB3Desc(final Class<?> beanClass) {
        initialize();
        EJB3DescImpl ejb3Desc = ejb3Descs.get(beanClass);
        if (ejb3Desc == null) {
            ejb3Desc = CollectionsUtil.putIfAbsent(ejb3Descs, beanClass,
                    new EJB3DescImpl(beanClass));
        }
        return ejb3Desc.isEJB3() ? ejb3Desc : null;
    }

}
