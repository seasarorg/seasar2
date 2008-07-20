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
        typeMap.put(Types.BLOB, Mssql2005Type.BLOB);
        typeMap.put(Types.CLOB, Mssql2005Type.CLOB);
    }

    /**
     * MS SQL Server用の{@link Type}の実装です。
     * 
     * @author taedium
     */
    public static class Mssql2005Type extends StandardType {

        private static Type BLOB = new Mssql2005Type() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return byte[].class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "varbinary(max)";
            }

        };

        private static Type CLOB = new Mssql2005Type() {

            @Override
            public Class<?> getJavaClass(int length, int precision, int scale,
                    String typeName) {
                return String.class;
            }

            @Override
            public String getColumnDefinition(int length, int precision,
                    int scale, String typeName) {
                return "varchar(max)";
            }
        };

        /**
         * インスタンスを構築します。
         */
        protected Mssql2005Type() {
        }
    }
}
