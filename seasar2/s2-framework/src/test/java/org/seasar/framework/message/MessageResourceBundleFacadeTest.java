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
package org.seasar.framework.message;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import junit.framework.TestCase;

import org.seasar.framework.container.hotdeploy.HotdeployUtil;
import org.seasar.framework.util.ResourceUtil;

/**
 * @author shot
 * @author higa
 */
public class MessageResourceBundleFacadeTest extends TestCase {

    private static final String PATH = "SSRMessages.properties";

    private static final String PATH2 = "SSRMessages_ja.properties";

    /**
     * @throws Exception
     */
    public void testCreateProperties_url() throws Exception {
        URL url = ResourceUtil.getResource(PATH);
        Properties props = MessageResourceBundleFacade.createProperties(url);
        assertNotNull(props);
        assertEquals("{0} not found", props.get("ESSR0001"));
    }

    /**
     * @throws Exception
     */
    public void testCreateProperties_file() throws Exception {
        File file = ResourceUtil.getResourceAsFile(PATH);
        Properties props = MessageResourceBundleFacade.createProperties(file);
        assertNotNull(props);
        assertEquals("{0} not found", props.get("ESSR0001"));

        file = ResourceUtil.getResourceAsFile(PATH2);
        props = MessageResourceBundleFacade.createProperties(file);
        System.out.println(props.get("ESSR0001"));
        assertEquals("{0}が見つかりません", props.get("ESSR0001"));
    }

    /**
     * @throws Exception
     */
    public void testIsModified_hot() throws Exception {
        HotdeployUtil.setHotdeploy(true);
        try {
            URL url = ResourceUtil.getResource(PATH);
            File file = ResourceUtil.getFile(url);
            MessageResourceBundleFacade facade = new MessageResourceBundleFacade(
                    url);

            assertFalse(facade.isModified());
            Thread.sleep(500);
            file.setLastModified(new Date().getTime());
            assertTrue(facade.isModified());
        } finally {
            HotdeployUtil.clearHotdeploy();
        }

    }

    /**
     * @throws Exception
     */
    public void testIsModified_cool() throws Exception {
        URL url = ResourceUtil.getResource(PATH);
        File file = ResourceUtil.getFile(url);
        MessageResourceBundleFacade facade = new MessageResourceBundleFacade(
                url);

        assertFalse(facade.isModified());
        Thread.sleep(500);
        file.setLastModified(new Date().getTime());
        assertFalse(facade.isModified());

    }

    /**
     * @throws Exception
     */
    public void testSetup() throws Exception {
        URL url = ResourceUtil.getResource(PATH);
        MessageResourceBundleFacade facade = new MessageResourceBundleFacade(
                url);
        MessageResourceBundle bundle = facade.getBundle();
        assertNotNull(bundle);
        assertEquals("{0} not found", bundle.get("ESSR0001"));
    }

    /**
     * @throws Exception
     */
    public void testParent() throws Exception {
        URL url = ResourceUtil.getResource(PATH2);
        MessageResourceBundleFacade facade = new MessageResourceBundleFacade(
                url);
        URL parentUrl = ResourceUtil.getResource(PATH);
        MessageResourceBundleFacade parent = new MessageResourceBundleFacade(
                parentUrl);
        facade.setParent(parent);
        MessageResourceBundle bundle = facade.getBundle();
        assertNotNull(bundle);
        assertNotNull(bundle.getParent());
    }
}
