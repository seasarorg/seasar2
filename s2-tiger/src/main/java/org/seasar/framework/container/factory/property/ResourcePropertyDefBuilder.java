package org.seasar.framework.container.factory.property;

import javax.annotation.Resource;

import org.seasar.extension.j2ee.JndiResourceLocator;
import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.PropertyDef;

public class ResourcePropertyDefBuilder extends
        AbstractPropertyDefBuilder<Resource> {

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
