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
package org.seasar.extension.jdbc.gen.event;

import java.util.EventObject;

import org.seasar.extension.jdbc.gen.internal.command.GenerateDdlCommand;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectory;

/**
 * このイベントは、{@link GenerateDdlCommand}がバージョンディレクトリやファイルを生成または削除する前後に送信されます。
 * 
 * @author koichik
 */
public class GenDdlEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    /** 現在のバージョンディレクトリです。 */
    protected final DdlVersionDirectory currentVersionDir;

    /** 次 (実際の生成対象) のバージョンディレクトリです。 */
    protected final DdlVersionDirectory nextVersionDir;

    /**
     * 生成対象のファイルまたはディレクトリの次バージョンディレクトリから見た相対パスです。
     * <p>
     * バージョンディレクトリ生成または削除イベントの場合は{@literal null}です。
     * </p>
     */
    protected final String targetFile;

    /**
     * インスタンスを構築します。
     * 
     * @param source
     *            イベントのソースオブジェクト
     * @param currentVersionDir
     *            現在のバージョンディレクトリのパス
     * @param nextVersionDir
     *            次 (実際の生成対象) のバージョンディレクトリのパス
     */
    public GenDdlEvent(final Object source,
            final DdlVersionDirectory currentVersionDir,
            final DdlVersionDirectory nextVersionDir) {
        super(source);
        this.currentVersionDir = currentVersionDir;
        this.nextVersionDir = nextVersionDir;
        this.targetFile = null;
    }

    /**
     * インスタンスを構築します。
     * 
     * @param source
     *            イベントのソースオブジェクト
     * @param currentVersionDir
     *            現在のバージョンディレクトリのパス
     * @param nextVersionDir
     *            次 (実際の生成対象) のバージョンディレクトリのパス
     * @param targetFile
     *            生成対象のファイルまたはディレクトリの次バージョンディレクトリから見た相対パス
     */
    public GenDdlEvent(final Object source,
            final DdlVersionDirectory currentVersionDir,
            final DdlVersionDirectory nextVersionDir, final String targetFile) {
        super(source);
        this.currentVersionDir = currentVersionDir;
        this.nextVersionDir = nextVersionDir;
        this.targetFile = targetFile;
    }

    /**
     * 一つ前のバージョンディレクトリのパスを返します。
     * 
     * @return 一つ前のバージョンディレクトリのパス
     */
    public DdlVersionDirectory getCurrentVersionDir() {
        return currentVersionDir;
    }

    /**
     * 現在のバージョンディレクトリのパスを返します。
     * 
     * @return 現在のバージョンディレクトリのパス
     */
    public DdlVersionDirectory getNextVersionDir() {
        return nextVersionDir;
    }

    /**
     * 生成対象のファイルまたはディレクトリの次バージョンディレクトリから見た相対パスを返します。
     * 
     * @return 生成対象のファイルまたはディレクトリの次バージョンディレクトリから見た相対パス
     */
    public String getTargetFile() {
        return targetFile;
    }

}
