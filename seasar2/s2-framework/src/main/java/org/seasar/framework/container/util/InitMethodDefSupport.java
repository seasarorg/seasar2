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
package org.seasar.framework.container.util;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.S2Container;

/**
 * {@link InitMethodDef}を補助するクラスです。
 * 
 * @author higa
 * 
 */
public class InitMethodDefSupport {

    private List methodDefs = new ArrayList();

    private S2Container container;

    /**
     * {@link InitMethodDefSupport}を作成します。
     */
    public InitMethodDefSupport() {
    }

    /**
     * {@link InitMethodDef}を追加します。
     * 
     * @param methodDef
     */
    public void addInitMethodDef(InitMethodDef methodDef) {
        if (container != null) {
            methodDef.setContainer(container);
        }
        methodDefs.add(methodDef);
    }

    /**
     * {@link InitMethodDef}の数を返します。
     * 
     * @return {@link InitMethodDef}の数
     */
    public int getInitMethodDefSize() {
        return methodDefs.size();
    }

    /**
     * {@link InitMethodDef}を返します。
     * 
     * @param index
     * @return {@link InitMethodDef}
     */
    public InitMethodDef getInitMethodDef(int index) {
        return (InitMethodDef) methodDefs.get(index);
    }

    /**
     * {@link S2Container}を返します。
     * 
     * @param container
     */
    public void setContainer(S2Container container) {
        this.container = container;
        for (int i = 0; i < getInitMethodDefSize(); ++i) {
            getInitMethodDef(i).setContainer(container);
        }
    }
}