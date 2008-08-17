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

/**
 * DDL情報を管理するファイルを表すインタフェースです。
 * 
 * @author taedium
 */
public interface DdlInfoFile {

    /**
     * 現在のバージョン番号を返します。
     * 
     * @return バージョン番号
     */
    int getCurrentVersionNo();

    /**
     * 次のバージョン番号を返します。
     * 
     * @return 次のバージョン番号
     */
    int getNextVersionNo();

    /**
     * 文字列に対応するバージョン番号を返します。
     * 
     * @param version
     *            文字列で表されたバージョン
     * @return バージョン番号
     */
    int getVersionNo(String version);

    /**
     * 次のバージョン番号を適用します。
     */
    void applyNextVersionNo();

}
