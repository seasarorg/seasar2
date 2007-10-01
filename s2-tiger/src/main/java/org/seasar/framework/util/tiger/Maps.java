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
package org.seasar.framework.util.tiger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 簡潔な記述で{@link Map}のインスタンスを生成するためのユーティリティクラスです。
 * <p>
 * 本クラスをstatic importすることにより、次のように<code>Map</code>のインスタンスを生成することができます。
 * </p>
 * 
 * <pre>
 * Map<String, Integer> map = map($("a", 1), $("b", 2), $("c", 3));
 * </pre>
 * 
 * @author koichik
 * @param <K>
 *            <code>Map</code>のキーの型
 * @param <V>
 *            <code>Map</code>の値の型
 */
public class Maps<K, V> {

    /**
     * {@link HashMap}のインスタンスを生成し、値を設定して返します。
     * 
     * @param <K>
     *            <code>Map</code>のキーの型
     * @param <V>
     *            <code>Map</code>の値の型
     * @param pairs
     *            <code>Map</code>に設定する値
     * @return {@link HashMap}
     */
    public static <K, V> Map<K, V> map(Pair<K, V>... pairs) {
        return hashMap(pairs);
    }

    /**
     * {@link HashMap}のインスタンスを生成し、値を設定して返します。
     * 
     * @param <K>
     *            <code>Map</code>のキーの型
     * @param <V>
     *            <code>Map</code>の値の型
     * @param pairs
     *            <code>Map</code>に設定する値
     * @return {@link HashMap}
     */
    public static <K, V> Map<K, V> hashMap(Pair<K, V>... pairs) {
        return putAll(new HashMap<K, V>(), pairs);
    }

    /**
     * {@link LinkedHashMap}のインスタンスを生成し、値を設定して返します。
     * 
     * @param <K>
     *            <code>Map</code>のキーの型
     * @param <V>
     *            <code>Map</code>の値の型
     * @param pairs
     *            <code>Map</code>に設定する値
     * @return {@link LinkedHashMap}
     */
    public static <K, V> Map<K, V> linkedHashMap(Pair<K, V>... pairs) {
        return putAll(new LinkedHashMap<K, V>(), pairs);
    }

    /**
     * {@link WeakHashMap}のインスタンスを生成し、値を設定して返します。
     * 
     * @param <K>
     *            <code>Map</code>のキーの型
     * @param <V>
     *            <code>Map</code>の値の型
     * @param pairs
     *            <code>Map</code>に設定する値
     * @return {@link WeakHashMap}
     */
    public static <K, V> Map<K, V> weakHashMap(Pair<K, V>... pairs) {
        return putAll(new WeakHashMap<K, V>(), pairs);
    }

    /**
     * {@link ConcurrentHashMap}のインスタンスを生成し、値を設定して返します。
     * 
     * @param <K>
     *            <code>Map</code>のキーの型
     * @param <V>
     *            <code>Map</code>の値の型
     * @param pairs
     *            <code>Map</code>に設定する値
     * @return {@link ConcurrentHashMap}
     */
    public static <K, V> Map<K, V> concurrentHashMap(Pair<K, V>... pairs) {
        return putAll(new ConcurrentHashMap<K, V>(), pairs);
    }

    /**
     * {@link TreeMap}のインスタンスを生成し、値を設定して返します。
     * 
     * @param <K>
     *            <code>Map</code>のキーの型
     * @param <V>
     *            <code>Map</code>の値の型
     * @param pairs
     *            <code>Map</code>に設定する値
     * @return {@link TreeMap}
     */
    public static <K, V> Map<K, V> treeMap(Pair<K, V>... pairs) {
        return putAll(new TreeMap<K, V>(), pairs);
    }

    /**
     * {@link Map}に追加されるキーと値を保持する{@link Pair}を作成して返します。
     * 
     * @param <K>
     *            <code>Map</code>のキーの型
     * @param <V>
     * @param key
     *            <code>Map</code>に追加されるキー
     * @param value
     *            <code>Map</code>に追加される値
     * @return <code>Map</code>に追加されるキーと値を保持する<code>Pair</code>
     */
    public static <K, V> Pair<K, V> $(K key, V value) {
        return new Pair<K, V>(key, value);
    }

    /**
     * {@link Map}にキーと値のマッピングを追加して返します。
     * 
     * @param <K>
     *            <code>Map</code>のキーの型
     * @param <V>
     *            <code>Map</code>の値の型
     * @param map
     *            マッピングを追加する対象の<code>Map</code>
     * @param pairs
     *            <code>Map</code>に設定する値
     * @return キーと値のマッピングが追加された<code>Map</code>
     */
    protected static <K, V> Map<K, V> putAll(Map<K, V> map, Pair<K, V>... pairs) {
        for (final Pair<K, V> pair : pairs) {
            map.put(pair.key, pair.value);
        }
        return map;
    }

    /**
     * キーと値のペアを保持するクラスです。
     * 
     * @author koichik
     * @param <K>
     *            キーの型
     * @param <V>
     *            値の型
     */
    public static class Pair<K, V> {

        /** キー */
        protected K key;

        /** 値 */
        protected V value;

        /**
         * インスタンスを構築します。
         * 
         * @param key
         *            キー
         * @param value
         *            値
         */
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

    }

}
