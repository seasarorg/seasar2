/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.cooldeploy;

import junit.framework.TestCase;

/**
 * @author higa
 */
public class DefaultConventionNamingTest extends TestCase {
    
    public void testDefineName() throws Exception {
        DefaultConventionNaming naming = new DefaultConventionNaming();
        assertEquals("1", "aaa_bbbPage", naming.defineName("xxx", "web", "Page", "xxx.web.aaa.BbbPage"));
        assertEquals("2", "aaa_bbbService", naming.defineName("xxx", "web", "Service", "xxx.web.aaa.impl.BbbServiceImpl"));
        assertEquals("3", "cccLogic", naming.defineName("xxx", "logic", "Logic", "xxx.logic.impl.CccLogicImpl"));
        assertEquals("4", "dddDao", naming.defineName("xxx", "dao", "Dao", "xxx.dao.DddDao"));
        
        String[] packageNames = {"web", "dxo"};
        assertEquals("5", "aaa_bbbDxo", naming.defineName("xxx", packageNames, "Dxo", "xxx.web.aaa.BbbDxo"));
        assertEquals("6", "aaa_bbbDxo", naming.defineName("xxx", packageNames, "Dxo", "xxx.web.aaa.impl.BbbDxoImpl"));
        assertEquals("7", "bbbDxo", naming.defineName("xxx", packageNames, "Dxo", "xxx.dxo.BbbDxo"));
        assertEquals("8", "bbbDxo", naming.defineName("xxx", packageNames, "Dxo", "xxx.dxo.impl.BbbDxoImpl"));
    }
}