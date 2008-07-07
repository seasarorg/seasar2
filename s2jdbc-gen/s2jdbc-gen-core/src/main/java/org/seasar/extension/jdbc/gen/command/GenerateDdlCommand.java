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
import org.seasar.extension.jdbc.gen.SchemaModel;
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
public class GenerateDdlCommand extends AbstractCommand {

    @BindableProperty(required = true)
    protected File classpathRootDir;

    @BindableProperty
    protected File destDir = new File("sql");

    @BindableProperty
    protected String sqlFileEncoding = "UTF-8";

    @BindableProperty
    protected String createTableSqlFileName = "create-table.sql";

    @BindableProperty
    protected String createConstraintSqlFileName = "create-constraint.sql";

    @BindableProperty
    protected String createSequenceSqlFileName = "create-sequence.sql";

    @BindableProperty
    protected String dropTableSqlFileName = "drop-table.sql";

    @BindableProperty
    protected String dropConstraintSqlFileName = "drop-constraint.sql";;

    @BindableProperty
    protected String dropSequenceSqlFileName = "drop-sequence.sql";

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

    public GenerateDdlCommand() {
    }

    public void setClasspathRootDir(File classpathRootDir) {
        this.classpathRootDir = classpathRootDir;
    }

    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    public void setSqlFileEncoding(String sqlFileEncoding) {
        this.sqlFileEncoding = sqlFileEncoding;
    }

    public void setCreateTableSqlFileName(String createTableSqlFileName) {
        this.createTableSqlFileName = createTableSqlFileName;
    }

    public void setCreateConstraintSqlFileName(
            String createConstraintSqlFileName) {
        this.createConstraintSqlFileName = createConstraintSqlFileName;
    }

    public void setCreateSequenceSqlFileName(String createSequenceSqlFileName) {
        this.createSequenceSqlFileName = createSequenceSqlFileName;
    }

    public void setDropTableSqlFileName(String dropTableSqlFileName) {
        this.dropTableSqlFileName = dropTableSqlFileName;
    }

    public void setDropConstraintSqlFileName(String dropConstraintSqlFileName) {
        this.dropConstraintSqlFileName = dropConstraintSqlFileName;
    }

    public void setDropSequenceSqlFileName(String dropSequenceSqlFileName) {
        this.dropSequenceSqlFileName = dropSequenceSqlFileName;
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
        generate(tableDescList);
    }

    protected void generate(List<TableDesc> tableDescList) {
        SchemaModel model = schemaModelFactory.getSchemaModel(tableDescList);
        generateTable(model);
        generateConstraint(model);
        generateSequence(model);
    }

    protected void generateTable(SchemaModel model) {
        GenerationContext createTableCtx = createGenerationContext(model,
                createTableSqlFileName, createTableTemplateName, true);
        GenerationContext dropTableCtx = createGenerationContext(model,
                dropTableSqlFileName, createTableTemplateName, true);
        generator.generate(createTableCtx);
        generator.generate(dropTableCtx);
    }

    protected void generateConstraint(SchemaModel model) {
        GenerationContext createConstraintCtx = createGenerationContext(model,
                createConstraintSqlFileName, createConstraintTemplateName, true);
        GenerationContext dropConstraintCtx = createGenerationContext(model,
                dropConstraintSqlFileName, dropConstraintTemplateName, true);
        generator.generate(createConstraintCtx);
        generator.generate(dropConstraintCtx);
    }

    protected void generateSequence(SchemaModel model) {
        GenerationContext createSequenceCtx = createGenerationContext(model,
                createSequenceSqlFileName, createSequenceTemplateName, true);
        GenerationContext dropSequenceCtx = createGenerationContext(model,
                dropSequenceSqlFileName, dropSequenceTemplateName, true);
        generator.generate(createSequenceCtx);
        generator.generate(dropSequenceCtx);
    }

    protected GenerationContext createGenerationContext(Object model,
            String SqlFileName, String templateName, boolean overwrite) {
        return new GenerationContext(model, destDir, new File(destDir,
                SqlFileName), templateName, sqlFileEncoding, overwrite);
    }
}
