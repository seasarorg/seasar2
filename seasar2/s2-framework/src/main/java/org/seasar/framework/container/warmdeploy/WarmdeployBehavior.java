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
package org.seasar.framework.container.warmdeploy;

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerBehavior.DefaultProvider;
import org.seasar.framework.container.util.S2ContainerUtil;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.log.Logger;

/**
 * WARM deploy時にコンポーネントを自動登録する{@link org.seasar.framework.container.factory.S2ContainerFactory.Provider}の実装です。
 * 
 * @author higa
 */
public class WarmdeployBehavior extends DefaultProvider {

    private static final Logger logger = Logger
            .getLogger(WarmdeployBehavior.class);

    private NamingConvention namingConvention;

    private ComponentCreator[] creators = new ComponentCreator[0];

    /**
     * 命名規則を返します。
     * 
     * @return 命名規則
     */
    public NamingConvention getNamingConvention() {
        return namingConvention;
    }

    /**
     * 命名規則を設定します。
     * 
     * @param namingConvention
     *            命名規則
     */
    public void setNamingConvention(NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    /**
     * コンポーネントクリエータの配列を返します。
     * 
     * @return コンポーネントクリエータの配列
     */
    public ComponentCreator[] getCreators() {
        return creators;
    }

    /**
     * コンポーネントクリエータの配列を設定します。
     * 
     * @param creators
     *            コンポーネントクリエータの配列
     */
    public void setCreators(ComponentCreator[] creators) {
        this.creators = creators;
    }

    protected ComponentDef getComponentDef(S2Container container, Object key) {
        synchronized (container.getRoot()) {
            ComponentDef cd = super.getComponentDef(container, key);
            if (cd != null) {
                return cd;
            }
            if (container != container.getRoot()) {
                return null;
            }
            if (key instanceof Class) {
                cd = createComponentDef((Class) key);
            } else if (key instanceof String) {
                cd = createComponentDef((String) key);
                if (cd != null && !key.equals(cd.getComponentName())) {
                    logger.log("WSSR0011", new Object[] { key,
                            cd.getComponentClass().getName(),
                            cd.getComponentName() });
                    cd = null;
                }
            } else {
                throw new IllegalArgumentException("key");
            }
            if (cd != null) {
                SingletonS2ContainerFactory.getContainer().register(cd);
                S2ContainerUtil.putRegisterLog(cd);
                cd.init();
            }
            return cd;
        }
    }

    /**
     * コンポーネント定義を作成します。
     * <p>
     * コンポーネントクリエータを順次呼び出し、コンポーネント定義が作成された場合はそれを返します。
     * どのコンポーネントクリエータからもコンポーネント定義が作成されなかった場合は<code>null</code>を返します。
     * </p>
     * 
     * @param componentClass
     *            コンポーネントのクラス
     * @return コンポーネント定義
     */
    protected ComponentDef createComponentDef(Class componentClass) {
        for (int i = 0; i < creators.length; ++i) {
            ComponentCreator creator = creators[i];
            ComponentDef cd = creator.createComponentDef(componentClass);
            if (cd != null) {
                return cd;
            }
        }
        return null;
    }

    /**
     * コンポーネント定義を作成します。
     * <p>
     * コンポーネントクリエータを順次呼び出し、コンポーネント定義が作成された場合はそれを返します。
     * どのコンポーネントクリエータからもコンポーネント定義が作成されなかった場合は<code>null</code>を返します。
     * </p>
     * 
     * @param componentName
     *            コンポーネント名
     * @return コンポーネント定義
     */
    protected ComponentDef createComponentDef(String componentName) {
        for (int i = 0; i < creators.length; ++i) {
            ComponentCreator creator = creators[i];
            ComponentDef cd = creator.createComponentDef(componentName);
            if (cd != null) {
                return cd;
            }
        }
        return null;
    }

}
