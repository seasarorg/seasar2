/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.net.URLConnection;

import junit.framework.TestCase;

/**
 * @author shot
 */
public class MimeTypeUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetFromStream() throws Exception {
        String path = ClassUtil.getPackageName(this.getClass()).replaceAll(
                "\\.", "/")
                + "/aaa.html";
        String contentType = MimeTypeUtil.guessContentType(path);
        assertEquals("text/html", contentType);
    }

    /**
     * @throws Exception
     */
    public void testGetFromStream_gif() throws Exception {
        String path = ClassUtil.getPackageName(this.getClass()).replaceAll(
                "\\.", "/")
                + "/ccc.gif";
        String contentType = MimeTypeUtil.guessContentType(path);
        assertEquals("image/gif", contentType);
    }

    /**
     * @throws Exception
     */
    public void testGetFromPath() throws Exception {
        String path = ClassUtil.getPackageName(this.getClass()).replaceAll(
                "\\.", "/")
                + "/bbb.html";
        String s = URLConnection.guessContentTypeFromStream(ResourceUtil
                .getResourceAsStream(path));
        assertNull(s);
        String contentType = MimeTypeUtil.guessContentType(path);
        assertEquals("text/html", contentType);
    }

}
