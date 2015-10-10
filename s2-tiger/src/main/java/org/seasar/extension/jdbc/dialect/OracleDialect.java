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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.SelectForUpdateType;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.types.OracleDateCalendarType;
import org.seasar.extension.jdbc.types.OracleDateType;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.util.tiger.Pair;

/**
 * Oracle用の方言をあつかうクラスです。
 * 
 * @author higa
 * 
 */
public class OracleDialect extends StandardDialect {

    /** {@link Date}型をOracle固有の{@literal DATE}型として扱う{@link ValueType} */
    public static final ValueType ORACLE_DATE_TYPE = new OracleDateType();

    /** {@link Calendar}型をOracle固有の{@literal DATE}型として扱う{@link ValueType} */
    public static final ValueType ORACLE_DATE_CALENDAR_TYPE = new OracleDateCalendarType();

    /**
     * 一意制約違反を表す例外コード
     */
    protected static final int uniqueConstraintViolationCode = 1;

    private boolean supportsBooleanToInt = true;

    private boolean supportsWaveDashToFullwidthTilde = true;

    /** Oracle固有の{@literal DATE}型を使用する場合は{@literal true} */
    protected boolean useOracleDate = true;

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
        buf.append("select * from ( select temp_.*, rownum rownumber_ from ( ");
        buf.append(sql);
        buf.append(" ) temp_ ) where");
        boolean hasOffset = offset > 0;
        if (hasOffset) {
            buf.append(" rownumber_ > ");
            buf.append(offset);
        }
        if (limit > 0) {
            if (hasOffset) {
                buf.append(" and");
            }
            buf.append(" rownumber_ <= ");
            buf.append(offset + limit);
        }
        if (isForUpdate) {
            buf.append(" for update");
        }
        return buf.toString();
    }

    @Override
    public ValueType getValueType(PropertyMeta propertyMeta) {
        Class<?> clazz = propertyMeta.getPropertyClass();
        if (clazz == String.class && supportsWaveDashToFullwidthTilde()) {
            if (propertyMeta.isLob()) {
                return ValueTypes.WAVE_DASH_CLOB;
            }
            return ValueTypes.WAVE_DASH_STRING;
        }
        if (useOracleDate
                && propertyMeta.getTemporalType() == TemporalType.TIMESTAMP) {
            if (clazz == Date.class) {
                return ORACLE_DATE_TYPE;
            }
            if (clazz == Calendar.class) {
                return ORACLE_DATE_CALENDAR_TYPE;
            }
        }
        ValueType valueType = getValueTypeInternal(propertyMeta
                .getPropertyClass());
        if (valueType != null) {
            return valueType;
        }
        return super.getValueType(propertyMeta);
    }

    @Override
    public ValueType getValueType(Class<?> clazz, boolean lob,
            TemporalType temporalType) {
        if (clazz == String.class && supportsWaveDashToFullwidthTilde()) {
            if (lob) {
                return ValueTypes.WAVE_DASH_CLOB;
            }
            return ValueTypes.WAVE_DASH_STRING;
        }
        if (useOracleDate && temporalType == TemporalType.TIMESTAMP) {
            if (clazz == Date.class) {
                return ORACLE_DATE_TYPE;
            }
            if (clazz == Calendar.class) {
                return ORACLE_DATE_CALENDAR_TYPE;
            }
        }
        return super.getValueType(clazz, lob, temporalType);
    }

    @Override
    protected ValueType getValueTypeInternal(Class<?> clazz) {
        if ((clazz == Boolean.class || clazz == boolean.class)
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

    /**
     * Oracle固有の{@literal DATE}型を使用する場合は{@literal true}を返します。
     * 
     * @return Oracle固有の{@literal DATE}型を使用する場合は{@literal true}
     */
    public boolean isUseOracleDate() {
        return useOracleDate;
    }

    /**
     * Oracle固有の{@literal DATE}型を使用する場合は{@literal true}を設定します。
     * 
     * @param useOracleDate
     *            Oracle固有の{@literal DATE}型を使用する場合は{@literal true}
     */
    public void setUseOracleDate(boolean useOracleDate) {
        this.useOracleDate = useOracleDate;
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
    public boolean supportsBatchUpdateResults() {
        return false;
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
