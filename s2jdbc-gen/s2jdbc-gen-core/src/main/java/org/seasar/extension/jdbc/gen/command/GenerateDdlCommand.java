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
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.ColumnDescFactory;
import org.seasar.extension.jdbc.gen.DbModel;
import org.seasar.extension.jdbc.gen.DbModelFactory;
import org.seasar.extension.jdbc.gen.EntityMetaReader;
import org.seasar.extension.jdbc.gen.ForeignKeyDescFactory;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.Generator;
import org.seasar.extension.jdbc.gen.IdTableDescFactory;
import org.seasar.extension.jdbc.gen.PrimaryKeyDescFactory;
import org.seasar.extension.jdbc.gen.SequenceDescFactory;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.TableDescFactory;
import org.seasar.extension.jdbc.gen.UniqueKeyDescFactory;
import org.seasar.extension.jdbc.gen.desc.ColumnDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.ForeignKeyDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.IdTableDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.SequenceDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.TableDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.UniqueKeyDescFactoryImpl;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.generator.GeneratorImpl;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReaderImpl;
import org.seasar.extension.jdbc.gen.model.DbModelFactoryImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class GenerateDdlCommand extends AbstractCommand {

    protected static Logger logger = Logger.getLogger(GenerateDdlCommand.class);

    protected File classpathRootDir;

    /** {@link JdbcManager}のコンポーネントを含むdiconファイル */
    protected String configPath = "s2jdbc.dicon";

    protected String createConstraintSqlFileName = "create-constraint.sql";

    protected String createConstraintTemplateFileName = "sql/create-constraint.ftl";

    protected String createTableSqlFileName = "create-table.sql";

    protected String createTableTemplateFileName = "sql/create-table.ftl";

    protected String createSequenceSqlFileName = "create-sequence.sql";

    protected String createSequenceTemplateFileName = "sql/create-sequence.ftl";

    protected String dropConstraintSqlFileName = "drop-constraint.sql";;

    protected String dropConstraintTemplateFileName = "sql/drop-constraint.ftl";

    protected String dropTableSqlFileName = "drop-table.sql";

    protected String dropTableTemplateFileName = "sql/drop-table.ftl";

    protected String dropSequenceSqlFileName = "drop-sequence.sql";

    protected String dropSequenceTemplateFileName = "sql/drop-sequence.ftl";

    /** エンティティパッケージ名 */
    protected String entityPackageName = "entity";

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName = "jdbcManager";

    protected boolean overwrite = true;

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    protected String schemaInfoColumnName = "VERSION";

    protected String schemaInfoTableName = "SCHEMA_INFO";

    protected File sqlFileDestDir = new File("db", "ddl");

    protected String sqlFileEncoding = "UTF-8";

    protected char statementDelimiter = ';';

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルを格納するディレクトリ */
    protected File templateFilePrimaryDir = null;

    protected S2ContainerFactorySupport containerFactorySupport;

    protected EntityMetaFactory entityMetaFactory;

    protected GenDialect dialect;

    protected EntityMetaReader entityMetaReader;

    protected TableDescFactory tableDescFactory;

    protected DbModelFactory dbModelFactory;

    protected Generator generator;

    public GenerateDdlCommand() {
    }

    public File getClasspathRootDir() {
        return classpathRootDir;
    }

    public void setClasspathRootDir(File classpathRootDir) {
        this.classpathRootDir = classpathRootDir;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getCreateConstraintTemplateFileName() {
        return createConstraintTemplateFileName;
    }

    public void setCreateConstraintTemplateFileName(
            String createConstraintTemplateFileName) {
        this.createConstraintTemplateFileName = createConstraintTemplateFileName;
    }

    public String getCreateConstraintSqlFileName() {
        return createConstraintSqlFileName;
    }

    public void setCreateConstraintSqlFileName(
            String createConstraintSqlFileName) {
        this.createConstraintSqlFileName = createConstraintSqlFileName;
    }

    public String getCreateTableSqlFileName() {
        return createTableSqlFileName;
    }

    public void setCreateTableSqlFileName(String createTableSqlFileName) {
        this.createTableSqlFileName = createTableSqlFileName;
    }

    public String getCreateTableTemplateFileName() {
        return createTableTemplateFileName;
    }

    public void setCreateTableTemplateFileName(
            String createTableTemplateFileName) {
        this.createTableTemplateFileName = createTableTemplateFileName;
    }

    public String getCreateSequenceSqlFileName() {
        return createSequenceSqlFileName;
    }

    public void setCreateSequenceSqlFileName(String createSequenceSqlFileName) {
        this.createSequenceSqlFileName = createSequenceSqlFileName;
    }

    public String getCreateSequenceTemplateFileName() {
        return createSequenceTemplateFileName;
    }

    public void setCreateSequenceTemplateFileName(
            String createSequenceTemplateFileName) {
        this.createSequenceTemplateFileName = createSequenceTemplateFileName;
    }

    public String getDropConstraintSqlFileName() {
        return dropConstraintSqlFileName;
    }

    public void setDropConstraintSqlFileName(String dropConstraintSqlFileName) {
        this.dropConstraintSqlFileName = dropConstraintSqlFileName;
    }

    public String getDropConstraintTemplateFileName() {
        return dropConstraintTemplateFileName;
    }

    public void setDropConstraintTemplateFileName(
            String dropConstraintTemplateFileName) {
        this.dropConstraintTemplateFileName = dropConstraintTemplateFileName;
    }

    public String getDropTableSqlFileName() {
        return dropTableSqlFileName;
    }

    public void setDropTableSqlFileName(String dropTableSqlFileName) {
        this.dropTableSqlFileName = dropTableSqlFileName;
    }

    public String getDropTableTemplateFileName() {
        return dropTableTemplateFileName;
    }

    public void setDropTableTemplateFileName(String dropTableTemplateFileName) {
        this.dropTableTemplateFileName = dropTableTemplateFileName;
    }

    public String getDropSequenceSqlFileName() {
        return dropSequenceSqlFileName;
    }

    public void setDropSequenceSqlFileName(String dropSequenceSqlFileName) {
        this.dropSequenceSqlFileName = dropSequenceSqlFileName;
    }

    public String getDropSequenceTemplateFileName() {
        return dropSequenceTemplateFileName;
    }

    public void setDropSequenceTemplateFileName(
            String dropSequenceTemplateFileName) {
        this.dropSequenceTemplateFileName = dropSequenceTemplateFileName;
    }

    public String getEntityPackageName() {
        return entityPackageName;
    }

    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    public String getJdbcManagerName() {
        return jdbcManagerName;
    }

    public void setJdbcManagerName(String jdbcManagerName) {
        this.jdbcManagerName = jdbcManagerName;
    }

    /**
     * @return Returns the statementDelimiter.
     */
    public char getStatementDelimiter() {
        return statementDelimiter;
    }

    /**
     * @param statementDelimiter
     *            The statementDelimiter to set.
     */
    public void setStatementDelimiter(char statementDelimiter) {
        this.statementDelimiter = statementDelimiter;
    }

    /**
     * @return Returns the overwrite.
     */
    public boolean isOverwrite() {
        return overwrite;
    }

    /**
     * @param overwrite
     *            The overwrite to set.
     */
    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public String getRootPackageName() {
        return rootPackageName;
    }

    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    public File getSqlFileDestDir() {
        return sqlFileDestDir;
    }

    public void setSqlFileDestDir(File sqlFileDestDir) {
        this.sqlFileDestDir = sqlFileDestDir;
    }

    public String getSqlFileEncoding() {
        return sqlFileEncoding;
    }

    public void setSqlFileEncoding(String sqlFileEncoding) {
        this.sqlFileEncoding = sqlFileEncoding;
    }

    public String getTemplateFileEncoding() {
        return templateFileEncoding;
    }

    public void setTemplateFileEncoding(String templateFileEncoding) {
        this.templateFileEncoding = templateFileEncoding;
    }

    public File getTemplateFilePrimaryDir() {
        return templateFilePrimaryDir;
    }

    public void setTemplateFilePrimaryDir(File templateFilePrimaryDir) {
        this.templateFilePrimaryDir = templateFilePrimaryDir;
    }

    @Override
    protected void doValidate() {
        if (classpathRootDir == null) {
            throw new RequiredPropertyNullRuntimeException("classpathRootDir");
        }
    }

    @Override
    protected void doInit() {
        containerFactorySupport = new S2ContainerFactorySupport(configPath);
        containerFactorySupport.init();

        JdbcManagerImplementor jdbcManager = SingletonS2Container
                .getComponent(jdbcManagerName);
        entityMetaFactory = jdbcManager.getEntityMetaFactory();
        dialect = GenDialectManager.getGenDialect(jdbcManager.getDialect());

        entityMetaReader = createEntityMetaReader();
        tableDescFactory = createTableDescFactory();
        dbModelFactory = createDbModelFactory();
        generator = createGenerator();

        logger.log("DS2JDBCGen0005", new Object[] { dialect.getClass()
                .getName() });
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

    @Override
    protected void doDestroy() {
        if (containerFactorySupport != null) {
            containerFactorySupport.destory();
        }
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
        IdTableDescFactory idTabFactory = new IdTableDescFactoryImpl(dialect,
                colFactory, pkFactory, ukFactory);
        return new TableDescFactoryImpl(colFactory, pkFactory, ukFactory,
                fkFactory, seqFactory, idTabFactory);
    }

    protected DbModelFactory createDbModelFactory() {
        return new DbModelFactoryImpl(dialect, statementDelimiter);
    }

    protected Generator createGenerator() {
        return new GeneratorImpl(templateFileEncoding, templateFilePrimaryDir);
    }

    protected void generate(List<TableDesc> tableDescList) {
        DbModel model = dbModelFactory.getSchemaModel(tableDescList);
        generateTable(model);
        generateConstraint(model);
        generateSequence(model);
    }

    protected void generateTable(DbModel model) {
        GenerationContext createTableCtx = createGenerationContext(model,
                createTableSqlFileName, createTableTemplateFileName);
        GenerationContext dropTableCtx = createGenerationContext(model,
                dropTableSqlFileName, dropTableTemplateFileName);
        generator.generate(createTableCtx);
        generator.generate(dropTableCtx);
    }

    protected void generateConstraint(DbModel model) {
        GenerationContext createConstraintCtx = createGenerationContext(model,
                createConstraintSqlFileName, createConstraintTemplateFileName);
        GenerationContext dropConstraintCtx = createGenerationContext(model,
                dropConstraintSqlFileName, dropConstraintTemplateFileName);
        generator.generate(createConstraintCtx);
        generator.generate(dropConstraintCtx);
    }

    protected void generateSequence(DbModel model) {
        GenerationContext createSequenceCtx = createGenerationContext(model,
                createSequenceSqlFileName, createSequenceTemplateFileName);
        GenerationContext dropSequenceCtx = createGenerationContext(model,
                dropSequenceSqlFileName, dropSequenceTemplateFileName);
        generator.generate(createSequenceCtx);
        generator.generate(dropSequenceCtx);
    }

    protected GenerationContext createGenerationContext(Object model,
            String SqlFileName, String templateName) {
        return new GenerationContext(model, sqlFileDestDir, new File(
                sqlFileDestDir, SqlFileName), templateName, sqlFileEncoding,
                overwrite);
    }

    protected Logger getLogger() {
        return logger;
    }
}
