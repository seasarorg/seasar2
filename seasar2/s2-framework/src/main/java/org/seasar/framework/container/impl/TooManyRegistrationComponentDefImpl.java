/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.TooManyRegistrationComponentDef;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;

/**
 * 1つのキーに複数のコンポーネントが登録されるときの {@link ComponentDef}です。
 * 
 * @author higa
 * 
 */
public class TooManyRegistrationComponentDefImpl extends SimpleComponentDef
        implements TooManyRegistrationComponentDef {

    private Object key;

    private List componentDefs = new ArrayList();

    /**
     * {@link TooManyRegistrationComponentDefImpl}を作成します。
     * 
     * @param key
     */
    public TooManyRegistrationComponentDefImpl(Object key) {
        this.key = key;
    }

    public void addComponentDef(ComponentDef cd) {
        componentDefs.add(cd);
    }

    public Object getComponent() throws TooManyRegistrationRuntimeException {
        throw new TooManyRegistrationRuntimeException(key,
                getComponentClasses());
    }

    /**
     * {@link ComponentDef}の数を返します。
     * 
     * @return {@link ComponentDef}の数
     */
    public int getComponentDefSize() {
        return componentDefs.size();
    }

    /**
     * {@link ComponentDef}を返します。
     * 
     * @param index
     * @return {@link ComponentDef}
     */
    public ComponentDef getComponentDef(int index) {
        return (ComponentDef) componentDefs.get(index);
    }

    public ComponentDef[] getComponentDefs() {
        return (ComponentDef[]) componentDefs
                .toArray(new ComponentDef[getComponentDefSize()]);
    }

    public Class[] getComponentClasses() {
        Class[] classes = new Class[getComponentDefSize()];
        for (int i = 0; i < classes.length; ++i) {
            classes[i] = getComponentDef(i).getComponentClass();
        }
        return classes;
    }
}