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
package org.seasar.framework.container.factory.property;

import javax.annotation.Resource;

import org.seasar.extension.j2ee.JndiResourceLocator;
import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.PropertyDef;

/**
 * {@link Resource}アノテーションを読み取り{@link PropertyDef}を作成するコンポーネントの実装クラスです。
 * 
 * @author koichik
 */
public class ResourcePropertyDefBuilder extends
        AbstractPropertyDefBuilder<Resource> {

    /**
     * インスタンスを構築します。
     */
    public ResourcePropertyDefBuilder() {
    }

    @Override
    protected Class<Resource> getAnnotationType() {
        return Resource.class;
    }

    @Override
    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef, final Resource resource) {
        return createPropertyDef(propertyName, accessTypeDef,
                JndiResourceLocator.resolveName(resource.name()));
    }
}
