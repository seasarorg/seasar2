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

import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.impl.AspectDefImpl;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.xml.TagHandler;
import org.seasar.framework.xml.TagHandlerContext;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;

/**
 * @author higa
 * 
 */
public class AspectTagHandler extends TagHandler {

    private static final long serialVersionUID = 5619707344253136193L;

    /**
     * @see org.seasar.framework.xml.sax.handler.TagHandler#start(org.seasar.framework.xml.sax.handler.TagHandlerContext,
     *      org.xml.sax.Attributes)
     */
    public void start(TagHandlerContext context, Attributes attributes) {
        AspectDef aspectDef = null;
        String pointcutStr = attributes.getValue("pointcut");
        if (pointcutStr != null) {
            String[] methodNames = StringUtil.split(pointcutStr, ", ");
            aspectDef = createAspectDef(createPointcut(methodNames));
        } else {
            aspectDef = createAspectDef();
        }
        context.push(aspectDef);
    }

    /**
     * @see org.seasar.framework.xml.sax.handler.TagHandler#end(org.seasar.framework.xml.sax.handler.TagHandlerContext,
     *      java.lang.String)
     */
    public void end(TagHandlerContext context, String body) {
        AspectDef aspectDef = (AspectDef) context.pop();
        if (!StringUtil.isEmpty(body)) {
            Locator locator = context.getLocator();
            aspectDef.setExpression(body, locator.getSystemId(), locator
                    .getLineNumber());
        }
        ComponentDef componentDef = (ComponentDef) context.peek();
        componentDef.addAspectDef(aspectDef);
    }

    protected AspectDefImpl createAspectDef() {
        return new AspectDefImpl();
    }

    protected AspectDefImpl createAspectDef(Pointcut pointcut) {
        return new AspectDefImpl(pointcut);
    }

    protected Pointcut createPointcut(String[] methodNames) {
        return new PointcutImpl(methodNames);
    }
}
