package org.seasar.framework.util;

import junit.framework.TestCase;
import sun.misc.BASE64Encoder;

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

    public void testEncode() throws Exception {
        assertEquals("1", ENCODED_DATA, Base64Util.encode(BINARY_DATA));
        assertEquals("2", ENCODED_DATA2, Base64Util.encode(BINARY_DATA2));
        assertEquals("3", ENCODED_DATA3, Base64Util.encode(BINARY_DATA3));
        assertEquals("4", ENCODED_DATA4, Base64Util.encode(BINARY_DATA4));
        System.out.println(Base64Util.encode(new byte[] { 'a', 'b', 'c' }));
    }

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
