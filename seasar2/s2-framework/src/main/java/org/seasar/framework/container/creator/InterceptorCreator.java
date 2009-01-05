/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.creator;

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

/**
 * Interceptor用の {@link ComponentCreator}です。
 * 
 * @author higa
 * 
 */
public class InterceptorCreator extends ComponentCreatorImpl {

    /**
     * {@link InterceptorCreator}を作成します。
     * 
     * @param namingConvention
     */
    public InterceptorCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setNameSuffix(namingConvention.getInterceptorSuffix());
        setInstanceDef(InstanceDefFactory.PROTOTYPE);
    }

    /**
     * Interceptor用の {@link ComponentCustomizer}を返します。
     * 
     * @return
     */
    public ComponentCustomizer getInterceptorCustomizer() {
        return getCustomizer();
    }

    /**
     * Interceptor用の {@link ComponentCustomizer}を設定します。
     * 
     * @param customizer
     */
    public void setInterceptorCustomizer(ComponentCustomizer customizer) {
        setCustomizer(customizer);
    }
}