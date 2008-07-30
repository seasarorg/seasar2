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

import javax.persistence.TemporalType;

/**
 * MS SQL Server 2005の方言を扱うクラスです。
 * 
 * @author taedium
 */
public class Mssql2005GenDialect extends MssqlGenDialect {

    /**
     * インスタンスを構築します。
     */
    public Mssql2005GenDialect() {
        typeMap.put(Types.BLOB, Mssql2005SqlType.BLOB);
        typeMap.put(Types.CLOB, Mssql2005SqlType.CLOB);

        columnTypeMap.put("image", MssqlColumnType.IMAGE);
        columnTypeMap.put("ntext", MssqlColumnType.NTEXT);
        columnTypeMap.put("text", MssqlColumnType.TEXT);
    }

    /**
     * MS SQL Server 2005用の{@link SqlType}の実装です。
     * 
     * @author taedium
     */
    public static class Mssql2005SqlType extends StandardSqlType {

        private static Mssql2005SqlType BLOB = new Mssql2005SqlType("varbinary(max)");

        private static Mssql2005SqlType CLOB = new Mssql2005SqlType("varchar(max)");

        /**
         * インスタンスを構築します。
         */
        protected Mssql2005SqlType(String columnDefinition) {
            super(columnDefinition);
        }
    }

    public static class MssqlColumnType extends StandardColumnType {

        private static MssqlColumnType IMAGE = new MssqlColumnType(
                "varbinary(max)", byte[].class, true);

        private static MssqlColumnType NTEXT = new MssqlColumnType(
                "nvarchar(max)", BigDecimal.class);

        private static MssqlColumnType TEXT = new MssqlColumnType(
                "varchar(max)", String.class);

        public MssqlColumnType(String columnDefinition, Class<?> attributeClass) {
            super(columnDefinition, attributeClass);
        }

        public MssqlColumnType(String columnDefinition,
                Class<?> attributeClass, boolean lob) {
            super(columnDefinition, attributeClass, lob);
        }

        public MssqlColumnType(String columnDefinition,
                Class<?> attributeClass, TemporalType temporalType) {
            super(columnDefinition, attributeClass, temporalType);
        }
    }
}
