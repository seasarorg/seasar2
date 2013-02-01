/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.assembler;

import java.lang.reflect.Constructor;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.util.BindingUtil;
import org.seasar.framework.util.ConstructorUtil;

/**
 * コンストラクタアセンブラの自動版です。
 * 
 * @author higa
 * 
 */
public class AutoConstructorAssembler extends AbstractConstructorAssembler {

    /**
     * {@link AutoConstructorAssembler}を作成します。
     * 
     * @param componentDef
     */
    public AutoConstructorAssembler(ComponentDef componentDef) {
        super(componentDef);
    }

    protected Object doAssemble() {
        Constructor constructor = getSuitableConstructor();
        if (constructor == null) {
            return assembleDefault();
        }
        Object[] args = getArgs(constructor.getParameterTypes());
        return ConstructorUtil.newInstance(constructor, args);
    }

    /**
     * 適した {@link Constructor}を返します。
     * 
     * @return 適した {@link Constructor}
     */
    protected Constructor getSuitableConstructor() {
        int argSize = -1;
        Constructor constructor = null;
        Constructor[] constructors = getComponentDef().getConcreteClass()
                .getConstructors();
        for (int i = 0; i < constructors.length; ++i) {
            int tempArgSize = constructors[i].getParameterTypes().length;
            if (tempArgSize == 0) {
                return null;
            }
            if (tempArgSize > argSize
                    && BindingUtil.isAutoBindable(constructors[i]
                            .getParameterTypes())) {
                constructor = constructors[i];
                argSize = tempArgSize;
            }
        }
        return constructor;
    }
}
