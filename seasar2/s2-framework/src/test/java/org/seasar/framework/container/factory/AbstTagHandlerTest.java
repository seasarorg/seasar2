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
package org.seasar.framework.container.factory;

import junit.framework.TestCase;

/**
 * @author koichik
 * 
 */
public class AbstTagHandlerTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testIsComponentName() throws Exception {
        assertTrue(AbstractTagHandler.isComponentName("abc"));
        assertTrue(AbstractTagHandler.isComponentName("abc_def"));
        assertTrue(AbstractTagHandler.isComponentName("abc$def"));
        assertTrue(AbstractTagHandler.isComponentName("abc000"));
        assertFalse(AbstractTagHandler.isComponentName("0abc"));
        assertFalse(AbstractTagHandler.isComponentName("000"));
        assertFalse(AbstractTagHandler.isComponentName("abc.def"));
    }

}
