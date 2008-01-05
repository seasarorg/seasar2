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
package org.seasar.framework.jpa.util;

import java.util.EventObject;

/**
 * クラスローダイベントは、クラスローダがクラスを定義する度に配信されます。
 * <p>
 * <code>ClassLoaderEvent</code>オブジェクトは、{@link ClassLoaderListener}メソッドの引数として送信されます。
 * </p>
 * 
 * @author koichik
 * @see ClassLoaderListener
 * @see ChildFirstClassLoader
 */
public class ClassLoaderEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    /** クラス名 */
    protected String className;

    /** クラスデータを構成するバイト列 */
    protected byte[] bytecode;

    /** 定義されたクラス */
    protected Class<?> definedClass;

    /**
     * インスタンスを構築します。
     * 
     * @param source
     *            イベントをトリがしたクラスローダ
     * @param className
     *            定義されたクラスの名前
     * @param bytecode
     *            クラスデータを構成するバイト列
     * @param definedClass
     *            定義されたクラス
     */
    public ClassLoaderEvent(Object source, String className, byte[] bytecode,
            Class<?> definedClass) {
        super(source);
        this.className = className;
        this.bytecode = bytecode;
        this.definedClass = definedClass;
    }

    /**
     * 定義されたクラスの名前を返します
     * 
     * @return 定義されたクラスの名前
     */
    public String getClassName() {
        return className;
    }

    /**
     * クラスデータを構成するバイト列を返します。
     * 
     * @return クラスデータを構成するバイト列
     */
    public byte[] getBytecode() {
        return bytecode;
    }

    /**
     * 定義されたクラスを返します。
     * 
     * @return 定義されたクラス
     */
    public Class<?> getDefinedClass() {
        return definedClass;
    }

}
