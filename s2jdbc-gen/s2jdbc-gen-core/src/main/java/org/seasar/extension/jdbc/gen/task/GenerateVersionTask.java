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
package org.seasar.extension.jdbc.gen.task;

import java.io.File;

import org.apache.tools.ant.Task;
import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.command.GenerateVersionCommand;

/**
 * バージョン番号を保持するテキストファイルとバージョン番号を更新するDMLファイルを生成する{@link Task}です。
 * 
 * @author taedium
 */
public class GenerateVersionTask extends AbstractTask {

    /** コマンド */
    protected GenerateVersionCommand command = new GenerateVersionCommand();

    /**
     * インスタンスを構築します。
     */
    public GenerateVersionTask() {
    }

    /**
     * スキーマのバージョンを保持するテーブルのカラム名を設定します。
     * 
     * @param schemaInfoColumnName
     *            スキーマのバージョンを保持するテーブルのカラム名
     */
    public void setSchemaInfoColumnName(String schemaInfoColumnName) {
        command.setSchemaInfoColumnName(schemaInfoColumnName);
    }

    /**
     * スキーマのバージョンを保持するテーブル名を設定します。
     * 
     * @param schemaInfoTableName
     *            スキーマのバージョンを保持するテーブル名
     */
    public void setSchemaInfoTableName(String schemaInfoTableName) {
        command.setSchemaInfoTableName(schemaInfoTableName);
    }

    /**
     * DMLファイルの出力先ディレクトリを設定します。
     * 
     * @param dmlFileDestDir
     *            DMLファイルの出力先ディレクトリ
     */
    public void setDmlFileDestDir(File dmlFileDestDir) {
        command.setDmlFileDestDir(dmlFileDestDir);
    }

    /**
     * SQLファイルのエンコーディングを設定します。
     * 
     * @param dmlFileEncoding
     *            SQLファイルのエンコーディング
     */
    public void setDmlFileEncoding(String dmlFileEncoding) {
        command.setDmlFileEncoding(dmlFileEncoding);
    }

    /**
     * SQLステートメントの区切り文字を設定します。
     * 
     * @param statementDelimiter
     *            SQLステートメントの区切り文字
     */
    public void setStatementDelimiter(char statementDelimiter) {
        command.setStatementDelimiter(statementDelimiter);
    }

    /**
     * テンプレートファイルのエンコーディングを設定します。
     * 
     * @param templateFileEncoding
     *            テンプレートファイルのエンコーディング
     */
    public void setTemplateFileEncoding(String templateFileEncoding) {
        command.setTemplateFileEncoding(templateFileEncoding);
    }

    /**
     * テンプレートファイルを格納するプライマリディレクトリを設定します。
     * 
     * @param templateFilePrimaryDir
     *            テンプレートファイルを格納するプライマリディレクトリ
     */
    public void setTemplateFilePrimaryDir(File templateFilePrimaryDir) {
        command.setTemplateFilePrimaryDir(templateFilePrimaryDir);
    }

    /**
     * バージョンを更新するDMLファイル名を設定します。
     * 
     * @param updateVersionDmlFileName
     *            バージョンを更新するDMLのSQLファイル名
     */
    public void setUpdateVersionDmlFileName(String updateVersionDmlFileName) {
        command.setUpdateVersionDmlFileName(updateVersionDmlFileName);
    }

    /**
     * バージョンを更新するDMLのテンプレートファイル名を設定します。
     * 
     * @param updateVersionTemplateFileName
     *            バージョンを更新するDMLのテンプレートファイル名
     */
    public void setUpdateVersionTemplateFileName(
            String updateVersionTemplateFileName) {
        command.setUpdateVersionTemplateFileName(updateVersionTemplateFileName);
    }

    /**
     * バージョンファイルの出力先ディレクトリを設定します。
     * 
     * @param versionFileDestDir
     *            バージョンファイルの出力先ディレクトリ
     */
    public void setVersionFileDestDir(File versionFileDestDir) {
        command.setVersionFileDestDir(versionFileDestDir);
    }

    /**
     * バージョンファイル名を設定します。
     * 
     * @param versionFileName
     *            バージョンファイル名
     */
    public void setVersionFileName(String versionFileName) {
        command.setVersionFileName(versionFileName);
    }

    /**
     * バージョンのテンプレートファイル名を設定します。
     * 
     * @param versionTemplateFileName
     *            バージョンのテンプレートファイル名
     */
    public void setVersionTemplateFileName(String versionTemplateFileName) {
        command.setVersionTemplateFileName(versionTemplateFileName);
    }

    @Override
    protected Command getCommand() {
        return command;
    }

}
