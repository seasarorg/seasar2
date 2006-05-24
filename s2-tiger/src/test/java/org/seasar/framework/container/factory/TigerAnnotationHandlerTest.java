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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.DestroyMethodDef;
import org.seasar.framework.container.IllegalDestroyMethodAnnotationRuntimeException;
import org.seasar.framework.container.IllegalInitMethodAnnotationRuntimeException;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.InterTypeDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.ejb.SEJBException;
import org.seasar.framework.jpa.TxScopedEntityManagerProxy;

/**
 * @author higa
 */
public class TigerAnnotationHandlerTest extends S2TestCase {

    private TigerAnnotationHandler handler = new TigerAnnotationHandler();

    public void testCreateComponentDef() throws Exception {
        assertNotNull("1", handler.createComponentDef(Hoge.class, null));
        ComponentDef cd = handler.createComponentDef(Hoge2.class, null);
        assertEquals("2", "aaa", cd.getComponentName());
        assertEquals("3", "prototype", cd.getInstanceDef().getName());
        assertEquals("4", "property", cd.getAutoBindingDef().getName());

        ComponentDef cd2 = handler.createComponentDef(Hoge.class,
                InstanceDefFactory.REQUEST);
        assertEquals("5", InstanceDef.REQUEST_NAME, cd2.getInstanceDef()
                .getName());

        ComponentDef cd3 = handler.createComponentDef(Hoge7.class, null);
        assertEquals("6", "hoge77", cd3.getComponentName());
        assertEquals("7", InstanceDef.PROTOTYPE_NAME, cd3.getInstanceDef()
                .getName());
        assertEquals("8", AutoBindingDef.SEMIAUTO_NAME, cd3.getAutoBindingDef()
                .getName());

        ComponentDef cd4 = handler.createComponentDef(Hoge8.class, null);
        assertEquals("9", "hoge7x", cd4.getComponentName());
        assertEquals("10", InstanceDef.SINGLETON_NAME, cd4.getInstanceDef()
                .getName());
        assertEquals("11", AutoBindingDef.PROPERTY_NAME, cd4
                .getAutoBindingDef().getName());

        ComponentDef cd5 = handler.createComponentDef(Hoge7.class,
                InstanceDefFactory.REQUEST);
        assertEquals("12", InstanceDef.REQUEST_NAME, cd5.getInstanceDef()
                .getName());

        ComponentDef cd6 = handler.createComponentDef(Hoge9.class, null);
        assertEquals("13", "hoge99", cd6.getComponentName());
        assertEquals("14", InstanceDef.PROTOTYPE_NAME, cd6.getInstanceDef()
                .getName());
        assertEquals("15", AutoBindingDef.SEMIAUTO_NAME, cd6
                .getAutoBindingDef().getName());

        ComponentDef cd7 = handler.createComponentDef(Hoge10.class, null);
        assertEquals("16", "hoge10x", cd7.getComponentName());
        assertEquals("17", InstanceDef.SINGLETON_NAME, cd7.getInstanceDef()
                .getName());
        assertEquals("18", AutoBindingDef.PROPERTY_NAME, cd7
                .getAutoBindingDef().getName());

        ComponentDef cd8 = handler.createComponentDef(Hoge9.class,
                InstanceDefFactory.REQUEST);
        assertEquals("19", InstanceDef.REQUEST_NAME, cd8.getInstanceDef()
                .getName());

        ComponentDef cd9 = handler.createComponentDef(Hoge.class,
                InstanceDefFactory.REQUEST, AutoBindingDefFactory.NONE);
        assertEquals("20", AutoBindingDef.NONE_NAME, cd9.getAutoBindingDef()
                .getName());

        ComponentDef cd10 = handler.createComponentDef(Hoge2.class,
                InstanceDefFactory.REQUEST, AutoBindingDefFactory.NONE);
        assertEquals("21", AutoBindingDef.PROPERTY_NAME, cd10
                .getAutoBindingDef().getName());

        ComponentDef cd11 = handler.createComponentDef(Hoge3.class,
                InstanceDefFactory.REQUEST, AutoBindingDefFactory.NONE);
        assertEquals("22", AutoBindingDef.PROPERTY_NAME, cd11
                .getAutoBindingDef().getName());
    }

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
        assertEquals("4", "none", propDef.getBindingTypeDef().getName());
    }

    public void testCreatePropertyDefForEJB3() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Hoge2.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("ddd");
        PropertyDef propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("1", "ddd", propDef.getPropertyName());
        assertNull("2", propDef.getExpression());

        propDesc = beanDesc.getPropertyDesc("eee");
        propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("3", "eee2", ((OgnlExpression) propDef.getExpression())
                .getSource());

        propDesc = beanDesc.getPropertyDesc("iii");
        propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("4", "ejb.iii", ((OgnlExpression) propDef.getExpression())
                .getSource());

        propDesc = beanDesc.getPropertyDesc("jjj");
        propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("5", "jjj2", ((OgnlExpression) propDef.getExpression())
                .getSource());

        propDesc = beanDesc.getPropertyDesc("kkk");
        propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("6", "kkk2", ((OgnlExpression) propDef.getExpression())
                .getSource());
    }

    public void testCreatePropertyDefForEJB3ForField() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Hoge2.class);
        Field field = beanDesc.getField("fff");
        PropertyDef propDef = handler.createPropertyDef(beanDesc, field);
        assertEquals("1", "fff", propDef.getPropertyName());
        assertNull("2", propDef.getExpression());

        field = beanDesc.getField("ggg");
        propDef = handler.createPropertyDef(beanDesc, field);
        assertEquals("3", "ggg2", ((OgnlExpression) propDef.getExpression())
                .getSource());

        field = beanDesc.getField("hhh");
        propDef = handler.createPropertyDef(beanDesc, field);
        assertEquals("3", "ejb.hhh", ((OgnlExpression) propDef.getExpression())
                .getSource());

        field = beanDesc.getField("lll");
        propDef = handler.createPropertyDef(beanDesc, field);
        assertEquals("4", "lll2", ((OgnlExpression) propDef.getExpression())
                .getSource());

        field = beanDesc.getField("mmm");
        propDef = handler.createPropertyDef(beanDesc, field);
        assertEquals("5", "mmm2", ((OgnlExpression) propDef.getExpression())
                .getSource());
    }

    public void testAppendDIForEJB3() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge2.class, null);
        handler.appendDI(cd);
        PropertyDef propDef = cd.getPropertyDef("ggg");
        assertEquals("1", "ggg2", ((OgnlExpression) propDef.getExpression())
                .getSource());
    }

    public void testCreatePropertyDefForConstantAnnotation() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(Hoge.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("aaa");
        assertNull("1", handler.createPropertyDef(beanDesc, propDesc));

        beanDesc = BeanDescFactory.getBeanDesc(Hoge3.class);
        propDesc = beanDesc.getPropertyDesc("aaa");
        PropertyDef propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("2", "aaa", propDef.getPropertyName());
        assertEquals("3", "aaa2", ((OgnlExpression) propDef.getExpression())
                .getSource());

        propDesc = beanDesc.getPropertyDesc("bbb");
        propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("4", "none", propDef.getBindingTypeDef().getName());
    }

    public void testAppendAspect() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 1, cd.getAspectDefSize());
        AspectDef aspectDef = cd.getAspectDef(0);
        assertEquals("2", "aop.traceInterceptor", ((OgnlExpression) aspectDef
                .getExpression()).getSource());
    }

    public void testAppendAspectForMethod() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge4.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 1, cd.getAspectDefSize());
        AspectDef aspectDef = cd.getAspectDef(0);
        assertEquals("2", "aop.traceInterceptor", ((OgnlExpression) aspectDef
                .getExpression()).getSource());
        assertTrue("3", aspectDef.getPointcut().isApplied(
                Hoge4.class.getMethod("getAaa")));
        assertFalse("4", aspectDef.getPointcut().isApplied(
                Hoge4.class.getMethod("getAaa", new Class[] { String.class })));
    }

    public void testAppendAspectForConstantAnnotation() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge3.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 1, cd.getAspectDefSize());
        AspectDef aspectDef = cd.getAspectDef(0);
        assertEquals("2", "aop.traceInterceptor", ((OgnlExpression) aspectDef
                .getExpression()).getSource());
    }

    public void testAppendAspectForEJB3CMT1() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge11.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 7, cd.getAspectDefSize());
        String[] methodNames = new String[] { "mandatory", "required",
                "requiresNew", "notSupported", "never", "defaultValue",
                "notAnnotated" };
        Map<String, String> map = gatherAspect(cd, methodNames);
        assertEquals("2", "ejb3tx.mandatoryTx", map.get("mandatory"));
        assertEquals("3", "ejb3tx.requiredTx", map.get("required"));
        assertEquals("4", "ejb3tx.requiresNewTx", map.get("requiresNew"));
        assertEquals("5", "ejb3tx.notSupportedTx", map.get("notSupported"));
        assertEquals("6", "ejb3tx.neverTx", map.get("never"));
        assertEquals("7", "ejb3tx.requiredTx", map.get("defaultValue"));
        assertEquals("8", "ejb3tx.requiredTx", map.get("notAnnotated"));
    }

    public void testAppendAspectForEJB3CMT2() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge12.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 2, cd.getAspectDefSize());
        String[] methodNames = new String[] { "mandatory", "notAnnotated" };
        Map<String, String> map = gatherAspect(cd, methodNames);
        assertEquals("2", "ejb3tx.mandatoryTx", map.get("mandatory"));
        assertEquals("3", "ejb3tx.requiresNewTx", map.get("notAnnotated"));
    }

    public void testAppendAspectForEJB3CMT3() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge19Derived.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 2, cd.getAspectDefSize());
        String[] methodNames = new String[] { "aMethod", "bMethod", "cMethod" };
        Map<String, String> map = gatherAspect(cd, methodNames);
        assertEquals("2", "ejb3tx.requiredTx", map.get("aMethod"));
        assertNull("3", map.get("bMethod"));
        assertEquals("4", "ejb3tx.requiresNewTx", map.get("cMethod"));
    }

    public void testAppendAspectForEJB3BMT() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge13.class, null);
        handler.appendAspect(cd);
        assertEquals("1", 0, cd.getAspectDefSize());
    }

    public void testAppendAspectForEJB3Interceptor() throws Exception {
        include("ejb3tx.dicon");
        ComponentDef cd = handler.createComponentDef(Hoge14.class, null);
        handler.appendAspect(cd);
        handler.appendInterType(cd);
        register(cd);
        assertEquals("1", 10, cd.getAspectDefSize());
        Hoge14 hoge14 = (Hoge14) cd.getComponent();
        assertEquals("2", "start-BEFORE-before-foo-after-AFTER", hoge14
                .foo("start"));
        assertEquals("3", "start-BEFORE-Before-before-bar-after-After-AFTER",
                hoge14.bar("start"));
        assertEquals("4", "start-Before-before-baz-after-After", hoge14
                .baz("start"));
        assertEquals("4", "start-hoge", hoge14.hoge("start"));
    }

    public void testAppendAspectForEJB3InterceptorInvalid1() throws Exception {
        try {
            ComponentDef cd = handler.createComponentDef(Hoge15.class, null);
            handler.appendAspect(cd);
            fail("1");
        } catch (EJBException ex) {
            System.out.println(ex);
        }
    }

    public void testAppendAspectForEJB3InterceptorInvalid2() throws Exception {
        try {
            ComponentDef cd = handler.createComponentDef(Hoge16.class, null);
            handler.appendAspect(cd);
            fail("1");
        } catch (EJBException ex) {
            System.out.println(ex);
        }
    }

    public void testAppendAspectForEJB3InterceptorInvalid3() throws Exception {
        try {
            ComponentDef cd = handler.createComponentDef(Hoge17.class, null);
            handler.appendAspect(cd);
            fail("1");
        } catch (EJBException ex) {
            System.out.println(ex);
        }
    }

    public void testPersistenceContextForEJB3ByField() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(PropertyDefImpl.class);

        ComponentDef cd = handler.createComponentDef(Hoge11.class, null);
        handler.appendDI(cd);
        assertEquals(6, cd.getPropertyDefSize());

        PropertyDef pd1 = cd.getPropertyDef("em1");
        assertNull(pd1.getExpression());
        assertEquals("field", pd1.getAccessTypeDef().getName());

        PropertyDef pd2 = cd.getPropertyDef("em2");
        assertEquals("em", ((OgnlExpression) pd2.getExpression()).getSource());
        assertEquals("field", pd2.getAccessTypeDef().getName());

        PropertyDef pd3 = cd.getPropertyDef("em3");
        assertNull(pd3.getExpression());
        assertEquals("field", pd3.getAccessTypeDef().getName());
        ComponentDef cd3 = (ComponentDef) beanDesc
                .getField("childComponentDef").get(pd3);
        assertEquals(TxScopedEntityManagerProxy.class, cd3.getComponentClass());
        PropertyDef pd4 = cd3.getPropertyDef("entityManagerFactory");
        assertEquals("emf", ((OgnlExpression) pd4.getExpression()).getSource());
    }

    public void testPersistenceContextForEJB3ByProperty() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(PropertyDefImpl.class);

        ComponentDef cd = handler.createComponentDef(Hoge11.class, null);
        handler.appendDI(cd);
        assertEquals(6, cd.getPropertyDefSize());

        PropertyDef pd1 = cd.getPropertyDef("em4");
        assertNull(pd1.getExpression());
        assertEquals("property", pd1.getAccessTypeDef().getName());

        PropertyDef pd2 = cd.getPropertyDef("em5");
        assertEquals("em", ((OgnlExpression) pd2.getExpression()).getSource());
        assertEquals("property", pd2.getAccessTypeDef().getName());

        PropertyDef pd3 = cd.getPropertyDef("em6");
        assertNull(pd3.getExpression());
        assertEquals("property", pd3.getAccessTypeDef().getName());
        ComponentDef cd3 = (ComponentDef) beanDesc
                .getField("childComponentDef").get(pd3);
        assertEquals(TxScopedEntityManagerProxy.class, cd3.getComponentClass());
        PropertyDef pd4 = cd3.getPropertyDef("entityManagerFactory");
        assertEquals("emf", ((OgnlExpression) pd4.getExpression()).getSource());
    }

    public void testInterType() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge.class, null);
        handler.appendInterType(cd);
        assertEquals("1", 1, cd.getInterTypeDefSize());
        InterTypeDef interTypeDef = cd.getInterTypeDef(0);
        assertEquals("2", "fieldInterType", ((OgnlExpression) interTypeDef
                .getExpression()).getSource());
    }

    public void testAppendInterTypeForConstantAnnotation() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge3.class, null);
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

    public void testAppendInitMethodForConstantAnnotation() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge3.class, null);
        handler.appendInitMethod(cd);
        assertEquals("1", 1, cd.getInitMethodDefSize());
        InitMethodDef initMethodDef = cd.getInitMethodDef(0);
        assertEquals("2", "init", initMethodDef.getMethodName());
    }

    public void setUpAppendInitMethodForDicon() throws Exception {
        include("TigerAnnotationHandlerTest.dicon");
    }

    public void testAppendInitMethodForDicon() throws Exception {
        ComponentDef cd = getComponentDef(Hoge5.class);
        assertEquals("1", 1, cd.getInitMethodDefSize());
    }

    public void testAppendInitMethodForException() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge6.class, null);
        try {
            handler.appendInitMethod(cd);
            fail("1");
        } catch (IllegalInitMethodAnnotationRuntimeException ex) {
            System.out.println(ex);
        }
    }

    public void testAppendInitMethodForEJB3() throws Exception {
        include("ejb3tx.dicon");
        ComponentDef cd = handler.createComponentDef(Hoge11.class, null);
        handler.appendAspect(cd);
        handler.appendInterType(cd);
        handler.appendInitMethod(cd);
        register(cd);
        assertEquals("1", 1, cd.getInitMethodDefSize());
        InitMethodDef initMethodDef = cd.getInitMethodDef(0);
        assertEquals("3", "initialize", initMethodDef.getMethod().getName());

        cd.init();
        Hoge11 hoge = (Hoge11) cd.getComponent();
        assertEquals("4", "FOO", hoge.foo);
    }

    public void testAppendInitMethodForEJB3Exception() throws Exception {
        try {
            ComponentDef cd = handler.createComponentDef(Hoge18.class, null);
            handler.appendInitMethod(cd);
            fail("1");
        } catch (SEJBException ex) {
            System.out.println(ex);
        }
    }

    public void testAppendDestroyMethod() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge.class, null);
        handler.appendDestroyMethod(cd);
        assertEquals("1", 1, cd.getDestroyMethodDefSize());
        DestroyMethodDef destroyMethodDef = cd.getDestroyMethodDef(0);
        assertEquals("2", "destroy", destroyMethodDef.getMethodName());
    }

    public void testAppendDestroyMethodForConstantAnnotation() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge3.class, null);
        handler.appendDestroyMethod(cd);
        assertEquals("1", 1, cd.getDestroyMethodDefSize());
        DestroyMethodDef destroyMethodDef = cd.getDestroyMethodDef(0);
        assertEquals("2", "destroy", destroyMethodDef.getMethodName());
    }

    public void setUpAppendDestroyMethodForDicon() throws Exception {
        include("TigerAnnotationHandlerTest.dicon");
    }

    public void testAppendDestroyMethodForDicon() throws Exception {
        ComponentDef cd = getComponentDef(Hoge5.class);
        assertEquals("1", 1, cd.getDestroyMethodDefSize());
    }

    public void testAppendDestroyMethodForException() throws Exception {
        ComponentDef cd = handler.createComponentDef(Hoge6.class, null);
        try {
            handler.appendDestroyMethod(cd);
            fail("1");
        } catch (IllegalDestroyMethodAnnotationRuntimeException ex) {
            System.out.println(ex);
        }
    }

    private Map<String, String> gatherAspect(ComponentDef cd,
            String[] methodNames) throws NoSuchMethodException {
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < methodNames.length; ++i) {
            Method method = cd.getComponentClass().getMethod(methodNames[i],
                    null);
            for (int j = 0; j < cd.getAspectDefSize(); ++j) {
                AspectDef aspectDef = cd.getAspectDef(j);
                if (aspectDef.getPointcut().isApplied(method)) {
                    map.put(methodNames[i], ((OgnlExpression) aspectDef
                            .getExpression()).getSource());
                }
            }
        }
        return map;
    }
}
