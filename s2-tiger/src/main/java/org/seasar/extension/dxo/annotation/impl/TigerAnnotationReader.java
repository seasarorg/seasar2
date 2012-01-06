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
package org.seasar.extension.dxo.annotation.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.seasar.extension.dxo.annotation.AnnotationReader;
import org.seasar.extension.dxo.annotation.ConversionRule;
import org.seasar.extension.dxo.annotation.DatePattern;
import org.seasar.extension.dxo.annotation.DestPrefix;
import org.seasar.extension.dxo.annotation.DxoConverter;
import org.seasar.extension.dxo.annotation.ExcludeNull;
import org.seasar.extension.dxo.annotation.ExcludeWhitespace;
import org.seasar.extension.dxo.annotation.SourcePrefix;
import org.seasar.extension.dxo.annotation.TimePattern;
import org.seasar.extension.dxo.annotation.TimestampPattern;
import org.seasar.extension.dxo.converter.Converter;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.beans.util.BeanUtil;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.tiger.AnnotationUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * DxoインタフェースまたはクラスやそのメソッドからTigerアノテーションを読み取るクラスです。
 * 
 * @author koichik
 */
public class TigerAnnotationReader implements AnnotationReader {

    /** S2コンテナ */
    protected S2Container container;

    /** 後続の{@link AnnotationReader} */
    protected AnnotationReader next;

    /** コンバータのキャッシュ */
    protected Map<Class<?>, Map<String, Converter>> convertersCache = CollectionsUtil
            .newConcurrentHashMap();

    /**
     * インスタンスを構築します。
     * 
     * @param container
     *            S2コンテナ
     */
    public TigerAnnotationReader(final S2Container container) {
        this(container, null);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param container
     *            S2コンテナ
     * @param next
     *            後続の{@link AnnotationReader}
     */
    public TigerAnnotationReader(final S2Container container,
            final AnnotationReader next) {
        this.container = container.getRoot();
        this.next = next;
    }

    @SuppressWarnings("unchecked")
    public String getDatePattern(final Class dxoClass, final Method method) {
        final DatePattern datePattern = getAnnotation(dxoClass, method,
                DatePattern.class);
        if (datePattern != null) {
            return datePattern.value();
        }
        if (next != null) {
            return next.getDatePattern(dxoClass, method);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public String getTimePattern(final Class dxoClass, final Method method) {
        final TimePattern timePattern = getAnnotation(dxoClass, method,
                TimePattern.class);
        if (timePattern != null) {
            return timePattern.value();
        }
        if (next != null) {
            return next.getTimePattern(dxoClass, method);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public String getTimestampPattern(final Class dxoClass, final Method method) {
        final TimestampPattern timestampPattern = getAnnotation(dxoClass,
                method, TimestampPattern.class);
        if (timestampPattern != null) {
            return timestampPattern.value();
        }
        if (next != null) {
            return next.getTimestampPattern(dxoClass, method);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public String getConversionRule(final Class dxoClass, final Method method) {
        final ConversionRule mapConversion = method
                .getAnnotation(ConversionRule.class);
        if (mapConversion != null) {
            return mapConversion.value();
        }
        if (next != null) {
            return next.getConversionRule(dxoClass, method);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public boolean isExcludeNull(final Class dxoClass, final Method method) {
        final ExcludeNull excludeNull = getAnnotation(dxoClass, method,
                ExcludeNull.class);
        if (excludeNull != null) {
            return true;
        }
        if (next != null) {
            return next.isExcludeNull(dxoClass, method);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean isExcludeWhitespace(final Class dxoClass, final Method method) {
        final ExcludeWhitespace excludeWhitespace = getAnnotation(dxoClass,
                method, ExcludeWhitespace.class);
        if (excludeWhitespace != null) {
            return true;
        }
        if (next != null) {
            return next.isExcludeWhitespace(dxoClass, method);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public String getSourcePrefix(Class dxoClass, Method method) {
        final SourcePrefix sourcePrefix = getAnnotation(dxoClass, method,
                SourcePrefix.class);
        if (sourcePrefix != null) {
            return sourcePrefix.value();
        }
        if (next != null) {
            return next.getSourcePrefix(dxoClass, method);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public String getDestPrefix(Class dxoClass, Method method) {
        final DestPrefix destPrefix = getAnnotation(dxoClass, method,
                DestPrefix.class);
        if (destPrefix != null) {
            return destPrefix.value();
        }
        if (next != null) {
            return next.getDestPrefix(dxoClass, method);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Map getConverters(final Class destClass) {
        final Map<String, Converter> converters = convertersCache
                .get(destClass);
        if (converters != null) {
            return converters;
        }
        return createConverters(destClass);
    }

    /**
     * 指定アノテーションを取得して返します。
     * <p>
     * メソッドに指定のアノテーションが付与されていればそれを返します。
     * メソッドに指定のアノテーションが付与されていなければ、クラスに付与されているアノテーションを返します。
     * メソッドにもクラスに指定アノテーションが付与されていなければ<code>null</code>を返します。
     * </p>
     * 
     * @param <T>
     *            アノテーションの型
     * @param dxoClass
     *            Dxoクラス
     * @param method
     *            Dxoメソッド
     * @param annotationType
     *            アノテーションの型
     * @return メソッドまたはクラスに付けられたアノテーション
     */
    protected <T extends Annotation> T getAnnotation(final Class<?> dxoClass,
            final Method method, final Class<T> annotationType) {
        final T annotation = method.getAnnotation(annotationType);
        if (annotation != null) {
            return annotation;
        }
        return dxoClass.getAnnotation(annotationType);
    }

    /**
     * クラスに指定されたコンバータの{@link Map}を返します。
     * <p>
     * 指定されたクラスのコンバータの指定されたプロパティについて、プロパティ名をキー、コンバータを値とする{@link Map}を作成します。
     * </p>
     * 
     * @param destClass
     *            変換先のクラス
     * @return クラスに指定されたコンバータの{@link Map}
     */
    protected Map<String, Converter> createConverters(final Class<?> destClass) {
        final Map<String, Converter> converters = CollectionsUtil.newHashMap();
        final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(destClass);
        for (int i = 0; i < beanDesc.getPropertyDescSize(); ++i) {
            final PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
            if (!propertyDesc.isWritable()) {
                continue;
            }
            final Annotation[] annotations = (propertyDesc.hasWriteMethod()) ? propertyDesc
                    .getWriteMethod().getDeclaredAnnotations()
                    : propertyDesc.getField().getDeclaredAnnotations();
            final Converter converter = detectConverter(annotations);
            if (converter != null) {
                converters.put(propertyDesc.getPropertyName(), converter);
            }
        }
        convertersCache.put(destClass, converters);
        return converters;
    }

    /**
     * アノテーションの配列に{@link DxoConverter}メタアノテーションで注釈されたアノテーションが含まれていれば、
     * そのアノテーションに従い{@link Converter}を作成して返します。
     * <p>
     * アノテーションの配列に{@link DxoConverter}メタアノテーションで注釈されたアノテーションが含まれていない場合は
     * <code>null</code>を返します。
     * </p>
     * 
     * @param annotations
     *            プロパティのsetterメソッドまたはpublicフィールドに指定されたアノテーションの配列
     * @return {@link DxoConverter}メタアノテーションで注釈されたアノテーションに従い作成された{@link Converter}
     */
    protected Converter detectConverter(final Annotation[] annotations) {
        for (final Annotation annotation : annotations) {
            final Class<? extends Annotation> annotationType = annotation
                    .annotationType();
            final Annotation metaAnnotation = annotationType
                    .getAnnotation(DxoConverter.class);
            if (metaAnnotation == null) {
                continue;
            }
            final DxoConverter dxoConverterAnnotation = DxoConverter.class
                    .cast(metaAnnotation);
            final String converterName = dxoConverterAnnotation.value();
            final Converter converter = Converter.class.cast(container
                    .getComponent(converterName));
            final Map<?, ?> props = AnnotationUtil.getProperties(annotation);
            BeanUtil.copyProperties(props, converter);
            return converter;
        }
        return null;
    }

}
