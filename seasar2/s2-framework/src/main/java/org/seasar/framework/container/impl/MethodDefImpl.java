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
package org.seasar.framework.container.impl;

import org.seasar.framework.container.ArgDef;
import org.seasar.framework.container.Expression;
import org.seasar.framework.container.MethodDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.util.ArgDefSupport;

/**
 * @author higa
 * 
 */
public abstract class MethodDefImpl implements MethodDef {

    private String methodName;

    private ArgDefSupport argDefSupport = new ArgDefSupport();

    private S2Container container;

    private Expression expression;

    public MethodDefImpl() {
    }

    public MethodDefImpl(String methodName) {
        this.methodName = methodName;
    }

    /**
     * @see org.seasar.framework.container.MethodDef#getMethodName()
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @see org.seasar.framework.container.MethodDef#addArgDef(org.seasar.framework.container.ArgDef)
     */
    public void addArgDef(ArgDef argDef) {
        argDefSupport.addArgDef(argDef);
    }

    /**
     * @see org.seasar.framework.container.ArgDefAware#getArgDefSize()
     */
    public int getArgDefSize() {
        return argDefSupport.getArgDefSize();
    }

    /**
     * @see org.seasar.framework.container.ArgDefAware#getArgDef(int)
     */
    public ArgDef getArgDef(int index) {
        return argDefSupport.getArgDef(index);
    }

    /**
     * @see org.seasar.framework.container.MethodDef#getArgs()
     */
    public Object[] getArgs() {
        Object[] args = new Object[getArgDefSize()];
        for (int i = 0; i < getArgDefSize(); ++i) {
            args[i] = getArgDef(i).getValue();
        }
        return args;
    }

    /**
     * @see org.seasar.framework.container.MethodDef#getContainer()
     */
    public S2Container getContainer() {
        return container;
    }

    /**
     * @see org.seasar.framework.container.MethodDef#setContainer(org.seasar.framework.container.S2Container)
     */
    public void setContainer(S2Container container) {
        this.container = container;
        argDefSupport.setContainer(container);
    }

    /**
     * @see org.seasar.framework.container.MethodDef#getExpression()
     */
    public Expression getExpression() {
        return expression;
    }

    /**
     * @see org.seasar.framework.container.MethodDef#setExpression(java.lang.String)
     */
    public void setExpression(Expression expression) {
        this.expression = expression;
    }
}
