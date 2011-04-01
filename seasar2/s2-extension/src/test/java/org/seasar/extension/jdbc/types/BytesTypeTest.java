/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.types;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class BytesTypeTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void testToText() throws Exception {
        BytesType type = new BytesType(BytesType.BYTES_TRAIT);
        byte[] value = new byte[] { 1, 2, 3, 4 };
        assertEquals("'" + value + "(byteLength=4)'", type.toText(value));
    }
}
