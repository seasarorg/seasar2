/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory;

import org.seasar.framework.container.S2Container;

/**
 * 
 * 特定の形式の定義情報から{@link org.seasar.framework.container.S2Container S2コンテナ}を組み立てるビルダのインターフェースです。
 * 
 * 
 * @author higa
 * @author azusa
 * 
 */
public interface S2ContainerBuilder {

    /**
     * 指定された設定ファイルからS2コンテナを組み立てます。
     * 
     * @param path
     *            設定ファイルのパス
     * @return S2コンテナ
     */
    public S2Container build(String path);

    /**
     * 指定された設定ファイルから指定されたクラスローダを使用してS2コンテナを組み立てます。
     * 
     * @param path
     *            設定ファイルのパス
     * @param classLoader
     *            S2コンテナを組み立てるのに使用するクラスローダ
     * @return S2コンテナ
     */
    public S2Container build(String path, ClassLoader classLoader);

    /**
     * 指定された設定ファイルからS2コンテナを組み立て、親S2コンテナに対してインクルードします。
     * 
     * @param parent
     *            親となるS2コンテナ
     * @param path
     *            設定ファイルのパス
     * @return 構築したS2コンテナ
     * @see S2Container#include(S2Container)
     */
    public S2Container include(S2Container parent, String path);
}
