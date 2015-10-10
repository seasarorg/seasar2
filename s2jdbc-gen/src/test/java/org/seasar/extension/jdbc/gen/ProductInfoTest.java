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
package org.seasar.extension.jdbc.gen;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class ProductInfoTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGetInstance() throws Exception {
        ProductInfo info = ProductInfo.getInstance();
        assertEquals("S2JDBC-Gen", info.getName());
        assertEquals("test-0.0.1", info.getVersion());
        assertEquals("test-org.seasar.container", info.getGroupId());
        assertEquals("test-s2jdbc-gen", info.getArtifactId());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testMain() throws Exception {
        ProductInfo.main(new String[] {});
    }
}
