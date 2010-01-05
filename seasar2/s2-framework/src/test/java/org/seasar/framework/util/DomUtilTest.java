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

import javax.xml.parsers.DocumentBuilder;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author higa
 *
 */
public class DomUtilTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetContentsAsStream() throws Exception {
        String contents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo/>";
        assertNotNull("1", DomUtil.getContentsAsStream(contents, "UTF-8"));
    }

    /**
     * @throws Exception
     */
    public void testToString() throws Exception {
        DocumentBuilder builder = DocumentBuilderFactoryUtil
                .newDocumentBuilder();
        Document doc = DocumentBuilderUtil.parse(builder, ResourceUtil
                .getResourceAsStream("org/seasar/framework/util/test1.xml"));
        Element root = doc.getDocumentElement();
        String contents = DomUtil.toString(root);
        System.out.println(contents);
    }
}