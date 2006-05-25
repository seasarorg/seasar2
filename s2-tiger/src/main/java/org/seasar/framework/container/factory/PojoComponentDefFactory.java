package org.seasar.framework.container.factory;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.annotation.tiger.AutoBindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.util.StringUtil;

public class PojoComponentDefFactory implements ComponentDefFactory {

    public PojoComponentDefFactory() {
    }

    public ComponentDef createComponentDef(final Class<?> componentClass,
            final InstanceDef defaultInstanceDef,
            final AutoBindingDef defaultAutoBindingDef) {
        final Component component = componentClass
                .getAnnotation(Component.class);
        if (component == null) {
            return null;
        }

        final ComponentDef componentDef = new ComponentDefImpl(componentClass);

        if (!StringUtil.isEmpty(component.name())) {
            componentDef.setComponentName(component.name());
        }

        final InstanceType instanceType = component.instance();
        if (instanceType != null && !StringUtil.isEmpty(instanceType.getName())) {
            componentDef.setInstanceDef(InstanceDefFactory
                    .getInstanceDef(instanceType.getName()));
        } else {
            componentDef.setInstanceDef(defaultInstanceDef);
        }

        final AutoBindingType autoBindingType = component.autoBinding();
        if (autoBindingType != null
                && !StringUtil.isEmpty(autoBindingType.getName())) {
            componentDef.setAutoBindingDef(AutoBindingDefFactory
                    .getAutoBindingDef(autoBindingType.getName()));
        } else {
            componentDef.setAutoBindingDef(defaultAutoBindingDef);
        }
        return componentDef;
    }
}
