/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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

/**
 * @author koichik
 * 
 */
public class EnvTigerTest extends TestCase {

    /**
     * [CONTAINER-255] Jarファイルの中の環境名設定ファイルを参照するテストケース。
     * Maven2でテストする場合は、s2-framework-X.Y.Z-test.jarの中の
     * <code>org/seasar/framework/env/env.txt</code>を参照する。
     * 
     * @throws Exception
     */
    public void testSetFileAndGetValue() throws Exception {
        Env.setFilePath(EnvTest.UT_PATH);
        assertEquals(Env.UT, Env.getValue());
    }

}
