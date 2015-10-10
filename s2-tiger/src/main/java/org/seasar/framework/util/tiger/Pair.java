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

/**
 * 二つの値のペアです。
 * 
 * @author koichik
 * @since 2.4.18
 * @param <T1>
 *            1番目の値の型
 * @param <T2>
 *            2番目の値の型
 */
public class Pair<T1, T2> {

    /** 1番目の値 */
    protected T1 first;

    /** 2番目の値 */
    protected T2 second;

    /**
     * 二つの値のペアを作成して返します。
     * 
     * @param <T1>
     *            1番目の値の型
     * @param <T2>
     *            2番目の値の型
     * @param first
     *            1番目の値
     * @param second
     *            2番目の値
     * @return 二つの値のペア
     */
    public static <T1, T2> Pair<T1, T2> pair(final T1 first, final T2 second) {
        return new Pair<T1, T2>(first, second);
    }

    /**
     * インスタンスを構築します。
     */
    public Pair() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param first
     *            1番目の値
     * @param second
     *            2番目の値
     */
    public Pair(final T1 first, final T2 second) {
        this.first = first;
        this.second = second;
    }

    /**
     * 1番目の値を返します。
     * 
     * @return 1番目の値
     */
    public T1 getFirst() {
        return first;
    }

    /**
     * 1番目の値を設定します。
     * 
     * @param first
     *            1番目の値
     */
    public void setFirst(final T1 first) {
        this.first = first;
    }

    /**
     * 2番目の値を返します。
     * 
     * @return 2番目の値
     */
    public T2 getSecond() {
        return second;
    }

    /**
     * 2番目の値を設定します。
     * 
     * @param second
     *            2番目の値
     */
    public void setSecond(final T2 second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "{" + first + ", " + second + "}";
    }

}
