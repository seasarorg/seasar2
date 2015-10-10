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
package org.seasar.extension.jdbc.gen.dialect;

import org.junit.After;
import org.junit.Test;
import org.seasar.extension.jdbc.dialect.MssqlDialect;
import org.seasar.extension.jdbc.dialect.OracleDialect;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.gen.internal.dialect.StandardGenDialect;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GenDialectRegistryTest {

    /** */
    @After
    public void tearDown() {
        GenDialectRegistry.dialectMap.remove(MyDialect.class.getName());
    }

    /**
     * 
     */
    @Test
    public void testGetGenDialect_oracle() {
        OracleDialect oracle = new OracleDialect();
        GenDialect genDialect = GenDialectRegistry.getGenDialect(oracle);
        assertNotNull(genDialect);
        assertSame(GenDialectRegistry.ORACLE, genDialect);
        GenDialect genDialect2 = GenDialectRegistry.getGenDialect(oracle);
        assertSame(genDialect, genDialect2);
    }

    /**
     * 
     */
    @Test
    public void testGetGenDialect_mssql() {
        MssqlDialect mssql = new MssqlDialect();
        GenDialect genDialect = GenDialectRegistry.getGenDialect(mssql);
        assertNotNull(genDialect);
        assertSame(GenDialectRegistry.MSSQL, genDialect);
        GenDialect genDialect2 = GenDialectRegistry.getGenDialect(mssql);
        assertSame(genDialect, genDialect2);
    }

    /**
     * 
     */
    @Test
    public void testRegister() {
        MyGenDialect myGenDialect = new MyGenDialect();
        GenDialectRegistry.register(MyDialect.class, myGenDialect);
        assertSame(myGenDialect, GenDialectRegistry
                .getGenDialect(MyDialect.class));
    }

    /** */
    public static class MyDialect extends StandardDialect {
    }

    /** */
    public static class MyGenDialect extends StandardGenDialect {
    }

}
