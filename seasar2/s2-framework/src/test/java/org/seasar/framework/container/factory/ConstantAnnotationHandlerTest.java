/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
import org.seasar.framework.container.DestroyMethodDef;
import org.seasar.framework.container.IllegalDestroyMethodAnnotationRuntimeException;
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
 * @author manhole
 */
public class ConstantAnnotationHandlerTest extends S2FrameworkTestCase {

    private ConstantAnnotationHandler handler = new ConstantAnnotationHandler();

    /**
     * @throws Exception
     */
    public void testCreateComponentDef() throws Exception {
        assertNotNull(handler.createComponentDef(Hoge.class, null));
        ComponentDef cd = handler.createComponentDef(Hoge2.class, null);
        assertEquals("aaa", cd.getComponentName());
        assertEquals(InstanceDefFactory.PROTOTYPE, cd.getInstanceDef());
        assertEquals(AutoBindingDefFactory.PROPERTY, cd.getAutoBindingDef());
        assertTrue(cd.isExternalBinding());
        ComponentDef cd2 = handler.createComponentDef(Hoge.class,
                InstanceDefFactory.REQUEST);
        assertEquals(InstanceDefFactory.REQUEST, cd2.getInstanceDef());

        try {
            handler.createComponentDef(Hoge3.class, null);
            fail();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * @throws Exception
     */
    public void testCreatePropertyDef() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Hoge.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("aaa");
        assertNull("1", handler.createPropertyDef(beanDesc, propDesc));

        beanDesc = BeanDescFactory.getBeanDesc(Hoge2.class);
        propDesc = beanDesc.getPropertyDesc("aaa");
        PropertyDef propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("2", "aaa", propDef.getPropertyName());
        assertEquals("3", "aaa2", ((OgnlExpression) propDef.getExpression())
                .getSource());

        propDesc = beanDesc.getPropertyDesc("bbb");
        propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("4", BindingTypeDefFactory.NONE, propDef
                .getBindingTypeDef());
    }

    /**
     * 
     */
    public void setUpAppendAspect() {
        include("aop.dicon");
    }

    /**
     * @throws Exception
     */
    public void testAppendAspect() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 1, cd.getAspectDefSize());
        AspectDef aspectDef = cd.getAspectDef(0);
        assertEquals("2", "aop.traceInterceptor", ((OgnlExpression) aspectDef
                .getExpression()).getSource());
    }

    /**
     * @throws Exception
     */
    public void testAppendAspect2() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge2.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 1, cd.getAspectDefSize());
        AspectDef aspectDef = cd.getAspectDef(0);
        assertEquals("2", "aop.traceInterceptor", ((OgnlExpression) aspectDef
                .getExpression()).getSource());
    }

    /**
     * @throws Exception
     */
    public void testAppendInterType() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge.class, null);
        handler.appendInterType(cd);
        assertEquals("1", 1, cd.getInterTypeDefSize());
        InterTypeDef interTypeDef = cd.getInterTypeDef(0);
        assertEquals("2", "fieldInterType", ((OgnlExpression) interTypeDef
                .getExpression()).getSource());
    }

    /**
     * @throws Exception
     */
    public void testAppendInitMethod() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge.class, null);
        handler.appendInitMethod(cd);
        assertEquals("1", 1, cd.getInitMethodDefSize());
        InitMethodDef initMethodDef = cd.getInitMethodDef(0);
        assertEquals("2", "init", initMethodDef.getMethodName());
    }

    /**
     * @throws Exception
     */
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

    /**
     * @throws Exception
     */
    public void testAppendDestroyMethod() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge.class, null);
        handler.appendDestroyMethod(cd);
        assertEquals("1", 1, cd.getDestroyMethodDefSize());
        DestroyMethodDef destroyMethodDef = cd.getDestroyMethodDef(0);
        assertEquals("2", "destroy", destroyMethodDef.getMethodName());
    }

    /**
     * @throws Exception
     */
    public void testAppendDestroyMethodForException() throws Exception {
        try {
            ComponentDef cd = handler.createComponentDef(Hoge4.class, null);
            handler.appendDestroyMethod(cd);
            fail("1");
        } catch (IllegalDestroyMethodAnnotationRuntimeException ex) {
            System.out.println(ex);
        }
        try {
            ComponentDef cd = handler.createComponentDef(Hoge5.class, null);
            handler.appendDestroyMethod(cd);
            fail("2");
        } catch (IllegalDestroyMethodAnnotationRuntimeException ex) {
            System.out.println(ex);
        }
    }

    /**
     * @throws Exception
     */
    public void testNotMistakeAsConstantAnnotation1() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge6.class, null);
        assertEquals(null, cd.getComponentName());
    }

    /**
     * @throws Exception
     */
    public void testNotMistakeAsConstantAnnotation2() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge7.class, null);
        assertEquals(null, cd.getComponentName());
    }

    /**
     * @throws Exception
     */
    public void testNotMistakeAsConstantAnnotation3() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge8.class, null);
        assertEquals(null, cd.getComponentName());
    }

    /**
     * @throws Exception
     */
    public void testNotMistakeAsConstantAnnotation4() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge9.class, null);
        assertEquals(null, cd.getComponentName());
    }

    /**
     * @throws Exception
     */
    public void testNotMistakeAsConstantAnnotation5() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge10.class, null);
        assertEquals(null, cd.getComponentName());
    }

    /**
     * @throws Exception
     */
    public void testNotMistakeAsConstantAnnotation6() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge11.class, null);
        assertEquals(null, cd.getComponentName());
    }

    /**
     * 
     */
    public static class Hoge {

        /**
         * 
         */
        public static final String INIT_METHOD = "init";

        /**
         * 
         */
        public static final String DESTROY_METHOD = "destroy";

        private boolean inited = false;

        private boolean destroyed = false;

        /**
         * 
         */
        public static final String ASPECT = "value=aop.traceInterceptor, pointcut=getAaa\ngetBbb";

        /**
         * 
         */
        public static final String INTER_TYPE = "fieldInterType";

        /**
         * @return
         */
        public String getAaa() {
            return null;
        }

        /**
         * 
         */
        public void init() {
            inited = true;
        }

        /**
         * @return
         */
        public boolean isInited() {
            return inited;
        }

        /**
         * 
         */
        public void destroy() {
            destroyed = true;
        }

        /**
         * @return
         */
        public boolean isDestroyed() {
            return destroyed;
        }
    }

    /**
     * 
     */
    public static class Hoge2 {

        /**
         * 
         */
        public static final String ASPECT = "aop.traceInterceptor";

        /**
         * 
         */
        public static final String COMPONENT = "name = aaa, instance = prototype, autoBinding = property, externalBinding = true";

        /**
         * 
         */
        public static final String aaa_BINDING = "aaa2";

        /**
         * 
         */
        public static final String bbb_BINDING = "bindingType=none";

        /**
         * 
         */
        public static final String ccc_BINDING = null;

        /**
         * @param aaa
         */
        public void setAaa(String aaa) {
        }

        /**
         * @param bbb
         */
        public void setBbb(String bbb) {
        }

        /**
         * @param ccc
         */
        public void setCcc(String ccc) {
        }
    }

    /**
     * 
     */
    public static class Hoge3 {
        /**
         * 
         */
        public static final String COMPONENT = "dummy = aaa";
    }

    /**
     * 
     */
    public static class Hoge4 {
        /**
         * 
         */
        public static final String INIT_METHOD = "xxx";

        /**
         * 
         */
        public static final String DESTROY_METHOD = "xxx";
    }

    /**
     * 
     */
    public static class Hoge5 {
        /**
         * 
         */
        public static final String INIT_METHOD = "init";

        /**
         * 
         */
        public static final String DESTROY_METHOD = "destroy";

        /**
         * @param s
         */
        public void init(String s) {
        }

        /**
         * @param s
         */
        public void destroy(String s) {
        }
    }

    /**
     * 
     */
    public static class Hoge6 {
        /**
         * 
         */
        protected String component = "name = a";

        /**
         * 
         */
        protected String aspect;

        /**
         * 
         */
        protected String init_method;
    }

    /**
     * 
     */
    public static class Hoge7 {
        /**
         * 
         */
        protected String COMPONENT = "name = a";

        /**
         * 
         */
        protected String ASPECT;

        /**
         * 
         */
        protected String INIT_METHOD;
    }

    /**
     * 
     */
    public static class Hoge8 {
        /**
         * 
         */
        public String COMPONENT = "name = a";

        /**
         * 
         */
        public String ASPECT;

        /**
         * 
         */
        public String INIT_METHOD;
    }

    /**
     * 
     */
    public static class Hoge9 {
        /**
         * 
         */
        public static String COMPONENT = "name = a";

        /**
         * 
         */
        public static String ASPECT;

        /**
         * 
         */
        public static String INIT_METHOD;
    }

    /**
     * 
     */
    public static class Hoge10 {
        /**
         * 
         */
        public final String COMPONENT = "name = a";

        /**
         * 
         */
        public final String ASPECT = "a";

        /**
         * 
         */
        public final String INIT_METHOD = "a";
    }

    /**
     * 
     */
    public static class Hoge11 {
        /**
         * 
         */
        protected static final String COMPONENT = "name = a";

        /**
         * 
         */
        protected static final String ASPECT = "a";

        /**
         * 
         */
        protected static final String INIT_METHOD = "a";
    }
}
