package org.seasar.framework.container.factory;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;
import org.seasar.framework.container.deployer.InstanceDefFactory;
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
        assertEquals("3", "aaa2", propDef.getExpression());

        propDesc = beanDesc.getPropertyDesc("bbb");
        propDef = handler.createPropertyDef(beanDesc, propDesc);
        assertEquals("4", BindingTypeDefFactory.NONE,
                propDef.getBindingTypeDef());
    }
    
    public void setUpAppendAspect() {
        include("aop.dicon");
    }

    public void testAppendAspect() throws Exception {
        ComponentDef cd = handler.createComponentDefWithDI(Hoge.class, null);
        assertEquals("1", 1, cd.getAspectDefSize());
        AspectDef aspectDef = cd.getAspectDef(0);
        assertEquals("2", "aop.traceInterceptor", aspectDef.getExpression());
    }
    
    public void testAppendAspect2() throws Exception {
        ComponentDef cd = handler.createComponentDefWithDI(Hoge2.class, null);
        assertEquals("1", 1, cd.getAspectDefSize());
        AspectDef aspectDef = cd.getAspectDef(0);
        assertEquals("2", "aop.traceInterceptor", aspectDef.getExpression());
    }
    
    public void testAppendInitMethod() throws Exception {
        ComponentDef cd = handler.createComponentDefWithDI(Hoge.class, null);
        assertEquals("1", 1, cd.getInitMethodDefSize());
        InitMethodDef initMethodDef = cd.getInitMethodDef(0);
        assertEquals("2", "init", initMethodDef.getMethodName());
    }

    public static class Hoge {
        
        public static final String INIT_METHOD = "init";
        
        private boolean inited = false;
        
        public static final String ASPECT =
            "value=aop.traceInterceptor, pointcut=getAaa\ngetBbb";

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
}