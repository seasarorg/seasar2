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

import java.util.Enumeration;
import java.util.Iterator;

/**
 * {@link Enumeration}を{@link Iterable}として扱うためのユーティリティ。
 * 
 * @param <E>
 *            {@link java.util.Enumeration}の要素型
 * @author koichik
 */
public class IterableAdapter<E> implements Iterable<E>, Iterator<E> {

    Enumeration<E> enumeration;

    /**
     * {@link Enumeration}を{@link Iterable}として扱う<code>IterableAdapter</code>を作成して返します。
     * 
     * @param <E>
     *            {@link Enumeration}の要素型
     * @param enumeration
     *            反復対象となる{@link java.util.Enumeration}
     * @return {@link Enumeration}を{@link Iterable}として扱う<code>IterableAdapter</code>
     */
    public static <E> IterableAdapter<E> iterable(Enumeration<E> enumeration) {
        return new IterableAdapter<E>(enumeration);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param enumeration
     *            反復対象となる{@link java.util.Enumeration}
     */
    public IterableAdapter(final Enumeration<E> enumeration) {
        this.enumeration = enumeration;
    }

    /**
     * {@link java.util.Enumeration}の反復子を返します。
     * 
     * @return {@link java.util.Enumeration}の反復子
     */
    public Iterator<E> iterator() {
        return this;
    }

    /**
     * @see java.util.Iterator#hasNext
     */
    public boolean hasNext() {
        return enumeration.hasMoreElements();
    }

    /**
     * @see java.util.Iterator#hasNext
     */
    public E next() {
        return enumeration.nextElement();
    }

    /**
     * このメソッドはサポートされません。
     * 
     * @see java.util.Iterator#remove
     */
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }
}
