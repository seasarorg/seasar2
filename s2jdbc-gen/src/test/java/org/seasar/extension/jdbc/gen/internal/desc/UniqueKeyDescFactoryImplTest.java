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
package org.seasar.extension.jdbc.gen.internal.desc;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.ColumnDesc;
import org.seasar.extension.jdbc.gen.desc.UniqueKeyDesc;
import org.seasar.extension.jdbc.gen.internal.desc.UniqueKeyDescFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.dialect.StandardGenDialect;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class UniqueKeyDescFactoryImplTest {

    private UniqueKeyDescFactoryImpl factory;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        factory = new UniqueKeyDescFactoryImpl(new StandardGenDialect());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetCompositeUniqueKeyDesc() throws Exception {
        UniqueConstraint[] uniqueConstraints = Aaa.class.getAnnotation(
                Table.class).uniqueConstraints();

        UniqueKeyDesc uniqueKeyDesc = factory
                .getCompositeUniqueKeyDesc(uniqueConstraints[0]);
        assertNotNull(uniqueKeyDesc);
        assertEquals(2, uniqueKeyDesc.getColumnNameList().size());
        assertEquals("aaa", uniqueKeyDesc.getColumnNameList().get(0));
        assertEquals("bbb", uniqueKeyDesc.getColumnNameList().get(1));

        uniqueKeyDesc = factory.getCompositeUniqueKeyDesc(uniqueConstraints[1]);
        assertNull(uniqueKeyDesc);
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetSingleUniqueKeyDesc() throws Exception {
        ColumnDesc columnDesc = new ColumnDesc();
        columnDesc.setName("aaa");
        columnDesc.setUnique(true);
        UniqueKeyDesc uniqueKeyDesc = factory
                .getSingleUniqueKeyDesc(columnDesc);
        assertNotNull(uniqueKeyDesc);
        assertEquals(1, uniqueKeyDesc.getColumnNameList().size());
        assertEquals("aaa", uniqueKeyDesc.getColumnNameList().get(0));

        columnDesc.setUnique(false);
        uniqueKeyDesc = factory.getSingleUniqueKeyDesc(columnDesc);
        assertNull(uniqueKeyDesc);
    }

    /** */
    @Table(uniqueConstraints = {
            @UniqueConstraint(columnNames = { "aaa", "bbb" }),
            @UniqueConstraint(columnNames = {}) })
    public static class Aaa {
    }

}
