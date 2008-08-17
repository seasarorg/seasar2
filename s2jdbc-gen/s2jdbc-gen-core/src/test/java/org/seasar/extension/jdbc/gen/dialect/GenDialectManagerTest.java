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

import org.junit.After;
import org.junit.Test;
import org.seasar.extension.jdbc.dialect.OracleDialect;
import org.seasar.extension.jdbc.dialect.StandardDialect;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.internal.dialect.StandardGenDialect;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GenDialectManagerTest {

    /** */
    @After
    public void tearDown() {
        GenDialectManager.dialectMap.remove(MyDialect.class.getName());
    }

    /**
     * 
     */
    @Test
    public void testGetGenDialect() {
        OracleDialect oracle = new OracleDialect();
        GenDialect genDialect = GenDialectManager.getGenDialect(oracle);
        assertNotNull(genDialect);
        assertSame(GenDialectManager.ORACLE, genDialect);
        GenDialect genDialect2 = GenDialectManager.getGenDialect(oracle);
        assertSame(genDialect, genDialect2);
    }

    /**
     * 
     */
    @Test
    public void testRegisterGenDialect() {
        MyDialect myDialect = new MyDialect();
        MyGenDialect myGenDialect = new MyGenDialect();
        GenDialectManager.registerGenDialect(myDialect, myGenDialect);
        assertSame(myGenDialect, GenDialectManager.getGenDialect(myDialect));
    }

    /** */
    public static class MyDialect extends StandardDialect {
    }

    /** */
    public static class MyGenDialect extends StandardGenDialect {
    }

}
