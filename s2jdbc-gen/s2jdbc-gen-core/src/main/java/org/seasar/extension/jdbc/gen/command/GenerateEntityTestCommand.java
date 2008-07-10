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

    /** 条件クラス名のサフィックス */
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

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** テンプレートファイルを格納するディレクトリ */
    protected File templateFileDir = null;

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    protected S2ContainerFactorySupport containerFactorySupport;

    /** 方言 */
    protected GenDialect dialect;

    protected EntityMetaFactory entityMetaFactory;

    protected EntityMetaReader entityMetaReader;

    protected EntityTestModelFactory entityTestModelFactory;

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
        return null;
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
    protected void generate(List<EntityMeta> entityMetaList) {
        for (EntityMeta entityMeta : entityMetaList) {
            generateCondition(entityMeta);
        }
    }

    protected void generateCondition(EntityMeta entityMeta) {
        String packageName = ClassUtil.concatName(rootPackageName,
                entityPackageName);
        String shortClassName = entityMeta.getName()
                + entityTestClassNameSuffix;
        String className = ClassUtil.concatName(packageName, shortClassName);
        EntityTestModel model = entityTestModelFactory.getEntityTestModel(
                entityMeta, className);
        GenerationContext context = createGenerationContext(model, className,
                entityTestTemplateFileName, true);
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

        File dir = new File(javaTestFileDestDir, packageName.replace('.',
                File.separatorChar));
        File file = new File(dir, shortClassName + ".java");

        return new GenerationContext(model, dir, file, templateName,
                javaFileEncoding, overwrite);
    }
}
