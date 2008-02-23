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

import java.util.HashMap;
import java.util.Map;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.dialect.Db2390Dialect;
import org.seasar.extension.jdbc.dialect.Db2400Dialect;
import org.seasar.extension.jdbc.dialect.Db2Dialect;
import org.seasar.extension.jdbc.dialect.DerbyDialect;
import org.seasar.extension.jdbc.dialect.FirebirdDialect;
import org.seasar.extension.jdbc.dialect.H2Dialect;
import org.seasar.extension.jdbc.dialect.HsqlDialect;
import org.seasar.extension.jdbc.dialect.InterbaseDialect;
import org.seasar.extension.jdbc.dialect.MaxdbDialect;
import org.seasar.extension.jdbc.dialect.Mssql2005Dialect;
import org.seasar.extension.jdbc.dialect.MysqlDialect;
import org.seasar.extension.jdbc.dialect.OracleDialect;
import org.seasar.extension.jdbc.dialect.PostgreDialect;
import org.seasar.extension.jdbc.dialect.SybaseDialect;
import org.seasar.extension.jdbc.gen.GenDialect;

/**
 * @author taedium
 * 
 */
public class GenDialectManager {

    /** DB2 390の{@code GenDialect} */
    protected static GenDialect DB2_390 = new Db2390GenDialect();

    /** DB2 400の{@code GenDialect} */
    protected static GenDialect DB2_400 = new Db2400GenDialect();

    /** DB2の{@code GenDialect} */
    protected static GenDialect DB2 = new Db2GenDialect();

    /** Derbyの{@code GenDialect} */
    protected static GenDialect DERBY = new DerbyGenDialect();

    /** Firebirdの{@code GenDialect} */
    protected static GenDialect FIREBIRD = new FirebirdGenDialect();

    /** H2の{@code GenDialect} */
    protected static GenDialect H2 = new H2GenDialect();

    /** HSQLDBの{@code GenDialect} */
    protected static GenDialect HSQL = new HsqlGenDialect();

    /** Interbaseの{@code GenDialect} */
    protected static GenDialect INTERBASE = new InterbaseGenDialect();

    /** MaxDBの{@code GenDialect} */
    protected static GenDialect MAXDB = new MaxdbGenDialect();

    /** MS SQL Server 2005の{@code GenDialect} */
    protected static GenDialect MSSQL_2005 = new Mssql2005GenDialect();

    /** MS SQL Serverの{@code GenDialect} */
    protected static GenDialect MSSQL = new MssqlGenDialect();

    /** MySQLの{@code GenDialect} */
    protected static GenDialect MYSQL = new MysqlGenDialect();

    /** Oracleの{@code GenDialect} */
    protected static GenDialect ORACLE = new OracleGenDialect();

    /** PostgreSQLの{@code GenDialect} */
    protected static GenDialect POSTGRE = new PostgreGenDialect();

    /** Sybaseの{@code GenDialect} */
    protected static GenDialect SYBASE = new SybaseGenDialect();

    /** 標準の{@code GenDialect} */
    protected static StandardGenDialect STANDARD = new StandardGenDialect();

    /** {@code DbmsDialect}をキー、{@code GenDialect}を値とするマップ */
    protected static Map<String, GenDialect> dialectMap = new HashMap<String, GenDialect>();
    static {
        dialectMap.put(Db2390Dialect.class.getName(), DB2_390);
        dialectMap.put(Db2400Dialect.class.getName(), DB2_400);
        dialectMap.put(Db2Dialect.class.getName(), DB2);
        dialectMap.put(DerbyDialect.class.getName(), DERBY);
        dialectMap.put(FirebirdDialect.class.getName(), FIREBIRD);
        dialectMap.put(H2Dialect.class.getName(), H2);
        dialectMap.put(HsqlDialect.class.getName(), HSQL);
        dialectMap.put(InterbaseDialect.class.getName(), INTERBASE);
        dialectMap.put(MaxdbDialect.class.getName(), MAXDB);
        dialectMap.put(Mssql2005Dialect.class.getName(), MSSQL_2005);
        dialectMap.put(MysqlDialect.class.getName(), MYSQL);
        dialectMap.put(OracleDialect.class.getName(), ORACLE);
        dialectMap.put(PostgreDialect.class.getName(), POSTGRE);
        dialectMap.put(SybaseDialect.class.getName(), SYBASE);
    }

    private GenDialectManager() {
    }

    /**
     * {@code DbmsDialect}に対応する{@code GenDialect}を返します。
     * 
     * @param dbmsDialect
     *            {@code DbmsDialect}
     * @return {@code GenDialect}
     */
    public static GenDialect getDialect(DbmsDialect dbmsDialect) {
        String className = dbmsDialect.getClass().getName();
        GenDialect generationDialect = dialectMap.get(className);
        if (generationDialect != null) {
            return generationDialect;
        }
        return STANDARD;
    }
}
