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
package org.seasar.framework.container.factory;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.impl.InitMethodDefImpl;
import org.seasar.framework.xml.TagHandlerContext;
import org.xml.sax.Attributes;

/**
 * @author higa
 *
 */
public class InitMethodTagHandler extends MethodTagHandler {

    private static final long serialVersionUID = 514017929221501933L;

	/**
	 * @see org.seasar.framework.xml.sax.handler.TagHandler#start(org.seasar.framework.xml.sax.handler.TagHandlerContext, org.xml.sax.Attributes)
	 */
	public void start(TagHandlerContext context, Attributes attributes) {
		String name = attributes.getValue("name");
		context.push(createInitMethodDef(name));
	}

	/**
	 * @see org.seasar.framework.xml.sax.handler.TagHandler#end(org.seasar.framework.xml.sax.handler.TagHandlerContext, java.lang.String)
	 */
	public void end(TagHandlerContext context, String body) {
		InitMethodDef methodDef = (InitMethodDef) context.pop();
		processExpression(methodDef, body, "initMethod");
		ComponentDef componentDef = (ComponentDef) context.peek();
		componentDef.addInitMethodDef(methodDef);
	}

	protected InitMethodDefImpl createInitMethodDef(String name) {
		return new InitMethodDefImpl(name);
    }
}
