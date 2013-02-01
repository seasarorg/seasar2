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
package org.seasar.framework.jpa.util;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javax.persistence.spi.ClassTransformer;

import org.seasar.framework.exception.IllegalClassFormatRuntimeException;

/**
 * {@link ClassTransformer}に対するユーティリティクラスです。
 * 
 * @author taedium
 */
public class ClassTransformerUtil {

    /**
     * インスタンスを構築します。
     */
    protected ClassTransformerUtil() {
    }

    /**
     * {@link ClassTransformer#transform(ClassLoader, String, Class, ProtectionDomain, byte[])}を実行します。
     * 
     * @param transformer
     *            トランスフォーマ
     * @param loader
     *            変換されるクラスを定義しているローダ。ブートストラップローダの場合は<code>null</code>
     * @param className
     *            『Java 仮想マシン仕様』で定義されている完全修飾クラスの内部形式のクラス名とインタフェース名
     * @param classBeingRedefined
     *            再定義の場合は、再定義されているクラス、そうでない場合は<code>null</code>
     * @param protectionDomain
     *            定義または再定義されているクラスの保護領域
     * @param classfileBuffer
     *            クラスファイル形式の入力バイトバッファ (変更されてはならない)
     * @return 整形式のクラスファイルバッファ (変換の結果)、変換されなかった場合は<code>null</code>
     */
    public static byte[] transform(final ClassTransformer transformer,
            final ClassLoader loader, final String className, final Class<?> classBeingRedefined,
            final ProtectionDomain protectionDomain, final byte[] classfileBuffer) {
        try {
            return transformer.transform(loader, className,
                    classBeingRedefined, protectionDomain, classfileBuffer);
        } catch (final IllegalClassFormatException e) {
            throw new IllegalClassFormatRuntimeException(className, e);
        }
    }
}
