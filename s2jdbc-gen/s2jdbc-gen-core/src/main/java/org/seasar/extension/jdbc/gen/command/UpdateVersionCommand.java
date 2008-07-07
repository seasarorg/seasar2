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
package org.seasar.extension.jdbc.gen.command;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.Generator;
import org.seasar.extension.jdbc.gen.generator.GeneratorImpl;
import org.seasar.framework.util.TextUtil;

/**
 * @author taedium
 * 
 */
public class UpdateVersionCommand extends AbstractCommand {

    protected File destDir = new File("sql");

    protected File versionFile = new File(destDir, "version.txt");

    protected String versionTemplateName = "version.ftl";

    protected String sqlFileName = "update-version.sql";

    protected String sqlTemplateName = "update-version.ftl";

    protected String sqlFileEncoding = "UTF-8";

    protected String tableName = "SCHEMA_INFO";

    protected String columnName = "VERSION";

    protected Generator generator;

    @Override
    protected void init() {
        super.init();
        generator = createGenerator();
    }

    protected Generator createGenerator() {
        return new GeneratorImpl(templateFileEncoding, templateDir);
    }

    protected void doExecute() {
        int currentVersion = readVersion();
        int nextVersion = currentVersion + 1;
        generateUpdateSql(nextVersion);
        generateVersion(nextVersion);
    }

    protected int readVersion() {
        if (!versionFile.exists()) {
            return 0;
        }
        String value = TextUtil.readUTF8(versionFile);
        return Integer.valueOf(value.trim());
    }

    protected void generateVersion(int version) {
        Map<String, Integer> model = new HashMap<String, Integer>();
        model.put("version", version);
        GenerationContext context = new GenerationContext(model, versionFile
                .getParentFile(), versionFile, versionTemplateName, "UTF-8",
                true);
        generator.generate(context);
    }

    protected void generateUpdateSql(int version) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("version", version);
        model.put("tableName", tableName);
        model.put("columnName", columnName);
        GenerationContext context = new GenerationContext(model, destDir,
                new File(destDir, sqlFileName), sqlTemplateName,
                sqlFileEncoding, true);
        generator.generate(context);
    }
}
