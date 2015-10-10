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
 * DDLのバージョンに対応するディレクトリです。
 * 
 * @author taedium
 */
public interface DdlVersionDirectory extends ManagedFile {

    /**
     * createディレクトリを返します。
     * 
     * @return createディレクトリ
     */
    ManagedFile getCreateDirectory();

    /**
     * dropディレクトリを返します。
     * 
     * @return dropディレクトリ
     */
    ManagedFile getDropDirectory();

    /**
     * バージョン番号を返します。
     * 
     * @return バージョン番号
     */
    int getVersionNo();

    /**
     * DDLの最初のバージョンである場合{@code true}を返します。
     * 
     * @return DDLの最初のバージョンである場合{@code true}
     */
    boolean isFirstVersion();

}
