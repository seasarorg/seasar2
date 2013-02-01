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
package org.seasar.extension.jdbc;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class IllegalBindArgSizeRuntimeExceptionTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void test() throws Exception {
        IllegalBindArgSizeRuntimeException ex = new IllegalBindArgSizeRuntimeException(
                "select * from Emp where hoge = ? and foo = ?", 1);
        System.out.println(ex.getMessage());
        assertEquals("select * from Emp where hoge = ? and foo = ?", ex
                .getSql());
        assertEquals(1, ex.getArgSize());
    }
}
