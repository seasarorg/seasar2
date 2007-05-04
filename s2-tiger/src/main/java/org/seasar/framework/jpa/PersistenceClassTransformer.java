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
package org.seasar.framework.jpa;

import java.net.URL;
import java.util.List;

import javax.persistence.spi.ClassTransformer;

/**
 * トランスフォーマ{@link ClassTransformer}を適用します。
 * 
 * @author taedium
 */
public interface PersistenceClassTransformer {

    /**
     * 指定されたjafファイルのリストにトランスフォーマを適用します。
     * 
     * @param transformers
     *            トランスフォーマのリスト
     * @param classLoader
     *            トランスフォーマが適用されたクラスが定義されるクラスローダー
     * @param jarFileUrls
     *            jarファイルを示すURLのリスト
     */
    public void transformJarFiles(List<ClassTransformer> transformers,
            ClassLoader classLoader, List<URL> jarFileUrls);

    /**
     * 指定されたクラスのリストにトランスフォーマを適用します。
     * 
     * @param transformers
     *            トランスフォーマのリスト
     * @param classLoader
     *            トランスフォーマが適用されたクラスが定義されるクラスローダー
     * @param classNames
     *            クラスの名前のリスト
     */
    public void transformClasses(List<ClassTransformer> transformers,
            ClassLoader classLoader, List<String> classNames);

}