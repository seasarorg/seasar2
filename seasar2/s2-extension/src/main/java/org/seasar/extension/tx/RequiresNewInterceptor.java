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
package org.seasar.extension.tx;

import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author higa
 *
 */
public class RequiresNewInterceptor extends AbstractTxInterceptor {
	
	public RequiresNewInterceptor(TransactionManager transactionManager) {
		super(transactionManager);
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Transaction tx = null;
		if (hasTransaction()) {
			tx = suspend();
		}
		Object ret = null;
		try {
			begin();
			try {
				ret = invocation.proceed();
				commit();
			} catch (Throwable t) {
				complete(t);
				throw t;
			}
		} finally {
			if (tx != null) {
				resume(tx);
			}
		}
		return ret;
		
	}
}