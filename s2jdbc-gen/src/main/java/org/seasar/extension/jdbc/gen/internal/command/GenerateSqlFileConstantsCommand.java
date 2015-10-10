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
import java.util.HashSet;
import java.util.Set;

import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.generator.Generator;
import org.seasar.extension.jdbc.gen.internal.exception.RequiredPropertyNullRuntimeException;
import org.seasar.extension.jdbc.gen.internal.model.SqlFileConstantNamingRuleImpl;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.internal.util.ReflectUtil;
import org.seasar.extension.jdbc.gen.model.ClassModel;
import org.seasar.extension.jdbc.gen.model.SqlFileConstantNamingRule;
import org.seasar.extension.jdbc.gen.model.SqlFileConstantsModel;
import org.seasar.extension.jdbc.gen.model.SqlFileConstantsModelFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassUtil;

/**
 * SQLファイルの定数クラスのJavaファイルを生成する{@link Command}の実装です。
 * 
 * @author taedium
 */
public class GenerateSqlFileConstantsCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(GenerateSqlFileConstantsCommand.class);

    /** クラスパスのディレクトリ */
    protected File classpathDir;

    /** SQLファイルのセット */
    protected Set<File> sqlFileSet = new HashSet<File>();

    /** ルートパッケージ名 */
    protected String rootPackageName = "";

    /** サブパッケージ名 */
    protected String subPackageName = "";

    /** 生成する定数クラスの単純名 */
    protected String shortClassName = "SqlFiles";

    /** テストクラスのテンプレート名 */
    protected String templateFileName = "java/sqlfileconstants.ftl";

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルを格納するプライマリディレクトリ */
    protected File templateFilePrimaryDir = null;

    /** 生成するJavaファイルの出力先ディレクトリ */
    protected File javaFileDestDir = new File(new File("src", "main"), "java");

    /** Javaファイルのエンコーディング */
    protected String javaFileEncoding = "UTF-8";

    /** 上書きをする場合{@code true}、しない場合{@code false} */
    protected boolean overwrite = true;

    /** {@link SqlFileConstantNamingRule}の実装クラス名 */
    protected String sqlFileConstantNamingRuleClassName = SqlFileConstantNamingRuleImpl.class
            .getName();

    /** SQLファイルのパスを表す定数の名前付けルール */
    protected SqlFileConstantNamingRule sqlFileConstantNamingRule;

    /** SQLファイル定数モデルのファクトリ */
    protected SqlFileConstantsModelFactory sqlFileConstantsModelFactory;

    /** ジェネレータ */
    protected Generator generator;

    /**
     *サブパッケージ名を返します。
     * 
     * @return サブパッケージ名
     */
    public String getSubPackageName() {
        return subPackageName;
    }

    /**
     * サブパッケージ名を設定します。
     * 
     * @param subPackageName
     *            サブパッケージ名
     */
    public void setSubPackageName(String subPackageName) {
        this.subPackageName = subPackageName;
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
     * SQLファイルのセットを返します。
     * 
     * @return SQLファイルのセット
     */
    public Set<File> getSqlFileSet() {
        return sqlFileSet;
    }

    /**
     * SQLファイルのセットを設定します。
     * 
     * @param sqlFileSet
     *            SQLファイルのセット
     */
    public void setSqlFileSet(Set<File> sqlFileSet) {
        this.sqlFileSet = sqlFileSet;
    }

    /**
     * 生成する定数クラスの単純名を返します。
     * 
     * @return 生成する定数クラスの単純名
     */
    public String getShortClassName() {
        return shortClassName;
    }

    /**
     * 生成する定数クラスの単純名を設定します。
     * 
     * @param shortClassName
     *            生成する定数クラスの単純名
     */
    public void setShortClassName(String shortClassName) {
        this.shortClassName = shortClassName;
    }

    /**
     * {@link SqlFileConstantNamingRule}の実装クラス名を返します。
     * 
     * @return {@link SqlFileConstantNamingRule}の実装クラス名
     */
    public String getSqlFileConstantNamingRuleClassName() {
        return sqlFileConstantNamingRuleClassName;
    }

    /**
     * {@link SqlFileConstantNamingRule}の実装クラス名を設定します。
     * 
     * @param sqlFileConstantNamingRuleClassName
     *            {@link SqlFileConstantNamingRule}の実装クラス名
     */
    public void setSqlFileConstantNamingRuleClassName(
            String sqlFileConstantNamingRuleClassName) {
        this.sqlFileConstantNamingRuleClassName = sqlFileConstantNamingRuleClassName;
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
        sqlFileConstantNamingRule = ReflectUtil.newInstance(
                SqlFileConstantNamingRule.class,
                sqlFileConstantNamingRuleClassName);
        sqlFileConstantsModelFactory = createSqlFileConstantsModelFactory();
        generator = createGenerator();
    }

    @Override
    protected void doExecute() {
        SqlFileConstantsModel model = sqlFileConstantsModelFactory
                .getSqlFileConstantsModel();
        GenerationContext context = createGenerationContext(model,
                templateFileName);
        generator.generate(context);
    }

    @Override
    protected void doDestroy() {
    }

    /**
     * {@link SqlFileConstantsModelFactory}の実装を作成します。
     * 
     * @return {@link SqlFileConstantsModelFactory}の実装
     */
    protected SqlFileConstantsModelFactory createSqlFileConstantsModelFactory() {
        return factory.createSqlFileConstantsModelFactory(this, classpathDir,
                sqlFileSet, sqlFileConstantNamingRule, ClassUtil.concatName(
                        rootPackageName, subPackageName), shortClassName);
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
