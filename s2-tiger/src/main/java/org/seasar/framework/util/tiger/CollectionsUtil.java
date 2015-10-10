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
package org.seasar.framework.util.tiger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Java5のgenericsや可変長を活用する、コレクションのためのユーティリティです。
 * 
 * @author koichik
 */
public abstract class CollectionsUtil {

    /**
     * インスタンスを構築します。
     */
    protected CollectionsUtil() {
    }

    /**
     * {@link ArrayBlockingQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link ArrayBlockingQueue}の要素型
     * @param capacity
     *            キューの容量
     * @return {@link ArrayBlockingQueue}の新しいインスタンス
     * @see ArrayBlockingQueue#ArrayBlockingQueue(int)
     */
    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(
            final int capacity) {
        return new ArrayBlockingQueue<E>(capacity);
    }

    /**
     * {@link ArrayBlockingQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link ArrayBlockingQueue}の要素型
     * @param capacity
     *            キューの容量
     * @param fair
     *            {@code true}の場合、挿入または削除時にブロックされたスレッドに対するキューアクセス
     * @return {@link ArrayBlockingQueue}の新しいインスタンス
     * @see ArrayBlockingQueue#ArrayBlockingQueue(int, boolean)
     */
    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(
            final int capacity, final boolean fair) {
        return new ArrayBlockingQueue<E>(capacity, fair);
    }

    /**
     * {@link ArrayBlockingQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link ArrayBlockingQueue}の要素型
     * @param capacity
     *            キューの容量
     * @param fair
     *            {@code true}の場合、挿入または削除時にブロックされたスレッドに対するキューアクセス
     * @param c
     *            最初に含む要素のコレクション
     * @return {@link ArrayBlockingQueue}の新しいインスタンス
     * @see ArrayBlockingQueue#ArrayBlockingQueue(int, boolean, Collection)
     */
    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(
            final int capacity, final boolean fair,
            final Collection<? extends E> c) {
        return new ArrayBlockingQueue<E>(capacity, fair, c);
    }

    /**
     * {@link ArrayList}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link ArrayList}の要素型
     * @return {@link ArrayList}の新しいインスタンス
     * @see ArrayList#ArrayList()
     */
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    /**
     * {@link ArrayList}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link ArrayList}の要素型
     * @param c
     *            要素がリストに配置されるコレクション
     * @return {@link ArrayList}の新しいインスタンス
     * @see ArrayList#ArrayList(Collection)
     */
    public static <E> ArrayList<E> newArrayList(final Collection<? extends E> c) {
        return new ArrayList<E>(c);
    }

    /**
     * {@link ArrayList}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link ArrayList}の要素型
     * @param initialCapacity
     *            リストの初期容量
     * @return {@link ArrayList}の新しいインスタンス
     * @see ArrayList#ArrayList(int)
     */
    public static <E> ArrayList<E> newArrayList(final int initialCapacity) {
        return new ArrayList<E>(initialCapacity);
    }

    /**
     * {@link ConcurrentHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link ConcurrentHashMap}のキーの型
     * @param <V>
     *            {@link ConcurrentHashMap}の値の型
     * @return {@link ConcurrentHashMap}の新しいインスタンス
     * @see ConcurrentHashMap#ConcurrentHashMap()
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<K, V>();
    }

    /**
     * {@link ConcurrentHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link ConcurrentHashMap}のキーの型
     * @param <V>
     *            {@link ConcurrentHashMap}の値の型
     * @param initialCapacity
     *            初期容量
     * @return {@link ConcurrentHashMap}の新しいインスタンス
     * @see ConcurrentHashMap#ConcurrentHashMap(int)
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(
            final int initialCapacity) {
        return new ConcurrentHashMap<K, V>(initialCapacity);
    }

    /**
     * {@link ConcurrentHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link ConcurrentHashMap}のキーの型
     * @param <V>
     *            {@link ConcurrentHashMap}の値の型
     * @param initialCapacity
     *            初期容量
     * @param loadFactor
     *            サイズ変更の制御に使用される負荷係数のしきい値
     * @param concurrencyLevel
     *            同時更新を行うスレッドの推定数
     * @return {@link ConcurrentHashMap}の新しいインスタンス
     * @see ConcurrentHashMap#ConcurrentHashMap(int, float, int)
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(
            final int initialCapacity, final float loadFactor,
            final int concurrencyLevel) {
        return new ConcurrentHashMap<K, V>(initialCapacity, loadFactor,
                concurrencyLevel);
    }

    /**
     * {@link ConcurrentHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link ConcurrentHashMap}のキーの型
     * @param <V>
     *            {@link ConcurrentHashMap}の値の型
     * @param m
     *            作成されるマップに配置されるマップ
     * @return {@link ConcurrentHashMap}の新しいインスタンス
     * @see ConcurrentHashMap#ConcurrentHashMap(Map)
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(
            final Map<? extends K, ? extends V> m) {
        return new ConcurrentHashMap<K, V>(m);
    }

    /**
     * {@link ConcurrentLinkedQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link ConcurrentLinkedQueue}の要素型
     * @return {@link ConcurrentLinkedQueue}の新しいインスタンス
     * @see ConcurrentLinkedQueue#ConcurrentLinkedQueue()
     */
    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
        return new ConcurrentLinkedQueue<E>();
    }

    /**
     * {@link ConcurrentLinkedQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link ConcurrentLinkedQueue}の要素型
     * @param c
     *            最初に含む要素のコレクション
     * @return {@link ConcurrentLinkedQueue}の新しいインスタンス
     * @see ConcurrentLinkedQueue#ConcurrentLinkedQueue(Collection)
     */
    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(
            final Collection<? extends E> c) {
        return new ConcurrentLinkedQueue<E>(c);
    }

    /**
     * {@link CopyOnWriteArrayList}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link CopyOnWriteArrayList}の要素型
     * @return {@link CopyOnWriteArrayList}の新しいインスタンス
     * @see CopyOnWriteArrayList#CopyOnWriteArrayList()
     */
    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<E>();
    }

    /**
     * {@link CopyOnWriteArrayList}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link CopyOnWriteArrayList}の要素型
     * @param c
     *            最初に保持していた要素のコレクション
     * @return {@link CopyOnWriteArrayList}の新しいインスタンス
     * @see CopyOnWriteArrayList#CopyOnWriteArrayList(Collection)
     */
    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(
            final Collection<? extends E> c) {
        return new CopyOnWriteArrayList<E>(c);
    }

    /**
     * {@link CopyOnWriteArrayList}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link CopyOnWriteArrayList}の要素型
     * @param toCopyIn
     *            配列 (この配列のコピーは内部配列として使用される)
     * @return {@link CopyOnWriteArrayList}の新しいインスタンス
     * @see CopyOnWriteArrayList#CopyOnWriteArrayList(Object[])
     */
    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(
            final E[] toCopyIn) {
        return new CopyOnWriteArrayList<E>(toCopyIn);
    }

    /**
     * {@link CopyOnWriteArraySet}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link CopyOnWriteArraySet}の要素型
     * @return {@link CopyOnWriteArraySet}の新しいインスタンス
     * @see CopyOnWriteArraySet#CopyOnWriteArraySet()
     */
    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
        return new CopyOnWriteArraySet<E>();
    }

    /**
     * {@link CopyOnWriteArraySet}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link CopyOnWriteArraySet}の要素型
     * @param c
     *            コレクション
     * @return {@link CopyOnWriteArraySet}の新しいインスタンス
     * @see CopyOnWriteArraySet#CopyOnWriteArraySet(Collection)
     */
    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(
            final Collection<? extends E> c) {
        return new CopyOnWriteArraySet<E>(c);
    }

    /**
     * {@link DelayQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link CopyOnWriteArraySet}の要素型
     * @return {@link DelayQueue}の新しいインスタンス
     * @see DelayQueue#DelayQueue()
     */
    public static <E extends Delayed> DelayQueue<E> newDelayQueue() {
        return new DelayQueue<E>();
    }

    /**
     * {@link DelayQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link CopyOnWriteArraySet}の要素型
     * @param c
     *            コレクション
     * @return {@link DelayQueue}の新しいインスタンス
     * @see DelayQueue#DelayQueue(Collection)
     */
    public static <E extends Delayed> DelayQueue<E> newDelayQueue(
            final Collection<? extends E> c) {
        return new DelayQueue<E>(c);
    }

    /**
     * {@link HashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link HashMap}のキーの型
     * @param <V>
     *            {@link HashMap}の値の型
     * @return {@link HashMap}の新しいインスタンス
     * @see HashMap#HashMap()
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    /**
     * {@link HashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link HashMap}のキーの型
     * @param <V>
     *            {@link HashMap}の値の型
     * @param initialCapacity
     *            初期容量
     * @return {@link HashMap}の新しいインスタンス
     * @see HashMap#HashMap(int)
     */
    public static <K, V> HashMap<K, V> newHashMap(final int initialCapacity) {
        return new HashMap<K, V>(initialCapacity);
    }

    /**
     * {@link HashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link HashMap}のキーの型
     * @param <V>
     *            {@link HashMap}の値の型
     * @param initialCapacity
     *            初期容量
     * @param loadFactor
     *            サイズ変更の制御に使用される負荷係数のしきい値
     * @return {@link HashMap}の新しいインスタンス
     * @see HashMap#HashMap(int, float)
     */
    public static <K, V> HashMap<K, V> newHashMap(final int initialCapacity,
            final float loadFactor) {
        return new HashMap<K, V>(initialCapacity, loadFactor);
    }

    /**
     * {@link HashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link HashMap}のキーの型
     * @param <V>
     *            {@link HashMap}の値の型
     * @param m
     *            作成されるマップに配置されるマップ
     * @return {@link HashMap}の新しいインスタンス
     * @see HashMap#HashMap(int, float)
     */
    public static <K, V> HashMap<K, V> newHashMap(
            final Map<? extends K, ? extends V> m) {
        return new HashMap<K, V>(m);
    }

    /**
     * {@link HashSet}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link HashSet}の要素型
     * @return {@link HashSet}の新しいインスタンス
     * @see HashSet#HashSet()
     */
    public static <E> HashSet<E> newHashSet() {
        return new HashSet<E>();
    }

    /**
     * {@link HashSet}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link HashSet}の要素型
     * @param c
     *            要素がセットに配置されるコレクション
     * @return {@link HashSet}の新しいインスタンス
     * @see HashSet#HashSet()
     */
    public static <E> HashSet<E> newHashSet(final Collection<? extends E> c) {
        return new HashSet<E>(c);
    }

    /**
     * {@link HashSet}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link HashSet}の要素型
     * @param initialCapacity
     *            初期容量
     * @return {@link HashSet}の新しいインスタンス
     * @see HashSet#HashSet()
     */
    public static <E> HashSet<E> newHashSet(final int initialCapacity) {
        return new HashSet<E>(initialCapacity);
    }

    /**
     * {@link HashSet}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link HashSet}の要素型
     * @param initialCapacity
     *            初期容量
     * @param loadFactor
     *            負荷係数
     * @return {@link HashSet}の新しいインスタンス
     * @see HashSet#HashSet()
     */
    public static <E> HashSet<E> newHashSet(final int initialCapacity,
            final float loadFactor) {
        return new HashSet<E>(initialCapacity, loadFactor);
    }

    /**
     * {@link Hashtable}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link Hashtable}のキーの型
     * @param <V>
     *            {@link Hashtable}の値の型
     * @return {@link Hashtable}の新しいインスタンス
     * @see Hashtable#Hashtable()
     */
    public static <K, V> Hashtable<K, V> newHashtable() {
        return new Hashtable<K, V>();
    }

    /**
     * {@link Hashtable}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link Hashtable}のキーの型
     * @param <V>
     *            {@link Hashtable}の値の型
     * @param initialCapacity
     *            ハッシュテーブルの初期容量
     * @return {@link Hashtable}の新しいインスタンス
     * @see Hashtable#Hashtable(int)
     */
    public static <K, V> Hashtable<K, V> newHashtable(final int initialCapacity) {
        return new Hashtable<K, V>(initialCapacity);
    }

    /**
     * {@link Hashtable}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link Hashtable}のキーの型
     * @param <V>
     *            {@link Hashtable}の値の型
     * @param initialCapacity
     *            ハッシュテーブルの初期容量
     * @param loadFactor
     *            ハッシュテーブルの負荷係数
     * @return {@link Hashtable}の新しいインスタンス
     * @see Hashtable#Hashtable(int, float)
     */
    public static <K, V> Hashtable<K, V> newHashtable(
            final int initialCapacity, final float loadFactor) {
        return new Hashtable<K, V>(initialCapacity, loadFactor);
    }

    /**
     * {@link Hashtable}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link Hashtable}のキーの型
     * @param <V>
     *            {@link Hashtable}の値の型
     * @param m
     *            作成されるマップに配置されるマップ
     * @return {@link Hashtable}の新しいインスタンス
     * @see Hashtable#Hashtable(Map)
     */
    public static <K, V> Hashtable<K, V> newHashtable(
            final Map<? extends K, ? extends V> m) {
        return new Hashtable<K, V>(m);
    }

    /**
     * {@link IdentityHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link IdentityHashMap}のキーの型
     * @param <V>
     *            {@link IdentityHashMap}の値の型
     * @return {@link IdentityHashMap}の新しいインスタンス
     * @see IdentityHashMap#IdentityHashMap()
     */
    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return new IdentityHashMap<K, V>();
    }

    /**
     * {@link IdentityHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link IdentityHashMap}のキーの型
     * @param <V>
     *            {@link IdentityHashMap}の値の型
     * @param expectedMaxSize
     *            マップの予想最大サイズ
     * @return {@link IdentityHashMap}の新しいインスタンス
     * @see IdentityHashMap#IdentityHashMap(int)
     */
    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap(
            final int expectedMaxSize) {
        return new IdentityHashMap<K, V>(expectedMaxSize);
    }

    /**
     * {@link IdentityHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link IdentityHashMap}のキーの型
     * @param <V>
     *            {@link IdentityHashMap}の値の型
     * @param m
     *            作成されるマップに配置されるマップ
     * @return {@link IdentityHashMap}の新しいインスタンス
     * @see IdentityHashMap#IdentityHashMap(Map)
     */
    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap(
            final Map<? extends K, ? extends V> m) {
        return new IdentityHashMap<K, V>(m);
    }

    /**
     * {@link LinkedBlockingQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link LinkedBlockingQueue}の要素型
     * @return {@link LinkedBlockingQueue}の新しいインスタンス
     * @see LinkedBlockingQueue#LinkedBlockingQueue()
     */
    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
        return new LinkedBlockingQueue<E>();
    }

    /**
     * {@link LinkedBlockingQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link LinkedBlockingQueue}の要素型
     * @param c
     *            最初に含む要素のコレクション
     * @return {@link LinkedBlockingQueue}の新しいインスタンス
     * @see LinkedBlockingQueue#LinkedBlockingQueue(Collection)
     */
    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(
            final Collection<? extends E> c) {
        return new LinkedBlockingQueue<E>(c);
    }

    /**
     * {@link LinkedBlockingQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link LinkedBlockingQueue}の要素型
     * @param initialCapacity
     *            このキューの容量
     * @return {@link LinkedBlockingQueue}の新しいインスタンス
     * @see LinkedBlockingQueue#LinkedBlockingQueue(int)
     */
    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(
            final int initialCapacity) {
        return new LinkedBlockingQueue<E>(initialCapacity);
    }

    /**
     * {@link LinkedHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link LinkedHashMap}のキーの型
     * @param <V>
     *            {@link LinkedHashMap}の値の型
     * @return {@link LinkedHashMap}の新しいインスタンス
     * @see LinkedHashMap#LinkedHashMap()
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }

    /**
     * {@link LinkedHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link LinkedHashMap}のキーの型
     * @param <V>
     *            {@link LinkedHashMap}の値の型
     * @param initialCapacity
     *            初期容量
     * @return {@link LinkedHashMap}の新しいインスタンス
     * @see LinkedHashMap#LinkedHashMap(int)
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(
            final int initialCapacity) {
        return new LinkedHashMap<K, V>(initialCapacity);
    }

    /**
     * {@link LinkedHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link LinkedHashMap}のキーの型
     * @param <V>
     *            {@link LinkedHashMap}の値の型
     * @param initialCapacity
     *            初期容量
     * @param loadFactor
     *            負荷係数
     * @return {@link LinkedHashMap}の新しいインスタンス
     * @see LinkedHashMap#LinkedHashMap(int, float)
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(
            final int initialCapacity, final float loadFactor) {
        return new LinkedHashMap<K, V>(initialCapacity, loadFactor);
    }

    /**
     * {@link LinkedHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link LinkedHashMap}のキーの型
     * @param <V>
     *            {@link LinkedHashMap}の値の型
     * @param m
     *            作成されるマップに配置されるマップ
     * @return {@link LinkedHashMap}の新しいインスタンス
     * @see LinkedHashMap#LinkedHashMap(Map)
     */
    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(
            final Map<? extends K, ? extends V> m) {
        return new LinkedHashMap<K, V>(m);
    }

    /**
     * {@link LinkedHashSet}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link LinkedHashSet}の要素型
     * @return {@link LinkedHashSet}の新しいインスタンス
     * @see LinkedHashSet#LinkedHashSet()
     */
    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet<E>();
    }

    /**
     * {@link LinkedHashSet}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link LinkedHashSet}の要素型
     * @param c
     *            要素がセットに配置されるコレクション
     * @return {@link LinkedHashSet}の新しいインスタンス
     * @see LinkedHashSet#LinkedHashSet(Collection)
     */
    public static <E> LinkedHashSet<E> newLinkedHashSet(
            final Collection<? extends E> c) {
        return new LinkedHashSet<E>(c);
    }

    /**
     * {@link LinkedHashSet}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link LinkedHashSet}の要素型
     * @param initialCapacity
     *            初期容量
     * @return {@link LinkedHashSet}の新しいインスタンス
     * @see LinkedHashSet#LinkedHashSet(int)
     */
    public static <E> LinkedHashSet<E> newLinkedHashSet(
            final int initialCapacity) {
        return new LinkedHashSet<E>(initialCapacity);
    }

    /**
     * {@link LinkedHashSet}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link LinkedHashSet}の要素型
     * @param initialCapacity
     *            初期容量
     * @param loadFactor
     *            負荷係数
     * @return {@link LinkedHashSet}の新しいインスタンス
     * @see LinkedHashSet#LinkedHashSet(int, float)
     */
    public static <E> LinkedHashSet<E> newLinkedHashSet(
            final int initialCapacity, final float loadFactor) {
        return new LinkedHashSet<E>(initialCapacity, loadFactor);
    }

    /**
     * {@link LinkedList}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link LinkedList}の要素型
     * @return {@link LinkedList}の新しいインスタンス
     * @see LinkedList#LinkedList()
     */
    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<E>();
    }

    /**
     * {@link LinkedList}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link LinkedList}の要素型
     * @param c
     *            要素がリストに配置されるコレクション
     * @return {@link LinkedList}の新しいインスタンス
     * @see LinkedList#LinkedList(Collection)
     */
    public static <E> LinkedList<E> newLinkedList(
            final Collection<? extends E> c) {
        return new LinkedList<E>(c);
    }

    /**
     * {@link PriorityBlockingQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link PriorityBlockingQueue}の要素型
     * @return {@link PriorityBlockingQueue}の新しいインスタンス
     * @see PriorityBlockingQueue#PriorityBlockingQueue()
     */
    public static <E> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
        return new PriorityBlockingQueue<E>();
    }

    /**
     * {@link PriorityBlockingQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link PriorityBlockingQueue}の要素型
     * @param c
     *            最初に含む要素のコレクション
     * @return {@link PriorityBlockingQueue}の新しいインスタンス
     * @see PriorityBlockingQueue#PriorityBlockingQueue(Collection)
     */
    public static <E> PriorityBlockingQueue<E> newPriorityBlockingQueue(
            final Collection<? extends E> c) {
        return new PriorityBlockingQueue<E>(c);
    }

    /**
     * {@link PriorityBlockingQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link PriorityBlockingQueue}の要素型
     * @param initialCapacity
     *            この優先度キューの初期容量
     * @return {@link PriorityBlockingQueue}の新しいインスタンス
     * @see PriorityBlockingQueue#PriorityBlockingQueue(int)
     */
    public static <E> PriorityBlockingQueue<E> newPriorityBlockingQueue(
            final int initialCapacity) {
        return new PriorityBlockingQueue<E>(initialCapacity);
    }

    /**
     * {@link PriorityBlockingQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link PriorityBlockingQueue}の要素型
     * @param initialCapacity
     *            この優先度キューの初期容量
     * @param comparator
     *            この優先度キューの順序付けに使用するコンパレータ
     * @return {@link PriorityBlockingQueue}の新しいインスタンス
     * @see PriorityBlockingQueue#PriorityBlockingQueue(int, Comparator)
     */
    public static <E> PriorityBlockingQueue<E> newPriorityBlockingQueue(
            final int initialCapacity, final Comparator<? super E> comparator) {
        return new PriorityBlockingQueue<E>(initialCapacity, comparator);
    }

    /**
     * {@link PriorityQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link PriorityQueue}の要素型
     * @return {@link PriorityQueue}の新しいインスタンス
     * @see PriorityQueue#PriorityQueue()
     */
    public static <E> PriorityQueue<E> newPriorityQueue() {
        return new PriorityQueue<E>();
    }

    /**
     * {@link PriorityQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link PriorityQueue}の要素型
     * @param c
     *            要素が優先度キューに配置されるコレクション
     * @return {@link PriorityQueue}の新しいインスタンス
     * @see PriorityQueue#PriorityQueue(Collection)
     */
    public static <E> PriorityQueue<E> newPriorityQueue(
            final Collection<? extends E> c) {
        return new PriorityQueue<E>(c);
    }

    /**
     * {@link PriorityQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link PriorityQueue}の要素型
     * @param initialCapacity
     *            この優先度キューの初期容量
     * @return {@link PriorityQueue}の新しいインスタンス
     * @see PriorityQueue#PriorityQueue(int)
     */
    public static <E> PriorityQueue<E> newPriorityQueue(
            final int initialCapacity) {
        return new PriorityQueue<E>(initialCapacity);
    }

    /**
     * {@link PriorityQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link PriorityQueue}の要素型
     * @param initialCapacity
     *            この優先度キューの初期容量
     * @param comparator
     *            この優先度キューの順序付けに使用するコンパレータ
     * @return {@link PriorityQueue}の新しいインスタンス
     * @see PriorityQueue#PriorityQueue(int, Comparator)
     */
    public static <E> PriorityQueue<E> newPriorityQueue(
            final int initialCapacity, final Comparator<? super E> comparator) {
        return new PriorityQueue<E>(initialCapacity, comparator);
    }

    /**
     * {@link PriorityQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link PriorityQueue}の要素型
     * @param c
     *            要素が優先度キューに配置されるコレクション
     * @return {@link PriorityQueue}の新しいインスタンス
     * @see PriorityQueue#PriorityQueue(PriorityQueue)
     */
    public static <E> PriorityQueue<E> newPriorityQueue(
            final PriorityQueue<? extends E> c) {
        return new PriorityQueue<E>(c);
    }

    /**
     * {@link PriorityQueue}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link PriorityQueue}の要素型
     * @param c
     *            要素が優先度キューに配置されるコレクション
     * @return {@link PriorityQueue}の新しいインスタンス
     * @see PriorityQueue#PriorityQueue(SortedSet)
     */
    public static <E> PriorityQueue<E> newPriorityQueue(
            final SortedSet<? extends E> c) {
        return new PriorityQueue<E>(c);
    }

    /**
     * {@link Stack}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link Stack}の要素型
     * @return {@link Stack}の新しいインスタンス
     * @see Stack#Stack()
     */
    public static <E> Stack<E> newStack() {
        return new Stack<E>();
    }

    /**
     * {@link TreeMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link TreeMap}のキーの型
     * @param <V>
     *            {@link TreeMap}の値の型
     * @return {@link TreeMap}の新しいインスタンス
     * @see TreeMap#TreeMap()
     */
    public static <K, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap<K, V>();
    }

    /**
     * {@link TreeMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link TreeMap}のキーの型
     * @param <V>
     *            {@link TreeMap}の値の型
     * @param c
     * @return {@link TreeMap}の新しいインスタンス
     * @see TreeMap#TreeMap()
     */
    public static <K, V> TreeMap<K, V> newTreeMap(final Comparator<? super K> c) {
        return new TreeMap<K, V>(c);
    }

    /**
     * {@link TreeMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link TreeMap}のキーの型
     * @param <V>
     *            {@link TreeMap}の値の型
     * @param m
     *            作成されるマップに配置されるマップ
     * @return {@link TreeMap}の新しいインスタンス
     * @see TreeMap#TreeMap(Map)
     */
    public static <K, V> TreeMap<K, V> newTreeMap(
            final Map<? extends K, ? extends V> m) {
        return new TreeMap<K, V>(m);
    }

    /**
     * {@link TreeMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link TreeMap}のキーの型
     * @param <V>
     *            {@link TreeMap}の値の型
     * @param m
     *            作成されるマップに配置されるマップ
     * @return {@link TreeMap}の新しいインスタンス
     * @see TreeMap#TreeMap(SortedMap)
     */
    public static <K, V> TreeMap<K, V> newTreeMap(
            final SortedMap<K, ? extends V> m) {
        return new TreeMap<K, V>(m);
    }

    /**
     * {@link TreeSet}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link TreeSet}の要素型
     * @return {@link TreeSet}の新しいインスタンス
     * @see TreeSet#TreeSet()
     */
    public static <E> TreeSet<E> newTreeSet() {
        return new TreeSet<E>();
    }

    /**
     * {@link TreeSet}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link TreeSet}の要素型
     * @param c
     *            要素がセットに配置されるコレクション
     * @return {@link TreeSet}の新しいインスタンス
     * @see TreeSet#TreeSet(Collection)
     */
    public static <E> TreeSet<E> newTreeSet(final Collection<? extends E> c) {
        return new TreeSet<E>(c);
    }

    /**
     * {@link TreeSet}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link TreeSet}の要素型
     * @param c
     *            このセットをソートするために使用されるコンパレータ
     * @return {@link TreeSet}の新しいインスタンス
     * @see TreeSet#TreeSet(Comparator)
     */
    public static <E> TreeSet<E> newTreeSet(final Comparator<? super E> c) {
        return new TreeSet<E>(c);
    }

    /**
     * {@link TreeSet}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link TreeSet}の要素型
     * @param s
     *            要素がセットに配置されるコレクション
     * @return {@link TreeSet}の新しいインスタンス
     * @see TreeSet#TreeSet(SortedSet)
     */
    public static <E> TreeSet<E> newTreeSet(final SortedSet<? extends E> s) {
        return new TreeSet<E>(s);
    }

    /**
     * {@link Vector}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link Vector}の要素型
     * @return {@link Vector}の新しいインスタンス
     * @see Vector#Vector()
     */
    public static <E> Vector<E> newVector() {
        return new Vector<E>();
    }

    /**
     * {@link Vector}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link Vector}の要素型
     * @param c
     *            要素がセットに配置されるコレクション
     * @return {@link Vector}の新しいインスタンス
     * @see Vector#Vector(Collection)
     */
    public static <E> Vector<E> newVector(final Collection<? extends E> c) {
        return new Vector<E>(c);
    }

    /**
     * {@link Vector}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link Vector}の要素型
     * @param initialCapacity
     *            {@link Vector}の初期容量
     * @return {@link Vector}の新しいインスタンス
     * @see Vector#Vector(int)
     */
    public static <E> Vector<E> newVector(final int initialCapacity) {
        return new Vector<E>(initialCapacity);
    }

    /**
     * {@link Vector}の新しいインスタンスを作成して返します。
     * 
     * @param <E>
     *            {@link Vector}の要素型
     * @param initialCapacity
     *            {@link Vector}の初期容量
     * @param capacityIncrement
     *            {@link Vector}があふれたときに増加される容量
     * @return {@link Vector}の新しいインスタンス
     * @see Vector#Vector(int, int)
     */
    public static <E> Vector<E> newVector(final int initialCapacity,
            final int capacityIncrement) {
        return new Vector<E>(initialCapacity, capacityIncrement);
    }

    /**
     * {@link WeakHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link WeakHashMap}のキーの型
     * @param <V>
     *            {@link WeakHashMap}の値の型
     * @return {@link WeakHashMap}の新しいインスタンス
     * @see WeakHashMap#WeakHashMap()
     */
    public static <K, V> WeakHashMap<K, V> newWeakHashMap() {
        return new WeakHashMap<K, V>();
    }

    /**
     * {@link WeakHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link WeakHashMap}のキーの型
     * @param <V>
     *            {@link WeakHashMap}の値の型
     * @param initialCapacity
     *            初期容量
     * @return {@link WeakHashMap}の新しいインスタンス
     * @see WeakHashMap#WeakHashMap(int)
     */
    public static <K, V> WeakHashMap<K, V> newWeakHashMap(
            final int initialCapacity) {
        return new WeakHashMap<K, V>(initialCapacity);
    }

    /**
     * {@link WeakHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link WeakHashMap}のキーの型
     * @param <V>
     *            {@link WeakHashMap}の値の型
     * @param initialCapacity
     *            初期容量
     * @param loadFactor
     *            サイズ変更の制御に使用される負荷係数のしきい値
     * @return {@link WeakHashMap}の新しいインスタンス
     * @see WeakHashMap#WeakHashMap(int, float)
     */
    public static <K, V> WeakHashMap<K, V> newWeakHashMap(
            final int initialCapacity, final float loadFactor) {
        return new WeakHashMap<K, V>(initialCapacity, loadFactor);
    }

    /**
     * {@link WeakHashMap}の新しいインスタンスを作成して返します。
     * 
     * @param <K>
     *            {@link WeakHashMap}のキーの型
     * @param <V>
     *            {@link WeakHashMap}の値の型
     * @param m
     *            作成されるマップに配置されるマップ
     * @return {@link WeakHashMap}の新しいインスタンス
     * @see WeakHashMap#WeakHashMap(Map)
     */
    public static <K, V> WeakHashMap<K, V> newWeakHashMap(
            final Map<? extends K, ? extends V> m) {
        return new WeakHashMap<K, V>(m);
    }

    /**
     * マップが指定されたキーを含んでいない場合は、キーを指定された値に関連付けます。
     * <p>
     * マップがすでに指定されたキーを含んでいる場合は、 キーに関連づけられている値を返します。 マップは変更されず、 指定された値は使われません。
     * マップがまだ指定されたキーを含んでいない場合は、 指定された値を値を返します。 マップは変更され、指定されたキーと指定された値が関連づけられます。
     * いずれの場合も、返される値はマップがその時点でキーと関連づけている値です。
     * </p>
     * 
     * @param <K>
     *            {@link HashMap}のキーの型
     * @param <V>
     *            {@link HashMap}の値の型
     * @param map
     *            マップ
     * @param key
     *            指定される値が関連付けられるキー
     * @param value
     *            指定されるキーに関連付けられる値
     * @return 指定されたキーと関連付けられていた以前の値または、キーに関連付けられる値
     * @see ConcurrentHashMap#putIfAbsent(Object, Object)
     */
    public static <K, V> V putIfAbsent(final ConcurrentMap<K, V> map,
            final K key, final V value) {
        V exists = map.putIfAbsent(key, value);
        if (exists != null) {
            return exists;
        }
        return value;
    }

}
