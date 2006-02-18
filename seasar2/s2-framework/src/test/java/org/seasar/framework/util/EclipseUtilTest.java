/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import java.io.File;

import junit.framework.TestCase;

import org.seasar.framework.exception.SIllegalStateException;

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

    public void testGetProjectRootNotFound() {
        try {
            EclipseUtil.getProjectRoot("no_such_project_1111");
            fail();
        } catch (SIllegalStateException e) {
            String message = e.getMessage();
            System.out.println(message);
            assertEquals(true, -1 < message.indexOf("no_such_project_1111"));
        }
    }

    public void testGetCurrentProjectRoot() {
        File file = EclipseUtil.getCurrentProjectRoot();
        System.out.println(file.getAbsolutePath());
        assertEquals("1", "seasar2", file.getName());
    }

}
