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

import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.util.TemplateUtil;
import org.seasar.extension.jdbc.gen.util.WriterUtil;
import org.seasar.framework.util.FileOutputStreamUtil;

import freemarker.template.Template;

/**
 * @author taedium
 * 
 */
public abstract class AbstractCodeGenerator {

    protected static String GAP_CLASSNAME_SUFFIX = "Abstract";

    protected static String EXTENSION = "java";

    protected String packageName;

    protected Template template;

    protected String encoding;

    protected File destDir;

    public AbstractCodeGenerator(String packageName, Template template,
            String encoding, File destDir) {
        this.packageName = packageName;
        this.template = template;
        this.encoding = encoding;
        this.destDir = destDir;
    }

    public void generate(EntityModel entityModel) {
        Map<String, Object> root = createRoot(entityModel);
        Writer writer = openWriter(entityModel);
        try {
            TemplateUtil.process(template, root, writer);
        } finally {
            WriterUtil.close(writer);
        }
    }

    protected Map<String, Object> createRoot(EntityModel entityModel) {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("package", packageName);
        root.put("imports", getImports(entityModel));
        root.put("entityModel", entityModel);
        root.put("gapClassName", getGapClassName(entityModel));
        return root;
    }

    protected Writer openWriter(EntityModel entityModel) {
        File packageDir = new File(destDir, packageName.replace(".", "/"));
        if (!packageDir.exists()) {
            packageDir.mkdirs();
        }
        File file = new File(packageDir, getFileName(entityModel));
        FileOutputStream fos = FileOutputStreamUtil.create(file);
        return new OutputStreamWriter(fos, Charset.forName(encoding));
    }

    protected String getGapClassName(EntityModel entityModel) {
        return GAP_CLASSNAME_SUFFIX + entityModel.getName();
    }

    protected String getFileName(EntityModel entityModel) {
        return getClassName(entityModel) + "." + EXTENSION;
    }

    protected abstract Set<String> getImports(EntityModel entityModel);

    protected abstract String getClassName(EntityModel entityModel);

}
