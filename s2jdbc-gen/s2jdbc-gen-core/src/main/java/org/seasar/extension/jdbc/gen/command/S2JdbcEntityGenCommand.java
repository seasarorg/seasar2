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
import java.util.List;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.EntityConditionBaseModelFactory;
import org.seasar.extension.jdbc.gen.EntityConditionModelFactory;
import org.seasar.extension.jdbc.gen.GenCommand;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.model.EntityConditionBaseModel;
import org.seasar.extension.jdbc.gen.model.EntityConditionModel;
import org.seasar.extension.jdbc.gen.model.factory.EntityConditionBaseModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.factory.EntityConditionModelFactoryImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;

import static org.seasar.extension.jdbc.gen.command.S2JdbcEntityGenCommand.Default.*;

/**
 * S2JDBC用エンティティのJavaファイルを生成する{@link GenCommand}の実装クラスです。
 * 
 * @author taedium
 */
public class S2JdbcEntityGenCommand extends AbstractEntityGenCommand {

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName;

    /** エンティティ条件クラスのパッケージ名 */
    protected String entityConditionPackageName;

    /** エンティティ条件基底クラスのパッケージ名 */
    protected String entityConditionBasePackageName;

    /** エンティティ条件クラス名のサフィックス */
    protected String entityConditionClassNameSuffix;

    /** エンティティ条件基底クラス名のプレフィックス */
    protected String entityConditionBaseClassNamePrefix;

    /** エンティティ条件基底クラス名のサフィックス */
    protected String entityConditionBaseClassNameSuffix;

    /** エンティティ条件クラスのテンプレート名 */
    protected String entityConditionTemplateName;

    /** エンティティ条件基底クラスのテンプレート名 */
    protected String entityConditionBaseTemplateName;

    /** 条件クラスを生成する場合{@code true} */
    protected Boolean generateCondition;

    /** {@link EntityCondition}のファクトリ */
    protected EntityConditionModelFactory entityConditionModelFactory;

    /** {@link EntityConditionBase}のファクトリ */
    protected EntityConditionBaseModelFactory entityConditionBaseModelFactory;

    /**
     * インスタンスを構築します。
     */
    public S2JdbcEntityGenCommand() {
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
     * エンティティ条件クラスのパッケージ名を設定します。
     * 
     * @param entityConditionPackageName
     *            エンティティ条件クラスのパッケージ名
     */
    public void setEntityConditionPackageName(String entityConditionPackageName) {
        this.entityConditionPackageName = entityConditionPackageName;
    }

    /**
     * エンティティ条件基底クラスのパッケージ名
     * 
     * @param entityConditionBasePackageName
     *            エンティティ条件基底クラスのパッケージ名
     */
    public void setEntityConditionBasePackageName(
            String entityConditionBasePackageName) {
        this.entityConditionBasePackageName = entityConditionBasePackageName;
    }

    /**
     * エンティティ条件クラス名のサフィックスを設定します。
     * 
     * @param entityConditionClassNameSuffix
     *            エンティティ条件クラス名のサフィックス
     */
    public void setEntityConditionClassNameSuffix(
            String entityConditionClassNameSuffix) {
        this.entityConditionClassNameSuffix = entityConditionClassNameSuffix;
    }

    /**
     * エンティティ条件基底クラス名のプレフィックスを設定します。
     * 
     * @param entityConditionBaseClassNamePrefix
     *            エンティティ条件基底クラス名のプレフィックス
     */
    public void setEntityConditionBaseClassNamePrefix(
            String entityConditionBaseClassNamePrefix) {
        this.entityConditionBaseClassNamePrefix = entityConditionBaseClassNamePrefix;
    }

    /**
     * エンティティ条件基底クラス名のサフィックスを設定します。
     * 
     * @param entityConditionBaseClassNameSuffix
     *            エンティティ条件基底クラス名のサフィックス
     */
    public void setEntityConditionBaseClassNameSuffix(
            String entityConditionBaseClassNameSuffix) {
        this.entityConditionBaseClassNameSuffix = entityConditionBaseClassNameSuffix;
    }

    /**
     * エンティティ条件クラスのテンプレート名を設定します。
     * 
     * @param entityConditionTemplateName
     *            エンティティ条件クラスのテンプレート名
     */
    public void setEntityConditionTemplateName(
            String entityConditionTemplateName) {
        this.entityConditionTemplateName = entityConditionTemplateName;
    }

    /**
     * エンティティ条件基底クラスのテンプレート名を設定します。
     * 
     * @param entityConditionBaseTemplateName
     *            エンティティ条件基底クラスのテンプレート名
     */

    public void setEntityConditionBaseTemplateName(
            String entityConditionBaseTemplateName) {
        this.entityConditionBaseTemplateName = entityConditionBaseTemplateName;
    }

    /**
     * エンティティ条件クラスを生成する場合{@code true}、生成しない場合{@code false}を設定します。
     * 
     * @param generateCondition
     *            エンティティ条件クラスを生成する場合{@code true}、生成しない場合{@code false}
     */
    public void setGenerateCondition(Boolean generateCondition) {
        this.generateCondition = generateCondition;
    }

    @Override
    protected void setupDefaultProperties() {
        super.setupDefaultProperties();

        if (diconFile == null) {
            diconFile = DICON_FILE;
        }
        if (jdbcManagerName == null) {
            jdbcManagerName = JDBC_MANAGER_NAME;
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
        if (entityConditionPackageName == null) {
            entityConditionPackageName = ENTITY_CONDITION_PACKAGE_NAME;
        }
        if (entityConditionBasePackageName == null) {
            entityConditionBasePackageName = ENTITY_CONDITION_BASE_PACKAGE_NAME;
        }
        if (entityBaseClassNamePrefix == null) {
            entityBaseClassNamePrefix = ENTITY_BASE_CLASS_NAME_PREFIX;
        }
        if (entityConditionClassNameSuffix == null) {
            entityConditionClassNameSuffix = ENTITY_CONDITION_CLASS_NAME_SUFFIX;
        }
        if (entityConditionBaseClassNamePrefix == null) {
            entityConditionBaseClassNamePrefix = ENTITY_CONDITION_BASE_CLASS_NAME_PREFIX;
        }
        if (entityConditionBaseClassNameSuffix == null) {
            entityConditionBaseClassNameSuffix = ENTITY_CONDITION_BASE_CLASS_NAME_SUFFIX;
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
        if (entityConditionTemplateName == null) {
            entityConditionTemplateName = ENTITY_CONDITION_TEMPLATE_NAME;
        }
        if (entityConditionBaseTemplateName == null) {
            entityConditionBaseTemplateName = ENTITY_CONDITION_BASE_TEMPLATE_NAME;
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
        if (generateCondition == null) {
            generateCondition = GENERATE_CONDITION;
        }
    }

    @Override
    protected void setupInternalProperties() {
        super.setupInternalProperties();

        JdbcManagerImplementor jdbcManager = SingletonS2Container
                .getComponent(jdbcManagerName);
        dataSource = jdbcManager.getDataSource();
        persistenceConvention = jdbcManager.getPersistenceConvention();
        dialect = GenDialectManager.getGenDialect(jdbcManager.getDialect());
    }

    @Override
    protected void setupCollaborators() {
        super.setupCollaborators();

        entityConditionModelFactory = createEntityConditionModelFactory();
        entityConditionBaseModelFactory = createEntityConditionBaseModelFactory();
    }

    /**
     * {@link EntityConditionModel}のファクトリを作成します。
     * 
     * @return {@link EntityConditionModel}のファクトリ
     */
    protected EntityConditionModelFactory createEntityConditionModelFactory() {
        return new EntityConditionModelFactoryImpl();
    }

    /**
     * {@link EntityConditionBaseModel}のファクトリを作成します。
     * 
     * @return {@link EntityConditionBaseModel}のファクトリ
     */
    protected EntityConditionBaseModelFactory createEntityConditionBaseModelFactory() {
        return new EntityConditionBaseModelFactoryImpl();
    }

    @Override
    protected void generate(List<EntityDesc> entityDescList) {
        super.generate(entityDescList);

        if (generateCondition) {
            for (EntityDesc entityDesc : entityDescList) {
                GenerationContext conditionCtx = getEntityConditionGenerationContext(entityDesc);
                generator.generate(conditionCtx);
                GenerationContext conditionBaseCtx = getEntityConditionBaseGenerationContext(entityDesc);
                generator.generate(conditionBaseCtx, true);
            }
        }
    }

    /**
     * エンティティ条件クラス用の{@link GenerationContext}を返します。
     * 
     * @param entityDesc
     *            エンティティ記述
     * @return エンティティ条件クラス用の{@link GenerationContext}
     */
    protected GenerationContext getEntityConditionGenerationContext(
            EntityDesc entityDesc) {
        String className = getEntityConditionClassName(entityDesc.getName());
        String baseClassName = getEntityConditionBaseClassName(entityDesc
                .getName());
        EntityConditionModel model = entityConditionModelFactory
                .getEntityConditionModel(entityDesc, className, baseClassName);

        return getGenerationContext(model, className,
                entityConditionTemplateName);
    }

    /**
     * エンティティ条件基底クラス用の{@link GenerationContext}を返します。
     * 
     * @param entityDesc
     *            エンティティ記述
     * @return エンティティ条件基底クラス用の{@link GenerationContext}
     */
    protected GenerationContext getEntityConditionBaseGenerationContext(
            EntityDesc entityDesc) {
        String className = getEntityConditionBaseClassName(entityDesc.getName());
        EntityConditionBaseModel model = entityConditionBaseModelFactory
                .getEntityConditionBaseModel(entityDesc, className);

        return getGenerationContext(model, className,
                entityConditionBaseTemplateName);
    }

    /**
     * エンティティ条件クラス名を返します。
     * 
     * @param entityName
     *            エンティティ名
     * @return エンティティ条件クラス名
     */
    protected String getEntityConditionClassName(String entityName) {
        String packageName = ClassUtil.concatName(rootPackageName,
                entityConditionPackageName);
        String shortClassName = entityName + entityConditionClassNameSuffix;
        return ClassUtil.concatName(packageName, shortClassName);
    }

    /**
     * エンティティ条件の基底クラス名を返します。
     * 
     * @param entityName
     *            エンティティ名
     * @return エンティティ条件の基底クラス名
     */
    protected String getEntityConditionBaseClassName(String entityName) {
        String packageName = ClassUtil.concatName(rootPackageName,
                entityConditionBasePackageName);
        String shortClassName = entityConditionBaseClassNamePrefix + entityName
                + entityConditionBaseClassNameSuffix;
        return ClassUtil.concatName(packageName, shortClassName);
    }

    /**
     * {@link S2JdbcEntityGenCommand}のプロパティのデフォルト値を表します。
     * 
     * @author taedium
     */
    public static class Default {

        /** エンティティ基底クラス名のプレフィックス */
        public static String ENTITY_BASE_CLASS_NAME_PREFIX = "Abstract";

        /** エンティティ条件基底クラス名のプレフィックス */
        public static String ENTITY_CONDITION_BASE_CLASS_NAME_PREFIX = "Abstract";

        /** エンティティ条件クラス名のサフィックス */
        public static String ENTITY_CONDITION_CLASS_NAME_SUFFIX = "Condition";

        /** エンティティ条件基底クラス名のサフィックス */
        public static String ENTITY_CONDITION_BASE_CLASS_NAME_SUFFIX = "Condition";

        /** ルートパッケージ名 */
        public static String ROOT_PACKAGE_NAME = "";

        /** エンティティパッケージ名 */
        public static String ENTITY_PACKAGE_NAME = "entity";

        /** エンティティ基底クラスのパッケージ名 */
        public static String ENTITY_BASE_PACKAGE_NAME = "entity";

        /** エンティティ条件クラスのパッケージ名 */
        public static String ENTITY_CONDITION_PACKAGE_NAME = "condition";

        /** エンティティ条件基底クラスのパッケージ名 */
        public static String ENTITY_CONDITION_BASE_PACKAGE_NAME = "condition";

        /** 条件クラスを生成するかどうか示すフラグ */
        public static Boolean GENERATE_CONDITION = Boolean.TRUE;

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
        public static String ENTITY_TEMPLATE_NAME = "s2jdbc-entity.ftl";

        /** エンティティ基底クラスのテンプレート名 */
        public static String ENTITY_BASE_TEMPLATE_NAME = "s2jdbc-entityBase.ftl";

        /** エンティティ条件クラスのテンプレート名 */
        public static String ENTITY_CONDITION_TEMPLATE_NAME = "s2jdbc-entityCondition.ftl";

        /** エンティティ条件基底クラスのテンプレート名 */
        public static String ENTITY_CONDITION_BASE_TEMPLATE_NAME = "s2jdbc-entityConditionBase.ftl";

        /** diconファイル */
        public static String DICON_FILE = "s2jdbc.dicon";

        /** スキーマ名 */
        public static String SCHEMA_NAME = null;

        /** テーブル名のパターン */
        public static String TABLE_NAME_PATTERN = ".*";

    }
}
