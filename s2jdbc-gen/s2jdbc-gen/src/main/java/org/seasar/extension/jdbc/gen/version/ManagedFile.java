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
package org.seasar.extension.jdbc.gen.version;

import java.io.File;
import java.util.List;

/**
 * バージョン管理されたファイルです。
 * 
 * @author taedium
 */
public interface ManagedFile {

    /**
     * {@link File}として返します。
     * 
     * @return {@link File}
     */
    File asFile();

    /**
     * バージョンディレクトリからの相対パスとして返します。
     * 
     * @return バージョンディレクトリからの相対パス
     */
    String asRelativePath();

    /**
     * このインスタンスがディレクトリを表す場合、ディレクトリ階層下に含まれる全ファイルを返します。
     * <p>
     * 返されるリストにはファイルのみが含まれ、ディレクトリは含まれません。
     * </p>
     * 
     * @return ファイルのリスト
     */
    List<File> listAllFiles();

    /**
     * このインスタンスの子となるバージョン管理されたファイルを作成します。
     * 
     * @param relativePath
     *            このインスタンスが表すファイルからの相対パス
     * @return バージョン管理されたファイル
     */
    ManagedFile createChild(String relativePath);
}