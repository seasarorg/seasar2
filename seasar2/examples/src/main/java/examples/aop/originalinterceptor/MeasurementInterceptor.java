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