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
package org.seasar.framework.container.assembler;

import java.lang.reflect.Constructor;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.ClassUnmatchRuntimeException;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.ConstructorAssembler;
import org.seasar.framework.container.Expression;
import org.seasar.framework.container.IllegalConstructorRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ConstructorUtil;

/**
 * コンストラクタアセンブラの抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractConstructorAssembler extends AbstractAssembler
        implements ConstructorAssembler {

    /**
     * @param componentDef
     */
    public AbstractConstructorAssembler(ComponentDef componentDef) {
        super(componentDef);
    }

    public Object assemble() {
        if (getComponentDef().getExpression() != null) {
            return assembleExpression();
        }
        if (getComponentDef().getArgDefSize() > 0) {
            return assembleManual();
        }
        return doAssemble();
    }

    /**
     * コンポーネント組み立てるためのメソッドです。
     * 
     * @return コンポーネント
     */
    protected abstract Object doAssemble();

    /**
     * 式にもとづいてコンポーネントを組み立てます。
     * 
     * @return コンポーネント
     * @throws ClassUnmatchRuntimeException
     *             コンポーネントと定義上のクラスが異なる場合
     */
    protected Object assembleExpression() throws ClassUnmatchRuntimeException {
        ComponentDef cd = getComponentDef();
        S2Container container = cd.getContainer();
        Expression expression = cd.getExpression();
        Object component = expression.evaluate(container, null);
        Class componentClass = cd.getComponentClass();
        if (componentClass != null) {
            if (!componentClass.isInstance(component)) {
                throw new ClassUnmatchRuntimeException(componentClass,
                        component != null ? component.getClass() : null);
            }
        }
        return component;
    }

    /**
     * コンポーネント定義に基づいてコンポーネントを組み立てます。
     * 
     * @return コンポーネント
     */
    protected Object assembleManual() {
        Object[] args = new Object[getComponentDef().getArgDefSize()];
        for (int i = 0; i < args.length; ++i) {
            try {
                args[i] = getComponentDef().getArgDef(i).getValue();
            } catch (ComponentNotFoundRuntimeException cause) {
                throw new IllegalConstructorRuntimeException(getComponentDef()
                        .getComponentClass(), cause);
            }
        }
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(getComponentDef()
                .getConcreteClass());
        return beanDesc.newInstance(args);
    }

    /**
     * デフォルトのコンストラクタを使ってコンポーネントを組み立てます。
     * 
     * @return コンポーネント
     */
    protected Object assembleDefault() {
        Class clazz = getComponentDef().getConcreteClass();
        Constructor constructor = ClassUtil.getConstructor(clazz, null);
        return ConstructorUtil.newInstance(constructor, null);
    }
}