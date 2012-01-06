/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.impl;

import org.seasar.framework.container.Expression;

/**
 * {@link Expression 式}の抽象基底クラスです。
 * <p>
 * 式は文字列で表現されます。この文字列はソースと呼ばれます。
 * ソースはファイルの中に記述される場合があります。その場合、式はソースファイル名とその中での行番号を持つことができます。
 * </p>
 * 
 * @author koichik
 */
public abstract class AbstractExpression implements Expression {

    /**
     * ソース名です。
     */
    protected String sourceName;

    /**
     * 行番号です。
     */
    protected int lineNumber;

    /**
     * ソースです。
     */
    protected String source;

    /**
     * <code>AbstractExpression</code>のインスタンスを構築します。
     * 
     * @param source
     *            式のソース
     */
    public AbstractExpression(final String source) {
        this(null, 0, source);
    }

    /**
     * <code>AbstractExpression</code>のインスタンスを構築します。
     * 
     * @param sourceName
     *            ソースファイル名
     * @param lineNumber
     *            ソースファイル中の行番号
     * @param source
     *            式のソース
     */
    public AbstractExpression(final String sourceName, int lineNumber,
            final String source) {
        this.sourceName = sourceName;
        this.lineNumber = lineNumber;
        this.source = source;
    }

    /**
     * 式のソースを返します。
     * 
     * @return 式のソース
     */
    public String getSource() {
        return source;
    }

    /**
     * 式を定義しているファイル名を返します。式がファイルに定義されていない場合は<code>null</code>を返します。
     * 
     * @return 式を定義しているファイル名
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * 式が定義されているファイル中の行番号を返します。 行番号は1から始まります。 式がファイルに定義されていない場合は<code>0</code>を返します。
     * 
     * @return 式が定義されているファイル中の行番号
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * このインスタンスの文字列表現として式のソースを返します。
     * 
     * @return 式のソース
     */
    public String toString() {
        return source;
    }

}
