/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.util.BindingUtil;
import org.seasar.framework.log.Logger;

/**
 * コンポーネントアセンブラの抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractAssembler {

    private static Logger logger = Logger.getLogger(AbstractAssembler.class);

    private ComponentDef componentDef;

    /**
     * {@link AbstractAssembler}のコンストラクタです。
     * 
     * @param componentDef
     */
    public AbstractAssembler(ComponentDef componentDef) {
        this.componentDef = componentDef;
    }

    /**
     * {@link ComponentDef}を返します。
     * 
     * @return {@link ComponentDef}
     */
    protected final ComponentDef getComponentDef() {
        return componentDef;
    }

    /**
     * {@link BeanDesc}を返します。
     * 
     * @param component
     * @return {@link BeanDesc}
     * @see BindingUtil#getBeanDesc(ComponentDef, Object)
     */
    protected BeanDesc getBeanDesc(Object component) {
        return BindingUtil.getBeanDesc(getComponentDef(), component);
    }

    /**
     * コンポーネントのクラスを返します。
     * 
     * @param component
     * @return コンポーネントのクラス
     * @see BindingUtil#getComponentClass(ComponentDef, Object)
     */
    protected Class getComponentClass(Object component) {
        return BindingUtil.getComponentClass(getComponentDef(), component);
    }

    /**
     * 引数を返します。
     * 
     * @param argTypes
     * @return 引数
     */
    protected Object[] getArgs(Class[] argTypes) {
        Object[] args = new Object[argTypes.length];
        for (int i = 0; i < argTypes.length; ++i) {
            try {
                args[i] = getComponentDef().getContainer().getComponent(
                        argTypes[i]);
            } catch (ComponentNotFoundRuntimeException ex) {
                logger.log("WSSR0007", new Object[] {
                        getComponentDef().getComponentClass().getName(),
                        ex.getComponentKey() });
                args[i] = null;
            }
        }
        return args;
    }
}