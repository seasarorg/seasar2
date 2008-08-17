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
package org.seasar.extension.jdbc.gen.internal.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.generator.Generator;
import org.seasar.extension.jdbc.gen.internal.util.CloseableUtil;
import org.seasar.extension.jdbc.gen.internal.util.ConfigurationUtil;
import org.seasar.extension.jdbc.gen.internal.util.TemplateUtil;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.FileOutputStreamUtil;
import org.seasar.framework.util.ResourceUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * {@link Generator}の実装クラスです。
 * <p>
 * テンプレートエンジンのFreeMarkerを利用します。
 * <p>
 * 
 * @author taedium
 */
public class GeneratorImpl implements Generator {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(GeneratorImpl.class);

    /** デフォルトのテンプレートディレクトリの名前 */
    protected static String DEFAULT_TEMPLATE_DIR_NAME = "org/seasar/extension/jdbc/gen/internal/generator/tempaltes";

    /** FreeMarkerの設定 */
    protected Configuration configuration;

    /**
     * インスタンスを構築します。
     * 
     * @param configuration
     *            FreeMarkerの設定
     */
    public GeneratorImpl(Configuration configuration) {
        if (configuration == null) {
            throw new NullPointerException("configuration");
        }
        this.configuration = configuration;
    }

    /**
     * インスタンスを構築します。
     * 
     * @param templateFileEncoding
     *            テンプレートファイルのエンコーディング
     * @param templateFilePrimaryDir
     *            テンプレートファイルを格納したディレクトリ
     */
    public GeneratorImpl(String templateFileEncoding,
            File templateFilePrimaryDir) {
        if (templateFileEncoding == null) {
            throw new NullPointerException("templateFileEncoding");
        }
        this.configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setEncoding(Locale.getDefault(), templateFileEncoding);
        configuration.setNumberFormat("0.#####");
        File templateFileSecondaryDir = ResourceUtil
                .getResourceAsFile(DEFAULT_TEMPLATE_DIR_NAME);
        ConfigurationUtil.setDirectoriesForTemplateLoading(configuration,
                templateFilePrimaryDir, templateFileSecondaryDir);
    }

    public void generate(GenerationContext context) {
        boolean exists = exists(context.getFile());
        if (!context.isOverwrite() && exists) {
            return;
        }
        mkdirs(context.getDir());
        Writer writer = openWriter(context);
        try {
            Template template = ConfigurationUtil.getTemplate(configuration,
                    context.getTemplateName());
            TemplateUtil.process(template, context.getModel(), writer);
            if (exists) {
                logger.log("DS2JDBCGen0009", new Object[] { context.getFile()
                        .getPath() });
            } else {
                logger.log("DS2JDBCGen0002", new Object[] { context.getFile()
                        .getPath() });
            }
        } finally {
            CloseableUtil.close(writer);
        }
    }

    /**
     * {@code file}が存在する場合に{@code true}を返します。
     * 
     * @param file
     *            ファイル
     * @return {@code file}が存在する場合は{@code true}、そうでない場合は{@code false}
     */
    protected boolean exists(File file) {
        return file.exists();
    }

    /**
     * ディレクトリを生成します。
     * 
     * @param dir
     *            ディレクトリ
     */
    protected void mkdirs(File dir) {
        dir.mkdirs();
    }

    /**
     * {@link Writer}を開きます。
     * 
     * @param context
     *            コンテキスト
     * @return {@link Writer}
     */
    protected Writer openWriter(GenerationContext context) {
        Charset charset = Charset.forName(context.getEncoding());
        FileOutputStream fos = FileOutputStreamUtil.create(context.getFile());
        OutputStreamWriter osw = new OutputStreamWriter(fos, charset);
        return new BufferedWriter(osw);
    }

}
