package org.seasar.framework.container.factory;

import javax.annotation.Resource;

import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.PropertyDef;

public class ResourcePropertyDefFactory extends
        AbstractPropertyDefFactory<Resource> {

    public ResourcePropertyDefFactory() {
    }

    @Override
    protected Class getAnnotationType() {
        return Resource.class;
    }

    @Override
    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef, final Resource resource) {
        return createPropertyDef(propertyName, accessTypeDef, resource.name());
    }
}
