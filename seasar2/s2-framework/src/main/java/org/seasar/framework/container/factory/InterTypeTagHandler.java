/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
import org.seasar.framework.container.InterTypeDef;
import org.seasar.framework.container.impl.InterTypeDefImpl;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.xml.TagHandlerContext;
import org.xml.sax.Attributes;

/**
 * diconファイルの<code>interType</code>要素を解釈するためのクラスです。
 * 
 * @author jundu
 */
public class InterTypeTagHandler extends AbstractTagHandler {
    private static final long serialVersionUID = 1L;

    public void start(final TagHandlerContext context,
            final Attributes attributes) {
        context.push(createInterTypeDef());
    }

    public void end(TagHandlerContext context, String body) {
        final InterTypeDef interTypeDef = (InterTypeDef) context.pop();
        if (!StringUtil.isEmpty(body)) {
            interTypeDef.setExpression(createExpression(context, body));
        }
        ComponentDef componentDef = (ComponentDef) context.peek();
        componentDef.addInterTypeDef(interTypeDef);
    }

    /**
     * インタータイプ定義を作成します。
     * 
     * @return インタータイプ定義
     */
    protected InterTypeDef createInterTypeDef() {
        return new InterTypeDefImpl();
    }
}
