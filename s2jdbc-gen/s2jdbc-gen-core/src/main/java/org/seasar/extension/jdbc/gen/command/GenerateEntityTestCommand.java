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

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.EntityMetaReader;
import org.seasar.extension.jdbc.gen.EntityTestModel;
import org.seasar.extension.jdbc.gen.EntityTestModelFactory;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.Generator;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.generator.GeneratorImpl;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReaderImpl;
import org.seasar.extension.jdbc.gen.model.EntityTestModelFactoryImpl;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class GenerateEntityTestCommand extends AbstractCommand {

    protected File classpathRootDir;

    /** {@link JdbcManager}のコンポーネントを含むdiconファイル */
    protected String configPath = "s2jdbc.dicon";

    /** エンティティテストクラス名のサフィックス */
    protected String entityTestClassNameSuffix = "Test";

    /** エンティティパッケージ名 */
    protected String entityPackageName = "entity";

    /** エンティティクラスのテンプレート名 */
    protected String entityTestTemplateFileName = "java/entity-test.ftl";

    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File javaTestFileDestDir = new File("src/test/java");

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding = "UTF-8";

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName = "jdbcManager";

    protected boolean overwrite = false;

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルを格納するディレクトリ */
    protected File templateFileSecondaryDir = null;

    protected S2ContainerFactorySupport containerFactorySupport;

    /** 方言 */
    protected GenDialect dialect;

    protected EntityMetaFactory entityMetaFactory;

    protected EntityMetaReader entityMetaReader;

    protected EntityTestModelFactory entityTestModelFactory;

    /**
     * @return Returns the entityTestClassNameSuffix.
     */
    public String getEntityTestClassNameSuffix() {
        return entityTestClassNameSuffix;
    }

    /**
     * @param entityTestClassNameSuffix
     *            The entityTestClassNameSuffix to set.
     */
    public void setEntityTestClassNameSuffix(String entityTestClassNameSuffix) {
        this.entityTestClassNameSuffix = entityTestClassNameSuffix;
    }

    /**
     * @return Returns the entityPackageName.
     */
    public String getEntityPackageName() {
        return entityPackageName;
    }

    /**
     * @param entityPackageName
     *            The entityPackageName to set.
     */
    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    /**
     * @return Returns the entityTestTemplateFileName.
     */
    public String getEntityTestTemplateFileName() {
        return entityTestTemplateFileName;
    }

    /**
     * @param entityTestTemplateFileName
     *            The entityTestTemplateFileName to set.
     */
    public void setEntityTestTemplateFileName(String entityTestTemplateFileName) {
        this.entityTestTemplateFileName = entityTestTemplateFileName;
    }

    /**
     * @return Returns the javaTestFileDestDir.
     */
    public File getJavaTestFileDestDir() {
        return javaTestFileDestDir;
    }

    /**
     * @param javaTestFileDestDir
     *            The javaTestFileDestDir to set.
     */
    public void setJavaTestFileDestDir(File javaTestFileDestDir) {
        this.javaTestFileDestDir = javaTestFileDestDir;
    }

    /**
     * @return Returns the javaFileEncoding.
     */
    public String getJavaFileEncoding() {
        return javaFileEncoding;
    }

    /**
     * @param javaFileEncoding
     *            The javaFileEncoding to set.
     */
    public void setJavaFileEncoding(String javaFileEncoding) {
        this.javaFileEncoding = javaFileEncoding;
    }

    /**
     * @return Returns the jdbcManagerName.
     */
    public String getJdbcManagerName() {
        return jdbcManagerName;
    }

    /**
     * @param jdbcManagerName
     *            The jdbcManagerName to set.
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        this.jdbcManagerName = jdbcManagerName;
    }

    /**
     * @return Returns the overwrite.
     */
    public boolean isOverwrite() {
        return overwrite;
    }

    /**
     * @param overwrite
     *            The overwrite to set.
     */
    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    /**
     * @return Returns the rootPackageName.
     */
    public String getRootPackageName() {
        return rootPackageName;
    }

    /**
     * @param rootPackageName
     *            The rootPackageName to set.
     */
    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    /**
     * @return Returns the templateFileEncoding.
     */
    public String getTemplateFileEncoding() {
        return templateFileEncoding;
    }

    /**
     * @param templateFileEncoding
     *            The templateFileEncoding to set.
     */
    public void setTemplateFileEncoding(String templateFileEncoding) {
        this.templateFileEncoding = templateFileEncoding;
    }

    /**
     * @return Returns the templateFileDir.
     */
    public File getTemplateFileSecondaryDir() {
        return templateFileSecondaryDir;
    }

    /**
     * @param templateFileDir
     *            The templateFileDir to set.
     */
    public void setTemplateFileSecondaryDir(File templateFileDir) {
        this.templateFileSecondaryDir = templateFileDir;
    }

    /**
     * @return Returns the classpathRootDir.
     */
    public File getClasspathRootDir() {
        return classpathRootDir;
    }

    /**
     * @return Returns the configPath.
     */
    public String getConfigPath() {
        return configPath;
    }

    /** ジェネレータ */
    protected Generator generator;

    /**
     * @param configPath
     *            The configPath to set.
     */
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    /**
     * @param classpathRootDir
     *            The classpathRootDir to set.
     */
    public void setClasspathRootDir(File classpathRootDir) {
        this.classpathRootDir = classpathRootDir;
    }

    @Override
    protected void doValidate() {
        if (classpathRootDir == null) {
            throw new RequiredPropertyNullRuntimeException("classpathRootDir");
        }
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
        entityMetaFactory = jdbcManager.getEntityMetaFactory();
        dialect = GenDialectManager.getGenDialect(jdbcManager.getDialect());

        entityMetaReader = createEntityMetaReader();
        entityTestModelFactory = createEntityTestModelFactory();
        generator = createGenerator();
    }

    @Override
    protected void doExecute() {
        List<EntityMeta> entityMetaList = entityMetaReader.read();
        generate(entityMetaList);
    }

    @Override
    protected void doDestroy() {
        if (containerFactorySupport != null) {
            containerFactorySupport.destory();
        }
    }

    protected EntityMetaReader createEntityMetaReader() {
        return new EntityMetaReaderImpl(classpathRootDir, ClassUtil.concatName(
                rootPackageName, entityPackageName), entityMetaFactory);
    }

    protected EntityTestModelFactory createEntityTestModelFactory() {
        String packageName = ClassUtil.concatName(rootPackageName,
                entityPackageName);
        return new EntityTestModelFactoryImpl(configPath, jdbcManagerName,
                packageName, entityTestClassNameSuffix);
    }

    /**
     * {@link Generator}の実装を作成します。
     * 
     * @return {@link Generator}の実装
     */
    protected Generator createGenerator() {
        return new GeneratorImpl(templateFileEncoding, templateFileSecondaryDir);
    }

    /**
     * エンティティクラスのJavaファイルを生成します。
     * 
     * @param entityDescList
     *            エンティティ記述のリスト
     */
    protected void generate(List<EntityMeta> entityMetaList) {
        for (EntityMeta entityMeta : entityMetaList) {
            generateEntityTest(entityMeta);
        }
    }

    protected void generateEntityTest(EntityMeta entityMeta) {
        EntityTestModel entityTestModel = entityTestModelFactory
                .getEntityTestModel(entityMeta);
        GenerationContext context = createGenerationContext(entityTestModel,
                entityTestTemplateFileName);
        generator.generate(context);
    }

    /**
     * {@link GenerationContext}を返します。
     * 
     * @param model
     *            モデル
     * @param templateName
     *            テンプレート名
     * @return {@link GenerationContext}
     */
    protected GenerationContext createGenerationContext(EntityTestModel model,
            String templateName) {
        String packageName = model.getPackageName();
        String shortClassName = model.getShortClassName();

        File dir = new File(javaTestFileDestDir, packageName.replace('.',
                File.separatorChar));
        File file = new File(dir, shortClassName + ".java");

        return new GenerationContext(model, dir, file, templateName,
                javaFileEncoding, overwrite);
    }
}
