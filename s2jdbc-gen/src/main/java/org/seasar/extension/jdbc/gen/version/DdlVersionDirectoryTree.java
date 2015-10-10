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
package org.seasar.extension.jdbc.gen.version;

/**
 * DDLのバージョンを管理するディレクトリツリーです。
 * 
 * @author taedium
 */
public interface DdlVersionDirectoryTree {

    /**
     * 現バージョンに対応するディレクトリを返します。
     * 
     * @return 現バージョンに対応するディレクトリ
     */
    DdlVersionDirectory getCurrentVersionDirectory();

    /**
     * 次バージョンに対応するディレクトリを返します。
     * 
     * @return 次バージョンに対応するディレクトリ
     */
    DdlVersionDirectory getNextVersionDirectory();

    /**
     * 指定されたバージョン番号に対応するディレクトリを返します。
     * 
     * @param versionNo
     *            バージョン番号
     * @return バージョン番号に対応するディレクトリ
     */
    DdlVersionDirectory getVersionDirectory(int versionNo);

    /**
     * DDL情報ファイルを返します。
     * 
     * @return DDL情報ファイル
     */
    DdlInfoFile getDdlInfoFile();
}
