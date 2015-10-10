/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.command;

import java.io.File;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.generator.Generator;
import org.seasar.extension.jdbc.gen.internal.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReader;
import org.seasar.extension.jdbc.gen.model.ClassModel;
import org.seasar.extension.jdbc.gen.model.ServiceTestModel;
import org.seasar.extension.jdbc.gen.model.ServiceTestModelFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;

/**
 * サービスに対するテストクラスのJavaファイルを生成する{@link Command}の実装です。
 * <p>
 * このコマンドは、エンティティクラスのメタデータからテストクラスのJavaファイルを生成します。 そのため、
 * コマンドを実行するにはエンティティクラスを参照できるようにエンティティクラスが格納されたディレクトリをあらかじめクラスパスに設定しておく必要があります。
 * また、そのディレクトリは、プロパティ{@link #classpathDir}に設定しておく必要があります。
 * </p>
 * <p>
 * このコマンドは、サービスクラス１つにつき１つのテストクラスのJavaファイルを生成します。
 * </p>
 * 
 * @author taedium
 */
public class GenerateServiceTestCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(GenerateServiceTestCommand.class);

    /** アプリケーション用の設定ファイルのパス */
    protected String appConfigPath = "app.dicon";

    /** クラスパスのディレクトリ */
    protected File classpathDir;

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** エンティティパッケージ名 */
    protected String entityPackageName = "entity";

    /** 対象とするエンティティクラス名の正規表現 */
    protected String entityClassNamePattern = ".*";

    /** 対象としないエンティティクラス名の正規表現 */
    protected String ignoreEntityClassNamePattern = "";

    /** サービスクラスのパッケージ名 */
    protected String servicePackageName = "service";

    /** サービスクラス名のサフィックス */
    protected String serviceClassNameSuffix = "Service";

    /** テストクラス名のサフィックス */
    protected String testClassNameSuffix = "Test";

    /** テストクラスでS2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false} */
    protected boolean useS2junit4;

    /** テストクラスのテンプレート名 */
    protected String templateFileName = "java/servicetest.ftl";

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルを格納するプライマリディレクトリ */
    protected File templateFilePrimaryDir = null;

    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File javaFileDestDir = new File(new File("src", "test"), "java");

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding = "UTF-8";

    /** 上書きをする場合{@code true}、しない場合{@code false} */
    protected boolean overwrite = false;

    /** エンティティメタデータのリーダ */
    protected EntityMetaReader entityMetaReader;

    /** サービステストモデルのファクトリ */
    protected ServiceTestModelFactory serviceTestModelFactory;

    /** ジェネレータ */
    protected Generator generator;

    /**
     * アプリケーション用の設定ファイルのパスを返します。
     * 
     * @return アプリケーション用の設定ファイルのパス
     */
    public String getAppConfigPath() {
        return appConfigPath;
    }

    /**
     * アプリケーション用の設定ファイルのパスを設定します。
     * 
     * @param appConfigPath
     *            アプリケーション用の設定ファイルのパス
     */
    public void setAppConfigPath(String appConfigPath) {
        this.appConfigPath = appConfigPath;
    }

    /**
     * サービスクラス名のサフィックスを返します。
     * 
     * @return サービスクラス名のサフィックス
     */
    public String getServiceClassNameSuffix() {
        return serviceClassNameSuffix;
    }

    /**
     * サービスクラス名のサフィックスを設定します。
     * 
     * @param serviceClassNameSuffix
     *            サービスクラス名のサフィックス
     */
    public void setServiceClassNameSuffix(String serviceClassNameSuffix) {
        this.serviceClassNameSuffix = serviceClassNameSuffix;
    }

    /**
     * サービスクラスのパッケージ名を返します。
     * 
     * @return サービスクラスのパッケージ名
     */
    public String getServicePackageName() {
        return servicePackageName;
    }

    /**
     * サービスクラスのパッケージ名を設定します。
     * 
     * @param servicePackageName
     *            サービスクラスのパッケージ名
     */
    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }

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
     * 対象とするエンティティクラス名の正規表現を返します。
     * 
     * @return 対象とするエンティティクラス名の正規表現
     */
    public String getEntityClassNamePattern() {
        return entityClassNamePattern;
    }

    /**
     * 対象とするエンティティクラス名の正規表現を設定します。
     * 
     * @param entityClassNamePattern
     *            対象とするエンティティクラス名の正規表現
     */
    public void setEntityClassNamePattern(String entityClassNamePattern) {
        this.entityClassNamePattern = entityClassNamePattern;
    }

    /**
     * 対象としないエンティティクラス名の正規表現を返します。
     * 
     * @return 対象としないエンティティクラス名の正規表現
     */
    public String getIgnoreEntityClassNamePattern() {
        return ignoreEntityClassNamePattern;
    }

    /**
     * 対象としないエンティティクラス名の正規表現を設定します。
     * 
     * @param ignoreEntityClassNamePattern
     *            対象としないエンティティクラス名の正規表現
     */
    public void setIgnoreEntityClassNamePattern(
            String ignoreEntityClassNamePattern) {
        this.ignoreEntityClassNamePattern = ignoreEntityClassNamePattern;
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
     * テストクラスでS2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false}を返します。
     * 
     * @return テストクラスでS2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false}
     */
    public boolean isUseS2junit4() {
        return useS2junit4;
    }

    /**
     * テストクラスでS2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false}を設定します。
     * 
     * @param useS2junit4
     *            テストクラスでS2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false}
     */
    public void setUseS2junit4(boolean useS2junit4) {
        this.useS2junit4 = useS2junit4;
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
        entityMetaReader = createEntityMetaReader();
        serviceTestModelFactory = createServiceTestModelFactory();
        generator = createGenerator();
    }

    @Override
    protected void doExecute() {
        for (EntityMeta entityMeta : entityMetaReader.read()) {
            generateTest(entityMeta);
        }
    }

    @Override
    protected void doDestroy() {
    }

    /**
     * テストクラスのJavaファイルを生成します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void generateTest(EntityMeta entityMeta) {
        ServiceTestModel serviceTestModel = serviceTestModelFactory
                .getServiceTestModel(entityMeta);
        GenerationContext context = createGenerationContext(serviceTestModel,
                templateFileName);
        generator.generate(context);
    }

    /**
     * {@link EntityMetaReader}の実装を作成します。
     * 
     * @return {@link EntityMetaReader}の実装
     */
    protected EntityMetaReader createEntityMetaReader() {
        return factory.createEntityMetaReader(this, classpathDir, ClassUtil
                .concatName(rootPackageName, entityPackageName), jdbcManager
                .getEntityMetaFactory(), entityClassNamePattern,
                ignoreEntityClassNamePattern, false, null, null);
    }

    /**
     * {@link ServiceTestModelFactory}の実装を作成します。
     * 
     * @return {@link ServiceTestModelFactory}の実装
     */
    protected ServiceTestModelFactory createServiceTestModelFactory() {
        return factory.createServiceTestModelFactory(this, appConfigPath,
                ClassUtil.concatName(rootPackageName, servicePackageName),
                serviceClassNameSuffix, testClassNameSuffix, useS2junit4);
    }

    /**
     * {@link Generator}の実装を作成します。
     * 
     * @return {@link Generator}の実装
     */
    protected Generator createGenerator() {
        return factory.createGenerator(this, templateFileEncoding,
                templateFilePrimaryDir);
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
    protected GenerationContext createGenerationContext(ClassModel model,
            String templateName) {
        File file = FileUtil.createJavaFile(javaFileDestDir, model
                .getPackageName(), model.getShortClassName());
        return factory.createGenerationContext(this, model, file, templateName,
                javaFileEncoding, overwrite);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
