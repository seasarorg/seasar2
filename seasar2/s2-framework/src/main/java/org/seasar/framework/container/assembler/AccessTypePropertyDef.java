package org.seasar.framework.container.assembler;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.BindingTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.util.BindingUtil;

public class AccessTypePropertyDef implements AccessTypeDef {

    public AccessTypePropertyDef() {
    }

    public String getName() {
        return PROPERTY_NAME;
    }

    public void bind(ComponentDef componentDef, PropertyDef propertyDef,
            Object component) {
        final BindingTypeDef bindingTypeDef = propertyDef.getBindingTypeDef();
        bind(componentDef, propertyDef, bindingTypeDef, component);
    }

    public void bind(ComponentDef componentDef, PropertyDef propertyDef,
            BindingTypeDef bindingTypeDef, Object component) {
        final BeanDesc beanDesc = BindingUtil.getBeanDesc(componentDef,
                component);
        final PropertyDesc propertyDesc = beanDesc.getPropertyDesc(propertyDef
                .getPropertyName());
        bindingTypeDef.bind(componentDef, propertyDef, propertyDesc, component);
    }

}
