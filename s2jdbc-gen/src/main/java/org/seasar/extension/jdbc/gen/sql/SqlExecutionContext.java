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
package org.seasar.extension.jdbc.gen.sql;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * SQLの実行コンテキストです。
 * 
 * @author taedium
 */
public interface SqlExecutionContext {

    /**
     * エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false}を返します。
     * 
     * @return エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false}
     */
    boolean isHaltOnError();

    /**
     * エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false}を設定します。
     * 
     * @param haltOnError
     *            エラー発生時に処理を即座に中断する場合{@code true}、中断しない場合{@code false}
     */
    void setHaltOnError(boolean haltOnError);

    /**
     * ステートメントを返します。
     * 
     * @return ステートメント
     */
    Statement getStatement();

    /**
     * 準備されたステートメントを返します。
     * 
     * @param sql
     *            SQL
     * @return 準備されたステートメント
     */
    PreparedStatement getPreparedStatement(String sql);

    /**
     * 例外のリストを返します。
     * 
     * @return 例外のリスト
     */
    List<RuntimeException> getExceptionList();

    /**
     * 例外を追加します。
     * 
     * @param exception
     *            例外
     */
    void addException(RuntimeException exception);

    /**
     * 例外を通知します。
     */
    void notifyException();

    /**
     * 破棄します。
     */
    public void destroy();

    /**
     * 開始します。
     */
    public void begin();

    /**
     * 終了します。
     */
    public void end();

    /**
     * ローカルトランザクションをコミットします。
     */
    public void commitLocalTx();

}
