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
package org.seasar.extension.jdbc.dialect;

import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.SelectForUpdateType;
import org.seasar.framework.util.tiger.Pair;

/**
 * DB2用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class Db2Dialect extends StandardDialect {

    /**
     * 一意制約違反を表すSQLステート
     */
    protected static final String uniqueConstraintViolationCode = "23505";

    @Override
    public String getName() {
        return "db2";
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    /**
     * 行番号ファンクション名を返します。
     * 
     * @return 行番号ファンクション名
     */
    @Override
    public String getRowNumberFunctionName() {
        return "rownumber()";
    }

    @Override
    public String convertLimitSql(String sql, int offset, int limit) {
        if (offset > 0) {
            return convertLimitSqlByRowNumber(sql, offset, limit);
        }
        return convertLimitOnlySql(sql, limit);

    }

    /**
     * limitのみがついたのSQLに変換します。
     * 
     * @param sql
     *            SQL
     * @param limit
     *            リミット
     * @return limitのみがついたのSQL
     */
    protected String convertLimitOnlySql(String sql, int limit) {
        StringBuilder buf = new StringBuilder(sql.length() + 30);
        buf.append(sql);
        buf.append(" fetch first ");
        buf.append(limit);
        buf.append(" rows only");
        return buf.toString();
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    @Override
    public boolean supportsGetGeneratedKeys() {
        return true;
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public String getSequenceNextValString(final String sequenceName,
            final int allocationSize) {
        return "values nextval for " + sequenceName;
    }

    @Override
    public String getForUpdateString(final SelectForUpdateType type,
            final int waitSeconds, final Pair<String, String>... aliases) {
        return " for update with rs";
    }

    @Override
    public boolean supportsInnerJoinForUpdate() {
        return false;
    }

    @Override
    public boolean supportsOuterJoinForUpdate() {
        return false;
    }

    @Override
    public boolean isUniqueConstraintViolation(Throwable t) {
        final String state = getSQLState(t);
        return uniqueConstraintViolationCode.equals(state);
    }

}
