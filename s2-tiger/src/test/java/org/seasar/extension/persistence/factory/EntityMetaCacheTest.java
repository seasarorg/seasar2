/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.persistence.factory;

import java.io.File;
import java.util.Date;

import junit.framework.TestCase;

import org.seasar.extension.persistence.EntityMeta;
import org.seasar.extension.persistence.entity.Employee;
import org.seasar.framework.util.ResourceUtil;

/**
 * @author higa
 * 
 */
public class EntityMetaCacheTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testIsModified() throws Exception {
        File file = ResourceUtil.getResourceAsFileNoException(Employee.class);
        EntityMeta entityMeta = new EntityMeta();
        EntityMetaCache cache = new EntityMetaCache(file, entityMeta);
        assertFalse(cache.isModified());
        Thread.sleep(1000);
        file.setLastModified(new Date().getTime());
        assertTrue(cache.isModified());
    }
}