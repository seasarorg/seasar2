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
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.generator.Generator;
import org.seasar.extension.jdbc.gen.internal.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReader;
import org.seasar.extension.jdbc.gen.model.ClassModel;
import org.seasar.extension.jdbc.gen.model.NamesAggregateModel;
import org.seasar.extension.jdbc.gen.model.NamesAggregateModelFactory;
import org.seasar.extension.jdbc.gen.model.NamesModel;
import org.seasar.extension.jdbc.gen.model.NamesModelFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;

/**
 * 名前クラスのJavaファイルを生成する{@link Command}の実装です。
 * <p>
 * このコマンドは、エンティティクラスのメタデータから名前クラスのJavaファイルを生成します。 そのため、
 * コマンドを実行するにはエンティティクラスを参照できるようにエンティティクラスが格納されたディレクトリをあらかじめクラスパスに設定しておく必要があります 。
 * また、そのディレクトリは、プロパティ{@link #classpathDir}に設定しておく必要があります。
 * </p>
 * <p>
 * このコマンドは、エンティティクラス１つにつき１つの名前クラスのJavaファイルを生成します。
 * </p>
 * 
 * @author taedium
 */
public class GenerateNamesCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(GenerateNamesCommand.class);

    /** クラスパスのディレクトリ */
    protected File classpathDir;

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** エンティティクラスのパッケージ名 */
    protected String entityPackageName = "entity";

    /** 対象とするエンティティクラス名の正規表現 */
    protected String entityClassNamePattern = ".*";

    /** 対象としないエンティティクラス名の正規表現 */
    protected String ignoreEntityClassNamePattern = "";

    /** 名前クラスのパッケージ名 */
    protected String namesPackageName = "entity";

    /** 名前クラス名のサフィックス */
    protected String namesClassNameSuffix = "Names";

    /** 名前クラスのテンプレート名 */
    protected String namesTemplateFileName = "java/names.ftl";

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルを格納したプライマリディレクトリ */
    protected File templateFilePrimaryDir = null;

    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File javaFileDestDir = new File(new File("src", "main"), "java");

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding = "UTF-8";

    /** 上書きをする場合{@code true}、しない場合{@code false} */
    protected boolean overwrite = true;

    /** 名前集約クラスを生成する場合{@code true}、しない場合{@code false} */
    protected boolean generateNamesAggregateClass = true;

    /** 名前集約クラスの単純名 */
    protected String namesAggregateShortClassName = "Names";

    /** 名前集約クラスのテンプレート名 */
    protected String namesAggregateTemplateFileName = "java/names-aggregate.ftl";

    /** エンティティメタデータのリーダ */
    protected EntityMetaReader entityMetaReader;

    /** 名前モデルのファクトリ */
    protected NamesModelFactory namesModelFactory;

    /** 名前集約モデルのファクトリ */
    protected NamesAggregateModelFactory namesAggregateModelFactory;

    /** ジェネレータ */
    protected Generator generator;

    /**
     * インスタンスを構築します。
     */
    public GenerateNamesCommand() {
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
     * 名前クラス名のサフィックスを返します。
     * 
     * @return 名前クラス名のサフィックス
     */
    public String getNamesClassNameSuffix() {
        return namesClassNameSuffix;
    }

    /**
     * 名前クラス名のサフィックスを設定します。
     * 
     * @param namesClassNameSuffix
     *            名前クラス名のサフィックス
     */
    public void setNamesClassNameSuffix(String namesClassNameSuffix) {
        this.namesClassNameSuffix = namesClassNameSuffix;
    }

    /**
     * 名前クラスのパッケージ名を返します。
     * 
     * @return 名前クラスのパッケージ名
     */
    public String getNamesPackageName() {
        return namesPackageName;
    }

    /**
     * 名前クラスのパッケージ名を設定します。
     * 
     * @param namesPackageName
     *            名前クラスのパッケージ名
     */
    public void setNamesPackageName(String namesPackageName) {
        this.namesPackageName = namesPackageName;
    }

    /**
     * 名前クラスのテンプレート名を返します。
     * 
     * @return 名前クラスのテンプレート名
     */
    public String getNamesTemplateFileName() {
        return namesTemplateFileName;
    }

    /**
     * 名前クラスのテンプレート名を設定します。
     * 
     * @param namesTemplateFileName
     *            名前クラスのテンプレート名
     */
    public void setNamesTemplateFileName(String namesTemplateFileName) {
        this.namesTemplateFileName = namesTemplateFileName;
    }

    /**
     * エンティティクラスのパッケージ名を返します。
     * 
     * @return エンティティクラスのパッケージ名
     */
    public String getEntityPackageName() {
        return entityPackageName;
    }

    /**
     * エンティティクラスのパッケージ名を設定します。
     * 
     * @param entityPackageName
     *            エンティティクラスのパッケージ名
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
     * ルートパッケージ名を返します。
     * 
     * @return ルートパッケージ名
     */
    public String getRootPackageName() {
        return rootPackageName;
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
     * テンプレートファイルを格納したプライマリディレクトリを返します。
     * 
     * @return テンプレートファイルを格納したプライマリディレクトリ
     */
    public File getTemplateFilePrimaryDir() {
        return templateFilePrimaryDir;
    }

    /**
     * テンプレートファイルを格納したプライマリディレクトリを設定します。
     * 
     * @param templateFilePrimaryDir
     *            テンプレートファイルを格納したプライマリディレクトリ
     */
    public void setTemplateFilePrimaryDir(File templateFilePrimaryDir) {
        this.templateFilePrimaryDir = templateFilePrimaryDir;
    }

    /**
     * 名前集約クラスを生成する場合{@code true}、しない場合{@code false}を返します。
     * 
     * @return 名前集約クラスを生成する場合{@code true}、しない場合{@code false}
     */
    public boolean isGenerateNamesAggregateClass() {
        return generateNamesAggregateClass;
    }

    /**
     * 名前集約クラスを生成する場合{@code true}、しない場合{@code false}を設定します。
     * 
     * @param generateNamesAggregateClass
     *            名前集約クラスを生成する場合{@code true}、しない場合{@code false}
     */
    public void setGenerateNamesAggregateClass(
            boolean generateNamesAggregateClass) {
        this.generateNamesAggregateClass = generateNamesAggregateClass;
    }

    /**
     * 名前集約クラスの単純名を返します。
     * 
     * @return 名前集約クラスの単純名
     */
    public String getNamesAggregateShortClassName() {
        return namesAggregateShortClassName;
    }

    /**
     * 名前集約クラスの単純名を設定します。
     * 
     * @param namesAggregateShortClassName
     *            名前集約クラスの単純名
     */
    public void setNamesAggregateShortClassName(
            String namesAggregateShortClassName) {
        this.namesAggregateShortClassName = namesAggregateShortClassName;
    }

    /**
     * 名前集約クラスのテンプレート名を返します。
     * 
     * @return 名前集約クラスのテンプレート名
     */
    public String getNamesAggregateTemplateFileName() {
        return namesAggregateTemplateFileName;
    }

    /**
     * 名前集約クラスのテンプレート名を設定します。
     * 
     * @param namesAggregateTemplateFileName
     *            名前集約クラスのテンプレート名
     */
    public void setNamesAggregateTemplateFileName(
            String namesAggregateTemplateFileName) {
        this.namesAggregateTemplateFileName = namesAggregateTemplateFileName;
    }

    @Override
    protected void doValidate() {
        if (classpathDir == null) {
            throw new RequiredPropertyNullRuntimeException("classpathDir");
        }
    }

    @Override
    protected void doInit() {
        entityMetaReader = createEntityMetaReader();
        namesModelFactory = createNamesModelFactory();
        namesAggregateModelFactory = createNamesAggregateModelFactory();
        generator = createGenerator();
    }

    @Override
    protected void doExecute() {
        List<NamesModel> namesModelList = new ArrayList<NamesModel>();
        for (EntityMeta entityMeta : entityMetaReader.read()) {
            NamesModel namesModel = namesModelFactory.getNamesModel(entityMeta);
            generateNames(namesModel);
            if (generateNamesAggregateClass) {
                namesModelList.add(namesModel);
            }
        }
        if (namesModelList.size() > 0) {
            NamesAggregateModel namesAggregateModel = namesAggregateModelFactory
                    .getNamesAggregateModel(namesModelList);
            generateNamesAggregate(namesAggregateModel);
        }
    }

    @Override
    protected void doDestroy() {
    }

    /**
     * 名前クラスのJavaファイルを生成します。
     * 
     * @param namesModel
     *            名前モデル
     */
    protected void generateNames(NamesModel namesModel) {
        GenerationContext context = createGenerationContext(namesModel,
                namesTemplateFileName);
        generator.generate(context);
    }

    /**
     * 名前集約クラスのJavaファイルを生成します。
     * 
     * @param namesAggregateModel
     *            名前集約モデル
     */
    protected void generateNamesAggregate(
            NamesAggregateModel namesAggregateModel) {
        GenerationContext context = createGenerationContext(
                namesAggregateModel, namesAggregateTemplateFileName);
        generator.generate(context);
    }

    /**
     * {@link GenerationContext}の実装を作成します。
     * 
     * @param model
     *            クラスモデル
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
     * {@link NamesModelFactory}の実装を作成します。
     * 
     * @return {@link NamesModelFactory}の実装
     */
    protected NamesModelFactory createNamesModelFactory() {
        return factory.createNamesModelFactory(this, ClassUtil.concatName(
                rootPackageName, namesPackageName), namesClassNameSuffix);
    }

    /**
     * {@link NamesAggregateModelFactory}の実装を作成します。
     * 
     * @return {@link NamesAggregateModelFactory}の実装
     */
    protected NamesAggregateModelFactory createNamesAggregateModelFactory() {
        return factory.createNamesAggregateModelFactory(this, ClassUtil
                .concatName(rootPackageName, namesPackageName),
                namesAggregateShortClassName);
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

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
