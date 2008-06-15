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

    /** エンコーディング */
    protected String encoding;

    /** データモデル */
    protected Object model;

    /** 生成するファイルのディレクトリ */
    protected File dir;

    /** 生成するファイル */
    protected File file;

    /** テンプレート名 */
    protected String templateName;

    /**
     * エンコーディングを返します。
     * 
     * @return エンコーディング
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * エンコーディングを設定します。
     * 
     * @param encoding
     *            エンコーディング
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
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
     * データモデルを設定します。
     * 
     * @param model
     *            データモデル
     */
    public void setModel(Object model) {
        this.model = model;
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
     * 生成するファイルのディレクトリを設定します。
     * 
     * @param dir
     *            生成するファイルのディレクトリ
     */
    public void setDir(File dir) {
        this.dir = dir;
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
     * 生成するファイルを設定します。
     * 
     * @param file
     *            生成するファイル
     */
    public void setFile(File file) {
        this.file = file;
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
     * テンプレート名を設定します。
     * 
     * @param templateName
     *            テンプレート名
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

}
