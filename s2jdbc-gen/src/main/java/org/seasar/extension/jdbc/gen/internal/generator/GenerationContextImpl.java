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
package org.seasar.extension.jdbc.gen.internal.generator;

import java.io.File;

import org.seasar.extension.jdbc.gen.generator.GenerationContext;

/**
 * {@link GenerationContext}の実装クラスです。
 * 
 * @author taedium
 */
public class GenerationContextImpl implements GenerationContext {

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
     * インスタンスを構築します。
     * 
     * @param model
     *            データモデル
     * @param file
     *            生成するファイル
     * @param templateName
     *            テンプレート名
     * @param encoding
     *            生成するファイルのエンコーディング
     * @param overwrite
     *            上書きする場合{@code true}、しない場合{@code false}
     */
    public GenerationContextImpl(Object model, File file, String templateName,
            String encoding, boolean overwrite) {
        if (model == null) {
            throw new NullPointerException("model");
        }
        if (file == null) {
            throw new NullPointerException("file");
        }
        if (templateName == null) {
            throw new NullPointerException("templateName");
        }
        if (encoding == null) {
            throw new NullPointerException("dumpFileEncoding");
        }
        this.model = model;
        this.file = file;
        this.templateName = templateName;
        this.encoding = encoding;
        this.overwrite = overwrite;
    }

    public String getEncoding() {
        return encoding;
    }

    public Object getModel() {
        return model;
    }

    public File getFile() {
        return file;
    }

    public String getTemplateName() {
        return templateName;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

}
