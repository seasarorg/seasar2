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
        dbTypeMap.put(Types.BLOB, Mssql2005DbType.BLOB);
        dbTypeMap.put(Types.CLOB, Mssql2005DbType.CLOB);
    }

    /**
     * MS SQL Server用の{@link DbType}の実装です。
     * 
     * @author taedium
     */
    public static class Mssql2005DbType extends StandardDbType {

        private static DbType BLOB = new Mssql2005DbType("varbinary(max)");

        private static DbType CLOB = new Mssql2005DbType("varchar(max)");

        /**
         * インスタンスを構築します。
         */
        protected Mssql2005DbType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param definition
         *            定義
         */
        protected Mssql2005DbType(String definition) {
            super(definition);
        }
    }
}
