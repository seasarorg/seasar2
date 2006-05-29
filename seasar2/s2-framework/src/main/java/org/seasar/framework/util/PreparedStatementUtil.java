/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.framework.exception.SQLRuntimeException;

/**
 * @author higa
 * 
 */
public final class PreparedStatementUtil {

    private PreparedStatementUtil() {
    }

    public static ResultSet executeQuery(PreparedStatement ps) {
        try {
            return ps.executeQuery();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    public static int executeUpdate(PreparedStatement ps) {
        try {
            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    public static int[] executeBatch(PreparedStatement ps) {
        try {
            return ps.executeBatch();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    public static void addBatch(PreparedStatement ps) {
        try {
            ps.addBatch();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }
}
