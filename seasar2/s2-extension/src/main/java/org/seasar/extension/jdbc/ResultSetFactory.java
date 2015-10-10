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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 結果セットのファクトリです。
 * 
 * @author higa
 * 
 */
public interface ResultSetFactory {

    /**
     * 結果セットを返します。
     * 
     * @param statement
     *            文
     * @return 結果セット
     */
    ResultSet getResultSet(Statement statement);

    /**
     * 結果セットを作成します。
     * 
     * @param ps
     *            準備された文
     * @return 結果セット
     */
    ResultSet createResultSet(PreparedStatement ps);

}
