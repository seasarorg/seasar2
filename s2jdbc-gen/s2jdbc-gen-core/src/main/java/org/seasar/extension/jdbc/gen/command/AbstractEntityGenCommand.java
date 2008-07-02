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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.AttributeDescFactory;
import org.seasar.extension.jdbc.gen.DbTableMeta;
import org.seasar.extension.jdbc.gen.EntityBaseModel;
import org.seasar.extension.jdbc.gen.EntityBaseModelFactory;
import org.seasar.extension.jdbc.gen.EntityDesc;
import org.seasar.extension.jdbc.gen.EntityDescFactory;
import org.seasar.extension.jdbc.gen.EntityModel;
import org.seasar.extension.jdbc.gen.EntityModelFactory;
import org.seasar.extension.jdbc.gen.GenCommand;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.Generator;
import org.seasar.extension.jdbc.gen.SchemaReader;
import org.seasar.extension.jdbc.gen.desc.AttributeDescFactoryImpl;
import org.seasar.extension.jdbc.gen.desc.EntityDescFactoryImpl;
import org.seasar.extension.jdbc.gen.generator.GeneratorImpl;
import org.seasar.extension.jdbc.gen.meta.SchemaReaderImpl;
import org.seasar.extension.jdbc.gen.model.EntityBaseModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.EntityModelFactoryImpl;
import org.seasar.extension.jdbc.gen.util.ConfigurationUtil;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FieldUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

/**
 * エンティティのを生成するコマンドの抽象クラスです。
 * 
 * @author taedium
 */
public abstract class AbstractEntityGenCommand implements GenCommand {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(AbstractEntityGenCommand.class);

    /** diconファイル */
    protected String diconFile;

    /** ルートパッケージ名 */
    protected String rootPackageName;

    /** エンティティクラスのパッケージ名 */
    protected String entityPackageName;

    /** エンティティ基底クラスのパッケージ名 */
    protected String entityBasePackageName;

    /** エンティティ基底クラス名のプレフィックス */
    protected String entityBaseClassNamePrefix;

    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File destDir;

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding;

    /** テンプレートファイルを格納するディレクトリ */
    protected File templateDir;

    /** エンティティクラスのテンプレート名 */
    protected String entityTemplateName;

    /** エンティティ基底クラスのテンプレート名 */
    protected String entityBaseTemplateName;

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding;

    /** スキーマ名 */
    protected String schemaName;

    /** 正規表現で表されるテーブル名のパターン */
    protected String tableNamePattern;

    /** バージョンカラムの名前 */
    protected String versionColumnName;

    /** このインスタンスで{@link SingletonS2ContainerFactory}が初期化されたら{@code true} */
    protected boolean initialized;

    /** データソース */
    protected DataSource dataSource;

    /** 方言 */
    protected GenDialect dialect;

    /** 永続化層の命名規約 */
    protected PersistenceConvention persistenceConvention;

    /** スキーマのリーダ */
    protected SchemaReader schemaReader;

    /** {@link EntityDesc}のファクトリ */
    protected EntityDescFactory entityDescFactory;

    /** ジェネレータ */
    protected Generator generator;

    /** {@link EntityModel}のファクトリ */
    protected EntityModelFactory entityModelFactory;

    /** {@link EntityBaseModel}のファクトリ */
    protected EntityBaseModelFactory entityBaseModelFactory;

    /**
     * インスタンスを構築します。
     */
    public AbstractEntityGenCommand() {
    }

    /**
     * diconファイルを設定します。
     * 
     * @param diconFile
     *            daiconファイル
     */
    public void setDiconFile(String diconFile) {
        this.diconFile = diconFile;
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
     * エンティティパッケージ名を設定します。
     * 
     * @param entityPackageName
     *            エンティティパッケージ名
     */
    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    /**
     * エンティティ基底クラスのパッケージ名を設定します。
     * 
     * @param entityBasePackageName
     *            エンティティ基底クラスのパッケージ名
     */
    public void setEntityBasePackageName(String entityBasePackageName) {
        this.entityBasePackageName = entityBasePackageName;
    }

    /**
     * エンティティ基底クラス名のサフィックスを設定します。
     * 
     * @param entityBaseClassNameSuffix
     *            エンティティ基底クラス名のサフィックス
     */
    public void setEntityBaseClassNamePrefix(String entityBaseClassNameSuffix) {
        this.entityBaseClassNamePrefix = entityBaseClassNameSuffix;
    }

    /**
     * テンプレートファイルを格納するディレクトリを設定します。
     * 
     * @param templateDir
     *            テンプレートファイルを格納するディレクトリ
     */
    public void setTemplateDir(File templateDir) {
        this.templateDir = templateDir;
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
     * エンティティ基底クラスのテンプレート名
     * 
     * @param entityBaseTemplateName
     *            エンティティ基底クラスのテンプレート名
     */
    public void setEntityBaseTemplateName(String entityBaseTemplateName) {
        this.entityBaseTemplateName = entityBaseTemplateName;
    }

    /**
     * テンプレートのエンコーディングを設定します。
     * 
     * @param templateEncoding
     *            テンプレートのエンコーディング
     */
    public void setTemplateFileEncoding(String templateEncoding) {
        this.templateFileEncoding = templateEncoding;
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
     * @param javaCodeEncoding
     *            Javaコードのエンコーディング
     */
    public void setJavaFileEncoding(String javaCodeEncoding) {
        this.javaFileEncoding = javaCodeEncoding;
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
     * バージョン用カラム名を設定します。
     * 
     * @param versionColumnName
     *            バージョン用カラム名を設定します。
     */
    public void setVersionColumnName(String versionColumnName) {
        this.versionColumnName = versionColumnName;
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

    public void execute() {
        init();
        try {
            List<DbTableMeta> tableMetaList = read();
            List<EntityDesc> entityDescList = convert(tableMetaList);
            generate(entityDescList);
        } finally {
            destroy();
        }
    }

    /**
     * 初期化します。
     */
    protected void init() {
        setupDefaultProperties();
        setupInternalProperties();
        setupCollaborators();
        if (logger.isDebugEnabled()) {
            logExternalProperties();
        }
    }

    /**
     * デフォルトのプロパティを準備します。
     */
    protected void setupDefaultProperties() {
    }

    /**
     * 内部的なプロパティを準備します。
     */
    protected void setupInternalProperties() {
        if (!SingletonS2ContainerFactory.hasContainer()) {
            initialized = true;
            SingletonS2ContainerFactory.setConfigPath(diconFile);
            SingletonS2ContainerFactory.init();
        }
    }

    /**
     * コレボレータを準備します。
     */
    protected void setupCollaborators() {
        schemaReader = createSchemaReader();
        entityDescFactory = createEntityDescFactory();
        generator = createGenerator();
        entityModelFactory = createEntityModelFactory();
        entityBaseModelFactory = createEntityBaseModelFactory();
    }

    /**
     * このインスタンスに設定可能なプロパティをログ出力します。
     */
    protected void logExternalProperties() {
        Class<?> clazz = getClass();
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(clazz);
        int propSize = beanDesc.getPropertyDescSize();
        for (int i = 0; i < propSize; i++) {
            PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
            if (propertyDesc.hasWriteMethod()) {
                Field f = propertyDesc.getField();
                logger.log("DS2JDBCGen0001", new Object[] { f.getName(),
                        FieldUtil.get(f, this) });
            }
        }
    }

    /**
     * {@link SchemaReader}の実装を作成します。
     * 
     * @return {@link SchemaReader}の実装
     */
    protected SchemaReader createSchemaReader() {
        return new SchemaReaderImpl(dataSource, dialect);
    }

    /**
     * {@link EntityDescFactory}の実装を作成します。
     * 
     * @return {@link EntityDescFactory}の実装
     */
    protected EntityDescFactory createEntityDescFactory() {
        return new EntityDescFactoryImpl(persistenceConvention,
                createAttributeDescFactory());
    }

    /**
     * {@link AttributeDescFactory}の実装を作成します。
     * 
     * @return {@link AttributeDescFactory}の実装
     */
    protected AttributeDescFactory createAttributeDescFactory() {
        return new AttributeDescFactoryImpl(persistenceConvention, dialect,
                versionColumnName);
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
     * {@link EntityBaseModelFactory}の実装を作成します。
     * 
     * @return {@link EntityBaseModelFactory}の実装
     */
    protected EntityBaseModelFactory createEntityBaseModelFactory() {
        return new EntityBaseModelFactoryImpl();
    }

    /**
     * {@link Generator}の実装を作成します。
     * 
     * @return {@link Generator}の実装
     */
    protected Generator createGenerator() {
        Configuration cfg = new Configuration();
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setEncoding(Locale.getDefault(), templateFileEncoding);
        ConfigurationUtil.setDirectoryForTemplateLoading(cfg, templateDir);
        return new GeneratorImpl(cfg);
    }

    /**
     * 破棄します。
     */
    protected void destroy() {
        if (initialized) {
            SingletonS2ContainerFactory.destroy();
        }
    }

    /**
     * 読み込みます。
     * 
     * @return 読み込まれたテーブル記述のリスト
     */
    protected List<DbTableMeta> read() {
        return schemaReader.read(schemaName, tableNamePattern);
    }

    /**
     * テーブルメタ情報をエンティティ記述に変換します。
     * 
     * @param tableMetaList
     *            テーブルメタ情報のリスト
     * @return エンティティ記述のリスト
     */
    protected List<EntityDesc> convert(List<DbTableMeta> tableMetaList) {
        List<EntityDesc> result = new ArrayList<EntityDesc>();
        for (DbTableMeta tableMeta : tableMetaList) {
            EntityDesc entityDesc = entityDescFactory.getEntityDesc(tableMeta);
            result.add(entityDesc);
        }
        return result;
    }

    /**
     * エンティティのJavaファイルを生成します。
     * 
     * @param entityDescList
     *            エンティティ記述のリスト
     */
    protected void generate(List<EntityDesc> entityDescList) {
        for (EntityDesc entityDesc : entityDescList) {
            GenerationContext entityCtx = getEntityGenerationContext(entityDesc);
            generator.generate(entityCtx);
            GenerationContext entityBaseCtx = getEntityBaseGenerationContext(entityDesc);
            generator.generate(entityBaseCtx, true);
        }
    }

    /**
     * エンティティクラス用の{@link GenerationContext}を返します。
     * 
     * @param entityDesc
     *            エンティティ記述
     * @return エンティティクラス用の{@link GenerationContext}
     */
    protected GenerationContext getEntityGenerationContext(EntityDesc entityDesc) {
        String className = getEntityClassName(entityDesc.getName());
        String baseClassName = getEntityBaseClassName(entityDesc.getName());
        EntityModel model = entityModelFactory.getEntityModel(entityDesc,
                className, baseClassName);

        return getGenerationContext(model, className, entityTemplateName);
    }

    /**
     * エンティティの基底クラス用の{@link GenerationContext}を返します。
     * 
     * @param entityDesc
     *            エンティティ記述
     * @return エンティティ用の{@link GenerationContext}
     */
    protected GenerationContext getEntityBaseGenerationContext(
            EntityDesc entityDesc) {
        String className = getEntityBaseClassName(entityDesc.getName());
        EntityBaseModel model = entityBaseModelFactory.getEntityBaseModel(
                entityDesc, className);
        return getGenerationContext(model, className, entityBaseTemplateName);
    }

    /**
     * エンティティクラス名を返します。
     * 
     * @param entityName
     *            エンティティ名
     * @return エンティティクラス名
     */
    protected String getEntityClassName(String entityName) {
        String packageName = ClassUtil.concatName(rootPackageName,
                entityPackageName);
        return ClassUtil.concatName(packageName, entityName);
    }

    /**
     * エンティティの基底クラス名を返します。
     * 
     * @param entityName
     *            エンティティ名
     * @return エンティティの基底クラス名
     */
    protected String getEntityBaseClassName(String entityName) {
        String packageName = ClassUtil.concatName(rootPackageName,
                entityBasePackageName);
        String shortClassName = entityBaseClassNamePrefix + entityName;
        return ClassUtil.concatName(packageName, shortClassName);
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
     * @return {@link GenerationContext}
     */
    protected GenerationContext getGenerationContext(Object model,
            String className, String templateName) {
        String[] elements = ClassUtil.splitPackageAndShortClassName(className);
        String packageName = elements[0];
        String shortClassName = elements[1];

        File dir = new File(destDir, packageName.replace('.',
                File.separatorChar));
        File file = new File(dir, shortClassName + ".java");

        GenerationContext context = new GenerationContext();
        context.setDir(dir);
        context.setFile(file);
        context.setEncoding(javaFileEncoding);
        context.setModel(model);
        context.setTemplateName(templateName);
        return context;
    }

}
