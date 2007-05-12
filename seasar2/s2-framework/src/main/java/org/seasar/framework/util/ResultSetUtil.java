/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.framework.exception.SQLRuntimeException;

/**
 * {@link ResultSet}のためのユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public final class ResultSetUtil {

    private ResultSetUtil() {
    }

    /**
     * {@link ResultSet#close()}を呼び出します。
     * 
     * @param resultSet
     * @throws SQLRuntimeException
     *             {@link SQLException}が起こった場合。
     */
    public static void close(ResultSet resultSet) throws SQLRuntimeException {
        if (resultSet == null) {
            return;
        }
        try {
            resultSet.close();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * {@link ResultSet#next()}を呼び出します。
     * 
     * @param resultSet
     * @return {@link ResultSet#next()}の結果
     * @throws SQLRuntimeException
     *             {@link SQLException}が起こった場合。
     */
    public static boolean next(ResultSet resultSet) {
        try {
            return resultSet.next();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }
}