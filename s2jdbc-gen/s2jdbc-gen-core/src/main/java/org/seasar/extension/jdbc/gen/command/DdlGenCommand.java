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
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.gen.ColumnDescFactory;
import org.seasar.extension.jdbc.gen.EntityMetaReader;
import org.seasar.extension.jdbc.gen.ForeignKeyDescFactory;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.Generator;
import org.seasar.extension.jdbc.gen.PrimaryKeyDescFactory;
import org.seasar.extension.jdbc.gen.SchemaModelFactory;
import org.seasar.extension.jdbc.gen.SequenceDescFactory;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.TableDescFactory;
import org.seasar.extension.jdbc.gen.UniqueKeyDescFactory;
import org.seasar.extension.jdbc.gen.desc.ColumnDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.ForeignKeyDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.SequenceDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.TableDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.UniqueKeyDescFactoryImpl;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.generator.GeneratorImpl;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReaderImpl;
import org.seasar.extension.jdbc.gen.model.SchemaModelFactoryImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class DdlGenCommand extends AbstractCommand {

    @BindableProperty(required = true)
    protected File classpathRootDir;

    @BindableProperty
    protected File destDir = new File("ddl");

    @BindableProperty
    protected String ddlFileEncoding = "UTF-8";

    @BindableProperty
    protected String createTableDdlName = "create-table.ddl";

    @BindableProperty
    protected String createConstraintDdlName = "create-constraint.ddl";

    @BindableProperty
    protected String createSequenceDdlName = "create-sequence.ddl";;

    @BindableProperty
    protected String dropTableDdlName = "drop-table.ddl";;

    @BindableProperty
    protected String dropConstraintDdlName = "drop-constraint.ddl";;

    @BindableProperty
    protected String dropSequenceDdlName = "drop-sequence.ddl";

    @BindableProperty
    protected String createTableTemplateName = "create-table.ftl";

    @BindableProperty
    protected String createConstraintTemplateName = "create-constraint.ftl";

    @BindableProperty
    protected String createSequenceTemplateName = "create-sequence.ftl";

    @BindableProperty
    protected String dropTableTemplateName = "drop-table.ftl";

    @BindableProperty
    protected String dropConstraintTemplateName = "drop-constraint.ftl";

    @BindableProperty
    protected String dropSequenceTemplateName = "drop-sequence.ftl";

    protected EntityMetaFactory entityMetaFactory;

    protected GenDialect dialect;

    protected EntityMetaReader entityMetaReader;

    protected TableDescFactory tableDescFactory;

    protected SchemaModelFactory schemaModelFactory;

    protected Generator generator;

    public DdlGenCommand() {
    }

    public void setClasspathRootDir(File classpathRootDir) {
        this.classpathRootDir = classpathRootDir;
    }

    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    public void setDdlFileEncoding(String ddlFileEncoding) {
        this.ddlFileEncoding = ddlFileEncoding;
    }

    public void setCreateTableDdlName(String createTableDdlName) {
        this.createTableDdlName = createTableDdlName;
    }

    public void setCreateConstraintDdlName(String createConstraintDdlName) {
        this.createConstraintDdlName = createConstraintDdlName;
    }

    public void setCreateSequenceDdlName(String createSequenceDdlName) {
        this.createSequenceDdlName = createSequenceDdlName;
    }

    public void setDropTableDdlName(String dropTableDdlName) {
        this.dropTableDdlName = dropTableDdlName;
    }

    public void setDropConstraintDdlName(String dropConstraintDdlName) {
        this.dropConstraintDdlName = dropConstraintDdlName;
    }

    public void setDropSequenceDdlName(String dropSequenceDdlName) {
        this.dropSequenceDdlName = dropSequenceDdlName;
    }

    public void setCreateTableTemplateName(String createTableTemplateName) {
        this.createTableTemplateName = createTableTemplateName;
    }

    public void setCreateConstraintTemplateName(
            String createConstraintTemplateName) {
        this.createConstraintTemplateName = createConstraintTemplateName;
    }

    public void setCreateSequenceTemplateName(String createSequenceTemplateName) {
        this.createSequenceTemplateName = createSequenceTemplateName;
    }

    public void setDropTableTemplateName(String dropTableTemplateName) {
        this.dropTableTemplateName = dropTableTemplateName;
    }

    public void setDropConstraintTemplateName(String dropConstraintTemplateName) {
        this.dropConstraintTemplateName = dropConstraintTemplateName;
    }

    public void setDropSequenceTemplateName(String dropSequenceTemplateName) {
        this.dropSequenceTemplateName = dropSequenceTemplateName;
    }

    @Override
    protected void init() {
        super.init();

        JdbcManagerImplementor jdbcManager = SingletonS2Container
                .getComponent(jdbcManagerName);
        entityMetaFactory = jdbcManager.getEntityMetaFactory();
        dialect = GenDialectManager.getGenDialect(jdbcManager.getDialect());

        entityMetaReader = createEntityMetaReader();
        tableDescFactory = createTableDescFactory();
        schemaModelFactory = createSchemaModelFactory();
        generator = createGenerator();
    }

    protected EntityMetaReader createEntityMetaReader() {
        return new EntityMetaReaderImpl(classpathRootDir, ClassUtil.concatName(
                rootPackageName, entityPackageName), entityMetaFactory);
    }

    protected TableDescFactory createTableDescFactory() {
        ColumnDescFactory colFactory = new ColumnDescFactoryImpl(dialect);
        PrimaryKeyDescFactory pkFactory = new PrimaryKeyDescFactoryImpl(dialect);
        UniqueKeyDescFactory ukFactory = new UniqueKeyDescFactoryImpl();
        ForeignKeyDescFactory fkFactory = new ForeignKeyDescFactoryImpl(
                entityMetaFactory);
        SequenceDescFactory seqFactory = new SequenceDescFactoryImpl(dialect);
        return new TableDescFactoryImpl(colFactory, pkFactory, ukFactory,
                fkFactory, seqFactory);
    }

    protected SchemaModelFactory createSchemaModelFactory() {
        return new SchemaModelFactoryImpl(dialect);
    }

    protected Generator createGenerator() {
        return new GeneratorImpl(templateFileEncoding, templateDir);
    }

    @Override
    protected void doExecute() {
        List<EntityMeta> entityMetaList = entityMetaReader.read();
        List<TableDesc> tableDescList = new ArrayList<TableDesc>();
        for (EntityMeta entityMeta : entityMetaList) {
            tableDescList.add(tableDescFactory.getTableDesc(entityMeta));
        }
    }

    protected void generate(List<TableDesc> tableDescList) {
        Object model = schemaModelFactory.getSchemaModel(tableDescList);
        GenerationContext createTableCtx = getGenerationContext(model,
                createTableDdlName, createTableTemplateName);
        GenerationContext dropTableCtx = getGenerationContext(model,
                dropTableDdlName, createTableTemplateName);
        GenerationContext createConstraintCtx = getGenerationContext(model,
                createConstraintDdlName, createConstraintTemplateName);
        GenerationContext dropConstraintCtx = getGenerationContext(model,
                dropConstraintDdlName, dropConstraintTemplateName);
        GenerationContext createSequenceCtx = getGenerationContext(model,
                createSequenceDdlName, createSequenceTemplateName);
        GenerationContext dropSequenceCtx = getGenerationContext(model,
                dropSequenceDdlName, dropSequenceTemplateName);
        generator.generate(createTableCtx);
        generator.generate(dropTableCtx);
        generator.generate(createConstraintCtx);
        generator.generate(dropConstraintCtx);
        generator.generate(createSequenceCtx);
        generator.generate(dropSequenceCtx);
    }

    protected GenerationContext getGenerationContext(Object model,
            String ddlName, String templateName) {
        return new GenerationContext(model, destDir,
                new File(destDir, ddlName), templateName, ddlFileEncoding, true);
    }
}
