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

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.exception.JoinColumnNotFoundRuntimeException;
import org.seasar.extension.jdbc.it.entity.NoId;
import org.seasar.extension.jdbc.it.entity.OwnerOfNoId;
import org.seasar.framework.unit.Seasar2;

import static junit.framework.Assert.*;

/**
 * @author taedium
 * 
 */
@RunWith(Seasar2.class)
public class AutoSelectNoIdTest {

    private JdbcManager jdbcManager;

    /**
     * 
     * @throws Exception
     */
    public void testNoId() throws Exception {
        List<NoId> list = jdbcManager.from(NoId.class).getResultList();
        assertEquals(2, list.size());
    }

    /**
     * 
     * @throws Exception
     */
    @Test(expected = JoinColumnNotFoundRuntimeException.class)
    public void testOwnerOfNoId() throws Exception {
        jdbcManager.from(OwnerOfNoId.class).innerJoin("noId").getResultList();
    }

}
