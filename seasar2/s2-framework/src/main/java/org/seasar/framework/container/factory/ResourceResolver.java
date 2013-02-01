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
package org.seasar.framework.container.factory;

import java.io.InputStream;

/**
 * リソースを取得する方法を定義するインターフェースです。
 * 
 * @author koichik
 * @author Maeno
 */
public interface ResourceResolver {
    /**
     * 読み込み対象となるリソースから入力ストリームを返します。
     * 
     * @param path 読み込み対象となるリソースのパス
     * @return 入力ストリーム
     */
    InputStream getInputStream(String path);
}
