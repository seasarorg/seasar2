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
package org.seasar.extension.jdbc.gen.javacode;

import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.model.EntityModel;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class EntityCodeTest {

    private EntityModel model;

    private EntityCode code;

    @Before
    public void setUp() {
        model = new EntityModel();
        code = new EntityCode(model, "hoge.Foo", "bar.AbstractFoo",
                "entityCode.ftl");
    }

    @Test
    public void testGetEntityModel() {
        assertEquals(model, code.getEntityModel());
    }

    @Test
    public void testGetShortClassName() {
        assertEquals("Foo", code.getShortClassName());
    }

    @Test
    public void testGetShortBaseClassName() {
        assertEquals("AbstractFoo", code.getShortBaseClassName());
    }

    @Test
    public void testGetImportPackageNames() throws Exception {
        Set<String> imports = code.getImportPackageNames();
        assertEquals(2, imports.size());
        Iterator<String> it = imports.iterator();
        assertEquals("bar.AbstractFoo", it.next());
        assertEquals("javax.persistence.Entity", it.next());
    }

}
