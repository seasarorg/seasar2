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
package org.seasar.framework.container.ognl;

import java.util.Map;

import org.seasar.framework.container.Expression;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.OgnlUtil;

public class OgnlExpression implements Expression {
    protected String sourceName;

    protected int lineNumber;

    protected String source;

    protected Object parsedExpression;

    public OgnlExpression(final String source) {
        this(null, 0, source);
    }

    public OgnlExpression(final String sourceName, int lineNumber,
            final String source) {
        this.sourceName = sourceName;
        this.lineNumber = lineNumber;
        this.source = source;
        this.parsedExpression = OgnlUtil.parseExpression(source, sourceName,
                lineNumber);
    }

    public Object evaluate(final S2Container container, final Map context) {
        return OgnlUtil.getValue(parsedExpression, context, container,
                sourceName, lineNumber);
    }

    public String getSource() {
        return source;
    }

    public String getSourceName() {
        return sourceName;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
