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
package org.seasar.extension.jdbc.gen.event;

import java.util.EventListener;

import org.seasar.extension.jdbc.gen.internal.command.GenerateDdlCommand;

/**
 * {@link GenerateDdlCommand}がバージョンディレクトリやファイルが生成されたイベントを受け取るためのリスナーインタフェースです。
 * 
 * @author koichik
 */
public interface GenerateDdlEventListener extends EventListener {

    /**
     * 次 (実際の生成対象) のバージョンディレクトリが作成される前に呼び出されます。
     * 
     * @param event
     *            イベント
     */
    void preCreateNextVersionDir(GenerateDdlEvent event);

    /**
     * 次 (実際の生成先) のバージョンディレクトリが作成された後に呼び出されます。
     * 
     * @param event
     *            イベント
     */
    void postCreateNextVersionDir(GenerateDdlEvent event);

    /**
     * 次 (実際の生成先) のバージョンディレクトリが削除される前に呼び出されます。
     * 
     * @param event
     *            イベント
     */
    void preRemoveNextVersionDir(GenerateDdlEvent event);

    /**
     * 次 (実際の生成先) のバージョンディレクトリが削除された後に呼び出されます。
     * 
     * @param event
     *            イベント
     */
    void postRemoveNextVersionDir(GenerateDdlEvent event);

    /**
     * 生成対象のファイルまたはディレクトリが作成される前に呼び出されます。
     * 
     * @param event
     *            イベント
     */
    void preCreateTargetFile(GenerateDdlEvent event);

    /**
     * 生成対象のファイルまたはディレクトリが作成された後に呼び出されます。
     * 
     * @param event
     *            イベント
     */
    void postCreateTargetFile(GenerateDdlEvent event);

}
