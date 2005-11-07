/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
package org.seasar.framework.util;

import java.util.Map;

import org.seasar.framework.exception.OgnlRuntimeException;

import ognl.Ognl;
import ognl.OgnlException;

/**
 * @author higa
 *
 */
public final class OgnlUtil {

	private OgnlUtil() {
	}

	public static Object getValue(Object exp, Object root) {
		try {
			return Ognl.getValue(exp, root);
		} catch (OgnlException ex) {
			throw new OgnlRuntimeException(ex);
		}
	}
	
	public static Object getValue(Object exp, Map ctx, Object root) {
		try {
			return Ognl.getValue(exp, ctx, root);
		} catch (OgnlException ex) {
			throw new OgnlRuntimeException(ex);
		}
	}
	
	public static Object parseExpression(String expression) {
		try {
			return Ognl.parseExpression(expression);
		} catch (OgnlException ex) {
			System.err.println("parseExpression[" + expression + "]");
			throw new OgnlRuntimeException(ex);
		}
	}
}
