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

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.GenCommand;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.util.ResourceUtil;

import static org.seasar.extension.jdbc.gen.command.JpaEntityGenCommand.Default.*;

/**
 * JPA用エンティティのJavaファイルを生成する{@link GenCommand}の実装クラスです。
 * 
 * @author taedium
 */
public class JpaEntityGenCommand extends AbstractEntityGenCommand {

    /** データソース名 */
    protected String dataSourceName;

    /** 方言名 */
    protected String dialectName;

    /**
     * インスタンスを構築します。
     */
    public JpaEntityGenCommand() {
    }

    /**
     * データソース名を設定します。
     * 
     * @param dataSourceName
     *            データソース名
     */
    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * 
     * @param dialectName
     *            方言名
     */
    public void setDialectName(String dialectName) {
        this.dialectName = dialectName;
    }

    /**
     * デフォルト値を設定します。
     */
    @Override
    protected void preInit() {
        super.preInit();

        if (diconFile == null) {
            diconFile = DICON_FILE;
        }
        if (dataSourceName == null) {
            dataSourceName = DATA_SOURCE_NAME;
        }
        if (dialectName == null) {
            dialectName = DIALECT_NAME;
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
     * 初期化します。
     */
    @Override
    protected void init() {
        super.init();

        dataSource = SingletonS2Container.getComponent(dataSourceName);
        persistenceConvention = new PersistenceConventionImpl();
        dialect = GenDialectManager.getGenDialect(dialectName);
    }

    /**
     * {@link JpaEntityGenCommand}のプロパティのデフォルト値を表します。
     * 
     * @author taedium
     */
    public static class Default {

        /** エンティティ基底クラス名のプレフィックス */
        public static String ENTITY_BASE_CLASS_NAME_PREFIX = "Abstract";

        /** エンティティパッケージ名 */
        public static String ENTITY_PACKAGE_NAME = "entity";

        /** エンティティ基底クラスのパッケージ名 */
        public static String ENTITY_BASE_PACKAGE_NAME = "entity";

        /** {@link DataSource}のコンポーネント名 */
        public static String DATA_SOURCE_NAME = "dataSource";

        /** {@link GenDialect}の名前 */
        public static String DIALECT_NAME = "standard";

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
        public static String ENTITY_TEMPLATE_NAME = "jpa-entity.ftl";

        /** エンティティ基底クラスのテンプレート名 */
        public static String ENTITY_BASE_TEMPLATE_NAME = "jpa-entityBase.ftl";

        /** diconファイル */
        public static String DICON_FILE = "jdbc.dicon";

        /** スキーマ名 */
        public static String SCHEMA_NAME = null;

        /** テーブル名のパターン */
        public static String TABLE_NAME_PATTERN = ".*";

    }
}
