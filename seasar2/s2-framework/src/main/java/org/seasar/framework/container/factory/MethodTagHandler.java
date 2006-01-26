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
package org.seasar.framework.container.factory;

import org.seasar.framework.container.MethodDef;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.xml.TagHandler;

/**
 * @author higa
 *
 */
public abstract class MethodTagHandler extends TagHandler {

	protected void processExpression(
		MethodDef methodDef,
		String expression,
		String tagName) {

		String expr = expression;
		if (expr != null) {
			expr = expr.trim();
			if (!StringUtil.isEmpty(expr)) {
				methodDef.setExpression(expr);
			} else {
				expr = null;
			}
		}
		if (methodDef.getMethodName() == null && expr == null) {
			throw new TagAttributeNotDefinedRuntimeException(tagName, "name");
		}
	}

}
