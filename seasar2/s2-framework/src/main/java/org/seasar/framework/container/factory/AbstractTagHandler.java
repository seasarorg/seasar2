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

import org.seasar.framework.container.Expression;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.xml.TagHandler;
import org.seasar.framework.xml.TagHandlerContext;
import org.xml.sax.Locator;

/**
 * diconファイル解析中、タグに遭遇したときにコールバックされます。
 * <p>
 * {@link org.seasar.framework.container.factory.XmlS2ContainerBuilder}で共通に必要になる機能を、サブクラスに対し提供します。
 * </p>
 * 
 * @author yatsu
 */
public class AbstractTagHandler extends TagHandler {
    private static final long serialVersionUID = 1L;

    /**
     * {@link org.seasar.framework.container.factory.AbstractTagHandler}を構築します。
     */
    public AbstractTagHandler() {
    }

    /**
     * {@link org.seasar.framework.container.ognl.OgnlExpression OGNL式}を生成します。
     * 
     * @param context
     *            {@link org.seasar.framework.xml.TagHandler}のコンテキスト情報
     * @param body
     *            解析対象の文字列
     * @return 生成された{@link org.seasar.framework.container.ognl.OgnlExpression OGNL式}
     * 
     * @throws org.seasar.framework.exception.OgnlRuntimeException
     *             <code>body</code>が、{@link org.seasar.framework.container.ognl.OgnlExpression OGNL式}として不正だった場合
     */
    protected Expression createExpression(final TagHandlerContext context,
            final String body) {
        Locator locator = context.getLocator();
        return new OgnlExpression(locator.getSystemId(), locator
                .getLineNumber(), body.trim());
    }
}
