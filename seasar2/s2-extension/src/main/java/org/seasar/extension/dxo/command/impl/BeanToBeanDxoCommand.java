/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import org.seasar.extension.dxo.annotation.AnnotationReader;
import org.seasar.extension.dxo.converter.Converter;
import org.seasar.extension.dxo.converter.ConverterFactory;
import org.seasar.framework.exception.SIllegalArgumentException;

/**
 * BeanからBeanに変換するコマンドです。
 * 
 * @author koichik
 * @author azusa
 */
public class BeanToBeanDxoCommand extends AbstractDxoCommand {

    /** 変換先のクラスです。 */
    protected Class destClass;

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
     * @param destClass
     *            変換先のクラス
     */
    public BeanToBeanDxoCommand(final Class dxoClass, final Method method,
            final ConverterFactory converterFactory,
            final AnnotationReader annotationReader, final Class destClass) {
        super(dxoClass, method, converterFactory, annotationReader);
        this.destClass = destClass;
    }

    protected Object convertScalar(final Object source) {
        assertSource(source);
        final Converter converter = converterFactory.getConverter(source
                .getClass(), destClass);
        return converter.convert(source, destClass, createContext(source));
    }

    protected void convertScalar(final Object source, final Object dest) {
        assertSource(source);
        assertDest(dest);
        final Converter converter = converterFactory.getConverter(source
                .getClass(), dest.getClass());
        converter.convert(source, dest, createContext(source));
    }

    protected Class getDestElementType() {
        return destClass;
    }

    /**
     * 変換元となる引数がnullの場合に{@link SIllegalArgumentException}をスローします。
     * 
     * @param source
     *            変換元となる引数
     * @throws SIllegalArgumentException
     *             引数がnullの場合
     */
    protected void assertSource(Object source) throws SIllegalArgumentException {
        if (source == null) {
            throw new SIllegalArgumentException("ESSR0601", null);
        }
    }

    /**
     * 変換先となる引数がnullの場合に{@link SIllegalArgumentException}をスローします。
     * 
     * @param dest
     *            変換先となる引数
     * @throws SIllegalArgumentException
     *             引数がnullの場合
     */
    protected void assertDest(Object dest) throws SIllegalArgumentException {
        if (dest == null) {
            throw new SIllegalArgumentException("ESSR0602", null);
        }
    }

}
