/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.desc.EntitySetDesc;
import org.seasar.extension.jdbc.gen.desc.EntitySetDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
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
 * 
 * @author taedium
 */
public class GenerateEntityCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(GenerateEntityCommand.class);

    /** スキーマ名 */
    protected String schemaName = null;

    /** Javaコード生成の対象とするテーブル名の正規表現 */
    protected String tableNamePattern = ".*";

    /** Javaコード生成の対象としないテーブル名の正規表現 */
    protected String ignoreTableNamePattern = "(SCHEMA_INFO|.*\\$.*)";

    /** バージョンカラム名のパターン */
    protected String versionColumnNamePattern = "VERSION([_]?NO)?";

    /** 単語を複数系に変換するための辞書ファイル */
    protected File pluralFormFile = null;

    /** {@link TemporalType}を使用する場合{@code true} */
    protected boolean useTemporalType = false;

    /** エンティティクラスでアクセサを使用する場合{@code true} */
    protected boolean useAccessor = false;

    /** エンティティの識別子の生成方法を示す列挙型、生成しない場合は{@code null}。 */
    protected GenerationType generationType = null;

    /** エンティティの識別子の初期値、指定しない場合は{@code null} */
    protected Integer initialValue = null;

    /** エンティティの識別子の割り当てサイズ、指定しない場合は{@code null} */
    protected Integer allocationSize = null;

    /** カタログ名を表示する場合{@code true} */
    protected boolean showCatalogName = false;

    /** スキーマ名を表示する場合{@code true} */
    protected boolean showSchemaName = false;

    /** テーブル名を表示する場合{@code true} */
    protected boolean showTableName = false;

    /** カラム名を表示する場合{@code true} */
    protected boolean showColumnName = false;

    /** カラム定義を表示する場合{@code true} */
    protected boolean showColumnDefinition = false;

    /** {@link JoinColumn}を表示する場合{@code true} */
    protected boolean showJoinColumn = false;

    /** エンティティクラスのテンプレート名 */
    protected String entityTemplateFileName = "java/entity.ftl";

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルを格納するプライマリディレクトリ */
    protected File templateFilePrimaryDir = null;

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** エンティティクラスのパッケージ名 */
    protected String entityPackageName = "entity";

    /** エンティティのスーパークラスの名前 */
    protected String entitySuperclassName = null;

    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File javaFileDestDir = new File(new File("src", "main"), "java");

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding = "UTF-8";

    /** 上書きをする場合{@code true}、しない場合{@code false} */
    protected boolean overwrite = false;

    /** データベースのコメントをJavaコードに適用する場合{@code true} */
    protected boolean applyDbCommentToJava = false;

    /** {@link GenDialect}の実装クラス名 */
    protected String genDialectClassName = null;

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
     * バージョンカラム名のパターンを返します。
     * 
     * @return バージョンカラム名のパターン
     */
    public String getVersionColumnNamePattern() {
        return versionColumnNamePattern;
    }

    /**
     *バージョンカラム名のパターンを設定します。
     * 
     * @param versionColumnNamePattern
     *            バージョンカラム名のパターン
     */
    public void setVersionColumnNamePattern(String versionColumnNamePattern) {
        this.versionColumnNamePattern = versionColumnNamePattern;
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

    /**
     * カタログ名を表示する場合{@code true}を返します。
     * 
     * @return カタログ名を表示する場合{@code true}
     */
    public boolean isShowCatalogName() {
        return showCatalogName;
    }

    /**
     * カタログ名を表示する場合{@code true}を設定します。
     * 
     * @param showCatalogName
     *            カタログ名を表示する場合{@code true}
     */
    public void setShowCatalogName(boolean showCatalogName) {
        this.showCatalogName = showCatalogName;
    }

    /**
     * スキーマ名を表示する場合{@code true}を返します。
     * 
     * @return スキーマ名を表示する場合{@code true}
     */
    public boolean isShowSchemaName() {
        return showSchemaName;
    }

    /**
     * スキーマ名を表示する場合{@code true}を設定します。
     * 
     * @param showSchemaName
     *            スキーマ名を表示する場合{@code true}
     */
    public void setShowSchemaName(boolean showSchemaName) {
        this.showSchemaName = showSchemaName;
    }

    /**
     * テーブル名を表示する場合{@code true}を返します。
     * 
     * @return テーブル名を表示する場合{@code true}
     */
    public boolean isShowTableName() {
        return showTableName;
    }

    /**
     * テーブル名を表示する場合{@code true}を設定します。
     * 
     * @param showTableName
     *            テーブル名を表示する場合{@code true}
     */
    public void setShowTableName(boolean showTableName) {
        this.showTableName = showTableName;
    }

    /**
     * カラム名を表示する場合{@code true}を返します。
     * 
     * @return カラム名を表示する場合{@code true}
     */
    public boolean isShowColumnName() {
        return showColumnName;
    }

    /**
     * カラム名を表示する場合{@code true}を設定します。
     * 
     * @param showColumnName
     *            カラム名を表示する場合{@code true}
     */
    public void setShowColumnName(boolean showColumnName) {
        this.showColumnName = showColumnName;
    }

    /**
     * カラム定義を表示する場合{@code true}を返します。
     * 
     * @return カラム定義を表示する場合{@code true}
     */
    public boolean isShowColumnDefinition() {
        return showColumnDefinition;
    }

    /**
     * カラム定義を表示する場合{@code true}を設定します。
     * 
     * @param showColumnDefinition
     *            カラム定義を表示する場合{@code true}を設定します。
     */
    public void setShowColumnDefinition(boolean showColumnDefinition) {
        this.showColumnDefinition = showColumnDefinition;
    }

    /**
     * {@link JoinColumn}を表示する場合{@code true}を返します。
     * 
     * @return {@link JoinColumn}を表示する場合{@code true}
     */
    public boolean isShowJoinColumn() {
        return showJoinColumn;
    }

    /**
     * {@link JoinColumn}を表示する場合{@code true}を設定します。
     * 
     * @param showJoinColumn
     *            {@link JoinColumn}を表示する場合{@code true}
     */
    public void setShowJoinColumn(boolean showJoinColumn) {
        this.showJoinColumn = showJoinColumn;
    }

    /**
     * {@link GenDialect}の実装クラス名を返します。
     * 
     * @return {@link GenDialect}の実装クラス名
     */
    public String getGenDialectClassName() {
        return genDialectClassName;
    }

    /**
     * {@link GenDialect}の実装クラス名を設定します。
     * 
     * @param genDialectClassName
     *            {@link GenDialect}の実装クラス名
     */
    public void setGenDialectClassName(String genDialectClassName) {
        this.genDialectClassName = genDialectClassName;
    }

    /**
     * エンティティの識別子の生成方法を示す列挙型を返します。
     * 
     * @return エンティティの識別子の生成方法を示す列挙型、生成しない場合は{@code null}
     */
    public GenerationType getGenerationType() {
        return generationType;
    }

    /**
     * エンティティの識別子の生成方法を示す列挙型を設定します。
     * 
     * @param generationType
     *            エンティティの識別子の生成方法を示す列挙型、生成しない場合は{@code null}
     */
    public void setGenerationType(GenerationType generationType) {
        this.generationType = generationType;
    }

    /**
     * エンティティの識別子の初期値を返します。
     * 
     * @return エンティティの識別子の初期値、指定しない場合は{@code null}
     */
    public Integer getInitialValue() {
        return initialValue;
    }

    /**
     * エンティティの識別子の初期値を設定します。
     * 
     * @param initialValue
     *            エンティティの識別子の初期値、指定しない場合は{@code null}
     */
    public void setInitialValue(Integer initialValue) {
        this.initialValue = initialValue;
    }

    /**
     * エンティティの識別子の割り当てサイズを返します。
     * 
     * @return エンティティの識別子の割り当てサイズ、指定しない場合は{@code null}
     */
    public Integer getAllocationSize() {
        return allocationSize;
    }

    /**
     * エンティティの識別子の割り当てサイズを設定します。
     * 
     * @param allocationSize
     *            エンティティの識別子の割り当てサイズ、指定しない場合は{@code null}
     */
    public void setAllocationSize(Integer allocationSize) {
        this.allocationSize = allocationSize;
    }

    /**
     * エンティティのスーパークラスの名前を返します。
     * 
     * @return エンティティのスーパークラスの名前
     */
    public String getEntitySuperclassName() {
        return entitySuperclassName;
    }

    /**
     * エンティティのスーパークラスの名前を設定します。
     * 
     * @param entitySuperclassName
     *            エンティティのスーパークラスの名前
     */
    public void setEntitySuperclassName(String entitySuperclassName) {
        this.entitySuperclassName = entitySuperclassName;
    }

    /**
     * エンティティクラスでアクセサを使用する場合{@code true}を返します。
     * 
     * @return エンティティクラスでアクセサを使用する場合{@code true}
     */
    public boolean isUseAccessor() {
        return useAccessor;
    }

    /**
     * エンティティクラスでアクセサを使用する場合{@code true}を設定します。
     * 
     * @param useAccessor
     *            エンティティクラスでアクセサを使用する場合{@code true}
     */
    public void setUseAccessor(boolean useAccessor) {
        this.useAccessor = useAccessor;
    }

    /**
     * データベースのコメントをJavaコードに適用する場合{@code true}を返します。
     * 
     * @return データベースのコメントをJavaコードに適用する場合{@code true}
     */
    public boolean isApplyDbCommentToJava() {
        return applyDbCommentToJava;
    }

    /**
     * データベースのコメントをJavaコードに適用する場合{@code true}を設定します。
     * 
     * @param applyDbCommentToJava
     *            データベースのコメントをJavaコードに適用する場合{@code true}
     */
    public void setApplyDbCommentToJava(boolean applyDbCommentToJava) {
        this.applyDbCommentToJava = applyDbCommentToJava;
    }

    /**
     * {@link TemporalType}を使用する場合{@code true}を返します。
     * 
     * @return {@link TemporalType}を使用する場合{@code true}
     */
    public boolean isUseTemporalType() {
        return useTemporalType;
    }

    /**
     * {@link TemporalType}を使用する場合{@code true}を設定します。
     * 
     * @param useTemporalType
     *            {@link TemporalType}を使用する場合{@code true}
     */
    public void setUseTemporalType(boolean useTemporalType) {
        this.useTemporalType = useTemporalType;
    }

    @Override
    protected void doValidate() {
    }

    /**
     * 初期化します。
     */
    @Override
    protected void doInit() {
        dialect = getGenDialect(genDialectClassName);
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
                ignoreTableNamePattern, applyDbCommentToJava);
    }

    /**
     * {@link EntitySetDescFactory}の実装を作成します。
     * 
     * @return {@link EntitySetDescFactory}の実装
     */
    protected EntitySetDescFactory createEntitySetDescFactory() {
        return factory.createEntitySetDescFactory(this, dbTableMetaReader,
                jdbcManager.getPersistenceConvention(), dialect,
                versionColumnNamePattern, pluralFormFile, generationType,
                initialValue, allocationSize);
    }

    /**
     * {@link EntityModelFactory}の実装を作成します。
     * 
     * @return {@link EntityModelFactory}の実装
     */
    protected EntityModelFactory createEntityModelFactory() {
        Class<?> superClass = entitySuperclassName != null ? ClassUtil
                .forName(entitySuperclassName) : null;
        return factory.createEntityModelFactory(this, ClassUtil.concatName(
                rootPackageName, entityPackageName), superClass,
                useTemporalType, useAccessor, applyDbCommentToJava,
                showCatalogName, showSchemaName, showTableName, showColumnName,
                showColumnDefinition, showJoinColumn, jdbcManager
                        .getPersistenceConvention());
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
