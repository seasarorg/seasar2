/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import org.seasar.framework.container.ArgDef;
import org.seasar.framework.container.ArgDefAware;
import org.seasar.framework.container.impl.ArgDefImpl;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.xml.TagHandlerContext;
import org.xml.sax.Attributes;

/**
 * diconファイルの<code>arg</code>要素を解釈するためのクラスです。
 * 
 * @author higa
 */
public class ArgTagHandler extends AbstractTagHandler {
    private static final long serialVersionUID = -6210356712008358336L;

    public void start(TagHandlerContext context, Attributes attributes) {
        context.push(new ArgDefImpl());
    }

    public void end(TagHandlerContext context, String body) {
        ArgDef argDef = (ArgDef) context.pop();
        if (!StringUtil.isEmpty(body)) {
            argDef.setExpression(createExpression(context, body));
        }
        ArgDefAware argDefAware = (ArgDefAware) context.peek();
        argDefAware.addArgDef(argDef);
    }
}
