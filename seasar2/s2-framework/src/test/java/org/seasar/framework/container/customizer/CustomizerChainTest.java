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
package org.seasar.framework.container.customizer;

import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.MetaDefImpl;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 */
public class CustomizerChainTest extends S2FrameworkTestCase {

    /**
     * @throws Exception
     */
    public void testCustomize() throws Exception {
        ComponentDefImpl cd = new ComponentDefImpl();
        CustomizerChain chain = new CustomizerChain();
        HogeCustomizer customizer = new HogeCustomizer();
        chain.addCustomizer(customizer);
        chain.addCustomizer(customizer);
        chain.customize(cd);
        MetaDef[] mdefs = cd.getMetaDefs("hoge");
        assertEquals(2, mdefs.length);
    }

    /**
     * 
     */
    public static class HogeCustomizer implements ComponentCustomizer {

        public void customize(ComponentDef componentDef) {
            MetaDefImpl md = new MetaDefImpl("hoge");
            componentDef.addMetaDef(md);
        }

    }
}