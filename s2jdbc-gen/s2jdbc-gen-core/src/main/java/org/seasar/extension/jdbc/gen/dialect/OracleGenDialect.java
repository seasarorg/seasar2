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
package org.seasar.extension.jdbc.gen.dialect;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Arrays;

import javax.persistence.GenerationType;

/**
 * Oracleの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class OracleGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public OracleGenDialect() {
        super();
        javaTypeMap.put(Types.DECIMAL, OracleJavaType.DECIMAL);

        dataTypeMap.put(Types.BINARY, OracleDbType.BINARY);
        dataTypeMap.put(Types.BIT, OracleDbType.BIT);
        dataTypeMap.put(Types.BIGINT, OracleDbType.BIGINT);
        dataTypeMap.put(Types.CHAR, OracleDbType.CHAR);
        dataTypeMap.put(Types.DECIMAL, OracleDbType.DECIMAL);
        dataTypeMap.put(Types.DOUBLE, OracleDbType.DOUBLE);
        dataTypeMap.put(Types.INTEGER, OracleDbType.INTEGER);
        dataTypeMap.put(Types.NUMERIC, OracleDbType.NUMERIC);
        dataTypeMap.put(Types.SMALLINT, OracleDbType.SMALLINT);
        dataTypeMap.put(Types.TIME, OracleDbType.TIME);
        dataTypeMap.put(Types.TINYINT, OracleDbType.TINYINT);
        dataTypeMap.put(Types.VARBINARY, OracleDbType.VARBINARY);
        dataTypeMap.put(Types.VARCHAR, OracleDbType.VARCHAR);

        sqlBlockStartWordsList.add(Arrays.asList("create", "or", "replace",
                "procedure"));
        sqlBlockStartWordsList.add(Arrays.asList("create", "or", "replace",
                "function"));
        sqlBlockStartWordsList.add(Arrays.asList("create", "procedure"));
        sqlBlockStartWordsList.add(Arrays.asList("create", "function"));
        sqlBlockStartWordsList.add(Arrays.asList("declare"));
        sqlBlockStartWordsList.add(Arrays.asList("begin"));
    }

    @Override
    public boolean isUserTable(String tableName) {
        return !tableName.contains("$");
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
    public String getSequenceDefinitionFragment(String dataType, int initValue,
            int allocationSize) {
        return "increment by " + allocationSize + " start with " + initValue;
    }

    @Override
    public String getSqlBlockDelimiter() {
        return "/";
    }

    public static class OracleJavaType extends StandardJavaType {

        private static JavaType DECIMAL = new OracleJavaType() {

            @Override
            public Class<?> getJavaClass(int length, int scale,
                    String typeName, boolean nullable) {
                if (scale > 0 || length > 10) {
                    return BigDecimal.class;
                }
                return Integer.class;
            }
        };

        protected OracleJavaType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param clazz
         *            クラス
         */
        protected OracleJavaType(Class<?> clazz) {
            super(clazz);
        }
    }

    /**
     * Oracle用の{@link DbType}の実装です。
     * 
     * @author taedium
     */
    public static class OracleDbType extends StandardDbType {

        private static DbType BIGINT = new OracleDbType("number(19,0)");

        private static DbType BINARY = new OracleDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return VARBINARY.getDefinition(length, precision, scale);
            }
        };

        private static DbType BIT = new OracleDbType("number(1,0)");

        private static DbType CHAR = new OracleDbType("char(1 char)");

        private static DbType DECIMAL = new OracleDbType() {

            @Override
            public String getDefinition(int length, int presision, int scale) {
                return format("number(%d,%d)", presision, scale);
            }
        };

        private static DbType DOUBLE = new OracleDbType("double precision");

        private static DbType INTEGER = new OracleDbType("number(10,0)");

        private static DbType NUMERIC = new OracleDbType() {

            @Override
            public String getDefinition(int length, int presision, int scale) {
                return format("number(%d,%d)", presision, scale);
            }
        };

        private static DbType SMALLINT = new OracleDbType("number(5,0)");

        private static DbType TIME = new OracleDbType("date");

        private static DbType TINYINT = new OracleDbType("number(3,0)");

        private static DbType VARBINARY = new OracleDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                if (length > 2000) {
                    return "long raw";
                }
                return format("raw(%d)", length);
            }
        };

        private static DbType VARCHAR = new OracleDbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                if (length > 4000) {
                    return "long";
                }
                return format("varchar2(%d)", length);
            }
        };

        protected OracleDbType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param definition
         *            定義
         */
        protected OracleDbType(String definition) {
            super(definition);
        }
    }
}
