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
package org.seasar.extension.jdbc.gen.task;

import java.io.File;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.command.LoadDataCommand;

/**
 * ダンプファイルをロードする{@link Task}の実装クラスです。
 * 
 * @author taedium
 * @see LoadDataCommand
 */
public class LoadDataTask extends AbstractTask {

    /** コマンド */
    protected LoadDataCommand command = new LoadDataCommand();

    @Override
    protected Command getCommand() {
        return command;
    }

    /**
     * 設定ファイルのパスを設定します。
     * 
     * @param configPath
     *            設定ファイルのパス
     */
    public void setConfigPath(String configPath) {
        command.setConfigPath(configPath);
    }

    /**
     * 環境名を設定します。
     * 
     * @param env
     *            環境名
     */
    public void setEnv(String env) {
        command.setEnv(env);
    }

    /**
     * {@link JdbcManager}のコンポーネント名を設定します。
     * 
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        command.setJdbcManagerName(jdbcManagerName);
    }

    /**
     * {@link Factory}の実装クラス名を設定します。
     * 
     * @param factoryClassName
     *            {@link Factory}の実装クラス名
     */
    public void setFactoryClassName(String factoryClassName) {
        command.setFactoryClassName(factoryClassName);
    }

    /**
     * クラスパスのディレクトリを設定します。
     * 
     * @param classpathDir
     *            クラスパスのディレクトリ
     */
    public void setClasspathDir(File classpathDir) {
        command.setClasspathDir(classpathDir);
    }

    /**
     * ダンプファイルのディレクトリを返します。
     * 
     * @param dumpDir
     *            ダンプファイルのディレクトリ
     */
    public void setDumpDir(File dumpDir) {
        command.setDumpDir(dumpDir);
    }

    /**
     * ダンプファイルのエンコーディングを設定します。
     * 
     * @param dumpFileEncoding
     *            ダンプファイルのエンコーディング
     */
    public void setDumpFileEncoding(String dumpFileEncoding) {
        command.setDumpFileEncoding(dumpFileEncoding);
    }

    /**
     * 対象とするエンティティクラス名の正規表現を設定します。
     * 
     * @param entityClassNamePattern
     *            対象とするエンティティクラス名の正規表現
     */
    public void setEntityClassNamePattern(String entityClassNamePattern) {
        command.setEntityClassNamePattern(entityClassNamePattern);
    }

    /**
     * エンティティクラスのパッケージ名を設定します。
     * 
     * @param entityPackageName
     *            エンティティクラスのパッケージ名
     */
    public void setEntityPackageName(String entityPackageName) {
        command.setEntityPackageName(entityPackageName);
    }

    /**
     * 対象としないエンティティクラス名の正規表現を設定します。
     * 
     * @param ignoreEntityClassNamePattern
     *            対象としないエンティティクラス名の正規表現
     */
    public void setIgnoreEntityClassNamePattern(
            String ignoreEntityClassNamePattern) {
        command.setIgnoreEntityClassNamePattern(ignoreEntityClassNamePattern);
    }

    /**
     * ルートパッケージ名を設定します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     */
    public void setRootPackageName(String rootPackageName) {
        command.setRootPackageName(rootPackageName);
    }

    /**
     * データをロードする際のバッチサイズを設定します。
     * 
     * @param loadBatchSize
     *            データをロードする際のバッチサイズ
     */
    public void setLoadBatchSize(int loadBatchSize) {
        command.setLoadBatchSize(loadBatchSize);
    }

    /**
     * トランザクション内で実行する場合{@code true}、そうでない場合{@code false}を設定します。
     * 
     * @param transactional
     *            トランザクション内で実行する場合{@code true}、そうでない場合{@code false}
     */
    public void setTransactional(boolean transactional) {
        command.setTransactional(transactional);
    }

    /**
     * {@link GenDialect}の実装クラス名を設定します。
     * 
     * @param genDialectClassName
     *            {@link GenDialect}の実装クラス名
     */
    public void setGenDialectClassName(String genDialectClassName) {
        command.setGenDialectClassName(genDialectClassName);
    }

    /**
     * DDL情報ファイルを設定します。
     * 
     * @param ddlInfoFile
     *            DDL情報ファイル
     */
    public void setDdlInfoFile(File ddlInfoFile) {
        command.setDdlInfoFile(ddlInfoFile);
    }

    /**
     * ダンプディレクトリ名を設定します。
     * 
     * @param dumpDirName
     *            ダンプディレクトリ名
     */
    public void setDumpDirName(String dumpDirName) {
        command.setDumpDirName(dumpDirName);
    }

    /**
     * マイグレーションのディレクトリを設定します。
     * 
     * @param migrateDir
     *            マイグレーションのディレクトリ
     */
    public void setMigrateDir(File migrateDir) {
        command.setMigrateDir(migrateDir);
    }

    /**
     * バージョン番号のパターンを設定します。
     * 
     * @param versionNoPattern
     *            バージョン番号のパターン
     */
    public void setVersionNoPattern(String versionNoPattern) {
        command.setVersionNoPattern(versionNoPattern);
    }

    /**
     * 環境名をバージョンに適用する場合{@code true}を設定します。
     * 
     * @param applyEnvToVersion
     *            環境名をバージョンに適用する場合{@code true}
     */
    public void setApplyEnvToVersion(boolean applyEnvToVersion) {
        command.setApplyEnvToVersion(applyEnvToVersion);
    }

    /**
     * ロードの前に存在するデータを削除する場合{@code true}、削除しない場合{@code false}を設定します。
     * 
     * @param delete
     *            ロードの前に存在するデータを削除する場合{@code true}、削除しない場合{@code false}
     */
    public void setDelete(boolean delete) {
        command.setDelete(delete);
    }

}
