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
package org.seasar.extension.dxo.util;

/**
 * 変換ルールの{@link SimpleExpression 簡略式}を表すソース文字列を解析します。
 * 
 * @author koichik
 */
public class SimpleExpressionParser {

    /** ソース文字列 */
    protected String source;

    /** ソース文字列の長さ */
    protected int length;

    /** ソース文字列中の未解析の位置 */
    protected int index;

    /** 解析結果 */
    protected SimpleExpression expression;

    /**
     * ソース文字列を解析して、結果の{@link SimpleExpression}を返します。
     * <p>
     * 解析できなかった場合は<code>null</code>を返します。
     * </p>
     * 
     * @param source
     *            ソース文字列
     * @return 解析結果の{@link SimpleExpression}
     */
    public static SimpleExpression parse(final String source) {
        final SimpleExpressionParser parser = new SimpleExpressionParser(source);
        return parser.conversionRuleList();
    }

    /**
     * インスタンスを構築します。
     * 
     * @param source
     *            ソース文字列
     */
    protected SimpleExpressionParser(final String source) {
        super();
        this.source = source;
        this.length = source.length();
        this.expression = new SimpleExpression();
    }

    /**
     * 変換ルールの並びを解析します。
     * 
     * <pre>
     * ConversionRuleList:
     *     ConversionRule
     *     ConversionRuleList , ConversionRule
     * </pre>
     * 
     * @return 解析結果の{@link SimpleExpression}
     */
    protected SimpleExpression conversionRuleList() {
        try {
            conversionRule();
            while (comma()) {
                conversionRule();
            }
            eof();
            return expression;
        } catch (final SimpleExpressionParseException e) {
            return null;
        }
    }

    /**
     * 変換ルールを解析します。
     * 
     * <pre>
     * ConversionRule:
     *     DestProperty : null
     *     DestProperty : SourcePropertyList
     * </pre>
     */
    protected void conversionRule() {
        destProperty();
        colon();
        if (!nullLiteral()) {
            sourcePropertyList();
        }
    }

    /**
     * 次のトークンがカンマなら<code>true</code>を返します。
     * 
     * @return 次のトークンがカンマなら<code>true</code>
     */
    protected boolean comma() {
        return nextChar(',');
    }

    /**
     * 未解析のソース文字列が空白文字だけであることを確認します。
     */
    protected void eof() {
        skipWhiteSpace();
        if (index < length) {
            throw new SimpleExpressionParseException();
        }
    }

    /**
     * 変換先のプロパティ名を解析します。
     * 
     * <pre>
     * DestProperty:
     *     Identifier
     * </pre>
     */
    protected void destProperty() {
        final String name = nextIdentifier();
        expression.addDestProperty(name);
    }

    /**
     * 次のトークンがコロンであることを確認します。
     */
    protected void colon() {
        if (!nextChar(':')) {
            throw new SimpleExpressionParseException();
        }
    }

    /**
     * 変換元プロパティ名の並びを解析します。
     * 
     * <pre>
     * SourcePropertyList:
     *     SourceProperty
     *     SourcePropertyList . SourceProperty
     * </pre>
     */
    protected void sourcePropertyList() {
        sourceProperty();
        while (period()) {
            sourceProperty();
        }
    }

    /**
     * 変換元プロパティ名を解析します。
     * 
     * <pre>
     * SourceProperty:
     *     Identifier
     * </pre>
     */
    protected void sourceProperty() {
        final String value = nextIdentifier();
        expression.addSourceProperty(value);
    }

    /**
     * 次のトークンがリテラル<code>null</code>なら<code>true</code>を返します。
     * 
     * @return 次のトークンがリテラル<code>null</code>なら<code>true</code>
     */
    protected boolean nullLiteral() {
        final int savedIndex = index;
        final String value = nextIdentifier();
        if ("null".equals(value)) {
            expression.addSourceProperty(null);
            return true;
        }
        index = savedIndex;
        return false;
    }

    /**
     * 次のトークンがピリオドなら<code>true</code>を返します。
     * 
     * @return 次のトークンがピリオドなら<code>true</code>
     */
    protected boolean period() {
        return nextChar('.');
    }

    /**
     * 未解析文字列の空白記号を読み飛ばします。
     */
    protected void skipWhiteSpace() {
        for (; index < length; ++index) {
            if (!Character.isWhitespace(source.charAt(index))) {
                return;
            }
        }
    }

    /**
     * 未解析文字列の次の文字が期待値であれば<code>true</code>を返します。
     * 
     * @param expected
     *            期待している文字
     * @return 未解析文字列の次の文字が期待値であれば<code>true</code>
     */
    protected boolean nextChar(final char expected) {
        skipWhiteSpace();
        if (index >= length) {
            return false;
        }
        if (source.charAt(index) == expected) {
            ++index;
            return true;
        }
        return false;
    }

    /**
     * 未解析文字列から識別子を返します。
     * 
     * @return 識別子
     */
    protected String nextIdentifier() {
        skipWhiteSpace();
        if (index >= length
                || !Character.isJavaIdentifierStart(source.charAt(index))) {
            throw new SimpleExpressionParseException();
        }

        final int beginIndex = index;
        for (; index < length; ++index) {
            if (!Character.isJavaIdentifierPart(source.charAt(index))) {
                break;
            }
        }
        final int endIndex = index;
        return source.substring(beginIndex, endIndex);
    }

}
