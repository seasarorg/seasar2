/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;

/**
 * 変換ルールで指定された簡略式の評価可能な表現です。
 * <p>
 * 簡略式の文法を以下に示します。
 * </p>
 * 
 * <pre>
 * ConversionRuleList:
 *     ConversionRule
 *     ConversionRuleList , ConversionRule
 * 
 * ConversionRule:
 *     DestProperty : null
 *     DestProperty : SourcePropertyList
 * 
 * SourcePropertyList:
 *     SourceProperty
 *     SourcePropertyList . SourceProperty
 * 
 * SourceProperty:
 *     Identifier
 * 
 * DestProperty:
 *     Identifier
 * </pre>
 * 
 * <p>
 * <code>ConversionRule</code>を評価する際、 <code>SourcePathList</code>中に値が<code>null</code>のプロパティが含まれていた場合は、
 * <code>null</code>が<code>ConversionRule</code>の値となります。
 * </p>
 * 
 * @author koichik
 */
public class SimpleExpression implements Expression {

    /** {@link ConversionRule}のリスト */
    protected LinkedList conversionRuleList = new LinkedList();

    /**
     * インスタンスを構築します。
     * 
     * @param source
     *            式のソース文字列
     */
    public SimpleExpression() {
    }

    public Map evaluate(final Object source) {
        final Map map = new LinkedHashMap();
        for (final Iterator it = conversionRuleList.iterator(); it.hasNext();) {
            final ConversionRule conversionRule = (ConversionRule) it.next();
            conversionRule.evaluate(source, map);
        }
        return map;
    }

    /**
     * 変換先プロパティ名を追加します。
     * 
     * @param destProperty
     *            変換先プロパティ名
     */
    protected void addDestProperty(final String destProperty) {
        conversionRuleList.add(new ConversionRule(destProperty));
    }

    /**
     * 変換元プロパティを追加します。
     * 
     * @param sourceProperty
     *            変換元プロパティ
     */
    protected void addSourceProperty(final String sourceProperty) {
        final ConversionRule pair = (ConversionRule) conversionRuleList
                .getLast();
        pair.addSourceProperty(sourceProperty);
    }

    /**
     * 変換ルールを表現します。
     */
    public static class ConversionRule {

        /** 変換先プロパティ名 */
        protected String destProperty;

        /** 変換元プロパティ名のリスト */
        protected LinkedList sourcePropertyList = new LinkedList();

        /**
         * インスタンスを構築します。
         * 
         * @param destProperty
         *            変換先プロパティ名
         */
        public ConversionRule(final String destProperty) {
            this.destProperty = destProperty;
        }

        /**
         * 変換元プロパティ名を追加します。
         * 
         * @param sourceProperty
         *            変換元プロパティ名
         */
        public void addSourceProperty(final String sourceProperty) {
            sourcePropertyList.addLast(sourceProperty);
        }

        /**
         * 変換ルールを評価します。
         * 
         * @param source
         *            変換元オブジェクト
         * @param dest
         *            変換先の{@link Map}
         */
        public void evaluate(Object source, final Map dest) {
            for (final Iterator it = sourcePropertyList.iterator(); it
                    .hasNext();) {
                final String sourceProperty = (String) it.next();
                if (sourceProperty == null) {
                    source = null;
                } else if (source instanceof Map) {
                    source = ((Map) source).get(sourceProperty);
                } else {
                    final BeanDesc beanDesc = BeanDescFactory
                            .getBeanDesc(source.getClass());
                    final PropertyDesc propertyDesc = beanDesc
                            .getPropertyDesc(sourceProperty);
                    source = propertyDesc.getValue(source);
                }
                if (source == null) {
                    break;
                }
            }
            dest.put(destProperty, source);
        }

        public String toString() {
            final StringBuffer buf = new StringBuffer();
            buf.append(destProperty).append(" : ");
            boolean hasValue = false;
            for (final Iterator it = sourcePropertyList.iterator(); it
                    .hasNext();) {
                buf.append(it.next()).append(", ");
                hasValue = true;
            }
            if (hasValue) {
                buf.setLength(buf.length() - 2);
            }
            return new String(buf);
        }
    }

}
