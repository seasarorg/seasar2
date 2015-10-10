/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import java.util.List;
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

    /**
     * @throws Exception
     */
    public void testLoadFacade() throws Exception {
        MessageResourceBundleFacade facade = MessageResourceBundleFactory
                .loadFacade(PATH);
        assertNotNull(facade);
        assertEquals("{0} not found", facade.getBundle().get("ESSR0001"));
    }

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
    public void testGetNullabelBundle2() throws Exception {
        String baseName = "org.seasar.framework.message.strings";
        MessageResourceBundle bundle = MessageResourceBundleFactory
                .getNullableBundle(baseName, Locale.JAPANESE);
        assertNotNull(bundle);
        assertNotNull(bundle.getParent());
        assertNull(bundle.getParent().getParent());
        assertEquals("hogehoge", bundle.get("text"));

        bundle = MessageResourceBundleFactory.getNullableBundle(baseName,
                new Locale("ja", "JP"));
        assertNotNull(bundle);
        assertNotNull(bundle.getParent());
        assertNotNull(bundle.getParent().getParent());
        assertNull(bundle.getParent().getParent().getParent());
        assertEquals("hogehogehoge", bundle.get("text"));

        bundle = MessageResourceBundleFactory.getNullableBundle(baseName,
                new Locale("ja", "JP", "WIN"));
        assertNotNull(bundle);
        assertNotNull(bundle.getParent());
        assertNotNull(bundle.getParent().getParent());
        assertNotNull(bundle.getParent().getParent().getParent());
        assertNull(bundle.getParent().getParent().getParent().getParent());
        assertEquals("hogehogehogehoge", bundle.get("text"));

        bundle = MessageResourceBundleFactory.getNullableBundle(baseName,
                new Locale("en", "US"));
        assertNotNull(bundle);
        assertNotNull(bundle.getParent());
        assertNotNull(bundle.getParent().getParent());
        assertNull(bundle.getParent().getParent().getParent());
        assertEquals("foobar", bundle.get("text"));

        bundle = MessageResourceBundleFactory.getNullableBundle(baseName,
                new Locale("en", "UK"));
        assertNotNull(bundle);
        assertNotNull(bundle.getParent());
        assertNull(bundle.getParent().getParent());
        assertEquals("foo", bundle.get("text"));
    }

    /**
     * @throws Exception
     */
    public void testCalcurateBundleNames() throws Exception {
        // 言語のみ
        String[] bundleNames = MessageResourceBundleFactory
                .calcurateBundleNames(BASE_NAME, Locale.JAPANESE);
        List expected = java.util.Arrays.asList( new String[]{BASE_NAME,
                BASE_NAME + "_ja"} );
        assertEquals( expected, java.util.Arrays.asList(bundleNames) );

        // 言語と国
        bundleNames = MessageResourceBundleFactory
                .calcurateBundleNames(BASE_NAME, new Locale("ja", "JP"));
        expected = java.util.Arrays.asList( new String[]{BASE_NAME,
                BASE_NAME + "_ja", BASE_NAME + "_ja_JP"} );
        assertEquals( expected, java.util.Arrays.asList(bundleNames) );

        // 言語と国とバリアント
        bundleNames = MessageResourceBundleFactory
                .calcurateBundleNames(BASE_NAME, new Locale("ja", "JP", "WIN"));
        expected = java.util.Arrays.asList( new String[]{BASE_NAME,
                BASE_NAME + "_ja", BASE_NAME + "_ja_JP", BASE_NAME + "_ja_JP_WIN"} );
        assertEquals( expected, java.util.Arrays.asList(bundleNames) );

        // 言語とバリアント
        bundleNames = MessageResourceBundleFactory
                .calcurateBundleNames(BASE_NAME, new Locale("ja", "", "WIN"));
        expected = java.util.Arrays.asList( new String[]{BASE_NAME,
                BASE_NAME + "_ja", BASE_NAME + "_ja__WIN"} );
        assertEquals( expected, java.util.Arrays.asList(bundleNames) );

        // 国とバリアント
        bundleNames = MessageResourceBundleFactory
                .calcurateBundleNames(BASE_NAME, new Locale("", "JP", "WIN"));
        expected = java.util.Arrays.asList( new String[]{BASE_NAME,
                BASE_NAME + "__JP", BASE_NAME + "__JP_WIN"} );
        assertEquals( expected, java.util.Arrays.asList(bundleNames) );

        // 国のみ
        bundleNames = MessageResourceBundleFactory
                .calcurateBundleNames(BASE_NAME, new Locale("", "JP"));
        expected = java.util.Arrays.asList( new String[]{BASE_NAME,
                BASE_NAME + "__JP"} );
        assertEquals( expected, java.util.Arrays.asList(bundleNames) );

        // バリアントのみ
        bundleNames = MessageResourceBundleFactory
                .calcurateBundleNames(BASE_NAME, new Locale("", "", "WIN"));
        expected = java.util.Arrays.asList( new String[]{BASE_NAME,
                BASE_NAME + "___WIN"} );
        assertEquals( expected, java.util.Arrays.asList(bundleNames) );
    }
}
