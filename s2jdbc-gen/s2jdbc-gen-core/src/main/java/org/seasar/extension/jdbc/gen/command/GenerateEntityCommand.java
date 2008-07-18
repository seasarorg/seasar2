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

import javax.sql.DataSource;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.AttributeDescFactory;
import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.DbTableMeta;
import org.seasar.extension.jdbc.gen.DbTableMetaReader;
import org.seasar.extension.jdbc.gen.EntityDesc;
import org.seasar.extension.jdbc.gen.EntityDescFactory;
import org.seasar.extension.jdbc.gen.EntityModel;
import org.seasar.extension.jdbc.gen.EntityModelFactory;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.Generator;
import org.seasar.extension.jdbc.gen.desc.AttributeDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.EntityDescFactoryImpl;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.generator.GenerationContextImpl;
import org.seasar.extension.jdbc.gen.generator.GeneratorImpl;
import org.seasar.extension.jdbc.gen.meta.DbTableMetaReaderImpl;
import org.seasar.extension.jdbc.gen.model.AttributeModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.EntityModelFactoryImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;

/**
 * エンティティクラスのJavaファイルを生成する{@link Command}の実装クラスです。
 * <p>
 * このコマンドは、データベースのメタデータからエンティティクラスのJavaファイルを生成します。
 * </p>
 * <p>
 * テーブル1つにつき、1つのエンティティクラスのJavaファイルを生成します。
 * </p>
 * <p>
 * 主キーやカラムのNOT NULL制約は認識してJavaコードに反映しますが、一意キーや外部キーは認識しません。
 * </p>
 * 
 * @author taedium
 */
public class GenerateEntityCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(GenerateEntityCommand.class);

    /** 設定ファイルのパス */
    protected String configPath = "s2jdbc.dicon";

    /** エンティティクラスのパッケージ名 */
    protected String entityPackageName = "entity";

    /** エンティティクラスのテンプレート名 */
    protected String entityTemplateFileName = "java/entity.ftl";

    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File javaFileDestDir = new File("src/main/java");

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding = "UTF-8";

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName = "jdbcManager";

    /** 上書きをする場合{@code true}、しない場合{@code false} */
    protected boolean overwrite = false;

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** スキーマ名 */
    protected String schemaName;

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルを格納するプライマリディレクトリ */
    protected File templateFilePrimaryDir = null;

    /** Javaコード生成の対象とするテーブル名の正規表現 */
    protected String tableNamePattern = ".*";

    /** バージョンカラムの名前 */
    protected String versionColumnName = "version";

    /** {@link SingletonS2ContainerFactory}のサポート */
    protected SingletonS2ContainerFactorySupport containerFactorySupport;

    /** データソース */
    protected DataSource dataSource;

    /** 方言 */
    protected GenDialect dialect;

    /** 永続化層の命名規約 */
    protected PersistenceConvention persistenceConvention;

    /** テーブルメタデータのリーダ */
    protected DbTableMetaReader dbTableMetaReader;

    /** エンティティ記述のファクトリ */
    protected EntityDescFactory entityDescFactory;

    /** ジェネレータ */
    protected Generator generator;

    /** エンティティのモデルのファクトリ */
    protected EntityModelFactory entityModelFactory;

    /**
     * インスタンスを構築します。
     */
    public GenerateEntityCommand() {
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
     * エンティティクラスのテンプレート名を返します。
     * 
     * @return エンティティクラスのテンプレート名
     */
    public String getEntityTemplateFileName() {
        return entityTemplateFileName;
    }

    /**
     * エンティティクラスのテンプレート名を設定します。
     * 
     * @param entityTemplateFileName
     *            エンティティクラスのテンプレート名
     */
    public void setEntityTemplateFileName(String entityTemplateFileName) {
        this.entityTemplateFileName = entityTemplateFileName;
    }

    /**
     * 生成するJavaファイルの出力先ディレクトリを返します。
     * 
     * @return 生成するJavaファイルの出力先ディレクトリ
     */
    public File getJavaFileDestDir() {
        return javaFileDestDir;
    }

    /**
     * 生成するJavaファイルの出力先ディレクトリを設定します。
     * 
     * @param javaFileDestDir
     *            生成するJavaファイルの出力先ディレクトリ
     */
    public void setJavaFileDestDir(File javaFileDestDir) {
        this.javaFileDestDir = javaFileDestDir;
    }

    /**
     * Javaファイルのエンコーディングを返します。
     * 
     * @return Javaファイルのエンコーディング
     */
    public String getJavaFileEncoding() {
        return javaFileEncoding;
    }

    /**
     * Javaファイルのエンコーディングを設定します。
     * 
     * @param javaFileEncoding
     *            Javaファイルのエンコーディング
     */
    public void setJavaFileEncoding(String javaFileEncoding) {
        this.javaFileEncoding = javaFileEncoding;
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
     * スキーマ名を返します。
     * 
     * @return スキーマ名
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * スキーマ名を設定します。
     * 
     * @param schemaName
     *            スキーマ名
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
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

    /**
     * Javaコード生成の対象とするテーブル名の正規表現を返します。
     * 
     * @return Javaコード生成の対象とするテーブル名の正規表現
     */
    public String getTableNamePattern() {
        return tableNamePattern;
    }

    /**
     * Javaコード生成の対象とするテーブル名の正規表現を設定します。
     * 
     * @param tableNamePattern
     *            Javaコード生成の対象とするテーブル名の正規表現
     */
    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    /**
     * バージョンカラムの名前を返します。
     * 
     * @return バージョンカラムの名前
     */
    public String getVersionColumnName() {
        return versionColumnName;
    }

    /**
     * バージョンカラムの名前を設定します。
     * 
     * @param versionColumnName
     *            バージョンカラムの名前
     */
    public void setVersionColumnName(String versionColumnName) {
        this.versionColumnName = versionColumnName;
    }

    @Override
    protected void doValidate() {
    }

    /**
     * 初期化します。
     */
    @Override
    protected void doInit() {
        containerFactorySupport = new SingletonS2ContainerFactorySupport(
                configPath);
        containerFactorySupport.init();

        JdbcManagerImplementor jdbcManager = SingletonS2Container
                .getComponent(jdbcManagerName);
        dataSource = jdbcManager.getDataSource();
        persistenceConvention = jdbcManager.getPersistenceConvention();
        dialect = GenDialectManager.getGenDialect(jdbcManager.getDialect());

        dbTableMetaReader = createDbTableMetaReader();
        entityDescFactory = createEntityDescFactory();
        generator = createGenerator();
        entityModelFactory = createEntityModelFactory();

        logger.log("DS2JDBCGen0005", new Object[] { dialect.getClass()
                .getName() });
    }

    @Override
    protected void doExecute() {
        List<DbTableMeta> tableMetaList = dbTableMetaReader.read();
        List<EntityDesc> entityDescList = new ArrayList<EntityDesc>();
        for (DbTableMeta tableMeta : tableMetaList) {
            EntityDesc entityDesc = entityDescFactory.getEntityDesc(tableMeta);
            entityDescList.add(entityDesc);
        }
        generate(entityDescList);
    }

    @Override
    protected void doDestroy() {
        if (containerFactorySupport != null) {
            containerFactorySupport.destory();
        }
    }

    /**
     * {@link DbTableMetaReader}の実装を作成します。
     * 
     * @return {@link DbTableMetaReader}の実装
     */
    protected DbTableMetaReader createDbTableMetaReader() {
        return new DbTableMetaReaderImpl(dataSource, dialect, schemaName,
                tableNamePattern);
    }

    /**
     * {@link EntityDescFactory}の実装を作成します。
     * 
     * @return {@link EntityDescFactory}の実装
     */
    protected EntityDescFactory createEntityDescFactory() {
        AttributeDescFactory attributeDescFactory = new AttributeDescFactoryImpl(
                persistenceConvention, dialect, versionColumnName);
        return new EntityDescFactoryImpl(persistenceConvention,
                attributeDescFactory, schemaName != null);
    }

    /**
     * {@link EntityModelFactory}の実装を作成します。
     * 
     * @return {@link EntityModelFactory}の実装
     */
    protected EntityModelFactory createEntityModelFactory() {
        String packageName = ClassUtil.concatName(rootPackageName,
                entityPackageName);
        return new EntityModelFactoryImpl(packageName,
                new AttributeModelFactoryImpl());
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
     * @param entityDescList
     *            エンティティ記述のリスト
     */
    protected void generate(List<EntityDesc> entityDescList) {
        for (EntityDesc entityDesc : entityDescList) {
            generateEntity(entityDesc);
        }
    }

    /**
     * エンティティクラスのJavaファイルを生成します。
     * 
     * @param entityDesc
     *            エンティティ記述
     */
    protected void generateEntity(EntityDesc entityDesc) {
        EntityModel model = entityModelFactory.getEntityModel(entityDesc);
        GenerationContext context = createGenerationContext(model,
                entityTemplateFileName);
        generator.generate(context);
    }

    /**
     * {@link GenerationContext}の実装を作成します。
     * 
     * @param model
     *            モデル
     * @param templateName
     *            テンプレート名
     * @return {@link GenerationContext}の実装
     */
    protected GenerationContext createGenerationContext(EntityModel model,
            String templateName) {
        String packageName = model.getPackageName();
        String shortClassName = model.getShortClassName();

        File dir = new File(javaFileDestDir, packageName.replace('.',
                File.separatorChar));
        File file = new File(dir, shortClassName + ".java");

        return new GenerationContextImpl(model, dir, file, templateName,
                javaFileEncoding, overwrite);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}
