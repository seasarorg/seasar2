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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;

import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.generator.Generator;
import org.seasar.extension.jdbc.gen.internal.exception.TemplateRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.CloseableUtil;
import org.seasar.extension.jdbc.gen.internal.util.DeleteEmptyFileWriter;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.FileOutputStreamUtil;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

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
     *            テンプレートファイルを格納したプライマリディレクトリ、プライマリディレクトリを使用しない場合{@code null}
     */
    public GeneratorImpl(String templateFileEncoding,
            File templateFilePrimaryDir) {
        if (templateFileEncoding == null) {
            throw new NullPointerException("templateFileEncoding");
        }
        this.configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setSharedVariable("include", new IncludeDirective());
        configuration.setSharedVariable("currentDate", new OnDemandDateModel());
        configuration.setEncoding(Locale.getDefault(), templateFileEncoding);
        configuration.setNumberFormat("0.#####");
        configuration
                .setTemplateLoader(createTemplateLoader(templateFilePrimaryDir));
    }

    /**
     * {@link TemplateLoader}を作成します。
     * 
     * @param templateFilePrimaryDir
     *            テンプレートファイルを格納したプライマリディレクトリ、プライマリディレクトリを使用しない場合{@code null}
     * @return {@link TemplateLoader}
     */
    protected TemplateLoader createTemplateLoader(File templateFilePrimaryDir) {
        TemplateLoader primary = null;
        if (templateFilePrimaryDir != null) {
            try {
                primary = new FileTemplateLoader(templateFilePrimaryDir);
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        }
        TemplateLoader secondary = new ResourceTemplateLoader(
                DEFAULT_TEMPLATE_DIR_NAME);
        if (primary == null) {
            return secondary;
        }
        return new MultiTemplateLoader(new TemplateLoader[] { primary,
                secondary });
    }

    public void generate(GenerationContext context) {
        boolean exists = exists(context.getFile());
        if (!context.isOverwrite() && exists) {
            return;
        }
        File dir = context.getFile().getParentFile();
        if (dir != null) {
            mkdirs(dir);
        }
        Writer writer = openWriter(context);
        try {
            Template template = getTemplate(context.getTemplateName());
            process(template, context.getModel(), writer);
        } finally {
            CloseableUtil.close(writer);
        }

        if (writer instanceof DeleteEmptyFileWriter) {
            if (((DeleteEmptyFileWriter) writer).isDeleted()) {
                return;
            }
        }
        if (exists) {
            logger.log("DS2JDBCGen0009", new Object[] { context.getFile()
                    .getPath() });
        } else {
            logger.log("DS2JDBCGen0002", new Object[] { context.getFile()
                    .getPath() });
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
        BufferedWriter bw = new BufferedWriter(osw);
        return new DeleteEmptyFileWriter(bw, context.getFile());
    }

    /**
     * テンプレートを取得します。
     * 
     * @param name
     *            テンプレートの名前
     * @return テンプレート
     */
    protected Template getTemplate(String name) {
        try {
            return configuration.getTemplate(name);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * テンプレートを処理します。
     * 
     * @param template
     *            テンプレート
     * @param dataModel
     *            データモデル
     * @param writer
     *            ライタ
     */
    protected void process(Template template, Object dataModel, Writer writer) {
        try {
            template.process(dataModel, writer);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } catch (TemplateException e) {
            throw new TemplateRuntimeException(e);
        }
    }
}
