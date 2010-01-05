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
package org.seasar.framework.jpa;

import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;

/**
 * 永続ユニット情報で管理されるクラスに{@link ClassTransformer トランスフォーマ}を適用します。
 * 
 * @author taedium
 */
public interface PersistenceClassTransformer {

    /**
     * 永続ユニット情報で管理されるクラスに{@link ClassTransformer トランスフォーマ}を適用します。
     * 
     * @param unitInfo
     *            永続ユニット情報
     */
    void transform(PersistenceUnitInfo unitInfo);

    /**
     * 指定されたクラスに{@link ClassTransformer トランスフォーマ}を適用します。
     * 
     * @param unitInfo
     *            永続ユニット情報
     * @param className
     *            クラス名
     * @param bytes
     *            クラスファイル形式のバイト列
     * @return 変換されたクラス。変換されなかった場合は引数のバイト列が表すクラス
     */
    Class<?> transform(PersistenceUnitInfo unitInfo, String className,
            byte[] bytes);

}