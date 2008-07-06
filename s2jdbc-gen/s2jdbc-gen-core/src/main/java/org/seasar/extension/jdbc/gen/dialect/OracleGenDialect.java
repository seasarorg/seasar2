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

import java.sql.Types;

import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.gen.DataType;

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
        dataTypeMap.put(Types.BIT, OracleDataType.BIT);
        dataTypeMap.put(Types.BIGINT, OracleDataType.BIGINT);
        dataTypeMap.put(Types.CHAR, OracleDataType.CHAR);
        dataTypeMap.put(Types.DECIMAL, OracleDataType.DECIMAL);
        dataTypeMap.put(Types.DOUBLE, OracleDataType.DOUBLE);
        dataTypeMap.put(Types.INTEGER, OracleDataType.INTEGER);
        dataTypeMap.put(Types.NUMERIC, OracleDataType.NUMERIC);
        dataTypeMap.put(Types.SMALLINT, OracleDataType.SMALLINT);
        dataTypeMap.put(Types.TIME, OracleDataType.TIME);
        dataTypeMap.put(Types.TINYINT, OracleDataType.TINYINT);
        dataTypeMap.put(Types.VARBINARY, OracleDataType.VARBINARY);
        dataTypeMap.put(Types.VARCHAR, OracleDataType.VARCHAR);
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

    /**
     * Oracle用の{@link DataType}の実装です。
     * 
     * @author taedium
     */
    public static class OracleDataType extends StandardDataType {

        private static DataType BIGINT = new OracleDataType("number(19,0)");

        private static DataType BIT = new OracleDataType("number(1,0)");

        private static DataType CHAR = new OracleDataType("char(1 char)");

        private static DataType DECIMAL = new OracleDataType("number(%d,%d)") {

            @Override
            public String getDefinition(int length, int presision, int scale) {
                return format(definition, presision, scale);
            }
        };

        private static DataType DOUBLE = new OracleDataType("double precision");

        private static DataType INTEGER = new OracleDataType("number(10,0)");

        private static DataType NUMERIC = new OracleDataType("number(%d,%d)") {

            @Override
            public String getDefinition(int length, int presision, int scale) {
                return format(definition, presision, scale);
            }
        };

        private static DataType SMALLINT = new OracleDataType("number(5,0)");

        private static DataType TIME = new OracleDataType("date");

        private static DataType TINYINT = new OracleDataType("number(3,0)");

        private static DataType VARBINARY = new OracleDataType("row(%d)") {

            @Override
            public String getDefinition(int length, int presision, int scale) {
                if (length > 2000) {
                    return "long row";
                }
                return format(definition, length);
            }
        };

        private static DataType VARCHAR = new OracleDataType("varchar2(%d)") {

            @Override
            public String getDefinition(int length, int presision, int scale) {
                if (length > 4000) {
                    return "long";
                }
                return format(definition, length);
            }
        };

        /**
         * インスタンスを構築します。
         * 
         * @param definition
         *            定義
         */
        protected OracleDataType(String definition) {
            super(definition);
        }
    }
}
