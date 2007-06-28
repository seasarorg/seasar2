/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory.property;

import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;

/**
 * {@link Binding}アノテーションを読み取り{@link PropertyDef}を作成するコンポーネントの実装クラスです。
 * 
 * @author koichik
 */
public class BindingPropertyDefBuilder extends
        AbstractPropertyDefBuilder<Binding> {

    /**
     * インスタンスを構築します。
     */
    public BindingPropertyDefBuilder() {
    }

    @Override
    protected Class<Binding> getAnnotationType() {
        return Binding.class;
    }

    @Override
    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef, final Binding binding) {
        final PropertyDef propertyDef = createPropertyDef(propertyName,
                accessTypeDef, binding.value());
        propertyDef.setBindingTypeDef(BindingTypeDefFactory
                .getBindingTypeDef(binding.bindingType().getName()));
        return propertyDef;
    }
}
