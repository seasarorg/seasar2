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

import org.seasar.extension.jdbc.gen.AttributeDescFactory;
import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.ConditionModelFactory;
import org.seasar.extension.jdbc.gen.DbTableMeta;
import org.seasar.extension.jdbc.gen.DbTableMetaReader;
import org.seasar.extension.jdbc.gen.EntityDesc;
import org.seasar.extension.jdbc.gen.EntityDescFactory;
import org.seasar.extension.jdbc.gen.EntityModelFactory;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.Generator;
import org.seasar.extension.jdbc.gen.desc.AttributeDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.EntityDescFactoryImpl;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.generator.GeneratorImpl;
import org.seasar.extension.jdbc.gen.meta.DbTableMetaReaderImpl;
import org.seasar.extension.jdbc.gen.model.ConditionModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.EntityModelFactoryImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.util.ClassUtil;

/**
 * S2JDBC用エンティティのJavaファイルを生成する{@link Command}の実装クラスです。
 * 
 * @author taedium
 */
public class EntityGenCommand extends AbstractCommand {

    /** 生成するJavaファイルの出力先ディレクトリ */
    @BindableProperty
    protected File destDir = new File("src/main/java");

    /** Javaファイルのエンコーディング */
    @BindableProperty
    protected String javaFileEncoding = "UTF-8";

    /** 条件クラス名のサフィックス */
    @BindableProperty
    protected String conditionClassNameSuffix = "Condition";

    /** 条件クラスのパッケージ名 */
    @BindableProperty
    protected String conditionPackageName = "condition";

    /** エンティティクラスのテンプレート名 */
    @BindableProperty
    protected String entityTemplateName = "entity.ftl";

    /** 条件クラスのテンプレート名 */
    @BindableProperty
    protected String conditionTemplateName = "condition.ftl";

    /** 条件クラスを生成する場合{@link Boolean#TRUE } */
    @BindableProperty
    protected Boolean generateConditionClass = Boolean.TRUE;

    /** スキーマ名 */
    @BindableProperty
    protected String schemaName;

    /** Javaコードの生成の対象とするテーブル名の正規表現 */
    @BindableProperty
    protected String tableNamePattern = ".*";

    /** バージョンカラムの名前 */
    @BindableProperty
    protected String versionColumnName = "version";

    /** データソース */
    protected DataSource dataSource;

    /** 方言 */
    protected GenDialect dialect;

    /** 永続化層の命名規約 */
    protected PersistenceConvention persistenceConvention;

    /** スキーマのリーダ */
    protected DbTableMetaReader dbTableMetaReader;

    /** {@link EntityDesc}のファクトリ */
    protected EntityDescFactory entityDescFactory;

    /** ジェネレータ */
    protected Generator generator;

    /** エンティティクラスのモデルのファクトリ */
    protected EntityModelFactory entityModelFactory;

    /** 条件クラスのモデルのファクトリ */
    protected ConditionModelFactory conditionModelFactory;

    /**
     * インスタンスを構築します。
     */
    public EntityGenCommand() {
    }

    /**
     * エンティティクラスのテンプレート名を設定します。
     * 
     * @param entityTemplateName
     *            エンティティクラスのテンプレート名
     */
    public void setEntityTemplateName(String entityTemplateName) {
        this.entityTemplateName = entityTemplateName;
    }

    /**
     * 生成Javaファイル出力先ディレクトリを設定します。
     * 
     * @param destDir
     *            生成Javaファイル出力先ディレクトリ
     */
    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    /**
     * Javaコードのエンコーディングを設定します。
     * 
     * @param javaFileEncoding
     *            Javaコードのエンコーディング
     */
    public void setJavaFileEncoding(String javaFileEncoding) {
        this.javaFileEncoding = javaFileEncoding;
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
     * 正規表現で表されたテーブル名のパターンを正規表現で設定します。
     * 
     * @param tableNamePattern
     *            テーブル名のパターン
     */
    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    /**
     * バージョン用カラム名を設定します。
     * 
     * @param versionColumnName
     *            バージョン用カラム名を設定します。
     */
    public void setVersionColumnName(String versionColumnName) {
        this.versionColumnName = versionColumnName;
    }

    /**
     * 条件クラスのパッケージ名を設定します。
     * 
     * @param conditionPackageName
     *            条件クラスのパッケージ名
     */
    public void setConditionPackageName(String conditionPackageName) {
        this.conditionPackageName = conditionPackageName;
    }

    /**
     * 条件クラス名のサフィックスを設定します。
     * 
     * @param conditionClassNameSuffix
     *            条件クラス名のサフィックス
     */
    public void setConditionClassNameSuffix(String conditionClassNameSuffix) {
        this.conditionClassNameSuffix = conditionClassNameSuffix;
    }

    /**
     * 条件クラスのテンプレート名を設定します。
     * 
     * @param conditionTemplateName
     *            条件クラスのテンプレート名
     */
    public void setConditionTemplateName(String conditionTemplateName) {
        this.conditionTemplateName = conditionTemplateName;
    }

    /**
     * エンティティ条件クラスを生成する場合{@code true}、生成しない場合{@code false}を設定します。
     * 
     * @param generateConditionClass
     *            エンティティ条件クラスを生成する場合{@code true}、生成しない場合{@code false}
     */
    public void setGenerateConditionClass(Boolean generateConditionClass) {
        this.generateConditionClass = generateConditionClass;
    }

    /**
     * 初期化します。
     */
    @Override
    protected void init() {
        super.init();

        JdbcManagerImplementor jdbcManager = SingletonS2Container
                .getComponent(jdbcManagerName);
        dataSource = jdbcManager.getDataSource();
        persistenceConvention = jdbcManager.getPersistenceConvention();
        dialect = GenDialectManager.getGenDialect(jdbcManager.getDialect());

        dbTableMetaReader = createSchemaReader();
        entityDescFactory = createEntityDescFactory();
        generator = createGenerator();
        entityModelFactory = createEntityModelFactory();
        conditionModelFactory = createConditionModelFactory();
    }

    /**
     * {@link DbTableMetaReader}の実装を作成します。
     * 
     * @return {@link DbTableMetaReader}の実装
     */
    protected DbTableMetaReader createSchemaReader() {
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
                attributeDescFactory);
    }

    /**
     * {@link EntityModelFactory}の実装を作成します。
     * 
     * @return {@link EntityModelFactory}の実装
     */
    protected EntityModelFactory createEntityModelFactory() {
        return new EntityModelFactoryImpl();
    }

    /**
     * {@link ConditionModelFactory}の実装を作成します。
     * 
     * @return {@link ConditionModelFactory}の実装
     */
    protected ConditionModelFactory createConditionModelFactory() {
        return new ConditionModelFactoryImpl();
    }

    /**
     * {@link Generator}の実装を作成します。
     * 
     * @return {@link Generator}の実装
     */
    protected Generator createGenerator() {
        return new GeneratorImpl(templateFileEncoding, templateDir);
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

    /**
     * エンティティクラスのJavaファイルを生成します。
     * 
     * @param entityDescList
     *            エンティティ記述のリスト
     */
    protected void generate(List<EntityDesc> entityDescList) {
        for (EntityDesc entityDesc : entityDescList) {
            generateEntity(entityDesc);
            if (generateConditionClass) {
                generateCondition(entityDesc);
            }
        }
    }

    /**
     * エンティティクラスのJavaファイルを生成します。
     * 
     * @param entityDesc
     *            エンティティ記述
     */
    protected void generateEntity(EntityDesc entityDesc) {
        String packageName = ClassUtil.concatName(rootPackageName,
                entityPackageName);
        String className = ClassUtil.concatName(packageName, entityDesc
                .getName());
        Object model = entityModelFactory.getEntityModel(entityDesc, className);
        GenerationContext context = createGenerationContext(model, className,
                entityTemplateName, false);
        generator.generate(context);
    }

    /**
     * 条件クラスのJavaファイルを生成します。
     * 
     * @param entityDesc
     *            エンティティ記述
     */
    protected void generateCondition(EntityDesc entityDesc) {
        String packageName = ClassUtil.concatName(rootPackageName,
                conditionPackageName);
        String shortClassName = entityDesc.getName() + conditionClassNameSuffix;
        String className = ClassUtil.concatName(packageName, shortClassName);
        Object model = conditionModelFactory.getConditionModel(entityDesc,
                className);
        GenerationContext context = createGenerationContext(model, className,
                conditionTemplateName, false);
        generator.generate(context);
    }

    /**
     * {@link GenerationContext}を返します。
     * 
     * @param model
     *            モデル
     * @param className
     *            クラス名
     * @param templateName
     *            テンプレート名
     * @param overwrite
     *            ファイルを上書きする場合 {@code true}
     * @return {@link GenerationContext}
     */
    protected GenerationContext createGenerationContext(Object model,
            String className, String templateName, boolean overwrite) {
        String[] elements = ClassUtil.splitPackageAndShortClassName(className);
        String packageName = elements[0];
        String shortClassName = elements[1];

        File dir = new File(destDir, packageName.replace('.',
                File.separatorChar));
        File file = new File(dir, shortClassName + ".java");

        return new GenerationContext(model, dir, file, templateName,
                javaFileEncoding, overwrite);
    }

}
