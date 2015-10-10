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

import org.seasar.framework.container.Expression;
import org.seasar.framework.container.impl.ComponentNameExpression;
import org.seasar.framework.container.impl.LiteralExpression;
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
     * <code>AbstractTagHandler</code>を構築します。
     */
    public AbstractTagHandler() {
    }

    /**
     * {@link org.seasar.framework.container.Expression 式}を生成します。
     * <p>
     * 解析対象の文字列が<code>null</code>、<code>true</code>、<code>false</code>であれば
     * {@link LiteralExpression リテラル式}を生成します。 解析対象の文字列が単一のJava識別子であれば{@link ComponentNameExpression コンポーネント名式}を生成します。
     * それ以外の場合は{@link OgnlExpression OGNL式}を生成します。
     * </p>
     * 
     * @param context
     *            {@link org.seasar.framework.xml.TagHandler}のコンテキスト情報
     * @param body
     *            解析対象の文字列
     * @return 生成された{@link Expression 式}
     * 
     * @throws org.seasar.framework.exception.OgnlRuntimeException
     *             <code>body</code>が、{@link OgnlExpression OGNL式}として不正だった場合
     */
    protected Expression createExpression(final TagHandlerContext context,
            final String body) {
        Locator locator = context.getLocator();
        final String expr = body.trim();
        final String source = locator.getSystemId();
        final int lineNumber = locator.getLineNumber();
        if ("null".equals(expr)) {
            return new LiteralExpression(source, lineNumber, expr, null);
        }
        if ("true".equals(expr)) {
            return new LiteralExpression(source, lineNumber, expr, Boolean.TRUE);
        }
        if ("false".equals(expr)) {
            return new LiteralExpression(source, lineNumber, expr,
                    Boolean.FALSE);
        }
        if (isComponentName(expr)) {
            return new ComponentNameExpression(locator.getSystemId(), locator
                    .getLineNumber(), expr);
        }
        return new OgnlExpression(locator.getSystemId(), locator
                .getLineNumber(), expr);
    }

    /**
     * 式が単なるコンポーネント名であれば<code>true</code>を、それ以外の場合は<code>false</code>を返します。
     * 
     * @param expr
     *            式
     * @return 式が単なるコンポーネント名であれば<code>true</code>
     */
    protected static boolean isComponentName(final String expr) {
        if (!Character.isJavaIdentifierStart(expr.charAt(0))) {
            return false;
        }
        for (int i = 1; i < expr.length(); ++i) {
            if (!Character.isJavaIdentifierPart(expr.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
