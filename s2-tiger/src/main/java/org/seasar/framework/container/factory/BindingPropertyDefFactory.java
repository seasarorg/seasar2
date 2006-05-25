package org.seasar.framework.container.factory;

import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;

public class BindingPropertyDefFactory extends
        AbstractPropertyDefFactory<Binding> {

    public BindingPropertyDefFactory() {
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
