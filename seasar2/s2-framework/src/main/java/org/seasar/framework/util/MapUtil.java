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
package org.seasar.framework.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link java.util.Map}を扱うユーティリティクラスです。
 * 
 * @author koichik
 */
public class MapUtil {

    /**
     * {@link MapFactory}です。
     */
    protected static final MapFactory factory = getMapFactory();

    /**
     * スレッドセーフな{@link java.util.HashMap}を作成して返します。
     * <p>
     * 実行環境がJava5の場合は{@link java.util.concurrent.ConcurrentHashMap}を、それ以外の場合は
     * {@link java.util.CollectionUtil#synchronizedMap}でラップされた{@link java.util.HashMap}を
     * 返します。
     * </p>
     * 
     * @return スレッドセーフな{@link java.util.HashMap}
     */
    public static Map createHashMap() {
        return factory.create();
    }

    /**
     * スレッドセーフな{@link java.util.HashMap}を作成して返します。
     * <p>
     * 実行環境がJava5の場合は{@link java.util.concurrent.ConcurrentHashMap}を、それ以外の場合は
     * {@link java.util.CollectionUtil#synchronizedMap}でラップされた{@link java.util.HashMap}を
     * 返します。
     * </p>
     * 
     * @param initialCapacity
     *            初期容量
     * @return スレッドセーフな{@link java.util.HashMap}
     */
    public static Map createHashMap(final int initialCapacity) {
        return factory.create(initialCapacity);
    }

    /**
     * スレッドセーフな{@link java.util.HashMap}を作成して返します。
     * <p>
     * 実行環境がJava5の場合は{@link java.util.concurrent.ConcurrentHashMap}を、それ以外の場合は
     * {@link java.util.CollectionUtil#synchronizedMap}でラップされた{@link java.util.HashMap}を
     * 返します。
     * </p>
     * 
     * @param initialCapacity
     *            初期容量
     * @param loadFactor
     *            負荷係数
     * @return スレッドセーフな{@link java.util.HashMap}
     */
    public static Map createHashMap(final int initialCapacity,
            final float loadFactor) {
        return factory.create(initialCapacity, loadFactor);
    }

    /**
     * {@link MapFactory}を作成して返します。
     * 
     * @return {@link MapFactory}
     */
    protected static MapFactory getMapFactory() {
        try {
            final Class clazz = Class
                    .forName("org.seasar.framework.util.ConcurrentMapFactory");
            return (MapFactory) clazz.newInstance();
        } catch (final Throwable ignore) {
        }
        return new SynchronizedMapFactory();
    }

    /**
     * スレッドセーフな{@link java.util.HashMap}のファクトリです。
     * 
     * @author koichik
     */
    interface MapFactory {
        /**
         * デフォルトの初期容量と負荷係数で{@link java.util.HashMap}を作成して返します。
         * 
         * @return スレッドセーフな{@link java.util.HashMap}
         */
        Map create();

        /**
         * 指定されて初期容量とデフォルトの負荷係数で{@link java.util.HashMap}を作成して返します。
         * 
         * @param initialCapacity
         * @return スレッドセーフな{@link java.util.HashMap}
         */
        Map create(int initialCapacity);

        /**
         * 指定された初期容量と負荷係数で{@link java.util.HashMap}を作成して返します。
         * 
         * @param initialCapacity
         *            初期容量
         * @param loadFactor
         *            負荷係数
         * @return スレッドセーフな{@link java.util.HashMap}
         */
        Map create(int initialCapacity, float loadFactor);
    }

    /**
     * {@link java.util.CollectionUtil#synchronizedMap}でラップされた{@link java.util.HashMap}を
     * 作成するファクトリの実装です。
     * 
     * @author koichik
     */
    public static class SynchronizedMapFactory implements MapFactory {

        public Map create() {
            return Collections.synchronizedMap(new HashMap());
        }

        public Map create(final int initialCapacity) {
            return Collections.synchronizedMap(new HashMap(initialCapacity));
        }

        public Map create(final int initialCapacity, final float loadFactor) {
            return Collections.synchronizedMap(new HashMap(initialCapacity,
                    loadFactor));
        }

    }

}
