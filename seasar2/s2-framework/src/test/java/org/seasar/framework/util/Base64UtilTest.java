/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.framework.util;

import junit.framework.TestCase;
import sun.misc.BASE64Encoder;

/**
 * @author higa
 *
 */
public class Base64UtilTest extends TestCase {

    private static final String ORIGINAL = "how now brown cow\r\n";

    private static final byte[] BINARY_DATA = ORIGINAL.getBytes();

    private static final String ENCODED_DATA = "aG93IG5vdyBicm93biBjb3cNCg==";

    private static final String ORIGINAL2 = "abc";

    private static final byte[] BINARY_DATA2 = ORIGINAL2.getBytes();

    private static final String ENCODED_DATA2 = new BASE64Encoder()
            .encode(BINARY_DATA2);

    private static final String ORIGINAL3 = "abcd";

    private static final byte[] BINARY_DATA3 = ORIGINAL3.getBytes();

    private static final String ENCODED_DATA3 = new BASE64Encoder()
            .encode(BINARY_DATA3);

    private static final String ORIGINAL4 = "abcde";

    private static final byte[] BINARY_DATA4 = ORIGINAL4.getBytes();

    private static final String ENCODED_DATA4 = new BASE64Encoder()
            .encode(BINARY_DATA4);

    /**
     * @throws Exception
     */
    public void testEncode() throws Exception {
        assertEquals("1", ENCODED_DATA, Base64Util.encode(BINARY_DATA));
        assertEquals("2", ENCODED_DATA2, Base64Util.encode(BINARY_DATA2));
        assertEquals("3", ENCODED_DATA3, Base64Util.encode(BINARY_DATA3));
        assertEquals("4", ENCODED_DATA4, Base64Util.encode(BINARY_DATA4));
        System.out.println(Base64Util.encode(new byte[] { 'a', 'b', 'c' }));
    }

    /**
     * @throws Exception
     */
    public void testDecode() throws Exception {
        byte[] decodedData = Base64Util.decode(ENCODED_DATA);
        assertEquals("1", BINARY_DATA.length, decodedData.length);
        for (int i = 0; i < decodedData.length; i++) {
            assertEquals("2", BINARY_DATA[i], decodedData[i]);
        }
        assertEquals("3", ORIGINAL2, new String(Base64Util
                .decode(ENCODED_DATA2)));
        assertEquals("4", ORIGINAL3, new String(Base64Util
                .decode(ENCODED_DATA3)));
        assertEquals("5", ORIGINAL4, new String(Base64Util
                .decode(ENCODED_DATA4)));
    }
}
