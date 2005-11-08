package org.seasar.framework.container.assembler;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.framework.aop.interceptors.TraceInterceptor;
import org.seasar.framework.container.ArgDef;
import org.seasar.framework.container.ConstructorAssembler;
import org.seasar.framework.container.IllegalConstructorRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.assembler.DefaultConstructorConstructorAssembler;
import org.seasar.framework.container.impl.ArgDefImpl;
import org.seasar.framework.container.impl.AspectDefImpl;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author higa
 *
 */
public class DefaultConstructorConstructorAssemblerTest extends TestCase {

	public void testAssemble() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(ArrayList.class);
		container.register(cd);
		ConstructorAssembler assempbler = new DefaultConstructorConstructorAssembler(cd);
		assertNotNull("1", assempbler.assemble());
	}

	public void testAssembleAspect() throws Exception {
		S2Container container = new S2ContainerImpl();
		ComponentDefImpl cd = new ComponentDefImpl(ArrayList.class);
		cd.addAspectDef(new AspectDefImpl(new TraceInterceptor()));
		container.register(cd);
		ConstructorAssembler assempbler = new DefaultConstructorConstructorAssembler(cd);
		List list = (List) assempbler.assemble();
		list.size();
	}
    
    public void testAssembleManual() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        ArgDef argDef = new ArgDefImpl(new B());
        cd.addArgDef(argDef);
        container.register(cd);
        ConstructorAssembler assembler = new DefaultConstructorConstructorAssembler(cd);
        A a = (A) assembler.assemble();
        assertEquals("1", "B", a.getHogeName());
    }

    public void testAssembleIllegalConstructorArgument() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(A.class);
        ArgDef argDef = new ArgDefImpl();
        argDef.setExpression("hoge");
        cd.addArgDef(argDef);
        container.register(cd);
        ConstructorAssembler assembler = new DefaultConstructorConstructorAssembler(cd);
        try {
            assembler.assemble();
            fail("1");
        } catch (IllegalConstructorRuntimeException ex) {
            System.out.println(ex);
        }
    }
    
    public interface Foo {
        public String getHogeName();
    }

    public static class A implements Foo {

        private Hoge hoge_;

        public A(Hoge hoge) {
            hoge_ = hoge;
        }

        public Hoge getHoge() {
            return hoge_;
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