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

import java.io.File;
import java.io.FilenameFilter;
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
     * バージョンディレクトリからの相対パスを返します。
     * 
     * @return バージョンディレクトリからの相対パス
     */
    String getRelativePath();

    /**
     * 親の{@link ManagedFile}を返します。
     * 
     * @return 親の{@link ManagedFile}
     */
    ManagedFile getParent();

    /**
     * 名前を返します。
     * 
     * @return 名前
     */
    String getName();

    /**
     * ファイルまたはディレクトリが存在するかどうかを判定します。
     * 
     * @return ファイルまたはディレクトリが存在する場合は{@code true}、そうでない場合は{@code false}
     */
    boolean exists();

    /**
     * ファイルまたはディレクトリを削除します。
     * 
     * @return ファイルまたはディレクトリが正常に削除された場合は{@code true}、そうでない場合は{@code false}
     */
    boolean delete();

    /**
     * ディレクトリを生成します。
     * 
     * @return ディレクトリが生成された場合は{@code true}、そうでない場合は{@code false}
     */
    boolean mkdir();

    /**
     * ディレクトリを生成します。存在していないが必要な親ディレクトリも一緒に作成されます。このオペレーションが失敗した場合でも、
     * いくつかの必要な親ディレクトリの生成には成功している場合があります。
     * 
     * @return 必要なすべての親ディレクトリを含めてディレクトリが生成された場合は{@code true}、そうでない場合は{@code
     *         false}
     */
    boolean mkdirs();

    /**
     * 空の新しいファイルを不可分 (atomic) に生成します。
     * 
     * @return 指定されたファイルが存在せず、ファイルの生成に成功した場合は{@code true}、示されたファイルがすでに存在する場合は
     *         {@code false}
     */
    boolean createNewFile();

    /**
     * ディレクトリであるかどうかを判定します。
     * 
     * @return 存在しディレクトリである場合は{@code true}、そうでない場合は{@code false}
     */
    boolean isDirectory();

    /**
     * ディレクトリ内の{@link ManagedFile}のリストを返します。
     * 
     * @return {@link ManagedFile}のリスト
     */
    List<ManagedFile> listManagedFiles();

    /**
     * ディレクトリ内の{@link ManagedFile}のリストを返します。
     * 
     * @param filter
     *            ファイル名フィルタ
     * @return {@link ManagedFile}のリスト
     */
    List<ManagedFile> listManagedFiles(FilenameFilter filter);

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
     * @param childName
     *            子となるファイルまたはディレクトリの名前
     * @return バージョン管理されたファイル
     */
    ManagedFile createChild(String childName);

    /**
     * 子ディレクトリもしくは子ファイルを持っている場合{@code true}を返します。
     * 
     * @return 子ディレクトリもしくは子ファイルを持っている場合{@code true}、そうでない場合{@code false}
     */
    boolean hasChild();

}