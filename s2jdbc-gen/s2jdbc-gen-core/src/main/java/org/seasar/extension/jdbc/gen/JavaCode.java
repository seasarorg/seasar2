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
package org.seasar.extension.jdbc.gen;

import java.io.File;
import java.util.Set;

/**
 * Javaのコードを表すインタフェースです。
 * 
 * @author taedium
 */
public interface JavaCode {

    /** 拡張子 */
    String EXT = ".java";

    /**
     * パッケージ名を返します。
     * 
     * @return
     */
    String getPackageName();

    /**
     * インポートパッケージ名のセットを返します。
     * 
     * @return インポートパッケージ名のセット
     */
    Set<String> getImportPackageNames();

    /**
     * クラス名を返します。
     * 
     * @return
     */
    String getClassName();

    /**
     * パッケージ名を除いたクラス名を返します。
     * 
     * @return
     */
    String getShortClassName();

    /**
     * 基底クラス名を返します。
     * 
     * @return 基底クラス名
     */
    String getBaseClassName();

    /**
     * パッケージ名を除いた基底クラス名
     * 
     * @return パッケージ名を除いた基底クラス名を返します。
     */
    String getShortBaseClassName();

    /**
     * テンプレート名を返します。
     * 
     * @return
     */
    String getTemplateName();

    /**
     * パッケージのディレクトリを返します。
     * 
     * @param baseDir
     *            基盤となるディレクトリ
     * @return パッケージのディレクトリ
     */
    File getPackageDir(File baseDir);

    /**
     * Javaファイルを返します。
     * 
     * @param baseDir
     *            基盤となるディレクトリ
     * @return Javaファイル
     */
    File getFile(File baseDir);

}
