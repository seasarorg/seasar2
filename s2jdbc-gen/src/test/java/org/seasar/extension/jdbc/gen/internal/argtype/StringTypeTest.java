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
package org.seasar.extension.jdbc.gen.internal.argtype;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class StringTypeTest {

    private StringType stringType = new StringType();

    /**
     * 
     */
    @Test
    public void testToObject() {
        assertEquals("aaa", stringType.toObject("'aaa'"));
    }

    /**
     * 
     */
    @Test
    public void testToObject_empty() {
        assertEquals("", stringType.toObject("''"));
    }

    /**
     * 
     */
    @Test
    public void testToText() {
        assertEquals("'aaa'", stringType.toText("aaa"));
    }

    /**
     * 
     */
    @Test
    public void testToText_empty() {
        assertEquals("''", stringType.toText(""));
    }

}
