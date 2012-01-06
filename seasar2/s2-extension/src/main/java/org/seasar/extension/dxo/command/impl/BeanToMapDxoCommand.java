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
package org.seasar.extension.dxo.command.impl;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.seasar.extension.dxo.annotation.AnnotationReader;
import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.Converter;
import org.seasar.extension.dxo.converter.ConverterFactory;
import org.seasar.extension.dxo.util.DxoUtil;
import org.seasar.extension.dxo.util.Expression;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.StringUtil;

/**
 * Beanから{@link Map}に変換するコマンドです。
 * 
 * @author koichik
 */
public class BeanToMapDxoCommand extends AbstractDxoCommand {

    /** パーズ済みの変換ルールです。 */
    protected Expression parsedExpression;

    /** 変換先の{@link Map}に<code>null</code>のマッピングを追加しない場合に<code>true</code>。 */
    protected boolean excludeNull;

    /** 変換先の{@link Map}に空白(スペース，復帰，改行，タブ文字のみ)のマッピングを追加しない場合に<code>true</code>。 */
    protected boolean excludeWhitespace;

    /** 変換元Beanのプロパティの接頭辞です。 */
    protected String sourcePrefix;

    /** 変換先Beanのプロパティの接頭辞です。 */
    protected String destPrefix;

    /** {@link Map}の要素型です。Java5以降の場合のみ有効です。 */
    protected Class valueType;

    /**
     * インスタンスを構築します。
     * 
     * @param dxoClass
     *            Dxoのインターフェースまたはクラス
     * @param method
     *            Dxoのメソッド
     * @param converterFactory
     *            {@link Converter}のファクトリ
     * @param annotationReader
     *            {@link AnnotationReader}のファクトリ
     */
    public BeanToMapDxoCommand(final Class dxoClass, final Method method,
            final ConverterFactory converterFactory,
            final AnnotationReader annotationReader) {
        this(dxoClass, method, converterFactory, annotationReader, null);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param dxoClass
     *            Dxoのインターフェースまたはクラス
     * @param method
     *            Dxoのメソッド
     * @param converterFactory
     *            {@link Converter}のファクトリ
     * @param annotationReader
     *            {@link AnnotationReader}のファクトリ
     * @param expression
     *            変換ルールを表すOGNL式
     */
    public BeanToMapDxoCommand(final Class dxoClass, final Method method,
            final ConverterFactory converterFactory,
            final AnnotationReader annotationReader, final String expression) {
        super(dxoClass, method, converterFactory, annotationReader);
        if (expression != null) {
            parsedExpression = DxoUtil.parseRule(expression);
        }
        excludeNull = annotationReader.isExcludeNull(dxoClass, method);
        excludeWhitespace = annotationReader.isExcludeWhitespace(dxoClass,
                method);
        sourcePrefix = annotationReader.getSourcePrefix(dxoClass, method);
        destPrefix = annotationReader.getDestPrefix(dxoClass, method);
        valueType = DxoUtil.getValueTypeOfTargetMap(method);
        if (valueType == Object.class) {
            valueType = null;
        }
    }

    protected Object convertScalar(final Object source) {
        if (source == null) {
            return null;
        }
        final Map dest;
        if (parsedExpression != null) {
            dest = parsedExpression.evaluate(source);
        } else {
            final String expression = createConversionRule(source.getClass());
            dest = DxoUtil.parseRule(expression).evaluate(source);
        }
        if (excludeNull) {
            removeNullEntry(dest);
        }
        if (excludeNull) {
            removeWhitespaceEntry(dest);
        }
        if (valueType == null) {
            return dest;
        }
        return convertValueType(dest, createContext(source));
    }

    protected void convertScalar(final Object source, final Object dest) {
        assertSource(source);
        assertDest(dest);
        ((Map) dest).putAll((Map) convertScalar(source));
    }

    /**
     * {@link Map}から値が<code>null</code>のマッピングを取り除きます。
     * 
     * @param map
     *            <code>null</code>のマッピングを取り除いた{@link Map}
     */
    protected void removeNullEntry(final Map map) {
        for (final Iterator it = map.entrySet().iterator(); it.hasNext();) {
            final Entry entry = (Entry) it.next();
            if (entry.getValue() == null) {
                it.remove();
            }
        }
    }

    /**
     * {@link Map}から値が空白(スペース，復帰，改行，タブ文字のみ)のマッピングを取り除きます。
     * 
     * @param map
     *            空白(スペース，復帰，改行，タブ文字のみ)のマッピングを取り除いた{@link Map}
     */
    protected void removeWhitespaceEntry(final Map map) {
        for (final Iterator it = map.entrySet().iterator(); it.hasNext();) {
            final Entry entry = (Entry) it.next();
            final Object value = entry.getValue();
            if (value != null && (value instanceof String)
                    && ((String) value).trim().length() == 0) {
                it.remove();
            }
        }
    }

    /**
     * 引数で渡された{@link Map}が持つマッピングの値を{@link #valueType}に変換したマッピングを持つ{@link Map}を作成して返します。
     * <p>
     * 変換元の{@link Map}は<code>Map&lt;String, Object&gt;</code>、 変換後の{@link Map}は<code>Map&lt;String, V&gt;です。
     * ただし、<code>V</code>は{@link #valueType}です。
     * </p>
     * 
     * @param from 変換元の{@link Map}
     * @param context 変換コンテキスト
     * @return 返還後の{@link Map}
     */
    protected Map convertValueType(final Map from,
            final ConversionContext context) {
        final Map to = new LinkedHashMap();
        for (final Iterator it = from.entrySet().iterator(); it.hasNext();) {
            final Entry entry = (Entry) it.next();
            final Object key = entry.getKey();
            final Object value = entry.getValue();
            if (value == null || valueType.isInstance(value)) {
                to.put(key, value);
            } else {
                final Converter converter = converterFactory.getConverter(value
                        .getClass(), valueType);
                to.put(key, converter.convert(value, valueType, context));
            }
        }
        return to;
    }

    protected Class getDestElementType() {
        return Map.class;
    }

    /**
     * Beanのプロパティ名をキー、プロパティ値を値とする{@link Map}を表現するOGNL式を返します。
     * 
     * @param sourceType
     *            変換元Beanの型
     * @return Beanのプロパティ名をキー、プロパティ値を値とする{@link Map}
     */
    protected String createConversionRule(final Class sourceType) {
        final StringBuffer buf = new StringBuffer(100);
        final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(sourceType);
        final int propertySize = beanDesc.getPropertyDescSize();
        for (int i = 0; i < propertySize; ++i) {
            final PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
            if (propertyDesc.isReadable()) {
                final String sourcePropertyName = propertyDesc
                        .getPropertyName();
                final String destPropertyName = toDestPropertyName(sourcePropertyName);
                if (destPropertyName == null) {
                    continue;
                }
                buf.append(destPropertyName).append(": ").append(
                        sourcePropertyName).append(", ");
            }
        }
        if (propertySize > 0) {
            buf.setLength(buf.length() - 2);
        }
        return new String(buf);
    }

    /**
     * 変換元のプロパティに対応する変換後のプロパティ名を返します。
     * <p>
     * 変換元プロパティの接頭辞が指定されている場合は、変換元のプロパティ名から接頭辞を取り除いてデキャピタライズしたものが
     * 変換後のプロパティ名となります。
     * </p>
     * <p>
     * 変換先プロパティの接頭辞が指定されている場合は、その後ろに変換元のプロパティ名をキャピタライズして付加たものが変換後のプロパティ名となります。
     * </p>
     * 
     * @param sourcePropertyName
     *            変換元のプロパティ名
     * @return 変換元のプロパティに対応する変換後のプロパティ名
     */
    protected String toDestPropertyName(final String sourcePropertyName) {
        String destPropertyName = sourcePropertyName;
        if (!StringUtil.isEmpty(sourcePrefix)) {
            if (!sourcePropertyName.startsWith(sourcePrefix)) {
                return null;
            }
            destPropertyName = StringUtil.decapitalize(sourcePropertyName
                    .substring(sourcePrefix.length()));
        }
        if (!StringUtil.isEmpty(destPrefix)) {
            if (destPrefix.endsWith("_")) {
                destPropertyName = destPrefix + destPropertyName;
            } else {
                destPropertyName = destPrefix
                        + StringUtil.capitalize(destPropertyName);
            }
        }
        return destPropertyName;
    }

}
