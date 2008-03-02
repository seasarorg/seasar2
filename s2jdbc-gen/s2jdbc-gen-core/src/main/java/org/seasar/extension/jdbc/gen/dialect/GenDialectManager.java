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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * @author taedium
 * 
 */
public class GenDialectManager {

    /** DB2 390用の{@code GenDialect}の実装クラス */
    protected static Class<Db2390GenDialect> DB2_390 = Db2390GenDialect.class;

    /** DB2 400用の{@code GenDialect}の実装クラス */
    protected static Class<Db2400GenDialect> DB2_400 = Db2400GenDialect.class;

    /** DB2用の{@code GenDialect}の実装クラス */
    protected static Class<Db2GenDialect> DB2 = Db2GenDialect.class;

    /** Derby用の{@code GenDialect}の実装クラス */
    protected static Class<DerbyGenDialect> DERBY = DerbyGenDialect.class;

    /** Firebird用の{@code GenDialect}の実装クラス */
    protected static Class<FirebirdGenDialect> FIREBIRD = FirebirdGenDialect.class;

    /** H2用の{@code GenDialect}の実装クラス */
    protected static Class<H2GenDialect> H2 = H2GenDialect.class;

    /** HSQLDB用の{@code GenDialect}の実装クラス */
    protected static Class<HsqlGenDialect> HSQL = HsqlGenDialect.class;

    /** Interbase用の{@code GenDialect}の実装クラス */
    protected static Class<InterbaseGenDialect> INTERBASE = InterbaseGenDialect.class;

    /** MaxDB用の{@code GenDialect}の実装クラス */
    protected static Class<MaxdbGenDialect> MAXDB = MaxdbGenDialect.class;

    /** MS SQL Server 2005用の{@code GenDialect}の実装クラス */
    protected static Class<Mssql2005GenDialect> MSSQL_2005 = Mssql2005GenDialect.class;

    /** MS SQL Server用の{@code GenDialect}の実装クラス */
    protected static Class<MssqlGenDialect> MSSQL = MssqlGenDialect.class;

    /** MySQL用の{@code GenDialect}の実装クラス */
    protected static Class<MysqlGenDialect> MYSQL = MysqlGenDialect.class;

    /** Oracle用の{@code GenDialect}の実装クラス */
    protected static Class<OracleGenDialect> ORACLE = OracleGenDialect.class;

    /** PostgreSQL用の{@code GenDialect}の実装クラス */
    protected static Class<PostgreGenDialect> POSTGRE = PostgreGenDialect.class;

    /** Sybase用の{@code GenDialect}の実装クラス */
    protected static Class<SybaseGenDialect> SYBASE = SybaseGenDialect.class;

    /** 標準の{@code GenDialect}の実装クラス */
    protected static Class<StandardGenDialect> STANDARD = StandardGenDialect.class;

    /** {@code DbmsDialect}のクラス名をキー、{@code Class<? extends GenDialect>}のクラスを値とするマップ */
    protected static Map<String, Class<? extends GenDialect>> getDialectClassMap = new HashMap<String, Class<? extends GenDialect>>();
    static {
        getDialectClassMap.put(Db2390Dialect.class.getName(), DB2_390);
        getDialectClassMap.put(Db2400Dialect.class.getName(), DB2_400);
        getDialectClassMap.put(Db2Dialect.class.getName(), DB2);
        getDialectClassMap.put(DerbyDialect.class.getName(), DERBY);
        getDialectClassMap.put(FirebirdDialect.class.getName(), FIREBIRD);
        getDialectClassMap.put(H2Dialect.class.getName(), H2);
        getDialectClassMap.put(HsqlDialect.class.getName(), HSQL);
        getDialectClassMap.put(InterbaseDialect.class.getName(), INTERBASE);
        getDialectClassMap.put(MaxdbDialect.class.getName(), MAXDB);
        getDialectClassMap.put(Mssql2005Dialect.class.getName(), MSSQL_2005);
        getDialectClassMap.put(MysqlDialect.class.getName(), MYSQL);
        getDialectClassMap.put(OracleDialect.class.getName(), ORACLE);
        getDialectClassMap.put(PostgreDialect.class.getName(), POSTGRE);
        getDialectClassMap.put(SybaseDialect.class.getName(), SYBASE);
    }

    /** {@link GenDialect}のキャッシュ */
    protected static ConcurrentMap<DbmsDialect, GenDialect> genDialectCache = new ConcurrentHashMap<DbmsDialect, GenDialect>();

    private GenDialectManager() {
    }

    /**
     * {@code DbmsDialect}に対応する{@code GenDialect}を返します。
     * 
     * @param dbmsDialect
     *            {@code DbmsDialect}
     * @return {@code GenDialect}
     */
    public static GenDialect getGenDialect(DbmsDialect dbmsDialect) {
        if (dbmsDialect == null) {
            throw new NullPointerException("dbmsDialect");
        }
        GenDialect dialect = genDialectCache.get(dbmsDialect);
        if (dialect != null) {
            return dialect;
        }
        dialect = createGenDialect(dbmsDialect);
        GenDialect dialect2 = genDialectCache.putIfAbsent(dbmsDialect, dialect);
        return dialect2 != null ? dialect2 : dialect;
    }

    protected static GenDialect createGenDialect(DbmsDialect dbmsDialect) {
        Class<? extends GenDialect> dialectClass = getDialectClassMap
                .get(dbmsDialect.getClass().getName());
        if (dialectClass == null) {
            dialectClass = STANDARD;
        }
        Constructor<? extends GenDialect> constructor = ReflectionUtil
                .getConstructor(dialectClass, DbmsDialect.class);
        return ReflectionUtil.newInstance(constructor, dbmsDialect);
    }

    protected static void addGenDialectClass(String dbmsDialectClassName,
            Class<? extends GenDialect> genDialectClass) {
        getDialectClassMap.put(dbmsDialectClassName, genDialectClass);
    }

}
