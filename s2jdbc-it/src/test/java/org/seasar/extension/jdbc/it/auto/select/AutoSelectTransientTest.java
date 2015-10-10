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
package org.seasar.extension.jdbc.it.auto.select;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.Department3;
import org.seasar.extension.jdbc.it.entity.Department4;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.unit.Seasar2;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoSelectTransientTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testTransientAnnotation() throws Exception {
        Department3 department =
            jdbcManager.from(Department3.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
        assertNull(department.departmentName);
    }

    /**
     * 
     * @throws Exception
     */
    public void testTransientModifier() throws Exception {
        Department4 department =
            jdbcManager.from(Department4.class).where(
                new SimpleWhere().eq("departmentId", 1)).getSingleResult();
        assertNull(department.departmentName);
    }
}
