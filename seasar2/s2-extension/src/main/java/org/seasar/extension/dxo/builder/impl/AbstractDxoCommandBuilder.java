/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo.builder.impl;

import org.seasar.extension.dxo.annotation.AnnotationReader;
import org.seasar.extension.dxo.annotation.AnnotationReaderFactory;
import org.seasar.extension.dxo.builder.DxoCommandBuilder;
import org.seasar.extension.dxo.command.DxoCommand;
import org.seasar.extension.dxo.converter.Converter;
import org.seasar.extension.dxo.converter.ConverterFactory;

/**
 * Dxoのメソッドに応じた{@link DxoCommand}のインスタンスを生成するビルダの抽象クラスです。
 * 
 * @author koichik
 */
public abstract class AbstractDxoCommandBuilder implements DxoCommandBuilder {

    /** {@link Converter}のファクトリです。 */
    protected ConverterFactory converterFactory;

    /** {@link AnnotationReader}のファクトリです。 */
    protected AnnotationReaderFactory annotationReaderFactory;

    /**
     * {@link Converter}のファクトリを設定します。
     * 
     * @param converterFactory
     *            {@link Converter}のファクトリ
     */
    public void setConverterFactory(final ConverterFactory converterFactory) {
        this.converterFactory = converterFactory;
    }

    /**
     * {@link AnnotationReader}のファクトリを設定します。
     * 
     * @param annotationReaderFactory
     *            {@link AnnotationReader}のファクトリ
     */
    public void setAnnotationReaderFactory(
            final AnnotationReaderFactory annotationReaderFactory) {
        this.annotationReaderFactory = annotationReaderFactory;
    }

    /**
     * {@link AnnotationReader}のファクトリを返します。
     * 
     * @return {@link AnnotationReader}のファクトリ
     */
    protected AnnotationReader getAnnotationReader() {
        return annotationReaderFactory.getAnnotationReader();
    }

}
