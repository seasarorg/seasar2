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
package org.seasar.framework.container.creator;

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentCustomizer;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.convention.NamingConvention;

/**
 * Validator用の {@link ComponentCreator}です。
 * 
 * @author higa
 * 
 */
public class ValidatorCreator extends ComponentCreatorImpl {

    /**
     * {@link ValidatorCreator}を作成します。
     * 
     * @param namingConvention
     */
    public ValidatorCreator(NamingConvention namingConvention) {
        super(namingConvention);
        setNameSuffix(namingConvention.getValidatorSuffix());
        setInstanceDef(InstanceDefFactory.PROTOTYPE);
    }

    /**
     * Validator用のカスタマイザを返します。
     * 
     * @return
     */
    public ComponentCustomizer getValidatorCustomizer() {
        return getCustomizer();
    }

    /**
     * Validator用のカスタマイザを設定します。
     * 
     * @param customizer
     */
    public void setValidatorCustomizer(ComponentCustomizer customizer) {
        setCustomizer(customizer);
    }
}