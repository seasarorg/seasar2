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
package org.seasar.extension.jdbc.gen;

import java.io.File;

/**
 * {@link Generator}のためのコンテキストです。
 * 
 * @author taedium
 */
public class GenerationContext {

    /** データモデル */
    protected Object model;

    /** 生成するファイルの出力先ディレクトリ */
    protected File dir;

    /** 生成するファイル */
    protected File file;

    /** テンプレート名 */
    protected String templateName;

    /** エンコーディング */
    protected String encoding;

    /** 上書きする場合{@code true} */
    protected boolean overwrite;

    /**
     * @param model
     *            データモデル
     * @param dir
     *            生成するファイルの出力先ディレクトリ
     * @param file
     *            生成するファイル
     * @param templateName
     *            テンプレート名
     * @param encoding
     *            生成するファイルのエンコーディング
     * @param overwrite
     *            上書きする場合{@code true}、しない場合{@code false}
     */
    public GenerationContext(Object model, File dir, File file,
            String templateName, String encoding, boolean overwrite) {
        if (model == null) {
            throw new NullPointerException("model");
        }
        if (dir == null) {
            throw new NullPointerException("dir");
        }
        if (file == null) {
            throw new NullPointerException("file");
        }
        if (templateName == null) {
            throw new NullPointerException("templateName");
        }
        if (encoding == null) {
            throw new NullPointerException("encoding");
        }
        this.model = model;
        this.dir = dir;
        this.file = file;
        this.templateName = templateName;
        this.encoding = encoding;
        this.overwrite = overwrite;
    }

    /**
     * エンコーディングを返します。
     * 
     * @return エンコーディング
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * データモデルを返します。
     * 
     * @return データモデル
     */
    public Object getModel() {
        return model;
    }

    /**
     * 生成するファイルのディレクトリを返します。
     * 
     * @return 生成するファイルのディレクトリ
     */
    public File getDir() {
        return dir;
    }

    /**
     * 生成するファイルを返します。
     * 
     * @return 生成するファイル
     */
    public File getFile() {
        return file;
    }

    /**
     * テンプレート名を返します。
     * 
     * @return テンプレート名
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * 上書きする場合は{@code true}、しない場合は{@code false}を返します。
     * 
     * @return 上書きする場合は{@code true}、しない場合は{@code false}
     */
    public boolean isOverwrite() {
        return overwrite;
    }

}
