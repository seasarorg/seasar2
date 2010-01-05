/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo.converter.impl;

/**
 * Tiger(Java5)固有の機能を利用するためのヘルパ・クラスです。
 * 
 * @author koichik
 */
public interface TigerSupport {

    /** <code>TigerSupport</code>のインスタンス */
    TigerSupport instance = TigerSupportFactory.createTigerSupport();

    /**
     * <code>clazz</code>が列挙である場合に<code>true</code>を返します。
     * 
     * @param clazz
     *            クラス
     * @return <code>clazz</code>が列挙である場合に<code>true</code>
     */
    boolean isEnum(Class clazz);

    /**
     * 列挙<code>o</code>の列挙定数の序数を返します。
     * 
     * @param o
     *            列挙
     * @return 列挙定数の序数
     * @throws ClassCastException
     *             <code>o</code>が列挙でない場合にスローされます
     * @see java.lang.Enum#ordinal()
     */
    int getEnumOrdinal(Object o);

    /**
     * 列挙<code>o</code>の列挙定数の名前を返します。
     * 
     * @param o
     *            列挙
     * @return 列挙定数の名前
     * @throws ClassCastException
     *             <code>o</code>が列挙でない場合にスローされます
     * @see java.lang.Enum#name()
     */
    String getEnumName(Object o);

    /**
     * <code>TigerSupport</code>オブジェクトのファクトリクラスです。
     * 
     * @author koichik
     */
    public class TigerSupportFactory {

        /**
         * <code>TigerSupport</code>のインスタンスを作成します。
         * 
         * @return <code>TigerSupport</code>のインスタンス
         */
        protected static TigerSupport createTigerSupport() {
            try {
                return (TigerSupport) Class
                        .forName(
                                "org.seasar.extension.dxo.converter.impl.TigerSupportImpl")
                        .newInstance();
            } catch (final Exception ignore) {
                return null;
            }
        }

    }

}
