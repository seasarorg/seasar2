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
package org.seasar.extension.jdbc.gen.generator;

import java.io.File;

/**
 * {@link Generator}のためのコンテキストを表すインタフェースです。
 * 
 * @author taedium
 */
public interface GenerationContext {

    /**
     * エンコーディングを返します。
     * 
     * @return エンコーディング
     */
    String getEncoding();

    /**
     * データモデルを返します。
     * 
     * @return データモデル
     */
    Object getModel();

    /**
     * 生成するファイルを返します。
     * 
     * @return 生成するファイル
     */
    public File getFile();

    /**
     * テンプレート名を返します。
     * 
     * @return テンプレート名
     */
    public String getTemplateName();

    /**
     * 上書きする場合は{@code true}、しない場合は{@code false}を返します。
     * 
     * @return 上書きする場合は{@code true}、しない場合は{@code false}
     */
    public boolean isOverwrite();
}
