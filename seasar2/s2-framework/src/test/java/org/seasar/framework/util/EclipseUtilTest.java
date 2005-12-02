package org.seasar.framework.util;

import java.io.File;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class EclipseUtilTest extends TestCase {

    public void testGetProjectRoot() {
        File file = EclipseUtil.getProjectRoot("seasar2");
        System.out.println(file.getAbsolutePath());
        assertEquals("1", "seasar2", file.getName());
    }
}
