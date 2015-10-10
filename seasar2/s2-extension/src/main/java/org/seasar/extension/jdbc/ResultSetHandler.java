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
package org.seasar.extension.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 結果セットを処理するインターフェースです。
 * 
 * @author higa
 * 
 */
public interface ResultSetHandler {

    /**
     * 結果セットを処理します。
     * 
     * @param resultSet
     *            結果セット
     * @return 処理した結果
     * @throws SQLException
     *             SQL例外が発生した場合
     */
    Object handle(ResultSet resultSet) throws SQLException;
}
