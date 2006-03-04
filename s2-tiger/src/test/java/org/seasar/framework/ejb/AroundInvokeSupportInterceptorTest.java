package org.seasar.framework.ejb;

import java.lang.reflect.Method;

import javax.interceptor.InvocationContext;

import junit.framework.TestCase;

import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.proxy.AopProxy;

public class AroundInvokeSupportInterceptorTest extends TestCase {

    public AroundInvokeSupportInterceptorTest() {
    }

    public AroundInvokeSupportInterceptorTest(String name) {
        super(name);
    }

    public void test() throws Exception {
        Method method = HogeImpl.class.getMethod("around", new Class<?>[] {InvocationContext.class});
        Aspect aspect = new AspectImpl(new AroundInvokeSupportInterceptor(method));
        Hoge hoge = (Hoge) new AopProxy(HogeImpl.class, new Aspect[] { aspect }).create();
        assertEquals("1", "origin-before-foo-after", hoge.foo("origin"));
        
    }
    
    public interface Hoge {
        String foo(String param);
    }
    
    public static class HogeImpl implements Hoge {
        public String foo(String param) {
            return param + "-foo";
        }

        public Object around(InvocationContext context) throws Exception {
            String param = (String) context.getParameters()[0];
            context.setParameters(new Object[] {param + "-before"});
            String result = (String) context.proceed();
            return result + "-after";
        }
    }
}
