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
import org.seasar.extension.jdbc.gen.GenerationDialect;

/**
 * @author taedium
 * 
 */
public class GenerationDialectManager {

    protected static GenerationDialect DB2_390 = new Db2390GenDialect();

    protected static GenerationDialect DB2_400 = new Db2400GenDialect();

    protected static GenerationDialect DB2 = new Db2GenDialect();

    protected static GenerationDialect DERBY = new DerbyGenDialect();

    protected static GenerationDialect FIREBIRD = new FirebirdGenDialect();

    protected static GenerationDialect H2 = new H2GenDialect();

    protected static GenerationDialect HSQL = new HsqlGenDialect();

    protected static GenerationDialect INTERBASE = new InterbaseGenDialect();

    protected static GenerationDialect MAXDB = new MaxdbGenDialect();

    protected static GenerationDialect MSSQL_2005 = new Mssql2005GenDialect();

    protected static GenerationDialect MSSQL = new MssqlGenDialect();

    protected static GenerationDialect MYSQL = new MysqlGenDialect();

    protected static GenerationDialect ORACLE = new OracleGenDialect();

    protected static GenerationDialect POSTGRE = new PostgreGenDialect();

    protected static GenerationDialect SYBASE = new SybaseGenDialect();

    protected static StandardGenDialect STANDARD = new StandardGenDialect();

    protected static Map<String, GenerationDialect> dialectMap = new HashMap<String, GenerationDialect>();
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

    private GenerationDialectManager() {
    }

    public static GenerationDialect getGenerationDialect(DbmsDialect dbmsDialect) {
        String className = dbmsDialect.getClass().getName();
        GenerationDialect generationDialect = dialectMap.get(className);
        if (generationDialect != null) {
            return generationDialect;
        }
        return STANDARD;
    }
}
