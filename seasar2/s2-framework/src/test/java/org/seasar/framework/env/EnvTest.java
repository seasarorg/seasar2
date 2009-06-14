/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.framework.env;

import junit.framework.TestCase;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 * 
 */
public class EnvTest extends TestCase {

    /** テスト用env.txtのパス */
    public static final String UT_PATH = StringUtil.replace(ClassUtil
            .getPackageName(EnvTest.class), ".", "/")
            + "/env.txt";

    protected void tearDown() {
        Env.initialize();
    }

    /**
     * @throws Exception
     */
    public void testSetFileAndGetValue() throws Exception {
        Env.setFilePath(UT_PATH);
        assertEquals(Env.UT, Env.getValue());
    }

    /**
     * @throws Exception
     */
    public void testDefault() throws Exception {
        assertEquals(Env.PRODUCT, Env.getValue());
    }

    /**
     * @throws Exception
     */
    public void testAdjustPath() throws Exception {
        assertEquals("hoge.dicon", Env.adjustPath("hoge.dicon"));
        Env.setFilePath(UT_PATH);
        assertEquals("hoge_ut.dicon", Env.adjustPath("hoge.dicon"));

    }
}