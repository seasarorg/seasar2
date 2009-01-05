/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

/**
 * 式です。
 * 
 * @author nakamura
 */
public interface Expression {

    /**
     * 式を評価します。
     * 
     * @return 評価された式の結果
     */
    Object evaluate();

    /**
     * 式を評価します。
     * <p>
     * 評価に失敗した場合は<code>null</code>を返します。
     * </p>
     * 
     * @return 評価に成功した場合は評価された式の結果、評価に失敗した場合は<code>null</code>
     */
    Object evaluateNoException();

    /**
     * {@link #evaluateNoException()}を実行した結果、式の評価にメソッドが存在しかつそのメソッド呼び出しに失敗した場合<code>true</code>を返します。
     * 
     * @return メソッド呼び出しに失敗した場合<code>true</code>、そうでない場合<code>false</code>
     */
    boolean isMethodFailed();

    /**
     * {@link #evaluateNoException()}を実行した結果、式の評価に失敗しているならばその原因を表す例外を返します。
     * 
     * @return 式の評価に失敗している場合その原因を表す例外、そうでない場合<code>null</code>
     */
    Exception getException();

    /**
     * 式の評価または式の実行で例外が発生した場合、その例外をスローします。
     */
    void throwExceptionIfNecessary();
}
