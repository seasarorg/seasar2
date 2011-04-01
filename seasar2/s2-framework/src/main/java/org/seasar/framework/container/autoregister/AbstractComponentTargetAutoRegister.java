/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.autoregister;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.ClassUtil;

/**
 * コンポーネントを対象にした自動登録を行うための抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractComponentTargetAutoRegister extends
        AbstractAutoRegister {

    public void registerAll() {
        S2Container container = getContainer();
        for (int i = 0; i < container.getComponentDefSize(); ++i) {
            ComponentDef cd = container.getComponentDef(i);
            if (isAppliedComponent(cd)) {
                register(cd);
            }
        }
    }

    /**
     * {@link ComponentDef}を登録します。
     * 
     * @param cd
     */
    protected abstract void register(ComponentDef cd);

    /**
     * 処理対象のコンポーネントかどうか返します。
     * 
     * @param cd
     * @return 処理対象のコンポーネントかどうか
     */
    protected boolean isAppliedComponent(final ComponentDef cd) {
        final Class componentClass = cd.getComponentClass();
        if (componentClass == null) {
            return false;
        }

        final String packageName = ClassUtil.getPackageName(componentClass);
        final String shortClassName = ClassUtil
                .getShortClassName(componentClass);
        for (int i = 0; i < getClassPatternSize(); ++i) {
            final ClassPattern cp = getClassPattern(i);
            if (isIgnore(packageName, shortClassName)) {
                return false;
            }
            if (cp.isAppliedPackageName(packageName)
                    && cp.isAppliedShortClassName(shortClassName)) {
                return true;
            }
        }
        return false;
    }
}
