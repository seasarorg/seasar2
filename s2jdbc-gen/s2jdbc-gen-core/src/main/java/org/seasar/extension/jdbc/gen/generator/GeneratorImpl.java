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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.Generator;
import org.seasar.extension.jdbc.gen.util.CloseableUtil;
import org.seasar.extension.jdbc.gen.util.ConfigurationUtil;
import org.seasar.extension.jdbc.gen.util.TemplateUtil;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.FileOutputStreamUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * {@link Generator}の実装クラスです。
 * 
 * @author taedium
 */
public class GeneratorImpl implements Generator {

    /** ロガー */
    protected Logger logger = Logger.getLogger(GeneratorImpl.class);

    /** FreeMarkerの設定 */
    protected Configuration configuration;

    /**
     * インスタンスを生成します。
     * 
     * @param configuration
     *            FreeMarkerの設定
     */
    public GeneratorImpl(Configuration configuration) {
        this.configuration = configuration;
    }

    public void generate(GenerationContext context) {
        if (!context.isOverwrite() && exists(context.getFile())) {
            return;
        }
        mkdirs(context.getDir());
        Writer writer = openWriter(context);
        try {
            Template template = ConfigurationUtil.getTemplate(configuration,
                    context.getTemplateName());
            TemplateUtil.process(template, context.getModel(), writer);
            logger.log("DS2JDBCGen0002", new Object[] { context.getFile()
                    .getPath() });
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
     * @return
     */
    protected Writer openWriter(GenerationContext context) {
        Charset charset = Charset.forName(context.getEncoding());
        FileOutputStream fos = FileOutputStreamUtil.create(context.getFile());
        OutputStreamWriter osw = new OutputStreamWriter(fos, charset);
        return new BufferedWriter(osw);
    }

}
