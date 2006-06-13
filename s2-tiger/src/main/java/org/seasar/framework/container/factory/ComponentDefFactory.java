package org.seasar.framework.container.factory;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;

public interface ComponentDefFactory {
    ComponentDef createComponentDef(Class<?> componentClass,
            InstanceDef defaultInstanceDef, AutoBindingDef defaultAutoBindingDef,
            boolean defaultExternalBinding);
}
