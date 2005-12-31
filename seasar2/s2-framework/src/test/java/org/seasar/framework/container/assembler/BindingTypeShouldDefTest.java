package org.seasar.framework.container.assembler;

import junit.framework.TestCase;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author higa
 * 
 */
public class BindingTypeShouldDefTest extends TestCase {

    public void testBindWarning() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(A.class);
        PropertyDesc propDesc = beanDesc.getPropertyDesc("hoge");
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        PropertyDef propDef = new PropertyDefImpl("hoge");
        cd.addPropertyDef(propDef);
        container.register(cd);
        A a = new A();
        BindingTypeDefFactory.SHOULD.bind(cd, propDef, propDesc, null, a);
    }

    public interface Foo {
        public String getHogeName();
    }

    public static class A implements Foo {

        private Hoge hoge_;

        private String message_;

        public A() {
        }

        public Hoge getHoge() {
            return hoge_;
        }

        public void setHoge(Hoge hoge) {
            hoge_ = hoge;
        }

        public String getMessage() {
            return message_;
        }

        public void setMessage(String message) {
            message_ = message;
        }

        public String getHogeName() {
            return hoge_.getName();
        }
    }

    public static class A2 implements Foo {

        private Hoge hoge_ = new B();

        public Hoge getHoge() {
            return hoge_;
        }

        public void setHoge(Hoge hoge) {
            hoge_ = hoge;
        }

        public String getHogeName() {
            return hoge_.getName();
        }
    }

    public interface Hoge {

        public String getName();
    }

    public static class B implements Hoge {

        public String getName() {
            return "B";
        }
    }
}