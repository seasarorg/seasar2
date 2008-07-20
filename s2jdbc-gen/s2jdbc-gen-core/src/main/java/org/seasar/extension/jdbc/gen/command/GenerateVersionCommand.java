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
import java.util.HashMap;
import java.util.Map;

import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.Generator;
import org.seasar.extension.jdbc.gen.exception.IllegalVersionValueRuntimeException;
import org.seasar.extension.jdbc.gen.generator.GenerationContextImpl;
import org.seasar.extension.jdbc.gen.generator.GeneratorImpl;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.TextUtil;

/**
 * バージョン番号を保持するテキストファイルとバージョン番号を更新するDMLファイルを生成する{@link Command}の実装です。
 * <p>
 * このコマンドは、バージョン番号を保持したテキストファイルから値を読み、それをインクリメントします。
 * また、新しいバージョン番号でデータベースのバージョン管理の用のテーブルを更新するUPDATE文を持つSQLファイルを生成します。
 * このコマンドを実行しても、データベースに対する更新は行われません。
 * </p>
 * 
 * @author taedium
 */
public class GenerateVersionCommand extends AbstractCommand {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(GenerateVersionCommand.class);

    /** スキーマのバージョンを保持するテーブルのカラム名 */
    protected String schemaInfoColumnName = "VERSION";

    /** スキーマのバージョンを保持するテーブル名 */
    protected String schemaInfoTableName = "SCHEMA_INFO";

    /** DMLファイルの出力先ディレクトリ */
    protected File dmlFileDestDir = new File("db/dml");

    /** DMLファイルのエンコーディング */
    protected String dmlFileEncoding = "UTF-8";

    /** SQLステートメントの区切り文字 */
    protected char statementDelimiter = ';';

    /** テンプレートファイルのエンコーディング */
    protected String templateFileEncoding = "UTF-8";

    /** テンプレートファイルの格納ディレクトリ */
    protected File templateFilePrimaryDir = null;

    /** バージョンファイルの出力先ディレクトリ */
    protected File versionFileDestDir = new File("db");

    /** バージョンファイル名 */
    protected String versionFileName = "version.txt";

    /** バージョンのテンプレートファイル名 */
    protected String versionTemplateFileName = "txt/version.ftl";

    /** バージョンを更新するDMLファイル名 */
    protected String updateVersionDmlFileName = "update-version.sql";

    /** バージョンを更新するDMLのテンプレートファイル名 */
    protected String updateVersionTemplateFileName = "sql/update-version.ftl";

    /** ジェネレータ */
    protected Generator generator;

    /**
     * インスタンスを構築します。
     */
    public GenerateVersionCommand() {
    }

    /**
     * スキーマのバージョンを保持するテーブルのカラム名を返します。
     * 
     * @return スキーマのバージョンを保持するテーブルのカラム名
     */
    public String getSchemaInfoColumnName() {
        return schemaInfoColumnName;
    }

    /**
     * スキーマのバージョンを保持するテーブルのカラム名を設定します。
     * 
     * @param schemaInfoColumnName
     *            スキーマのバージョンを保持するテーブルのカラム名
     */
    public void setSchemaInfoColumnName(String schemaInfoColumnName) {
        this.schemaInfoColumnName = schemaInfoColumnName;
    }

    /**
     * スキーマのバージョンを保持するテーブル名を返します。
     * 
     * @return スキーマのバージョンを保持するテーブル名
     */
    public String getSchemaInfoTableName() {
        return schemaInfoTableName;
    }

    /**
     * スキーマのバージョンを保持するテーブル名を設定します。
     * 
     * @param schemaInfoTableName
     *            スキーマのバージョンを保持するテーブル名
     */
    public void setSchemaInfoTableName(String schemaInfoTableName) {
        this.schemaInfoTableName = schemaInfoTableName;
    }

    /**
     * DMLファイルの出力先ディレクトリを返します。
     * 
     * @return DMLファイルの出力先ディレクトリ
     */
    public File getDmlFileDestDir() {
        return dmlFileDestDir;
    }

    /**
     * DMLファイルの出力先ディレクトリを設定します。
     * 
     * @param dmlFileDestDir
     *            DMLファイルの出力先ディレクトリ
     */
    public void setDmlFileDestDir(File dmlFileDestDir) {
        this.dmlFileDestDir = dmlFileDestDir;
    }

    /**
     * DMLファイルの出力先ディレクトリを返します。
     * 
     * @return DMLファイルの出力先ディレクトリ
     */
    public String getDmlFileEncoding() {
        return dmlFileEncoding;
    }

    /**
     * SQLファイルのエンコーディングを設定します。
     * 
     * @param dmlFileEncoding
     *            SQLファイルのエンコーディング
     */
    public void setDmlFileEncoding(String dmlFileEncoding) {
        this.dmlFileEncoding = dmlFileEncoding;
    }

    /**
     * SQLステートメントの区切り文字を返します。
     * 
     * @return SQLステートメントの区切り文字
     */
    public char getStatementDelimiter() {
        return statementDelimiter;
    }

    /**
     * SQLステートメントの区切り文字を設定します。
     * 
     * @param statementDelimiter
     *            SQLステートメントの区切り文字
     */
    public void setStatementDelimiter(char statementDelimiter) {
        this.statementDelimiter = statementDelimiter;
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
     * バージョンファイルの出力先ディレクトリを返します。
     * 
     * @return バージョンファイルの出力先ディレクトリ
     */
    public File getVersionFileDestDir() {
        return versionFileDestDir;
    }

    /**
     * バージョンファイルの出力先ディレクトリを設定します。
     * 
     * @param versionFileDestDir
     *            バージョンファイルの出力先ディレクトリ
     */
    public void setVersionFileDestDir(File versionFileDestDir) {
        this.versionFileDestDir = versionFileDestDir;
    }

    /**
     * バージョンファイル名を返します。
     * 
     * @return バージョンファイル名
     */
    public String getVersionFileName() {
        return versionFileName;
    }

    /**
     * バージョンファイル名を設定します。
     * 
     * @param versionFileName
     *            バージョンファイル名
     */
    public void setVersionFileName(String versionFileName) {
        this.versionFileName = versionFileName;
    }

    /**
     * バージョンのテンプレートファイル名を返します。
     * 
     * @return バージョンのテンプレートファイル名
     */
    public String getVersionTemplateFileName() {
        return versionTemplateFileName;
    }

    /**
     * バージョンのテンプレートファイル名を設定します。
     * 
     * @param versionTemplateFileName
     *            バージョンのテンプレートファイル名
     */
    public void setVersionTemplateFileName(String versionTemplateFileName) {
        this.versionTemplateFileName = versionTemplateFileName;
    }

    /**
     * バージョンを更新するDMLのSQLファイル名を返します。
     * 
     * @return バージョンを更新するDMLファイル名
     */
    public String getUpdateVersionDmlFileName() {
        return updateVersionDmlFileName;
    }

    /**
     * バージョンを更新するDMLファイル名を設定します。
     * 
     * @param updateVersionDmlFileName
     *            バージョンを更新するDMLのSQLファイル名
     */
    public void setUpdateVersionDmlFileName(String updateVersionDmlFileName) {
        this.updateVersionDmlFileName = updateVersionDmlFileName;
    }

    /**
     * バージョンを更新するDMLのテンプレートファイル名を返します。
     * 
     * @return バージョンを更新するDMLのテンプレートファイル名
     */
    public String getUpdateVersionTemplateFileName() {
        return updateVersionTemplateFileName;
    }

    /**
     * バージョンを更新するDMLのテンプレートファイル名を設定します。
     * 
     * @param updateVersionTemplateFileName
     *            バージョンを更新するDMLのテンプレートファイル名
     */
    public void setUpdateVersionTemplateFileName(
            String updateVersionTemplateFileName) {
        this.updateVersionTemplateFileName = updateVersionTemplateFileName;
    }

    @Override
    protected void doValidate() {
    }

    @Override
    protected void doInit() {
        generator = createGenerator();
    }

    @Override
    protected void doExecute() {
        int currentVersion = readVersion();
        int nextVersion = currentVersion + 1;
        generateUpdateVersionSql(nextVersion);
        generateVersion(nextVersion);
        logger.log("DS2JDBCGen0004", new Object[] { currentVersion,
                nextVersion, updateVersionDmlFileName });
    }

    @Override
    protected void doDestroy() {
    }

    /**
     * バージョン番号を読み取ります。
     * 
     * @return バージョン番号
     */
    protected int readVersion() {
        File versionFile = new File(versionFileDestDir, versionFileName);
        if (!versionFile.exists()) {
            return 0;
        }
        String value = TextUtil.readUTF8(versionFile);
        if (StringUtil.isNumber(value)) {
            return Integer.valueOf(value.trim());
        }
        throw new IllegalVersionValueRuntimeException(versionFileName, value);
    }

    /**
     * バージョン番号を更新するSQLを生成します。
     * 
     * @param version
     *            バージョン番号
     */
    protected void generateUpdateVersionSql(int version) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("version", version);
        model.put("schemaInfoTableName", schemaInfoTableName);
        model.put("schemaInfoColumnName", schemaInfoColumnName);
        model.put("delimiter", statementDelimiter);
        GenerationContext context = new GenerationContextImpl(model,
                dmlFileDestDir, new File(dmlFileDestDir,
                        updateVersionDmlFileName),
                updateVersionTemplateFileName, dmlFileEncoding, true);
        generator.generate(context);
    }

    /**
     * バージョン番号を含むテキストファイルを生成します。
     * 
     * @param version
     *            バージョン番号
     */
    protected void generateVersion(int version) {
        Map<String, Integer> model = new HashMap<String, Integer>();
        model.put("version", version);
        GenerationContext context = new GenerationContextImpl(model,
                dmlFileDestDir, new File(versionFileDestDir, versionFileName),
                versionTemplateFileName, "UTF-8", true);
        generator.generate(context);
    }

    /**
     * {@link Generator}の実装を作成します。
     * 
     * @return {@link Generator}の実装
     */
    protected Generator createGenerator() {
        return new GeneratorImpl(templateFileEncoding, templateFilePrimaryDir);
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }
}
