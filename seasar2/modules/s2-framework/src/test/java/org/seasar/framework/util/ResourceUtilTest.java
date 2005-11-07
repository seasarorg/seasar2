package org.seasar.framework.util;

import java.io.File;

import junit.framework.TestCase;

public class ResourceUtilTest extends TestCase {

    public void testGetResourcePath() throws Exception {
        assertEquals("1", "aaa/bbb.xml", ResourceUtil.getResourcePath(
                "aaa/bbb.xml", "xml"));
        assertEquals("2", "aaa/bbb.xml", ResourceUtil.getResourcePath(
                "aaa.bbb", "xml"));
        assertEquals("3", "org/seasar/framework/util/ResourceUtilTest.class",
                ResourceUtil.getResourcePath(getClass()));
    }

    public void testGetResource() throws Exception {
        assertNotNull("1", ResourceUtil.getResource("java/lang/String.class",
                "class"));
        try {
            ResourceUtil.getResource("hoge", "xml");
            fail("2");
        } catch (ResourceNotFoundRuntimeException e) {
            System.out.println(e);
            assertEquals("3", "hoge.xml", e.getPath());
        }
        System.out.println(ResourceUtil.getResource("."));
    }

    public void testGetBuildDir() throws Exception {
        File file = ResourceUtil.getBuildDir(getClass());
        System.out.println(file);
    }

    public void testIsExist() throws Exception {
        assertEquals("1", true, ResourceUtil.isExist("SSRMessages.properties"));
        assertEquals("2", false, ResourceUtil.isExist("hoge"));
    }

    public void testGetExtension() throws Exception {
        assertEquals("1", "xml", ResourceUtil.getExtension("aaa/bbb.xml"));
        assertEquals("2", null, ResourceUtil.getExtension("aaa"));
    }

    public void testRemoteExtension() throws Exception {
        assertEquals("1", "aaa/bbb", ResourceUtil
                .removeExtension("aaa/bbb.xml"));
        assertEquals("2", "aaa/bbb", ResourceUtil.removeExtension("aaa/bbb"));
    }
}