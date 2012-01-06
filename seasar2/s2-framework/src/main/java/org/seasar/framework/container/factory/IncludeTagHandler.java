/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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

import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.env.Env;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.xml.TagHandlerContext;
import org.xml.sax.Attributes;

/**
 * diconファイルの<code>include</code>要素を解釈するためのクラスです。
 * 
 * @author higa
 */
public class IncludeTagHandler extends AbstractTagHandler {

    private static final long serialVersionUID = 7770349626071675269L;

    public void start(TagHandlerContext context, Attributes attributes) {
        String path = attributes.getValue("path");
        if (path == null) {
            throw new TagAttributeNotDefinedRuntimeException("include", "path");
        }
        S2Container container = (S2Container) context.peek();
        String condition = attributes.getValue("condition");
        if (!StringUtil.isEmpty(condition)) {
            Map map = new HashMap();
            map.put("ENV", Env.getValue());
            Object o = createExpression(context, condition).evaluate(container,
                    map);
            if (!(o instanceof Boolean)) {
                throw new IllegalStateException("condition:" + condition);
            }
            if (!((Boolean) o).booleanValue()) {
                return;
            }
        }
        S2ContainerFactory.include(container, path);
    }
}