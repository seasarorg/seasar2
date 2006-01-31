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
package org.seasar.framework.container.factory;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.IllegalInitMethodAnnotationRuntimeException;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.InterTypeDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 */
public class ConstantAnnotationHandlerTest extends S2FrameworkTestCase {

    private ConstantAnnotationHandler handler = new ConstantAnnotationHandler();

    public void testCreateComponentDef() throws Exception {
        assertNotNull("1", handler.createComponentDef(Hoge.class, null));
        ComponentDef cd = handler.createComponentDef(Hoge2.class, null);
        assertEquals("2", "aaa", cd.getComponentName());
        assertEquals("3", InstanceDefFactory.PROTOTYPE, cd.getInstanceDef());
        assertEquals("4", AutoBindingDefFactory.PROPERTY, cd.getAutoBindingDef());
        ComponentDef cd2 = handler.createComponentDef(Hoge.class, InstanceDefFactory.REQUEST);
        assertEquals("5", InstanceDefFactory.REQUEST, cd2.getInstanceDef());
        try {
            handler.createComponentDef(Hoge3.class, null);
            fail("6");
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void testCreatePropertyDef() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Hoge.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("aaa");
        assertNull("1", handler.createPropertyDef(beanDesc,
                propDesc));

        beanDesc = BeanDescFactory.getBeanDesc(Hoge2.class);
        propDesc = beanDesc.getPropertyDesc("aaa");
        PropertyDef propDef = handler.createPropertyDef(
                beanDesc, propDesc);
        assertEquals("2", "aaa", propDef.getPropertyName());
        assertEquals("3", "aaa2", ((OgnlExpression) propDef.getExpression())
                .getSource());

        propDesc = beanDesc.getPropertyDesc("bbb");
        propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("4", BindingTypeDefFactory.NONE,
                propDef.getBindingTypeDef());
    }
    
    public void setUpAppendAspect() {
        include("aop.dicon");
    }

    public void testAppendAspect() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 1, cd.getAspectDefSize());
        AspectDef aspectDef = cd.getAspectDef(0);
        assertEquals("2", "aop.traceInterceptor", ((OgnlExpression) aspectDef
                .getExpression()).getSource());
    }
    
    public void testAppendAspect2() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge2.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 1, cd.getAspectDefSize());
        AspectDef aspectDef = cd.getAspectDef(0);
        assertEquals("2", "aop.traceInterceptor", ((OgnlExpression) aspectDef
                .getExpression()).getSource());
    }

    public void testAppendInterType() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge.class, null);
        handler.appendInterType(cd);
        assertEquals("1", 1, cd.getInterTypeDefSize());
        InterTypeDef interTypeDef = cd.getInterTypeDef(0);
        assertEquals("2", "fieldInterType", ((OgnlExpression) interTypeDef
                .getExpression()).getSource());
    }
    
    public void testAppendInitMethod() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge.class, null);
        handler.appendInitMethod(cd);
        assertEquals("1", 1, cd.getInitMethodDefSize());
        InitMethodDef initMethodDef = cd.getInitMethodDef(0);
        assertEquals("2", "init", initMethodDef.getMethodName());
    }
    
    public void testAppendInitMethodForException() throws Exception {
        try {
            ComponentDef cd = handler.createComponentDef(Hoge4.class, null);
            handler.appendInitMethod(cd);
            fail("1");
        } catch (IllegalInitMethodAnnotationRuntimeException ex) {
            System.out.println(ex);
        }
        try {
            ComponentDef cd = handler.createComponentDef(Hoge5.class, null);
            handler.appendInitMethod(cd);
            fail("2");
        } catch (IllegalInitMethodAnnotationRuntimeException ex) {
            System.out.println(ex);
        }
    }

    public static class Hoge {
        
        public static final String INIT_METHOD = "init";
        
        private boolean inited = false;
        
        public static final String ASPECT =
            "value=aop.traceInterceptor, pointcut=getAaa\ngetBbb";

        public static final String INTER_TYPE = "fieldInterType";

        public String getAaa() {
            return null;
        }
        
        public void init() {
            inited = true;
        }
        
        public boolean isInited() {
            return inited;
        }
    }

    public static class Hoge2 {
        
        public static final String ASPECT =
            "aop.traceInterceptor";
        
        public static final String COMPONENT = "name = aaa, instance = prototype, autoBinding = property";

        public static final String aaa_BINDING = "aaa2";

        public static final String bbb_BINDING = "bindingType=none";
        
        public static final String ccc_BINDING = null;

        public void setAaa(String aaa) {
        }

        public void setBbb(String bbb) {
        }
        
        public void setCcc(String ccc) {
        }
    }

    public static class Hoge3 {
        public static final String COMPONENT = "dummy = aaa";
    }
    
    public static class Hoge4 {
        public static final String INIT_METHOD = "xxx";
    }
    
    public static class Hoge5 {
        public static final String INIT_METHOD = "init";
        
        public void init(String s) {
        }
    }
}