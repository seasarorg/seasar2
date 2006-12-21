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
package org.seasar.framework.container.customizer;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InterTypeDef;
import org.seasar.framework.container.impl.InterTypeDefImpl;
import org.seasar.framework.container.ognl.OgnlExpression;

/**
 * @author koichik
 * 
 */
public class InterTypeCustomizer implements ComponentCustomizer {

    protected final List interTypeNames = new ArrayList();

    public void setInterTypeName(final String interceptorName) {
        interTypeNames.clear();
        interTypeNames.add(interceptorName);
    }

    public void addInterTypeName(final String interTypeName) {
        interTypeNames.add(interTypeName);
    }

    public void customize(final ComponentDef componentDef) {
        for (int i = 0; i < interTypeNames.size(); ++i) {
            final InterTypeDef interTypeDef = new InterTypeDefImpl();
            interTypeDef.setExpression(new OgnlExpression(
                    (String) interTypeNames.get(i)));
            componentDef.addInterTypeDef(interTypeDef);
        }
    }

}
