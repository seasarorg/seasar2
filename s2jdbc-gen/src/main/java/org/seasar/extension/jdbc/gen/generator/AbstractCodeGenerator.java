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
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.seasar.extension.jdbc.gen.CodeGenerator;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.util.ConfigurationUtil;
import org.seasar.extension.jdbc.gen.util.JavaFileUtil;
import org.seasar.extension.jdbc.gen.util.TemplateUtil;
import org.seasar.extension.jdbc.gen.util.WriterUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FileOutputStreamUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author taedium
 * 
 */
public abstract class AbstractCodeGenerator implements CodeGenerator {

    protected EntityModel entityModel;

    protected String entityClassName;

    protected String entityGapClassName;

    protected String templateName;

    protected Configuration configuration;

    protected String encoding;

    protected File destDir;

    public AbstractCodeGenerator(EntityModel entityModel,
            String entityClassName, String entityGapClassName,
            String templateName, Configuration configuration, String encoding,
            File destDir) {
        this.entityModel = entityModel;
        this.entityClassName = entityClassName;
        this.entityGapClassName = entityGapClassName;
        this.templateName = templateName;
        this.configuration = configuration;
        this.encoding = encoding;
        this.destDir = destDir;
    }

    public void generate() {
        Map<String, Object> root = createRoot();
        Writer writer = openWriter();
        try {
            Template template = ConfigurationUtil.getTemplate(configuration,
                    templateName);
            TemplateUtil.process(template, root, writer);
        } finally {
            WriterUtil.close(writer);
        }
    }

    protected Map<String, Object> createRoot() {
        Map<String, Object> root = new HashMap<String, Object>();
        String[] elements = ClassUtil
                .splitPackageAndShortClassName(getTargetClassName());
        String packageName = elements[0];
        String shortClassName = elements[1];
        String gapShortClassName = ClassUtil
                .splitPackageAndShortClassName(entityGapClassName)[1];
        root.put("packageName", packageName);
        root.put("imports", getImports());
        root.put("shortClassName", shortClassName);
        root.put("gapShortClassName", gapShortClassName);
        root.put("entityModel", entityModel);
        return root;
    }

    protected Writer openWriter() {
        String className = getTargetClassName();
        File packageDir = new File(destDir, JavaFileUtil
                .getPackageDirName(className));
        if (!packageDir.exists()) {
            packageDir.mkdirs();
        }
        File javaFile = new File(destDir, JavaFileUtil
                .getJavaFileName(className));
        FileOutputStream fos = FileOutputStreamUtil.create(javaFile);
        return new OutputStreamWriter(fos, Charset.forName(encoding));
    }

    protected abstract Set<String> getImports();

    protected abstract String getTargetClassName();
}
