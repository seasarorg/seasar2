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
package org.seasar.extension.jdbc.gen.desc;

import junitx.framework.Assert;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.UniqueKeyDesc;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class UniqueKeyDescTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testEquals() throws Exception {
        UniqueKeyDesc uniqueKeyDesc = new UniqueKeyDesc();
        uniqueKeyDesc.addColumnName("AAA");
        UniqueKeyDesc uniqueKeyDesc2 = new UniqueKeyDesc();
        uniqueKeyDesc2.addColumnName("aaa");
        UniqueKeyDesc uniqueKeyDesc3 = new UniqueKeyDesc();
        uniqueKeyDesc3.addColumnName("XXX");

        assertEquals(uniqueKeyDesc, uniqueKeyDesc2);
        assertEquals(uniqueKeyDesc.hashCode(), uniqueKeyDesc2.hashCode());
        Assert.assertNotEquals(uniqueKeyDesc, uniqueKeyDesc3);
        Assert.assertNotEquals(uniqueKeyDesc.hashCode(), uniqueKeyDesc3
                .hashCode());
    }

}
