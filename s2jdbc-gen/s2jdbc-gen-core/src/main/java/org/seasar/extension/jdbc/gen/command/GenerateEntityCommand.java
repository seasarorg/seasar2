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
import org.seasar.extension.jdbc.gen.generator.GeneratorImpl;
import org.seasar.extension.jdbc.gen.meta.DbTableMetaReaderImpl;
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
public class GenerateEntityCommand extends AbstractCommand {

    /** {@link JdbcManager}のコンポーネントを含むdiconファイル */
    protected String configPath = "s2jdbc.dicon";

    /** エンティティパッケージ名 */
    protected String entityPackageName = "entity";

    /** エンティティクラスのテンプレート名 */
    protected String entityTemplateName = "java/entity.ftl";

    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File javaFileDestDir = new File("src/main/java");

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding = "UTF-8";

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName = "jdbcManager";

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** スキーマ名 */
    protected String schemaName;

    /** テンプレートファイルを格納するディレクトリ */
    protected File templateFileDir = null;

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** Javaコードの生成の対象とするテーブル名の正規表現 */
    protected String tableNamePattern = ".*";

    /** バージョンカラムの名前 */
    protected String versionColumnName = "version";

    protected S2ContainerFactorySupport containerFactorySupport;

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

    /**
     * インスタンスを構築します。
     */
    public GenerateEntityCommand() {
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    public String getEntityPackageName() {
        return entityPackageName;
    }

    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    public String getEntityTemplateName() {
        return entityTemplateName;
    }

    public void setEntityTemplateName(String entityTemplateName) {
        this.entityTemplateName = entityTemplateName;
    }

    public File getJavaFileDestDir() {
        return javaFileDestDir;
    }

    public void setJavaFileDestDir(File javaFileDestDir) {
        this.javaFileDestDir = javaFileDestDir;
    }

    public String getJavaFileEncoding() {
        return javaFileEncoding;
    }

    public void setJavaFileEncoding(String javaFileEncoding) {
        this.javaFileEncoding = javaFileEncoding;
    }

    public String getJdbcManagerName() {
        return jdbcManagerName;
    }

    public void setJdbcManagerName(String jdbcManagerName) {
        this.jdbcManagerName = jdbcManagerName;
    }

    public String getRootPackageName() {
        return rootPackageName;
    }

    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public File getTemplateFileDir() {
        return templateFileDir;
    }

    public void setTemplateFileDir(File templateFileDir) {
        this.templateFileDir = templateFileDir;
    }

    public String getTemplateFileEncoding() {
        return templateFileEncoding;
    }

    public void setTemplateFileEncoding(String templateFileEncoding) {
        this.templateFileEncoding = templateFileEncoding;
    }

    public String getTableNamePattern() {
        return tableNamePattern;
    }

    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    public String getVersionColumnName() {
        return versionColumnName;
    }

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
        containerFactorySupport = new S2ContainerFactorySupport(configPath);
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
     * {@link Generator}の実装を作成します。
     * 
     * @return {@link Generator}の実装
     */
    protected Generator createGenerator() {
        return new GeneratorImpl(templateFileEncoding, templateFileDir);
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
        EntityModel model = entityModelFactory.getEntityModel(entityDesc,
                className);
        GenerationContext context = createGenerationContext(model, className,
                entityTemplateName, false);
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

        File dir = new File(javaFileDestDir, packageName.replace('.',
                File.separatorChar));
        File file = new File(dir, shortClassName + ".java");

        return new GenerationContext(model, dir, file, templateName,
                javaFileEncoding, overwrite);
    }

}
