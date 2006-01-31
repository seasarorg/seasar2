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

import org.seasar.framework.container.BindingTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.xml.TagHandlerContext;
import org.xml.sax.Attributes;

/**
 * @author higa
 * 
 */
public class PropertyTagHandler extends AbstractTagHandler {
    private static final long serialVersionUID = -8153752681379626269L;

    /**
     * @see org.seasar.framework.xml.sax.handler.TagHandler#start(org.seasar.framework.xml.sax.handler.TagHandlerContext,
     *      org.xml.sax.Attributes)
     */
    public void start(TagHandlerContext context, Attributes attributes) {
        String name = attributes.getValue("name");
        if (name == null) {
            throw new TagAttributeNotDefinedRuntimeException("property", "name");
        }
        PropertyDef pd = createPropertyDef(name);
        String bindingTypeName = attributes.getValue("bindingType");
        if (bindingTypeName != null) {
            BindingTypeDef bindingTypeDef = BindingTypeDefFactory
                    .getBindingTypeDef(bindingTypeName);
            pd.setBindingTypeDef(bindingTypeDef);
        }
        context.push(pd);
    }

    /**
     * @see org.seasar.framework.xml.sax.handler.TagHandler#end(org.seasar.framework.xml.sax.handler.TagHandlerContext,
     *      java.lang.String)
     */
    public void end(TagHandlerContext context, String body) {
        PropertyDef propertyDef = (PropertyDef) context.pop();
        if (!StringUtil.isEmpty(body)) {
            propertyDef.setExpression(createExpression(context, body));
        }
        ComponentDef componentDef = (ComponentDef) context.peek();
        componentDef.addPropertyDef(propertyDef);
    }

    protected PropertyDefImpl createPropertyDef(String name) {
        return new PropertyDefImpl(name);
    }
}
