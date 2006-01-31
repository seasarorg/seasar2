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
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.Expression;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.util.MetaDefSupport;

/**
 * @author higa
 * 
 */
public class ArgDefImpl implements ArgDef {

    private Object value;

    private S2Container container;

    private Expression expression;

    private ComponentDef childComponentDef;

    private MetaDefSupport metaDefSupport = new MetaDefSupport();

    public ArgDefImpl() {
    }

    public ArgDefImpl(Object value) {
        setValue(value);
    }

    public final Object getValue() {
        if (expression != null) {
            return expression.evaluate(container, null);
        }
        if (childComponentDef != null) {
            return childComponentDef.getComponent();
        }
        return value;
    }

    public final void setValue(Object value) {
        this.value = value;
    }

    public boolean isValueGettable() {
        return value != null || childComponentDef != null || expression != null;
    }

    /**
     * @see org.seasar.framework.container.ArgDef#getContainer()
     */
    public final S2Container getContainer() {
        return container;
    }

    /**
     * @see org.seasar.framework.container.ArgDef#setContainer(org.seasar.framework.container.S2Container)
     */
    public final void setContainer(S2Container container) {
        this.container = container;
        if (childComponentDef != null) {
            childComponentDef.setContainer(container);
        }
        metaDefSupport.setContainer(container);
    }

    /**
     * @see org.seasar.framework.container.ArgDef#getExpression()
     */
    public final Expression getExpression() {
        return expression;
    }

    /**
     * @see org.seasar.framework.container.ArgDef#setExpression(java.lang.String)
     */
    public final void setExpression(Expression expression) {
        this.expression = expression;
    }

    /**
     * @see org.seasar.framework.container.ArgDef#setChildComponentDef(org.seasar.framework.container.ComponentDef)
     */
    public final void setChildComponentDef(ComponentDef componentDef) {
        if (container != null) {
            componentDef.setContainer(container);
        }
        childComponentDef = componentDef;
    }

    /**
     * @see org.seasar.framework.container.MetaDefAware#addMetaDef(org.seasar.framework.container.MetaDef)
     */
    public void addMetaDef(MetaDef metaDef) {
        metaDefSupport.addMetaDef(metaDef);
    }

    /**
     * @see org.seasar.framework.container.MetaDefAware#getMetaDef(int)
     */
    public MetaDef getMetaDef(int index) {
        return metaDefSupport.getMetaDef(index);
    }

    /**
     * @see org.seasar.framework.container.MetaDefAware#getMetaDef(java.lang.String)
     */
    public MetaDef getMetaDef(String name) {
        return metaDefSupport.getMetaDef(name);
    }

    /**
     * @see org.seasar.framework.container.MetaDefAware#getMetaDefs(java.lang.String)
     */
    public MetaDef[] getMetaDefs(String name) {
        return metaDefSupport.getMetaDefs(name);
    }

    /**
     * @see org.seasar.framework.container.MetaDefAware#getMetaDefSize()
     */
    public int getMetaDefSize() {
        return metaDefSupport.getMetaDefSize();
    }
}
