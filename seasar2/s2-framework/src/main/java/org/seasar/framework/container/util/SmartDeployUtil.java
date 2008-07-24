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
package org.seasar.framework.container.util;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.cooldeploy.S2ContainerFactoryCoolProvider;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.S2ContainerFactory.Provider;
import org.seasar.framework.container.hotdeploy.HotdeployUtil;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.container.warmdeploy.WarmdeployBehavior;
import org.seasar.framework.util.FieldUtil;

/**
 * SMART deploy用のユーティリティクラスです。
 * 
 * @author shot
 * 
 */
public class SmartDeployUtil {

    private SmartDeployUtil() {
    }

    /**
     * SMART deployかどうか返します。
     * 
     * @param container
     * @return SMART deployかどうか
     */
    public static boolean isSmartdeployMode(S2Container container) {
        return isHotdeployMode(container) || isCooldeployMode(container)
                || isWarmdeployMode(container);
    }

    /**
     * HOT deployかどうか返します。
     * 
     * @param container
     * @return HOT deployかどうか
     */
    public static boolean isHotdeployMode(S2Container container) {
        return HotdeployUtil.isHotdeploy();
    }

    /**
     * COOL deployかどうか返します。
     * 
     * @param container
     * @return COOL deployかどうか
     */
    public static boolean isCooldeployMode(S2Container container) {
        BeanDesc bd = BeanDescFactory.getBeanDesc(S2ContainerFactory.class);
        S2ContainerFactory.Provider provider = (Provider) FieldUtil.get(bd
                .getField("provider"), null);
        if (provider instanceof S2ContainerFactoryCoolProvider) {
            return true;
        }
        return false;
    }

    /**
     * WARM deployかどうか返します。
     * 
     * @param container
     * @return WARM deployかどうか
     */
    public static boolean isWarmdeployMode(S2Container container) {
        S2ContainerBehavior.Provider provider = S2ContainerBehavior
                .getProvider();
        return provider instanceof WarmdeployBehavior;
    }

    /**
     * Deployモードを返します。
     * 
     * @param container
     * @return Deployモード
     */
    public static String getDeployMode(S2Container container) {
        if (isHotdeployMode(container)) {
            return "Hot Deploy";
        } else if (isCooldeployMode(container)) {
            return "Cool Deploy";
        } else if (isWarmdeployMode(container)) {
            return "Warm Deploy";
        } else {
            return "Normal Mode";
        }
    }
}
