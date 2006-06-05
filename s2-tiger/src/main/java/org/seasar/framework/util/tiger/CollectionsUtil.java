/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author koichik
 */
public abstract class CollectionsUtil {
    private CollectionsUtil() {
    }

    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(
            final int initialCapacity) {
        return new ArrayBlockingQueue<E>(initialCapacity);
    }

    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(
            final int capacity, final boolean fair) {
        return new ArrayBlockingQueue<E>(capacity, fair);
    }

    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(
            final int capacity, final boolean fair,
            final Collection<? extends E> c) {
        return new ArrayBlockingQueue<E>(capacity, fair, c);
    }

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    public static <E> ArrayList<E> newArrayList(final Collection<? extends E> c) {
        return new ArrayList<E>(c);
    }

    public static <E> ArrayList<E> newArrayList(final int initialCapacity) {
        return new ArrayList<E>(initialCapacity);
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<K, V>();
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(
            final int initialCapacity) {
        return new ConcurrentHashMap<K, V>(initialCapacity);
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(
            final int initialCapacity, final float loadFactor,
            final int concurrencyLevel) {
        return new ConcurrentHashMap<K, V>(initialCapacity, loadFactor,
                concurrencyLevel);
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(
            final Map<? extends K, ? extends V> m) {
        return new ConcurrentHashMap<K, V>(m);
    }

    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
        return new ConcurrentLinkedQueue<E>();
    }

    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(
            final Collection<? extends E> c) {
        return new ConcurrentLinkedQueue<E>(c);
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<E>();
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(
            final Collection<? extends E> c) {
        return new CopyOnWriteArrayList<E>(c);
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(
            final E[] toCopyIn) {
        return new CopyOnWriteArrayList<E>(toCopyIn);
    }

    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet() {
        return new CopyOnWriteArraySet<E>();
    }

    public static <E> CopyOnWriteArraySet<E> newCopyOnWriteArraySet(
            final Collection<? extends E> c) {
        return new CopyOnWriteArraySet<E>(c);
    }

    public static <E extends Delayed> DelayQueue<E> newDelayQueue() {
        return new DelayQueue<E>();
    }

    public static <E extends Delayed> DelayQueue<E> newDelayQueue(
            final Collection<? extends E> c) {
        return new DelayQueue<E>(c);
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    public static <K, V> HashMap<K, V> newHashMap(final int initialCapacity) {
        return new HashMap<K, V>(initialCapacity);
    }

    public static <K, V> HashMap<K, V> newHashMap(final int initialCapacity,
            final float loadFactor) {
        return new HashMap<K, V>(initialCapacity, loadFactor);
    }

    public static <K, V> HashMap<K, V> newHashMap(
            final Map<? extends K, ? extends V> m) {
        return new HashMap<K, V>(m);
    }

    public static <E> HashSet<E> newHashSet() {
        return new HashSet<E>();
    }

    public static <E> HashSet<E> newHashSet(final Collection<? extends E> c) {
        return new HashSet<E>(c);
    }

    public static <E> HashSet<E> newHashSet(final int initialCapacity) {
        return new HashSet<E>(initialCapacity);
    }

    public static <E> HashSet<E> newHashSet(final int initialCapacity,
            final float loadFactor) {
        return new HashSet<E>(initialCapacity, loadFactor);
    }

    public static <K, V> Hashtable<K, V> newHashtable() {
        return new Hashtable<K, V>();
    }

    public static <K, V> Hashtable<K, V> newHashtable(final int initialCapacity) {
        return new Hashtable<K, V>(initialCapacity);
    }

    public static <K, V> Hashtable<K, V> newHashtable(
            final int initialCapacity, final float loadFactor) {
        return new Hashtable<K, V>(initialCapacity, loadFactor);
    }

    public static <K, V> Hashtable<K, V> newHashtable(
            final Map<? extends K, ? extends V> m) {
        return new Hashtable<K, V>(m);
    }

    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
        return new IdentityHashMap<K, V>();
    }

    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap(
            final int expectedMaxSize) {
        return new IdentityHashMap<K, V>(expectedMaxSize);
    }

    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap(
            final Map<? extends K, ? extends V> m) {
        return new IdentityHashMap<K, V>(m);
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
        return new LinkedBlockingQueue<E>();
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(
            final Collection<? extends E> c) {
        return new LinkedBlockingQueue<E>(c);
    }

    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(
            final int initialCapacity) {
        return new LinkedBlockingQueue<E>(initialCapacity);
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
        return new LinkedHashMap<K, V>();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(
            final int initialCapacity) {
        return new LinkedHashMap<K, V>(initialCapacity);
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(
            final int initialCapacity, final float loadFactor) {
        return new LinkedHashMap<K, V>(initialCapacity, loadFactor);
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(
            final Map<? extends K, ? extends V> m) {
        return new LinkedHashMap<K, V>(m);
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet<E>();
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(
            final Collection<? extends E> c) {
        return new LinkedHashSet<E>(c);
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(
            final int initialCapacity) {
        return new LinkedHashSet<E>(initialCapacity);
    }

    public static <E> LinkedHashSet<E> newLinkedHashSet(
            final int initialCapacity, final float loadFactor) {
        return new LinkedHashSet<E>(initialCapacity, loadFactor);
    }

    public static <E> LinkedList<E> newLinkedList() {
        return new LinkedList<E>();
    }

    public static <E> LinkedList<E> newLinkedList(
            final Collection<? extends E> c) {
        return new LinkedList<E>(c);
    }

    public static <E> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
        return new PriorityBlockingQueue<E>();
    }

    public static <E> PriorityBlockingQueue<E> newPriorityBlockingQueue(
            final Collection<? extends E> c) {
        return new PriorityBlockingQueue<E>(c);
    }

    public static <E> PriorityBlockingQueue<E> newPriorityBlockingQueue(
            final int initialCapacity) {
        return new PriorityBlockingQueue<E>(initialCapacity);
    }

    public static <E> PriorityBlockingQueue<E> newPriorityBlockingQueue(
            final int initialCapacity, final Comparator<? super E> comparator) {
        return new PriorityBlockingQueue<E>(initialCapacity, comparator);
    }

    public static <E> PriorityQueue<E> newPriorityQueue() {
        return new PriorityQueue<E>();
    }

    public static <E> PriorityQueue<E> newPriorityQueue(
            final Collection<? extends E> c) {
        return new PriorityQueue<E>(c);
    }

    public static <E> PriorityQueue<E> newPriorityQueue(
            final int initialCapacity) {
        return new PriorityQueue<E>(initialCapacity);
    }

    public static <E> PriorityQueue<E> newPriorityQueue(
            final int initialCapacity, final Comparator<? super E> comparator) {
        return new PriorityQueue<E>(initialCapacity, comparator);
    }

    public static <E> PriorityQueue<E> newPriorityQueue(
            final PriorityQueue<? extends E> c) {
        return new PriorityQueue<E>(c);
    }

    public static <E> PriorityQueue<E> newPriorityQueue(
            final SortedSet<? extends E> c) {
        return new PriorityQueue<E>(c);
    }

    public static <E> Stack<E> newStack() {
        return new Stack<E>();
    }

    public static <K, V> TreeMap<K, V> newTreeMap() {
        return new TreeMap<K, V>();
    }

    public static <K, V> TreeMap<K, V> newTreeMap(final Comparator<? super K> c) {
        return new TreeMap<K, V>(c);
    }

    public static <K, V> TreeMap<K, V> newTreeMap(
            final Map<? extends K, ? extends V> m) {
        return new TreeMap<K, V>(m);
    }

    public static <K, V> TreeMap<K, V> newTreeMap(
            final SortedMap<K, ? extends V> m) {
        return new TreeMap<K, V>(m);
    }

    public static <E> TreeSet<E> newTreeSet() {
        return new TreeSet<E>();
    }

    public static <E> TreeSet<E> newTreeSet(final Collection<? extends E> c) {
        return new TreeSet<E>(c);
    }

    public static <E> TreeSet<E> newTreeSet(final Comparator<? super E> c) {
        return new TreeSet<E>(c);
    }

    public static <E> TreeSet<E> newTreeSet(final SortedSet<? extends E> s) {
        return new TreeSet<E>(s);
    }

    public static <E> Vector<E> newVector() {
        return new Vector<E>();
    }

    public static <E> Vector<E> newVector(final Collection<? extends E> c) {
        return new Vector<E>(c);
    }

    public static <E> Vector<E> newVector(final int initialCapacity) {
        return new Vector<E>(initialCapacity);
    }

    public static <E> Vector<E> newVector(final int initialCapacity,
            final int capacityIncrement) {
        return new Vector<E>(initialCapacity, capacityIncrement);
    }

    public static <K, V> WeakHashMap<K, V> newWeakHashMap() {
        return new WeakHashMap<K, V>();
    }

    public static <K, V> WeakHashMap<K, V> newWeakHashMap(
            final int initialCapacity) {
        return new WeakHashMap<K, V>(initialCapacity);
    }

    public static <K, V> WeakHashMap<K, V> newWeakHashMap(
            final int initialCapacity, final float loadFactor) {
        return new WeakHashMap<K, V>(initialCapacity, loadFactor);
    }

    public static <K, V> WeakHashMap<K, V> newWeakHashMap(
            final Map<? extends K, ? extends V> m) {
        return new WeakHashMap<K, V>(m);
    }
}
