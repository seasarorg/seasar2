/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.impl;

import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.impl.entity.Foo;
import org.seasar.framework.jpa.impl.entity.aaa.Bar;
import org.seasar.framework.unit.S2TigerTestCase;

/**
 * @author koichik
 * 
 */
public class PersistenceUnitManagerImplTest extends S2TigerTestCase {

    PersistenceUnitManager pum;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        include(getClass().getName().replace('.', '/') + ".dicon");
    }

    /**
     * @throws Exception
     */
    public void testGetPersistenceUnitNameByClass() throws Exception {
        assertEquals("persistenceUnit", pum
                .getAbstractPersistenceUnitName(Foo.class));
        assertEquals("aaaPersistenceUnit", pum
                .getAbstractPersistenceUnitName(Bar.class));
    }

    /**
     * @throws Exception
     */
    public void testGetPersistenceUnitNameByMappingFile() throws Exception {
        assertEquals(
                "persistenceUnit",
                pum
                        .getAbstractPersistenceUnitName("org/seasar/framework/jpa/impl/entity/FooOrm.xml"));
        assertEquals(
                "aaaPersistenceUnit",
                pum
                        .getAbstractPersistenceUnitName("org/seasar/framework/jpa/impl/entity/aaa/FooOrm.xml"));
        assertEquals(
                "persistenceUnit",
                pum
                        .getAbstractPersistenceUnitName("org/seasar/framework/jpa/impl/dao/FooOrm.xml"));
        assertEquals(
                "aaaPersistenceUnit",
                pum
                        .getAbstractPersistenceUnitName("org/seasar/framework/jpa/impl/dao/aaa/FooOrm.xml"));
    }

}
