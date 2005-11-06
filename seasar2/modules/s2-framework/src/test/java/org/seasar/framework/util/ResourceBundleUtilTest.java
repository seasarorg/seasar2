package org.seasar.framework.util;

import java.util.Map;
import java.util.ResourceBundle;

import junit.framework.TestCase;

public class ResourceBundleUtilTest extends TestCase {

    public void testConvertMap() throws Exception {
        ResourceBundle bundle = ResourceBundleUtil.getBundle("SSRMessages",
                null);
        Map map = ResourceBundleUtil.convertMap(bundle);
        String value = (String) map.get("ESSR0001");
        System.out.println(value);
        assertNotNull("1", value);
    }
}