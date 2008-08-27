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
package org.seasar.extension.jdbc.gen.internal.command;

import java.io.File;

import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.desc.EntitySetDesc;
import org.seasar.extension.jdbc.gen.desc.EntitySetDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.dialect.GenDialectRegistry;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.generator.Generator;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.meta.DbTableMetaReader;
import org.seasar.extension.jdbc.gen.model.ClassModel;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.model.EntityModelFactory;
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
 * 主キーやカラムのNOT NULL制約は認識してJavaコードに反映しますが、一意キーや外部キーは反映しません。
 * </p>
 * 
 * @author taedium
 */
public class GenerateEntityCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(GenerateEntityCommand.class);

    /** エンティティクラスのパッケージ名 */
    protected String entityPackageName = "entity";

    /** エンティティクラスのテンプレート名 */
    protected String entityTemplateFileName = "java/entity.ftl";

    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File javaFileDestDir = new File(new File("src", "main"), "java");

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding = "UTF-8";

    /** 上書きをする場合{@code true}、しない場合{@code false} */
    protected boolean overwrite = false;

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** スキーマ名 */
    protected String schemaName = null;

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルを格納するプライマリディレクトリ */
    protected File templateFilePrimaryDir = null;

    /** Javaコード生成の対象とするテーブル名の正規表現 */
    protected String tableNamePattern = ".*";

    /** Javaコード生成の対象としないテーブル名の正規表現 */
    protected String ignoreTableNamePattern = "(SCHEMA_INFO|.*\\$.*)";

    /** バージョンカラムの名前 */
    protected String versionColumnName = "version";

    /** 単語を複数系に変換するための辞書ファイル */
    protected File pluralFormFile = null;

    /** 方言 */
    protected GenDialect dialect;

    /** テーブルメタデータのリーダ */
    protected DbTableMetaReader dbTableMetaReader;

    /** エンティティセット記述のファクトリ */
    protected EntitySetDescFactory entitySetDescFactory;

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
     * Javaコード生成の対象としないテーブル名の正規表現を返します。
     * 
     * @return Javaコード生成の対象としないテーブル名の正規表現
     */
    public String getIgnoreTableNamePattern() {
        return ignoreTableNamePattern;
    }

    /**
     * Javaコード生成の対象としないテーブル名の正規表現を設定します。
     * 
     * @param ignoreTableNamePattern
     *            Javaコード生成の対象としないテーブル名の正規表現
     */
    public void setIgnoreTableNamePattern(String ignoreTableNamePattern) {
        this.ignoreTableNamePattern = ignoreTableNamePattern;
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

    /**
     * 単語を複数系に変換するための辞書ファイルを返します。
     * 
     * @return 単語を複数系に変換するための辞書ファイル
     */
    public File getPluralFormFile() {
        return pluralFormFile;
    }

    /**
     * 単語を複数系に変換するための辞書ファイルを設定します。
     * 
     * @param pluralFormFile
     *            単語を複数系に変換するための辞書ファイル
     */
    public void setPluralFormFile(File pluralFormFile) {
        this.pluralFormFile = pluralFormFile;
    }

    @Override
    protected void doValidate() {
    }

    /**
     * 初期化します。
     */
    @Override
    protected void doInit() {
        dialect = GenDialectRegistry.getGenDialect(jdbcManager.getDialect());
        dbTableMetaReader = createDbTableMetaReader();
        entitySetDescFactory = createEntitySetDescFactory();
        generator = createGenerator();
        entityModelFactory = createEntityModelFactory();

        logRdbmsAndGenDialect(dialect);
    }

    @Override
    protected void doExecute() {
        EntitySetDesc entitySetDesc = entitySetDescFactory.getEntitySetDesc();
        for (EntityDesc entityDesc : entitySetDesc.getEntityDescList()) {
            generateEntity(entityDesc);
        }
    }

    @Override
    protected void doDestroy() {
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
     * {@link DbTableMetaReader}の実装を作成します。
     * 
     * @return {@link DbTableMetaReader}の実装
     */
    protected DbTableMetaReader createDbTableMetaReader() {
        return factory.createDbTableMetaReader(this, jdbcManager
                .getDataSource(), dialect, schemaName, tableNamePattern,
                ignoreTableNamePattern);
    }

    /**
     * {@link EntitySetDescFactory}の実装を作成します。
     * 
     * @return {@link EntitySetDescFactory}の実装
     */
    protected EntitySetDescFactory createEntitySetDescFactory() {
        return factory.createEntitySetDescFactory(this, dbTableMetaReader,
                jdbcManager.getPersistenceConvention(), dialect,
                versionColumnName, pluralFormFile);
    }

    /**
     * {@link EntityModelFactory}の実装を作成します。
     * 
     * @return {@link EntityModelFactory}の実装
     */
    protected EntityModelFactory createEntityModelFactory() {
        return factory.createEntityModelFactory(this, ClassUtil.concatName(
                rootPackageName, entityPackageName), schemaName != null);
    }

    /**
     * {@link Generator}の実装を作成します。
     * 
     * @return {@link Generator}の実装
     */
    protected Generator createGenerator() {
        return factory.createGenerator(this, templateFileEncoding,
                templateFilePrimaryDir);
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
    protected GenerationContext createGenerationContext(ClassModel model,
            String templateName) {
        File file = FileUtil.createJavaFile(javaFileDestDir, model
                .getPackageName(), model.getShortClassName());
        return factory.createGenerationContext(this, model, file, templateName,
                javaFileEncoding, overwrite);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}
