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
package org.seasar.extension.sql.node;

import org.seasar.extension.sql.SqlContext;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 * 
 */
public class EmbeddedValueNode extends AbstractNode {

    private String expression;

    private String baseName;

    private String propertyName;

    public EmbeddedValueNode(String expression) {
        this.expression = expression;
        String[] array = StringUtil.split(expression, ".");
        this.baseName = array[0];
        if (array.length > 1) {
            this.propertyName = array[1];
        }
    }

    public String getExpression() {
        return expression;
    }

    public void accept(SqlContext ctx) {
        Object value = ctx.getArg(baseName);
        Class clazz = ctx.getArgType(baseName);
        if (propertyName != null) {
            BeanDesc beanDesc = BeanDescFactory.getBeanDesc(clazz);
            PropertyDesc pd = beanDesc.getPropertyDesc(propertyName);
            value = pd.getValue(value);
            clazz = pd.getPropertyType();
        }
        if (value != null) {
            ctx.addSql(value.toString());
        }
    }
}