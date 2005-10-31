package org.seasar.framework.util;

import java.io.InputStream;

import junit.framework.TestCase;

import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 * 
 */
public class InputStreamUtilTest extends TestCase {

    public void testGetBytes() {
        InputStream is = ResourceUtil.getResourceAsStream(StringUtil.replace(
                getClass().getName(), ".", "/")
                + ".class");
        assertNotNull("1", InputStreamUtil.getBytes(is));
    }
}
