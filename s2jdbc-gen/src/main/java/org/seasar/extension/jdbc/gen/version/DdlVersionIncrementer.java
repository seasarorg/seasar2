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
 * DDLのバージョンを増分するインタフェースです。
 * 
 * @author taedium
 */
public interface DdlVersionIncrementer {

    /**
     * バージョンを増分します。
     * 
     * @param comment
     *            バージョンを増分する理由を示すコメント
     * @param callback
     *            コールバック
     */
    void increment(String comment, Callback callback);

    /**
     * コールバックのインタフェースです。
     * 
     * @author taedium
     */
    interface Callback {

        /**
         * 実行します。
         * 
         * @param ddlVersionDirectory
         *            DDLのバージョンディレクトリ
         */
        void execute(DdlVersionDirectory ddlVersionDirectory);
    }

}
