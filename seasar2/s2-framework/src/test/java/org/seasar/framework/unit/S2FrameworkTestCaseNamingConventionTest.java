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
package org.seasar.framework.unit;

import java.util.HashMap;

import org.seasar.framework.convention.NamingConvention;

/**
 * @author koichik
 */
public class S2FrameworkTestCaseNamingConventionTest extends
        S2FrameworkTestCase {

    NamingConvention namingConvention;

    protected String getRootDicon() throws Throwable {
        return "org/seasar/framework/unit/S2FrameworkTestCase_convention.dicon";
    }

    /**
     * @throws Exception
     */
    public void test() throws Exception {
        assertEquals("/", namingConvention.getViewRootPath());
        assertEquals(".htm", namingConvention.getViewExtension());
    }

    /**
     * @throws Exception
     */
    public void testRegister() throws Exception {
        register(HashMap.class);
    }

}
