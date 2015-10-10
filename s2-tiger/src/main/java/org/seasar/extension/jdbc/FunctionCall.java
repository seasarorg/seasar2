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
package org.seasar.extension.jdbc;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;

/**
 * ストアドファンクションの呼び出しのベースとなるインターフェースです。
 * 
 * @author koichik
 * @param <T>
 *            ファンクションの戻り値の型。戻り値が結果セットの場合は<code>List</code>の要素の型
 * @param <S>
 *            <code>FunctionCall</code>のサブタイプです。
 */
public interface FunctionCall<T, S extends FunctionCall<T, S>> extends
        ModuleCall<S> {

    /**
     * ストアドファンクションの戻り値またはその要素がLOBであることを指定します。
     * <p>
     * このメソッドの呼び出しは、戻り値またはその要素が{@link String}型の場合に有効です。
     * </p>
     * 
     * @return このインスタンス自身
     */
    S lob();

    /**
     * ストアドファンクションの戻り値またはその要素の{@link TemporalType}を指定します。
     * <p>
     * このメソッドの呼び出しは、戻り値またはその要素が{@link Date}もしくは{@link Calendar}型の場合に有効です。
     * </p>
     * 
     * @param temporalType
     *            時制の種別
     * @return このインスタンス自身
     */
    S temporal(TemporalType temporalType);

    /**
     * ストアドファンクションを呼び出し、その戻り値を返します。
     * 
     * @return ストアドファンクションの戻り値
     */
    T getSingleResult();

    /**
     * 結果セットを返すストアドファンクションを呼び出し、その戻り値を返します。
     * 
     * @return ストアドファンクションの戻り値
     */
    List<T> getResultList();

}
