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
import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.EntityMetaReader;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.Generator;
import org.seasar.extension.jdbc.gen.TestModel;
import org.seasar.extension.jdbc.gen.TestModelFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.generator.GenerationContextImpl;
import org.seasar.extension.jdbc.gen.generator.GeneratorImpl;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReaderImpl;
import org.seasar.extension.jdbc.gen.model.TestModelFactoryImpl;
import org.seasar.extension.jdbc.gen.util.SingletonS2ContainerFactorySupport;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;

/**
 * テストクラスのJavaファイルを生成する{@link Command}の実装です。
 * <p>
 * このコマンドは、エンティティクラスのメタデータからテストクラスのJavaファイルを生成します。 そのため、
 * コマンドを実行するにはエンティティクラスを参照できるようにエンティティクラスが格納されたディレクトリをあらかじめクラスパスに設定しておく必要があります。
 * また、そのディレクトリは、プロパティ{@link #classpathDir}に設定しておく必要があります。
 * </p>
 * <p>
 * このコマンドは、エンティティクラス１つにつき１つのテストクラスを生成します。
 * </p>
 * 
 * @author taedium
 */
public class GenerateTestCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(GenerateTestCommand.class);

    /** クラスパスのディレクトリ */
    protected File classpathDir;

    /** {@link JdbcManager}のコンポーネントを含むdiconファイル */
    protected String configPath = "s2jdbc.dicon";

    /** エンティティパッケージ名 */
    protected String entityPackageName = "entity";

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding = "UTF-8";

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName = "jdbcManager";

    /** 上書きをする場合{@code true}、しない場合{@code false} */
    protected boolean overwrite = false;

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルを格納するプライマリディレクトリ */
    protected File templateFilePrimaryDir = null;

    /** テストクラス名のサフィックス */
    protected String testClassNameSuffix = "Test";

    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File javaFileDestDir = new File("src/test/java");

    /** テストクラスのテンプレート名 */
    protected String templateFileName = "java/test.ftl";

    /** {@link SingletonS2ContainerFactory}のサポート */
    protected SingletonS2ContainerFactorySupport containerFactorySupport;

    /** 方言 */
    protected GenDialect dialect;

    /** エンティティメタデータのファクトリ */
    protected EntityMetaFactory entityMetaFactory;

    /** エンティティメタデータのリーダ */
    protected EntityMetaReader entityMetaReader;

    /** 環境名 */
    protected String env = "ut";

    /** テストのモデルのファクトリ */
    protected TestModelFactory testModelFactory;

    /** ジェネレータ */
    protected Generator generator;

    /**
     * テストクラス名のサフィックスを返します。
     * 
     * @return テストクラス名のサフィックス
     */
    public String getTestClassNameSuffix() {
        return testClassNameSuffix;
    }

    /**
     * テストクラス名のサフィックスを設定します。
     * 
     * @param testClassNameSuffix
     *            テストクラス名のサフィックス
     */
    public void setTestClassNameSuffix(String testClassNameSuffix) {
        this.testClassNameSuffix = testClassNameSuffix;
    }

    /**
     * エンティティのパッケージ名を返します。
     * 
     * @return エンティティのパッケージ名
     */
    public String getEntityPackageName() {
        return entityPackageName;
    }

    /**
     * エンティティのパッケージ名を設定します。
     * 
     * @param entityPackageName
     *            エンティティのパッケージ名
     */
    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    /**
     * 環境名を返します。
     * 
     * @return 環境名
     */
    public String getEnv() {
        return env;
    }

    /**
     * 環境名を設定します。
     * 
     * @param env
     *            環境名
     */
    public void setEnv(String env) {
        this.env = env;
    }

    /**
     * テストクラスのテンプレート名を返します。
     * 
     * @return テストクラスのテンプレート名
     */
    public String getTemplateFileName() {
        return templateFileName;
    }

    /**
     * テストクラスのテンプレート名を設定します。
     * 
     * @param templateFileName
     *            テストクラスのテンプレート名
     */
    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
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
     * {@link JdbcManager}のコンポーネント名を返します。
     * 
     * @return {@link JdbcManager}のコンポーネント名
     */
    public String getJdbcManagerName() {
        return jdbcManagerName;
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
     * ルートパッケージ名を設定します。
     * 
     * @return ルートパッケージ名
     */
    public String getRootPackageName() {
        return rootPackageName;
    }

    /**
     * ルートパッケージ名を返します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     */
    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
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
     * クラスパスのディレクトリを返します。
     * 
     * @return クラスパスのディレクトリ
     */
    public File getClasspathDir() {
        return classpathDir;
    }

    /**
     * クラスパスのディレクトリを設定します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     */
    public void setClasspathDir(File classpathDir) {
        this.classpathDir = classpathDir;
    }

    /**
     * 設定ファイルのパスを返します。
     * 
     * @return 設定ファイルのパス
     */
    public String getConfigPath() {
        return configPath;
    }

    /**
     * 設定ファイルのパスを設定します。
     * 
     * @param configPath
     *            設定ファイルのパス
     */
    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    @Override
    protected void doValidate() {
        if (classpathDir == null) {
            throw new RequiredPropertyNullRuntimeException("classpathDir");
        }
    }

    /**
     * 初期化します。
     */
    @Override
    protected void doInit() {
        containerFactorySupport = new SingletonS2ContainerFactorySupport(
                configPath, env);
        containerFactorySupport.init();

        JdbcManagerImplementor jdbcManager = SingletonS2Container
                .getComponent(jdbcManagerName);
        entityMetaFactory = jdbcManager.getEntityMetaFactory();
        dialect = GenDialectManager.getGenDialect(jdbcManager.getDialect());

        entityMetaReader = createEntityMetaReader();
        testModelFactory = createTestModelFactory();
        generator = createGenerator();

        logger.log("DS2JDBCGen0005", new Object[] { dialect.getClass()
                .getName() });
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

    /**
     * {@link EntityMetaReader}の実装を作成します。
     * 
     * @return {@link EntityMetaReader}の実装
     */
    protected EntityMetaReader createEntityMetaReader() {
        return new EntityMetaReaderImpl(classpathDir, ClassUtil.concatName(
                rootPackageName, entityPackageName), entityMetaFactory);
    }

    /**
     * {@link TestModelFactory}の実装を作成します。
     * 
     * @return {@link TestModelFactory}の実装
     */
    protected TestModelFactory createTestModelFactory() {
        return new TestModelFactoryImpl(configPath, jdbcManagerName,
                testClassNameSuffix);
    }

    /**
     * {@link Generator}の実装を作成します。
     * 
     * @return {@link Generator}の実装
     */
    protected Generator createGenerator() {
        return new GeneratorImpl(templateFileEncoding, templateFilePrimaryDir);
    }

    /**
     * 生成します。
     * 
     * @param entityMetaList
     *            エンティティメタデータのリスト
     */
    protected void generate(List<EntityMeta> entityMetaList) {
        for (EntityMeta entityMeta : entityMetaList) {
            generateEntityTest(entityMeta);
        }
    }

    /**
     * テストクラスのJavaファイルを生成します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void generateEntityTest(EntityMeta entityMeta) {
        TestModel testModel = testModelFactory.getEntityTestModel(entityMeta);
        GenerationContext context = createGenerationContext(testModel,
                templateFileName);
        generator.generate(context);
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
    protected GenerationContext createGenerationContext(TestModel model,
            String templateName) {
        String packageName = model.getPackageName();
        String shortClassName = model.getShortClassName();

        File dir = new File(javaFileDestDir, packageName.replace('.',
                File.separatorChar));
        File file = new File(dir, shortClassName + ".java");

        return new GenerationContextImpl(model, dir, file, templateName,
                javaFileEncoding, overwrite);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
