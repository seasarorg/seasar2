package examples.aop.originalinterceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;

public class MeasurementInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = -7570371617884305963L;

	public Object invoke(MethodInvocation invocation) throws Throwable {
		long start = 0;
		long end = 0;
		StringBuffer buf = new StringBuffer(100);

		buf.append(getTargetClass(invocation).getName());
		buf.append("#");
		buf.append(invocation.getMethod().getName());
		buf.append("(");
		Object[] args = invocation.getArguments();
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; ++i) {
				buf.append(args[i]);
				buf.append(", ");
			}
			buf.setLength(buf.length() - 2);
		}
		buf.append(")");
		try {
			start = System.currentTimeMillis();
			Object ret = invocation.proceed();
			end = System.currentTimeMillis();
			buf.append(" : ");
			return ret;
		} catch (Throwable t) {
			buf.append(" Throwable:");
			buf.append(t);
			throw t;
		} finally {
			System.out.println(buf.toString() + (end - start));
		}
	}

}