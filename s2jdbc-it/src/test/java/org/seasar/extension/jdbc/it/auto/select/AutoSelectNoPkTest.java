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
package org.seasar.extension.jdbc.it.auto.select;

import java.util.List;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.it.entity.NoPk;
import org.seasar.extension.jdbc.it.entity.NoPk2;
import org.seasar.framework.unit.Seasar2;

import static junit.framework.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoSelectNoPkTest {

    private JdbcManager jdbcManager;

    public void testSingleEntity() throws Exception {
        List<NoPk> list = jdbcManager.from(NoPk.class).getResultList();
        assertEquals(2, list.size());
    }

    public void testManyToOne_innerJoin() throws Exception {
        List<NoPk2> list =
            jdbcManager.from(NoPk2.class).innerJoin("noPk2").getResultList();
        assertEquals(1, list.size());
    }

    public void testManyToOne_leftOuterJoin() throws Exception {
        List<NoPk2> list =
            jdbcManager
                .from(NoPk2.class)
                .leftOuterJoin("noPk2")
                .getResultList();
        assertEquals(2, list.size());
    }

    public void testOneToMany_innerJoin() throws Exception {
        List<NoPk2> list =
            jdbcManager.from(NoPk2.class).innerJoin("noPk2s").getResultList();
        assertEquals(1, list.size());
    }

    public void testOneToMany_leftOuterJoin() throws Exception {
        List<NoPk2> list =
            jdbcManager
                .from(NoPk2.class)
                .leftOuterJoin("noPk2s")
                .getResultList();
        assertEquals(2, list.size());
    }

}
