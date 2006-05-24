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
package examples.aop.throwsinterceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.ThrowsInterceptor;
import org.seasar.framework.exception.SRuntimeException;

public class HandleThrowableInterceptor extends ThrowsInterceptor {
    
    private static final long serialVersionUID = 7210875523467761009L;

    public void handleThrowable(NullPointerException t,
            MethodInvocation invocation) throws Throwable {

        throw new SRuntimeException("ESSR0007", new Object[] { "引数" });
    }
}