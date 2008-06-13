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

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.AttributeDescFactory;
import org.seasar.extension.jdbc.gen.EntityBaseCodeFactory;
import org.seasar.extension.jdbc.gen.EntityCodeFactory;
import org.seasar.extension.jdbc.gen.EntityDescFactory;
import org.seasar.extension.jdbc.gen.EntityGenerator;
import org.seasar.extension.jdbc.gen.GenCommand;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.SchemaReader;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.factory.AttributeDescFactoryImpl;
import org.seasar.extension.jdbc.gen.factory.EntityBaseCodeFactoryImpl;
import org.seasar.extension.jdbc.gen.factory.EntityCodeFactoryImpl;
import org.seasar.extension.jdbc.gen.factory.EntityDescFactoryImpl;
import org.seasar.extension.jdbc.gen.generator.EntityGeneratorImpl;
import org.seasar.extension.jdbc.gen.model.DbTableMeta;
import org.seasar.extension.jdbc.gen.model.EntityBaseCode;
import org.seasar.extension.jdbc.gen.model.EntityCode;
import org.seasar.extension.jdbc.gen.model.EntityDesc;
import org.seasar.extension.jdbc.gen.reader.SchemaReaderImpl;
import org.seasar.extension.jdbc.gen.util.ConfigurationUtil;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.ResourceUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import static org.seasar.extension.jdbc.gen.command.GenEntitiesCommand.Default.*;

/**
 * エンティティのJavaファイルを生成する{@link GenCommand}の実装クラスです。
 * 
 * @author taedium
 */
public class GenEntitiesCommand implements GenCommand {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(GenEntitiesCommand.class);

    /** diconファイル */
    protected String diconFile;

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName;

    /** エンティティクラスのパッケージ名 */
    protected String entityPackageName;

    /** エンティティ基底クラスのパッケージ名 */
    protected String entityBasePackageName;

    /** エンティティ基底クラスのプレフィックス */
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

    protected SchemaReader schemaReader;

    protected EntityDescFactory entityDescFactory;

    protected EntityGenerator entityGenerator;

    protected EntityCodeFactory entityCodeFactory;

    protected EntityBaseCodeFactory entityBaseCodeFactory;

    /**
     * インスタンスを構築します。
     */
    public GenEntitiesCommand() {
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
     * {@link JdbcManager}のコンポーネント名を設定します。
     * 
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        this.jdbcManagerName = jdbcManagerName;
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
        setupDefaults();

        if (!SingletonS2ContainerFactory.hasContainer()) {
            initialized = true;
            SingletonS2ContainerFactory.setConfigPath(diconFile);
            SingletonS2ContainerFactory.init();
        }
        JdbcManagerImplementor jdbcManager = SingletonS2Container
                .getComponent(jdbcManagerName);
        dataSource = jdbcManager.getDataSource();
        persistenceConvention = jdbcManager.getPersistenceConvention();
        dialect = GenDialectManager.getGenDialect(jdbcManager.getDialect());
        if (logger.isDebugEnabled()) {
            logProperties();
        }

        schemaReader = createSchemaReader();
        entityDescFactory = createEntityDescFactory();
        entityGenerator = createEntityGenerator();
        entityCodeFactory = createEntityCodeFactory();
        entityBaseCodeFactory = createEntityBaseCodeFactory();
    }

    /**
     * デフォルト値を設定します。
     */
    protected void setupDefaults() {
        if (diconFile == null) {
            diconFile = DICON_FILE;
        }
        if (jdbcManagerName == null) {
            jdbcManagerName = JDBC_MANAGER_NAME;
        }
        if (entityPackageName == null) {
            entityPackageName = ENTITY_PACKAGE_NAME;
        }
        if (entityBasePackageName == null) {
            entityBasePackageName = ENTITY_BASE_PACKAGE_NAME;
        }
        if (entityBaseClassNamePrefix == null) {
            entityBaseClassNamePrefix = ENTITY_BASE_CLASS_NAME_PREFIX;
        }
        if (destDir == null) {
            destDir = DEST_DIR;
        }
        if (javaFileEncoding == null) {
            javaFileEncoding = JAVA_FILE_ENCODING;
        }
        if (templateDir == null) {
            templateDir = TEMPLATE_DIR;
        }
        if (templateFileEncoding == null) {
            templateFileEncoding = TEMPLATE_FILE_ENCODING;
        }
        if (entityTemplateName == null) {
            entityTemplateName = ENTITY_TEMPLATE_NAME;
        }
        if (entityBaseTemplateName == null) {
            entityBaseTemplateName = ENTITY_BASE_TEMPLATE_NAME;
        }
        if (schemaName == null) {
            schemaName = SCHEMA_NAME;
        }
        if (versionColumnName == null) {
            versionColumnName = VERSION_COLUMN_NAME;
        }
        if (tableNamePattern == null) {
            tableNamePattern = TABLE_NAME_PATTERN;
        }
    }

    /**
     * このインスタンスに設定可能なプロパティをログ出力します。
     */
    protected void logProperties() {
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

    protected EntityCodeFactory createEntityCodeFactory() {
        return new EntityCodeFactoryImpl();
    }

    protected EntityBaseCodeFactory createEntityBaseCodeFactory() {
        return new EntityBaseCodeFactoryImpl();
    }

    /**
     * {@link EntityGenerator}の実装を作成します。
     * 
     * @return {@link EntityGenerator}の実装
     */
    protected EntityGenerator createEntityGenerator() {
        Configuration cfg = new Configuration();
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setEncoding(Locale.getDefault(), templateFileEncoding);
        ConfigurationUtil.setDirectoryForTemplateLoading(cfg, templateDir);
        return new EntityGeneratorImpl(cfg);
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
     *            エンティティモデルのリスト
     */
    protected void generate(List<EntityDesc> entityDescList) {
        for (EntityDesc entityDesc : entityDescList) {
            GenerationContext entityCtx = getEntityGenerationContext(entityDesc);
            entityGenerator.generate(entityCtx, false);
            GenerationContext entityBaseCtx = getEntityBaseGenerationContext(entityDesc);
            entityGenerator.generate(entityBaseCtx);
        }
    }

    protected GenerationContext getEntityGenerationContext(EntityDesc entityDesc) {
        String className = ClassUtil.concatName(entityPackageName, entityDesc
                .getName());
        String baseClassName = getEntityBaseClassName(entityDesc.getName());
        EntityCode code = entityCodeFactory.getEntityCode(entityDesc,
                className, baseClassName);

        return getGenerationContext(code, className, entityTemplateName);
    }

    protected GenerationContext getEntityBaseGenerationContext(
            EntityDesc entityDesc) {
        String className = getEntityBaseClassName(entityDesc.getName());
        EntityBaseCode code = entityBaseCodeFactory.getEntityBaseCode(
                entityDesc, className);
        return getGenerationContext(code, className, entityBaseTemplateName);
    }

    protected String getEntityBaseClassName(String entityClassName) {
        return ClassUtil.concatName(entityBasePackageName,
                entityBaseClassNamePrefix + entityClassName);
    }

    protected GenerationContext getGenerationContext(Object model,
            String className, String templateName) {
        String[] elements = ClassUtil.splitPackageAndShortClassName(className);

        String packagePath = elements[0].replace('.', File.separatorChar);
        File dir = new File(destDir, packagePath);
        File file = new File(dir, elements[1] + ".java");

        GenerationContext context = new GenerationContext();
        context.setDir(dir);
        context.setFile(file);
        context.setEncoding(javaFileEncoding);
        context.setModel(model);
        context.setTemplateName(templateName);
        return context;
    }

    /**
     * {@link GenEntitiesCommand}のプロパティのデフォルト値を表します。
     * 
     * @author taedium
     */
    public static class Default {

        /** エンティティ基底クラス名のプレフィックス */
        public static String ENTITY_BASE_CLASS_NAME_PREFIX = "Abstract";

        /** ルートパッケージ名 */
        public static String ROOT_PACKAGE_NAME = "";

        /** エンティティパッケージ名 */
        public static String ENTITY_PACKAGE_NAME = "entity";

        /** エンティティ基底クラスのパッケージ名 */
        public static String ENTITY_BASE_PACKAGE_NAME = "entity";

        /** {@link JdbcManager}のコンポーネント名 */
        public static String JDBC_MANAGER_NAME = "jdbcManager";

        /** テンプレートを格納するディレクトリ */
        public static File TEMPLATE_DIR = ResourceUtil
                .getResourceAsFile("templates");

        /** 生成Javaファイル出力ディレクトリ */
        public static File DEST_DIR = new File("src/main/java");

        /** テンプレートファイルのエンコーディング */
        public static String TEMPLATE_FILE_ENCODING = "UTF-8";

        /** Javaファイルのエンコーディング */
        public static String JAVA_FILE_ENCODING = "UTF-8";

        /** バージョン用カラムの名前 */
        public static String VERSION_COLUMN_NAME = "version";

        /** エンティティクラスのテンプレート名 */
        public static String ENTITY_TEMPLATE_NAME = "entity.ftl";

        /** エンティティ基底クラスのテンプレート名 */
        public static String ENTITY_BASE_TEMPLATE_NAME = "entityBase.ftl";

        /** diconファイル */
        public static String DICON_FILE = "s2jdbc.dicon";

        /** スキーマ名 */
        public static String SCHEMA_NAME = null;

        /** テーブル名のパターン */
        public static String TABLE_NAME_PATTERN = ".*";

    }
}
