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
import org.seasar.extension.jdbc.gen.EntityModelConverter;
import org.seasar.extension.jdbc.gen.GenCommand;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.JavaCode;
import org.seasar.extension.jdbc.gen.JavaFileGenerator;
import org.seasar.extension.jdbc.gen.PropertyModelConverter;
import org.seasar.extension.jdbc.gen.SchemaReader;
import org.seasar.extension.jdbc.gen.converter.EntityModelConverterImpl;
import org.seasar.extension.jdbc.gen.converter.PropertyModelConverterImpl;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.generator.JavaFileGeneratorImpl;
import org.seasar.extension.jdbc.gen.javacode.EntityBaseJavaCode;
import org.seasar.extension.jdbc.gen.javacode.EntityJavaCode;
import org.seasar.extension.jdbc.gen.model.DbTableDesc;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.reader.SchemaReaderImpl;
import org.seasar.extension.jdbc.gen.util.ConfigurationUtil;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.convention.NamingConvention;
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

    /** ルートパッケージ名 */
    protected String rootPackageName;

    /** エンティティパッケージ名 */
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
            generate(convert(read()));
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
        S2Container container = SingletonS2ContainerFactory.getContainer();
        JdbcManagerImplementor jdbcManager = (JdbcManagerImplementor) container
                .getComponent(jdbcManagerName);
        dataSource = jdbcManager.getDataSource();
        persistenceConvention = jdbcManager.getPersistenceConvention();
        dialect = GenDialectManager.getDialect(jdbcManager.getDialect());
        if (container.hasComponentDef(NamingConvention.class)) {
            NamingConvention nc = (NamingConvention) container
                    .getComponent(NamingConvention.class);
            if (ROOT_PACKAGE_NAME.equals(rootPackageName)) {
                if (nc.getRootPackageNames().length > 0) {
                    rootPackageName = nc.getRootPackageNames()[0];
                }
            }
            if (ENTITY_PACKAGE_NAME.equals(entityPackageName)) {
                entityPackageName = nc.getEntityPackageName();
            }
            if (ENTITY_BASE_PACKAGE_NAME.equals(entityBasePackageName)) {
                entityBasePackageName = nc.getEntityPackageName();
            }
        }
        if (logger.isDebugEnabled()) {
            logProperties();
        }
    }

    /**
     * デフォルト値を設定します。
     */
    protected void setupDefaults() {
        if (diconFile == null) {
            diconFile = DICON_FILE;
        }
        if (jdbcManagerName == null) {
            jdbcManagerName = Default.JDBC_MANAGER_NAME;
        }
        if (rootPackageName == null) {
            rootPackageName = ROOT_PACKAGE_NAME;
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
            if (!propertyDesc.hasWriteMethod()) {
                return;
            }
            Field f = propertyDesc.getField();
            logger.log("DS2JDBCGen0001", new Object[] { f.getName(),
                    FieldUtil.get(f, this) });
        }
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
    protected List<DbTableDesc> read() {
        SchemaReader reader = createSchemaReader();
        return reader.read(schemaName, tableNamePattern);
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
     * テーブル記述のリストをエンティティモデルのリストに変換します。
     * 
     * @param tableDescs
     *            テーブル記述のリスト
     * @return エンティティモデルのリスト
     */
    protected List<EntityModel> convert(List<DbTableDesc> tableDescs) {
        List<EntityModel> entityModels = new ArrayList<EntityModel>();
        EntityModelConverter entityConverter = createEntityModelConverter();
        for (DbTableDesc tableDesc : tableDescs) {
            EntityModel entityModel = entityConverter.convert(tableDesc);
            entityModels.add(entityModel);
        }
        return entityModels;
    }

    /**
     * {@link EntityModelConverter}の実装を作成します。
     * 
     * @return {@link EntityModelConverter}の実装
     */
    protected EntityModelConverter createEntityModelConverter() {
        return new EntityModelConverterImpl(persistenceConvention,
                createPropertyModelConverter());
    }

    /**
     * {@link PropertyModelConverter}の実装を作成します。
     * 
     * @return {@link PropertyModelConverter}の実装
     */
    protected PropertyModelConverter createPropertyModelConverter() {
        return new PropertyModelConverterImpl(persistenceConvention, dialect,
                versionColumnName);
    }

    /**
     * エンティティのJavaファイルを生成します。
     * 
     * @param entityModels
     *            エンティティモデルのリスト
     */
    protected void generate(List<EntityModel> entityModels) {
        JavaFileGenerator generator = createJavaFileGenerator();
        for (EntityModel entityModel : entityModels) {
            JavaCode entityCode = createEntityJavaCode(entityModel);
            if (!exists(entityCode, destDir)) {
                generator.generate(entityCode);
            }
            JavaCode entityBaseCode = createEntityBaseJavaCode(entityModel);
            generator.generate(entityBaseCode);
        }
    }

    /**
     * {@link JavaFileGenerator}の実装を作成します。
     * 
     * @return {@link JavaFileGenerator}の実装
     */
    protected JavaFileGenerator createJavaFileGenerator() {
        Configuration cfg = new Configuration();
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setEncoding(Locale.getDefault(), templateFileEncoding);
        ConfigurationUtil.setDirectoryForTemplateLoading(cfg, templateDir);
        return new JavaFileGeneratorImpl(cfg, destDir, javaFileEncoding);
    }

    /**
     * エンティティクラスのJavaコードを表す{@link JavaCode}の実装を作成します。
     * 
     * @param entityModel
     *            エンティティモデル
     * @return {@link JavaCode}の実装
     */
    protected JavaCode createEntityJavaCode(EntityModel entityModel) {
        String entityClassName = getEntityClassName(entityModel.getName());
        String entityBaseClassName = getEntityBaseClassName(entityModel
                .getName());
        return new EntityJavaCode(entityModel, entityClassName,
                entityBaseClassName, entityTemplateName);
    }

    /**
     * エンティティ基底クラスのJavaコードを表す{@link JavaCode}の実装を作成します。
     * 
     * @param entityModel
     *            エンティティモデル
     * @return {@link JavaCode}の実装
     */
    protected JavaCode createEntityBaseJavaCode(EntityModel entityModel) {
        String entityBaseClassName = getEntityBaseClassName(entityModel
                .getName());
        return new EntityBaseJavaCode(entityModel, entityBaseClassName,
                entityBaseTemplateName);
    }

    /**
     * エンティティクラス名を返します。
     * 
     * @param entityName
     *            エンティティ名
     * @return エンティティクラス名
     */
    protected String getEntityClassName(String entityName) {
        return getClassName(rootPackageName, entityPackageName, entityName);
    }

    /**
     * エンティティ基底クラス名を返します。
     * 
     * @param entityName
     *            エンティティ名
     * @return エンティティ基底クラス名
     */
    protected String getEntityBaseClassName(String entityName) {
        return getClassName(rootPackageName, entityBasePackageName,
                entityBaseClassNamePrefix + entityName);
    }

    /**
     * クラス名を返します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     * @param subPackageName
     *            サブパッケージ名
     * @param entityName
     *            エンティティ名
     * @return クラス名
     */
    protected String getClassName(String rootPackageName,
            String subPackageName, String entityName) {
        String fullPackageName = ClassUtil.concatName(rootPackageName,
                subPackageName);
        return ClassUtil.concatName(fullPackageName, entityName);
    }

    /**
     * {@code javaCode}がすでに存在する場合に{@code true}を返します。
     * 
     * @param javaCode
     *            Javaコード
     * @param baseDir
     *            基盤となるディレクトリ
     * @return すでに存在する場合に{@code true}、そうでない場合{@code false}
     */
    protected boolean exists(JavaCode javaCode, File baseDir) {
        return javaCode.getFile(baseDir).exists();
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
