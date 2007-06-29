/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
import org.seasar.framework.container.DestroyMethodDef;
import org.seasar.framework.container.impl.DestroyMethodDefImpl;
import org.seasar.framework.xml.TagHandlerContext;
import org.xml.sax.Attributes;

/**
 * diconファイルの<code>destroyMethod</code>要素を解釈するためのクラスです。
 * 
 * @author higa
 */
public class DestroyMethodTagHandler extends MethodTagHandler {
    private static final long serialVersionUID = 3083124978566352071L;

    public void start(TagHandlerContext context, Attributes attributes) {
        String name = attributes.getValue("name");
        context.push(createDestroyMethodDef(name));
    }

    public void end(TagHandlerContext context, String body) {
        DestroyMethodDef methodDef = (DestroyMethodDef) context.pop();
        processExpression(methodDef, body, "destroyMethod", context);
        ComponentDef componentDef = (ComponentDef) context.peek();
        componentDef.addDestroyMethodDef(methodDef);
    }

    /**
     * 終了メソッド定義を作成します。
     * 
     * @param name
     * @return 終了メソッド定義
     */
    protected DestroyMethodDefImpl createDestroyMethodDef(String name) {
        return new DestroyMethodDefImpl(name);
    }
}
