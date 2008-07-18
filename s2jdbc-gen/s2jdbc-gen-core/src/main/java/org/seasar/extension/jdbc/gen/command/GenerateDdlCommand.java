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
import org.seasar.extension.jdbc.gen.Command;
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
import org.seasar.extension.jdbc.gen.generator.GenerationContextImpl;
import org.seasar.extension.jdbc.gen.generator.GeneratorImpl;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReaderImpl;
import org.seasar.extension.jdbc.gen.model.DbModelFactoryImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;

/**
 * DDLのSQLファイルを生成する{@link Command}の実装です。
 * <p>
 * このコマンドは、エンティティクラスのメタデータからDDLのSQLファイルを生成します。 そのため、
 * ココマンドを実行するにはエンティティクラスを参照できるようにエンティティクラスが格納されたディレクトリをあらかじめクラスパスに設定しておく必要があります。
 * また、そのディレクトリは、プロパティ{@link #classpathRootDir}に設定しておく必要があります。
 * </p>
 * <p>
 * このコマンドが生成するDDLは次の6つです。
 * <ul>
 * <li>テーブルを作成するDDL</li>
 * <li>テーブルを削除するDDL</li>
 * <li>制約を作成するDDL</li>
 * <li>制約を削除するDDL</li>
 * <li>シーケンスを作成するDDL</li>
 * <li>シーケンスを削除するDDL</li>
 * </ul>
 * </p>
 * 
 * @author taedium
 */
public class GenerateDdlCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(GenerateDdlCommand.class);

    /** クラスパスのルートとなるディレクトリ */
    protected File classpathRootDir;

    /** 設定ファイルのパス */
    protected String configPath = "s2jdbc.dicon";

    /** 制約を作成するDDLのSQLファイル名 */
    protected String createConstraintSqlFileName = "create-constraint.sql";

    /** 制約を作成するDDLのテンプレートファイル名 */
    protected String createConstraintTemplateFileName = "sql/create-constraint.ftl";

    /** テーブルを作成するDDLのSQLファイル名 */
    protected String createTableSqlFileName = "create-table.sql";

    /** テーブルを作成するDDLのテンプレートファイル名 */
    protected String createTableTemplateFileName = "sql/create-table.ftl";

    /** シーケンスを生成するDDLのSQLファイル */
    protected String createSequenceSqlFileName = "create-sequence.sql";

    /** シーケンスを生成するDDLのテンプレートファイル */
    protected String createSequenceTemplateFileName = "sql/create-sequence.ftl";

    /** 制約を削除するDDLのSQLファイル名 */
    protected String dropConstraintSqlFileName = "drop-constraint.sql";

    /** 制約を削除するDDLのテンプレートファイル名 */
    protected String dropConstraintTemplateFileName = "sql/drop-constraint.ftl";

    /** テーブルを削除するDDLのSQLファイル名 */
    protected String dropTableSqlFileName = "drop-table.sql";

    /** テーブルを削除するDDLのテンプレートファイル名 */
    protected String dropTableTemplateFileName = "sql/drop-table.ftl";

    /** シーケンスを削除するDDLのSQLファイル名 */
    protected String dropSequenceSqlFileName = "drop-sequence.sql";

    /** シーケンスを削除するDDLのテンプレートファイル名 */
    protected String dropSequenceTemplateFileName = "sql/drop-sequence.ftl";

    /** エンティティクラスのパッケージ名 */
    protected String entityPackageName = "entity";

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName = "jdbcManager";

    /** 上書きをする場合{@code true}、しない場合{@code false} */
    protected boolean overwrite = true;

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** SQLファイルの出力先ディレクトリ */
    protected File sqlFileDestDir = new File("db", "ddl");

    /** SQLファイルのエンコーディング */
    protected String sqlFileEncoding = "UTF-8";

    /** SQLステートメントの区切り文字 */
    protected char statementDelimiter = ';';

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルを格納するプライマリディレクトリ */
    protected File templateFilePrimaryDir = null;

    /** {@link SingletonS2ContainerFactory}のサポート */
    protected SingletonS2ContainerFactorySupport containerFactorySupport;

    /** エンティティメタデータのファクトリ */
    protected EntityMetaFactory entityMetaFactory;

    /** 方言 */
    protected GenDialect dialect;

    /** エンティティメタデータのリーダ */
    protected EntityMetaReader entityMetaReader;

    /** テーブル記述のファクトリ */
    protected TableDescFactory tableDescFactory;

    /** データベースのモデルのファクトリ */
    protected DbModelFactory dbModelFactory;

    /** ジェネレータ */
    protected Generator generator;

    /**
     * インスタンスを構築します。
     */
    public GenerateDdlCommand() {
    }

    /**
     * クラスパスのルートとなるディレクトリを返します。
     * 
     * @return クラスパスのルートとなるディレクトリ
     */
    public File getClasspathRootDir() {
        return classpathRootDir;
    }

    /**
     * クラスパスのルートとなるディレクトリを設定します。
     * 
     * @param classpathRootDir
     *            クラスパスのルートとなるディレクトリ
     */
    public void setClasspathRootDir(File classpathRootDir) {
        this.classpathRootDir = classpathRootDir;
    }

    /**
     * 設定ファイルのパスを返します。
     * 
     * @return 設定ファイルのパス
     */
    public String getConfigPath() {
        return configPath;
    }

    /**
     * 設定ファイルのパスを設定します。
     * 
     * @param configPath
     *            設定ファイルのパス
     */
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    /**
     * 制約を作成するDDLのテンプレートファイル名を返します。
     * 
     * @return 制約を作成するDDLのテンプレートファイル名
     */
    public String getCreateConstraintTemplateFileName() {
        return createConstraintTemplateFileName;
    }

    /**
     * 制約を作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createConstraintTemplateFileName
     *            制約を作成するDDLのテンプレートファイル名
     */
    public void setCreateConstraintTemplateFileName(
            String createConstraintTemplateFileName) {
        this.createConstraintTemplateFileName = createConstraintTemplateFileName;
    }

    /**
     * 制約を作成するDDLのSQLファイル名を返します。
     * 
     * @return 制約を作成するDDLのSQLファイル名
     */
    public String getCreateConstraintSqlFileName() {
        return createConstraintSqlFileName;
    }

    /**
     * 制約を作成するDDLのSQLファイル名を設定します。
     * 
     * @param createConstraintSqlFileName
     *            制約を作成するDDLのSQLファイル名
     */
    public void setCreateConstraintSqlFileName(
            String createConstraintSqlFileName) {
        this.createConstraintSqlFileName = createConstraintSqlFileName;
    }

    /**
     * テーブルを作成するDDLのSQLファイル名を返します。
     * 
     * @return テーブルを作成するDDLのSQLファイル名
     */
    public String getCreateTableSqlFileName() {
        return createTableSqlFileName;
    }

    /**
     * テーブルを作成するDDLのSQLファイル名を設定します。
     * 
     * @param createTableSqlFileName
     *            テーブルを作成するDDLのSQLファイル名
     */
    public void setCreateTableSqlFileName(String createTableSqlFileName) {
        this.createTableSqlFileName = createTableSqlFileName;
    }

    /**
     * テーブルを作成するDDLのテンプレートファイル名を返します。
     * 
     * @return テーブルを作成するDDLのテンプレートファイル名
     */
    public String getCreateTableTemplateFileName() {
        return createTableTemplateFileName;
    }

    /**
     * テーブルを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createTableTemplateFileName
     *            テーブルを作成するDDLのテンプレートファイル名
     */
    public void setCreateTableTemplateFileName(
            String createTableTemplateFileName) {
        this.createTableTemplateFileName = createTableTemplateFileName;
    }

    /**
     * シーケンスを作成するDDLのSQLファイル名を返します。
     * 
     * @return シーケンスを作成するDDLのSQLレートファイル名
     */
    public String getCreateSequenceSqlFileName() {
        return createSequenceSqlFileName;
    }

    /**
     * シーケンスを作成するDDLのSQLファイル名を設定します。
     * 
     * @param createSequenceSqlFileName
     *            シーケンスを作成するDDLのSQLレートファイル名
     */
    public void setCreateSequenceSqlFileName(String createSequenceSqlFileName) {
        this.createSequenceSqlFileName = createSequenceSqlFileName;
    }

    /**
     * シーケンスを作成するDDLのテンプレートファイル名を返します。
     * 
     * @return シーケンスを作成するDDLのテンプレートファイル名
     */
    public String getCreateSequenceTemplateFileName() {
        return createSequenceTemplateFileName;
    }

    /**
     * シーケンスを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createSequenceTemplateFileName
     *            シーケンスを作成するDDLのテンプレートファイル名
     */
    public void setCreateSequenceTemplateFileName(
            String createSequenceTemplateFileName) {
        this.createSequenceTemplateFileName = createSequenceTemplateFileName;
    }

    /**
     * 制約を削除するDDLのSQLファイル名を返します。
     * 
     * @return 制約を削除するDDLのSQLファイル名
     */
    public String getDropConstraintSqlFileName() {
        return dropConstraintSqlFileName;
    }

    /**
     * 制約を削除するDDLのSQLファイル名を設定します。
     * 
     * @param dropConstraintSqlFileName
     *            制約を削除するDDLのSQLファイル名
     */
    public void setDropConstraintSqlFileName(String dropConstraintSqlFileName) {
        this.dropConstraintSqlFileName = dropConstraintSqlFileName;
    }

    /**
     * 制約を削除するDDLのテンプレートファイル名を返します。
     * 
     * @return 制約を削除するDDLのテンプレートファイル名
     */
    public String getDropConstraintTemplateFileName() {
        return dropConstraintTemplateFileName;
    }

    /**
     * 制約を削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropConstraintTemplateFileName
     *            制約を削除するDDLのテンプレートファイル名
     */
    public void setDropConstraintTemplateFileName(
            String dropConstraintTemplateFileName) {
        this.dropConstraintTemplateFileName = dropConstraintTemplateFileName;
    }

    /**
     * テーブルを削除するDDLのSQLファイル名を返します。
     * 
     * @return テーブルを削除するDDLのSQLファイル名
     */
    public String getDropTableSqlFileName() {
        return dropTableSqlFileName;
    }

    /**
     * テーブルを削除するDDLのSQLファイル名を設定します。
     * 
     * @param dropTableSqlFileName
     *            テーブルを削除するDDLのSQLファイル名
     */
    public void setDropTableSqlFileName(String dropTableSqlFileName) {
        this.dropTableSqlFileName = dropTableSqlFileName;
    }

    /**
     * テーブルを削除するDDLのテンプレートファイル名を返します。
     * 
     * @return テーブルを削除するDDLのテンプレートファイル名
     */
    public String getDropTableTemplateFileName() {
        return dropTableTemplateFileName;
    }

    /**
     * テーブルを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropTableTemplateFileName
     *            テーブルを削除するDDLのテンプレートファイル名
     */
    public void setDropTableTemplateFileName(String dropTableTemplateFileName) {
        this.dropTableTemplateFileName = dropTableTemplateFileName;
    }

    /**
     * シーケンスを削除するDDLのSQLファイル名を返します。
     * 
     * @return シーケンスを削除するDDLのSQLファイル名
     */
    public String getDropSequenceSqlFileName() {
        return dropSequenceSqlFileName;
    }

    /**
     * シーケンスを削除するDDLのSQLファイル名を設定します。
     * 
     * @param dropSequenceSqlFileName
     *            シーケンスを削除するDDLのSQLファイル名
     */
    public void setDropSequenceSqlFileName(String dropSequenceSqlFileName) {
        this.dropSequenceSqlFileName = dropSequenceSqlFileName;
    }

    /**
     * シーケンスを削除するDDLのテンプレートファイル名を返します。
     * 
     * @return シーケンスを削除するDDLのテンプレートファイル名
     */
    public String getDropSequenceTemplateFileName() {
        return dropSequenceTemplateFileName;
    }

    /**
     * シーケンスを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropSequenceTemplateFileName
     *            シーケンスを削除するDDLのテンプレートファイル名
     */
    public void setDropSequenceTemplateFileName(
            String dropSequenceTemplateFileName) {
        this.dropSequenceTemplateFileName = dropSequenceTemplateFileName;
    }

    /**
     * エンティティクラスのパッケージ名を返します。
     * 
     * @return エンティティクラスのパッケージ名
     */
    public String getEntityPackageName() {
        return entityPackageName;
    }

    /**
     * エンティティクラスのパッケージ名を設定します。
     * 
     * @param entityPackageName
     *            エンティティクラスのパッケージ名
     */
    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    /**
     * {@link JdbcManager}のコンポーネント名を返します。
     * 
     * @return {@link JdbcManager}のコンポーネント名
     */
    public String getJdbcManagerName() {
        return jdbcManagerName;
    }

    /**
     * {@link JdbcManager}のコンポーネント名を設定します。
     * 
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        this.jdbcManagerName = jdbcManagerName;
    }

    /**
     * SQLステートメントの区切り文字を返します。
     * 
     * @return SQLステートメントの区切り文字
     */
    public char getStatementDelimiter() {
        return statementDelimiter;
    }

    /**
     * SQLステートメントの区切り文字を設定します。
     * 
     * @param statementDelimiter
     *            SQLステートメントの区切り文字
     */
    public void setStatementDelimiter(char statementDelimiter) {
        this.statementDelimiter = statementDelimiter;
    }

    /**
     * 上書きをする場合{@code true}、しない場合{@code false}を返します。
     * 
     * @return 上書きをする場合{@code true}、しない場合{@code false}
     */
    public boolean isOverwrite() {
        return overwrite;
    }

    /**
     * 上書きをする場合{@code true}、しない場合{@code false}を設定します。
     * 
     * @param overwrite
     *            上書きをする場合{@code true}、しない場合{@code false}
     */
    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    /**
     * ルートパッケージ名を返します。
     * 
     * @return ルートパッケージ名
     */
    public String getRootPackageName() {
        return rootPackageName;
    }

    /**
     * ルートパッケージ名を設定します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     */
    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    /**
     * SQLファイルの出力先ディレクトリを返します。
     * 
     * @return SQLファイルの出力先ディレクトリ
     */
    public File getSqlFileDestDir() {
        return sqlFileDestDir;
    }

    /**
     * SQLファイルの出力先ディレクトリを設定します。
     * 
     * @param sqlFileDestDir
     *            SQLファイルの出力先ディレクトリ
     */
    public void setSqlFileDestDir(File sqlFileDestDir) {
        this.sqlFileDestDir = sqlFileDestDir;
    }

    /**
     * SQLファイルのエンコーディングを返します。
     * 
     * @return SQLファイルのエンコーディング
     */
    public String getSqlFileEncoding() {
        return sqlFileEncoding;
    }

    /**
     * SQLファイルのエンコーディングを設定します。
     * 
     * @param sqlFileEncoding
     *            SQLファイルのエンコーディング
     */
    public void setSqlFileEncoding(String sqlFileEncoding) {
        this.sqlFileEncoding = sqlFileEncoding;
    }

    /**
     * テンプレートファイルのエンコーディングを返します。
     * 
     * @return テンプレートファイルのエンコーディング
     */
    public String getTemplateFileEncoding() {
        return templateFileEncoding;
    }

    /**
     * テンプレートファイルのエンコーディングを設定します。
     * 
     * @param templateFileEncoding
     *            テンプレートファイルのエンコーディング
     */
    public void setTemplateFileEncoding(String templateFileEncoding) {
        this.templateFileEncoding = templateFileEncoding;
    }

    /**
     * テンプレートファイルを格納するプライマリディレクトリを返します。
     * 
     * @return テンプレートファイルを格納するプライマリディレクトリ
     */
    public File getTemplateFilePrimaryDir() {
        return templateFilePrimaryDir;
    }

    /**
     * テンプレートファイルを格納するプライマリディレクトリを設定します。
     * 
     * @param templateFilePrimaryDir
     *            テンプレートファイルを格納するプライマリディレクトリ
     */
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
        containerFactorySupport = new SingletonS2ContainerFactorySupport(
                configPath);
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

    /**
     * {@link EntityMetaReader}の実装を作成します。
     * 
     * @return {@link EntityMetaReader}の実装
     */
    protected EntityMetaReader createEntityMetaReader() {
        return new EntityMetaReaderImpl(classpathRootDir, ClassUtil.concatName(
                rootPackageName, entityPackageName), entityMetaFactory);
    }

    /**
     * {@link TableDescFactory}の実装を作成します。
     * 
     * @return {@link TableDescFactory}の実装
     */
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

    /**
     * {@link DbModelFactory}の実装を作成します。
     * 
     * @return {@link DbModelFactory}の実装
     */
    protected DbModelFactory createDbModelFactory() {
        return new DbModelFactoryImpl(dialect, statementDelimiter);
    }

    /**
     * {@link Generator}の実装を作成します。
     * 
     * @return {@link Generator}の実装
     */
    protected Generator createGenerator() {
        return new GeneratorImpl(templateFileEncoding, templateFilePrimaryDir);
    }

    /**
     * 生成します。
     * 
     * @param tableDescList
     *            テーブル記述のリスト
     */
    protected void generate(List<TableDesc> tableDescList) {
        DbModel model = dbModelFactory.getDbModel(tableDescList);
        generateTable(model);
        generateConstraint(model);
        generateSequence(model);
    }

    /**
     * テーブルに関するDDLのSQLファイルを作成します。
     * 
     * @param model
     *            データベースのモデル
     */
    protected void generateTable(DbModel model) {
        GenerationContext createTableCtx = createGenerationContext(model,
                createTableSqlFileName, createTableTemplateFileName);
        GenerationContext dropTableCtx = createGenerationContext(model,
                dropTableSqlFileName, dropTableTemplateFileName);
        generator.generate(createTableCtx);
        generator.generate(dropTableCtx);
    }

    /**
     * 制約に関するDDLのSQLファイルを作成します。
     * 
     * @param model
     *            データベースのモデル
     */
    protected void generateConstraint(DbModel model) {
        GenerationContext createConstraintCtx = createGenerationContext(model,
                createConstraintSqlFileName, createConstraintTemplateFileName);
        GenerationContext dropConstraintCtx = createGenerationContext(model,
                dropConstraintSqlFileName, dropConstraintTemplateFileName);
        generator.generate(createConstraintCtx);
        generator.generate(dropConstraintCtx);
    }

    /**
     *シーケンスに関するDDLのSQLファイルを作成します。
     * 
     * @param model
     *            データベースのモデル
     */
    protected void generateSequence(DbModel model) {
        GenerationContext createSequenceCtx = createGenerationContext(model,
                createSequenceSqlFileName, createSequenceTemplateFileName);
        GenerationContext dropSequenceCtx = createGenerationContext(model,
                dropSequenceSqlFileName, dropSequenceTemplateFileName);
        generator.generate(createSequenceCtx);
        generator.generate(dropSequenceCtx);
    }

    /**
     * {@link GenerationContext}の実装を作成します。
     * 
     * @param model
     *            データベースのモデル
     * @param SqlFileName
     *            SQLファイルの名前
     * @param templateName
     *            テンプレートファイルの名前
     * @return
     */
    protected GenerationContext createGenerationContext(Object model,
            String SqlFileName, String templateName) {
        return new GenerationContextImpl(model, sqlFileDestDir, new File(
                sqlFileDestDir, SqlFileName), templateName, sqlFileEncoding,
                overwrite);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
