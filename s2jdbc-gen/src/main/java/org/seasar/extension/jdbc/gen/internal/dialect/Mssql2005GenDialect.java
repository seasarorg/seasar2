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
package org.seasar.extension.jdbc.gen.internal.dialect;

import java.math.BigDecimal;
import java.sql.Types;

import org.seasar.extension.jdbc.gen.internal.sqltype.BlobType;
import org.seasar.extension.jdbc.gen.internal.sqltype.ClobType;

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
        sqlTypeMap.put(Types.BLOB, new BlobType("varbinary(max)"));
        sqlTypeMap.put(Types.CLOB, new ClobType("varchar(max)"));

        columnTypeMap.put("image", MssqlColumnType.IMAGE);
        columnTypeMap.put("ntext", MssqlColumnType.NTEXT);
        columnTypeMap.put("text", MssqlColumnType.TEXT);
    }

    /**
     * MS SQL Server 2005用の{@link ColumnType}の実装です。
     * 
     * @author taedium
     */
    public static class MssqlColumnType extends StandardColumnType {

        private static MssqlColumnType IMAGE = new MssqlColumnType(
                "varbinary(max)", byte[].class, true);

        private static MssqlColumnType NTEXT = new MssqlColumnType(
                "nvarchar(max)", BigDecimal.class);

        private static MssqlColumnType TEXT = new MssqlColumnType(
                "varchar(max)", String.class);

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         */
        public MssqlColumnType(String dataType, Class<?> attributeClass) {
            super(dataType, attributeClass);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         * @param lob
         *            LOBの場合{@code true}
         */
        public MssqlColumnType(String dataType, Class<?> attributeClass,
                boolean lob) {
            super(dataType, attributeClass, lob);
        }

    }
}
