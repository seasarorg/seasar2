/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
 * diconファイルの<code>initMethod</code>要素を解釈するためのクラスです。
 * 
 * @author higa
 */
public class InitMethodTagHandler extends MethodTagHandler {
    private static final long serialVersionUID = 514017929221501933L;

    public void start(TagHandlerContext context, Attributes attributes) {
        String name = attributes.getValue("name");
        context.push(createInitMethodDef(name));
    }

    public void end(TagHandlerContext context, String body) {
        InitMethodDef methodDef = (InitMethodDef) context.pop();
        ComponentDef componentDef = (ComponentDef) context.peek();
        processExpression(methodDef, body, "initMethod", context);
        componentDef.addInitMethodDef(methodDef);
    }

    /**
     * 初期化メソッド定義を作成します。
     * 
     * @param name
     * @return 初期化メソッド定義
     */
    protected InitMethodDefImpl createInitMethodDef(String name) {
        return new InitMethodDefImpl(name);
    }
}
