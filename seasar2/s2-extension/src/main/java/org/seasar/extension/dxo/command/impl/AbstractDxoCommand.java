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
package org.seasar.extension.dxo.command.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.seasar.extension.dxo.IllegalSignatureRuntimeException;
import org.seasar.extension.dxo.annotation.AnnotationReader;
import org.seasar.extension.dxo.command.DxoCommand;
import org.seasar.extension.dxo.converter.ConversionContext;
import org.seasar.extension.dxo.converter.Converter;
import org.seasar.extension.dxo.converter.ConverterFactory;
import org.seasar.extension.dxo.converter.impl.ConversionContextImpl;
import org.seasar.framework.exception.SIllegalArgumentException;

/**
 * Dxoのメソッドに応じた変換を行うコマンドの抽象クラスです。
 * 
 * @author koichik
 */
public abstract class AbstractDxoCommand implements DxoCommand {

    /** Dxoのインターフェースまたはクラス */
    protected Class dxoClass;

    /** Dxoのメソッド */
    protected Method method;

    /** {@link Converter}のファクトリです。 */
    protected ConverterFactory converterFactory;

    /** {@link AnnotationReader}のファクトリです。 */
    protected AnnotationReader annotationReader;

    /** 変換のヘルパです。 */
    protected ConversionHelper conversionHelper;

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
    public AbstractDxoCommand(final Class dxoClass, final Method method,
            final ConverterFactory converterFactory,
            final AnnotationReader annotationReader) {
        this.dxoClass = dxoClass;
        this.method = method;
        this.converterFactory = converterFactory;
        this.annotationReader = annotationReader;
        this.conversionHelper = getConversionHelper(method);
    }

    public Object execute(final Object[] args) {
        return conversionHelper.convert(args);
    }

    /**
     * 単一のオブジェクトを変換して返します。
     * 
     * @param source
     *            変換元のオブジェクト
     * @return 変換した結果のオブジェクト
     */
    protected abstract Object convertScalar(Object source);

    /**
     * 単一のオブジェクト<code>src</code>を<code>dest</code>に変換します。
     * 
     * @param source
     *            変換元のオブジェクト
     * @param dest
     *            変換先のオブジェクト
     */
    protected abstract void convertScalar(Object source, Object dest);

    /**
     * 変換先の要素の型を返します。
     * 
     * @return 変換先の要素の型
     */
    protected abstract Class getDestElementType();

    /**
     * 変換先の要素型の配列を作成して返します。
     * 
     * @param length
     *            配列の長さ
     * @return 変換先の要素型の配列
     */
    protected Object[] createArray(final int length) {
        return (Object[]) Array.newInstance(getDestElementType(), length);
    }

    /**
     * 変換コンテキストを作成して返します。
     * 
     * @param source
     *            変換元のオブジェクト
     * @return 変換コンテキスト
     */
    protected ConversionContext createContext(final Object source) {
        return new ConversionContextImpl(dxoClass, method, converterFactory,
                annotationReader, source);
    }

    /**
     * 変換ヘルパを作成して返します。
     * 
     * @param method
     *            Dxoのメソッド
     * @return 変換ヘルパ
     */
    protected ConversionHelper getConversionHelper(final Method method) {
        final Class[] parameterTypes = method.getParameterTypes();
        final int parameterSize = parameterTypes.length;
        if (parameterSize != 1 && parameterSize != 2) {
            throw new IllegalSignatureRuntimeException(method
                    .getDeclaringClass(), method);
        }
        final Class sourceType = parameterTypes[0];
        final Class destType = parameterSize == 1 ? method.getReturnType()
                : parameterTypes[1];

        if (sourceType.isArray()) {
            if (destType.isArray()) {
                return new ArrayToArrayConversionHelper();
            } else if (List.class.isAssignableFrom(destType)) {
                return new ArrayToListConversionHelper();
            }
        } else if (List.class.isAssignableFrom(sourceType)) {
            if (destType.isArray()) {
                return new ListToArrayConversionHelper();
            } else if (List.class.isAssignableFrom(destType)) {
                return new ListToListConvertsionHelper();
            }
        } else {
            return new ScalarConversionHelper();
        }

        throw new IllegalSignatureRuntimeException(method.getDeclaringClass(),
                method);
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
            throw new SIllegalArgumentException("ESSR0601", new Object[] {
                    dxoClass, method });
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
            throw new SIllegalArgumentException("ESSR0602", new Object[] {
                    dxoClass, method });
        }
    }

    /**
     * メソッドのシグネチャに応じて変換を行うヘルパのインターフェースです。
     */
    public interface ConversionHelper {

        /**
         * 変換を行います。
         * 
         * @param args
         *            Dxoメソッドの引数の配列
         * @return Dxoメソッドの戻り値
         */
        Object convert(Object[] args);

    }

    /**
     * スカラ値 (配列でも{@link List}でもない型) を変換するヘルパです。
     */
    public class ScalarConversionHelper implements ConversionHelper {
        public Object convert(final Object[] args) {
            if (args.length == 1) {
                return convertScalar(args[0]);
            }
            final Object dest = args[1];
            convertScalar(args[0], dest);
            return dest;
        }
    }

    /**
     * 配列から配列へ変換するヘルパです。
     */
    public class ArrayToArrayConversionHelper implements ConversionHelper {
        public Object convert(final Object[] args) {
            final Object[] src = (Object[]) args[0];
            final Object[] dest = args.length == 1 ? createArray(src.length)
                    : (Object[]) args[1];
            for (int i = 0; i < src.length && i < dest.length; ++i) {
                dest[i] = convertScalar(src[i]);
            }
            return dest;
        }
    }

    /**
     * 配列から{@link List}へ変換するヘルパです。
     */
    public class ArrayToListConversionHelper implements ConversionHelper {
        public Object convert(final Object[] args) {
            final Object[] src = (Object[]) args[0];
            final List dest = args.length == 1 ? new ArrayList()
                    : (List) args[1];
            dest.clear();
            for (int i = 0; i < src.length; ++i) {
                dest.add(convertScalar(src[i]));
            }
            return dest;
        }
    }

    /**
     * {@link List}から配列へ変換するヘルパです。
     */
    public class ListToArrayConversionHelper implements ConversionHelper {
        public Object convert(final Object[] args) {
            final List src = (List) args[0];
            final Object[] dest = args.length == 1 ? createArray(src.size())
                    : (Object[]) args[1];
            int i = 0;
            for (final Iterator it = src.iterator(); it.hasNext()
                    && i < dest.length; ++i) {
                dest[i] = convertScalar(it.next());
            }
            return dest;
        }
    }

    /**
     * {@link List}から{@link List}へ変換するヘルパです。
     */
    public class ListToListConvertsionHelper implements ConversionHelper {
        public Object convert(final Object[] args) {
            final List source = (List) args[0];
            final List dest = args.length == 1 ? new ArrayList()
                    : (List) args[1];
            dest.clear();
            for (final Iterator it = source.iterator(); it.hasNext();) {
                dest.add(convertScalar(it.next()));
            }
            return dest;
        }
    }

}
