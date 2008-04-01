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
package org.seasar.extension.jdbc.dialect;

import java.util.List;

import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.SelectForUpdateType;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.util.tiger.Pair;

/**
 * Oracle用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class OracleDialect extends StandardDialect {

    /**
     * 一意制約違反を表す例外コード
     */
    protected static final int uniqueConstraintViolationCode = 1;

    private boolean supportsBooleanToInt = true;

    private boolean supportsWaveDashToFullwidthTilde = true;

    @Override
    public String getName() {
        return "oracle";
    }

    @Override
    public boolean supportsLimit() {
        return true;
    }

    /**
     * <p>
     * booleanからintへの変換をサポートしているかどうかを返します。
     * </p>
     * <p>
     * オラクルのようなbooleanをサポートしていないデータベースでは必要になります。
     * </p>
     * 
     * @return booleanからintへの変換をサポートしているかどうか
     */
    public boolean supportsBooleanToInt() {
        return supportsBooleanToInt;
    }

    /**
     * <p>
     * WAVE DASH(U+301C)からFULLWIDTH TILDE(U+FF5E)への変換をサポートしているかどうかを返します。
     * </p>
     * <p>
     * オラクルのようなFULLWIDTH TILDEの変換にバグがあるデータベースでは必要になります。
     * </p>
     * 
     * @return WAVE DASH(U+301C)からFULLWIDTH TILDE(U+FF5E)への変換をサポートしているかどうか
     */
    public boolean supportsWaveDashToFullwidthTilde() {
        return supportsWaveDashToFullwidthTilde;
    }

    @Override
    public boolean needsParameterForResultSet() {
        return true;
    }

    @Override
    public String convertLimitSql(String sql, int offset, int limit) {
        StringBuilder buf = new StringBuilder(sql.length() + 100);
        sql = sql.trim();
        String lowerSql = sql.toLowerCase();
        boolean isForUpdate = false;
        if (lowerSql.endsWith(" for update")) {
            sql = sql.substring(0, sql.length() - 11);
            isForUpdate = true;
        }
        boolean hasOffset = offset > 0;
        if (hasOffset) {
            buf
                    .append("select * from ( select temp_.*, rownum rownumber_ from ( ");
            buf.append(sql);
            buf.append(" ) temp_");
            if (limit > 0) {
                buf.append(" where rownum <= ");
                buf.append(offset + limit);
            }
            buf.append(" ) where rownumber_ > ");
            buf.append(offset);
        } else {
            buf.append("select * from ( ");
            buf.append(sql);
            buf.append(" ) where rownum <= ");
            buf.append(limit);
        }
        if (isForUpdate) {
            buf.append(" for update");
        }
        return buf.toString();
    }

    @Override
    public ValueType getValueType(PropertyMeta propertyMeta) {
        ValueType valueType = getValueTypeInternal(propertyMeta
                .getPropertyClass());
        if (valueType != null) {
            return valueType;
        }
        return super.getValueType(propertyMeta);
    }

    @Override
    protected ValueType getValueTypeInternal(Class<?> clazz) {
        if (clazz == String.class && supportsWaveDashToFullwidthTilde()) {
            return ValueTypes.WAVE_DASH_STRING;
        } else if ((clazz == Boolean.class || clazz == boolean.class)
                && supportsBooleanToInt()) {
            return ValueTypes.BOOLEAN_INTEGER;
        } else if (List.class.isAssignableFrom(clazz)) {
            return ValueTypes.ORACLE_RESULT_SET;
        }
        return null;
    }

    /**
     * booleanからintへの変換をサポートしているかどうかを設定します。
     * 
     * @param supportsBooleanToInt
     *            booleanからintへの変換をサポートしているかどうか
     */
    public void setSupportsBooleanToInt(boolean supportsBooleanToInt) {
        this.supportsBooleanToInt = supportsBooleanToInt;
    }

    /**
     * WAVE DASH(U+301C)からFULLWIDTH TILDE(U+FF5E)への変換をサポートしているかどうかを設定します。
     * 
     * @param supportsWaveDashToFullwidthTilde
     *            WAVE DASH(U+301C)からFULLWIDTH TILDE(U+FF5E)への変換をサポートしているかどうか
     */
    public void setSupportsWaveDashToFullwidthTilde(
            boolean supportsWaveDashToFullwidthTilde) {
        this.supportsWaveDashToFullwidthTilde = supportsWaveDashToFullwidthTilde;
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.SEQUENCE;
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public String getSequenceNextValString(final String sequenceName,
            final int allocationSize) {
        return "select " + sequenceName + ".nextval from dual";
    }

    @Override
    public boolean supportsForUpdate(final SelectForUpdateType type,
            boolean withTarget) {
        return true;
    }

    @Override
    public String getForUpdateString(final SelectForUpdateType type,
            final int waitSeconds, final Pair<String, String>... aliases) {
        final StringBuilder buf = new StringBuilder(100).append(" for update");
        if (aliases.length > 0) {
            buf.append(" of ");
            for (final Pair<String, String> alias : aliases) {
                buf.append(alias.getFirst()).append('.').append(
                        alias.getSecond()).append(", ");
            }
            buf.setLength(buf.length() - 2);
        }
        switch (type) {
        case NORMAL:
            break;
        case NOWAIT:
            buf.append(" nowait");
            break;
        case WAIT:
            buf.append(" wait ").append(waitSeconds);
            break;
        }
        return new String(buf);
    }

    @Override
    public String getHintComment(final String hint) {
        return "/*+ " + hint + " */ ";
    }

    @Override
    public boolean isUniqueConstraintViolation(Throwable t) {
        final Integer code = getErrorCode(t);
        if (code != null) {
            return uniqueConstraintViolationCode == code.intValue();
        }
        return false;
    }

}
