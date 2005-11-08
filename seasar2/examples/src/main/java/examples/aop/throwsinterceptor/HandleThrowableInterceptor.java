package examples.aop.throwsinterceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.ThrowsInterceptor;
import org.seasar.framework.exception.SRuntimeException;

public class HandleThrowableInterceptor extends ThrowsInterceptor {
	
    private static final long serialVersionUID = 7210875523467761009L;

	public void handleThrowable(NullPointerException t,
			MethodInvocation invocation) throws Throwable {

		throw new SRuntimeException("ESSR0007", new Object[] { "à¯êî" });
	}
}