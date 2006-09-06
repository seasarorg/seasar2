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
package org.seasar.extension.dxo.command.impl;

import java.lang.reflect.Method;
import java.util.Map;

import org.seasar.extension.dxo.util.DxoUtil;
import org.seasar.framework.util.OgnlUtil;

/**
 * @author koichik
 * 
 */
public class BeanToMapDxoCommand extends AbstractDxoCommand {

    protected Object parsedExpression;

    public BeanToMapDxoCommand(final Method method, final String expression) {
        super(method);
        parsedExpression = DxoUtil.parseMap(expression);
    }

    protected Object convertScalar(final Object source) {
        return OgnlUtil.getValue(parsedExpression, source);
    }

    protected void copy(final Object src, final Object dest) {
        final Map srcMap = (Map) src;
        final Map destMap = (Map) dest;
        destMap.clear();
        destMap.putAll(srcMap);
    }

    protected Class getDestElementType() {
        return Map.class;
    }

}
