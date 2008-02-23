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
package org.seasar.extension.jdbc.gen.generator;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.seasar.extension.jdbc.gen.JavaCode;
import org.seasar.extension.jdbc.gen.JavaFileGenerator;
import org.seasar.extension.jdbc.gen.util.CloseableUtil;
import org.seasar.extension.jdbc.gen.util.ConfigurationUtil;
import org.seasar.extension.jdbc.gen.util.TemplateUtil;
import org.seasar.framework.util.FileOutputStreamUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * {@link JavaFileGenerator}の実装クラスです。
 * 
 * @author taedium
 */
public class JavaFileGeneratorImpl implements JavaFileGenerator {

    /** FreeMarkerの設定 */
    protected Configuration configuration;

    /** 基盤となるディレクトリ */
    protected File baseDir;

    /** エンコーディング */
    protected String encoding;

    /**
     * インスタンスを生成します。
     * 
     * @param configuration
     *            FreeMarkerの設定
     * @param baseDir
     *            基盤となるディレクトリ
     * @param encoding
     *            エンコーディング
     */
    public JavaFileGeneratorImpl(Configuration configuration, File baseDir,
            String encoding) {
        this.configuration = configuration;
        this.baseDir = baseDir;
        this.encoding = encoding;
    }

    public void generate(JavaCode javaCode) {
        makeDirsIfNecessary(javaCode.getPackageDir(baseDir));
        Writer writer = openWriter(javaCode.getFile(baseDir));
        try {
            Template template = ConfigurationUtil.getTemplate(configuration,
                    javaCode.getTemplateName());
            TemplateUtil.process(template, javaCode, writer);
        } finally {
            CloseableUtil.close(writer);
        }
    }

    /**
     * 必要であればディレクトリを作成します。
     * 
     * @param dir
     *            ディレクトリ
     */
    protected void makeDirsIfNecessary(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * {@link Writer}を作成します。
     * 
     * @param file
     *            Javaファイル
     * @return {@link Writer}
     */
    protected Writer openWriter(File file) {
        return new OutputStreamWriter(FileOutputStreamUtil.create(file),
                Charset.forName(encoding));
    }

}
