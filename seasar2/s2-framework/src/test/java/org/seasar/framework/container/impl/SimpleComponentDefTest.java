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
package org.seasar.framework.container.impl;

import junit.framework.TestCase;

import org.seasar.framework.beans.PropertyNotFoundRuntimeException;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.ognl.OgnlExpression;

/**
 * @author koichik
 */
public class SimpleComponentDefTest extends TestCase {

    /**
     * 
     */
    public void testBasics() {
        SimpleComponentDef cd = new SimpleComponentDef("Hoge", "hoge");
        assertEquals("Hoge", cd.getComponent());
        assertEquals("hoge", cd.getComponentName());
        assertEquals(String.class, cd.getComponentClass());
        assertEquals(String.class, cd.getConcreteClass());
        assertEquals(InstanceDefFactory.SINGLETON, cd.getInstanceDef());
        try {
            cd.setInstanceDef(InstanceDefFactory.OUTER);
            fail();
        } catch (UnsupportedOperationException expected) {
        }
        assertEquals(AutoBindingDefFactory.NONE, cd.getAutoBindingDef());
        try {
            cd.setAutoBindingDef(AutoBindingDefFactory.AUTO);
            fail();
        } catch (UnsupportedOperationException expected) {
        }
        assertFalse(cd.isExternalBinding());
        try {
            cd.setExternalBinding(true);
            fail();
        } catch (UnsupportedOperationException expected) {
        }
        assertNull(cd.getExpression());
        try {
            cd.setExpression(new OgnlExpression("true"));
            fail();
        } catch (UnsupportedOperationException expected) {
        }
        cd.init();
        cd.destroy();
    }

    /**
     * 
     */
    public void testArgDef() {
        SimpleComponentDef cd = new SimpleComponentDef("Hoge", "hoge");
        assertEquals(0, cd.getArgDefSize());
        try {
            cd.addArgDef(new ArgDefImpl());
            fail();
        } catch (UnsupportedOperationException expected) {
        }
        try {
            cd.getArgDef(0);
            fail();
        } catch (ArrayIndexOutOfBoundsException expected) {
        }
    }

    /**
     * 
     */
    public void testPropertyDef() {
        SimpleComponentDef cd = new SimpleComponentDef("Hoge", "hoge");
        assertEquals(0, cd.getPropertyDefSize());
        try {
            cd.addPropertyDef(new PropertyDefImpl("hoge"));
            fail();
        } catch (UnsupportedOperationException expected) {
        }
        try {
            cd.getPropertyDef(0);
            fail();
        } catch (ArrayIndexOutOfBoundsException expected) {
        }
        assertFalse(cd.hasPropertyDef("hoge"));
        try {
            cd.getPropertyDef("hoge");
            fail();
        } catch (PropertyNotFoundRuntimeException expected) {
        }
    }

    /**
     * 
     */
    public void testInitMethodDef() {
        SimpleComponentDef cd = new SimpleComponentDef("Hoge", "hoge");
        assertEquals(0, cd.getInitMethodDefSize());
        try {
            cd.addInitMethodDef(new InitMethodDefImpl());
            fail();
        } catch (UnsupportedOperationException expected) {
        }
        try {
            cd.getInitMethodDef(0);
            fail();
        } catch (ArrayIndexOutOfBoundsException expected) {
        }
    }

    /**
     * 
     */
    public void testDestroyMethodDef() {
        SimpleComponentDef cd = new SimpleComponentDef("Hoge", "hoge");
        assertEquals(0, cd.getDestroyMethodDefSize());
        try {
            cd.addDestroyMethodDef(new DestroyMethodDefImpl());
            fail();
        } catch (UnsupportedOperationException expected) {
        }
        try {
            cd.getDestroyMethodDef(0);
            fail();
        } catch (ArrayIndexOutOfBoundsException expected) {
        }
    }

    /**
     * 
     */
    public void testAspectDef() {
        SimpleComponentDef cd = new SimpleComponentDef("Hoge", "hoge");
        assertEquals(0, cd.getAspectDefSize());
        try {
            cd.addAspectDef(new AspectDefImpl());
            fail();
        } catch (UnsupportedOperationException expected) {
        }
        try {
            cd.getAspectDef(0);
            fail();
        } catch (ArrayIndexOutOfBoundsException expected) {
        }
    }

    /**
     * 
     */
    public void testInterTypeDef() {
        SimpleComponentDef cd = new SimpleComponentDef("Hoge", "hoge");
        assertEquals(0, cd.getInterTypeDefSize());
        try {
            cd.addInterTypeDef(new InterTypeDefImpl());
            fail();
        } catch (UnsupportedOperationException expected) {
        }
        try {
            cd.getInterTypeDef(0);
            fail();
        } catch (ArrayIndexOutOfBoundsException expected) {
        }
    }

    /**
     * 
     */
    public void testMetaDef() {
        SimpleComponentDef cd = new SimpleComponentDef("Hoge", "hoge");
        assertEquals(0, cd.getMetaDefSize());
        try {
            cd.addMetaDef(new MetaDefImpl());
            fail();
        } catch (UnsupportedOperationException expected) {
        }
        try {
            cd.getMetaDef(0);
            fail();
        } catch (ArrayIndexOutOfBoundsException expected) {
        }
        assertNull(cd.getMetaDef("hoge"));
        MetaDef[] metaDefs = cd.getMetaDefs("hoge");
        assertNotNull(metaDefs);
        assertEquals(0, metaDefs.length);
    }

}
