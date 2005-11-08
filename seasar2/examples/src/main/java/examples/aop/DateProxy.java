package examples.aop;

import java.util.Date;

import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.interceptors.TraceInterceptor;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.aop.proxy.AopProxy;

public class DateProxy {

	public static void main(String[] args) {
		Pointcut pointcut = new PointcutImpl(new String[]{"getTime"});
		Aspect aspect = new AspectImpl(new TraceInterceptor(), pointcut);
		AopProxy aopProxy = new AopProxy(Date.class, new Aspect[]{aspect});
		Date proxy = (Date) aopProxy.create();
		proxy.getTime();
		System.out.println(byte[].class.getName());
	}
}
