package org.seasar.framework.container.assembler;

import java.lang.reflect.Field;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.BindingTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.util.BindingUtil;

public class AccessTypeFieldDef implements AccessTypeDef {

    public AccessTypeFieldDef() {
    }

    public String getName() {
        return FIELD_NAME;
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
        final Field field = beanDesc.getField(propertyDef.getPropertyName());
        bindingTypeDef.bind(componentDef, propertyDef, field, component);
    }
}
