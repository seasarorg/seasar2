/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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

import org.seasar.framework.container.InterTypeDef;
import org.seasar.framework.container.S2Container;

/**
 * {@link InterTypeDef}を補助するクラスです。
 * 
 */
public class InterTypeDefSupport {

    private List interTypeDefs = new ArrayList();

    private S2Container container;

    /**
     * {@link InterTypeDefSupport}を作成します。
     */
    public InterTypeDefSupport() {
    }

    /**
     * {@link InterTypeDef}を追加します。
     * 
     * @param interTypeDef
     */
    public void addInterTypeDef(InterTypeDef interTypeDef) {
        if (container != null) {
            interTypeDef.setContainer(container);
        }
        interTypeDefs.add(interTypeDef);
    }

    /**
     * {@link InterTypeDef}の数を返します。
     * 
     * @return {@link InterTypeDef}の数
     */
    public int getInterTypeDefSize() {
        return interTypeDefs.size();
    }

    /**
     * {@link InterTypeDef}を返します。
     * 
     * @param index
     * @return {@link InterTypeDef}
     */
    public InterTypeDef getInterTypeDef(int index) {
        return (InterTypeDef) interTypeDefs.get(index);
    }

    /**
     * {@link S2Container}を設定します。
     * 
     * @param container
     */
    public void setContainer(S2Container container) {
        this.container = container;
        for (int i = 0; i < getInterTypeDefSize(); ++i) {
            getInterTypeDef(i).setContainer(container);
        }
    }
}
