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
package org.seasar.extension.jdbc.gen.exception;

import java.sql.SQLException;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.exception.SqlFailedRuntimeException;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class SqlFailedExceptionTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        SQLException cause = new SQLException();
        SqlFailedRuntimeException e = new SqlFailedRuntimeException(cause, "aaa", 1, "bbb");
        assertSame(cause, e.getCause());
        assertEquals("aaa", e.getSqlFilePath());
        assertEquals(1, e.getLineNumber());
        assertEquals("bbb", e.getSql());
        System.out.println(e.getMessage());
    }
}
