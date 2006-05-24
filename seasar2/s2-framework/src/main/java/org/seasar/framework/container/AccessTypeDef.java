package org.seasar.framework.container;

public interface AccessTypeDef {

    String PROPERTY_NAME = "property";

    String FIELD_NAME = "field";

    String getName();

    void bind(ComponentDef componentDef, PropertyDef propertyDef,
            Object component);

    void bind(ComponentDef componentDef, PropertyDef propertyDef,
            BindingTypeDef bindingTypeDef, Object component);
}
