/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import java.util.Locale;

import junit.framework.TestCase;

/**
 * @author shot
 * @author higa
 */
public class MessageResourceBundleFactoryTest extends TestCase {

    private static final String BASE_NAME = "SSRMessages";

    private static final String PATH = BASE_NAME + ".properties";

    public void tearDown() {
        MessageResourceBundleFactory.clear();
    }

    public void testLoadFacade() throws Exception {
        MessageResourceBundleFacade facade = MessageResourceBundleFactory
                .loadFacade(PATH);
        assertNotNull(facade);
        assertEquals("{0} not found", facade.getBundle().get("ESSR0001"));
    }

    public void testGetNullableBundle() throws Exception {
        MessageResourceBundle bundle = MessageResourceBundleFactory
                .getNullableBundle(BASE_NAME, Locale.JAPANESE);
        assertNotNull(bundle);
        assertNotNull(bundle.getParent());
        assertEquals("{0}が見つかりません", bundle.get("ESSR0001"));

        bundle = MessageResourceBundleFactory.getNullableBundle(BASE_NAME,
                Locale.ITALIAN);
        assertNotNull(bundle);
        assertNull(bundle.getParent());
        assertEquals("{0} not found", bundle.get("ESSR0001"));
    }
}
