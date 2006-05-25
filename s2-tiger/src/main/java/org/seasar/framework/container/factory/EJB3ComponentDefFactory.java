package org.seasar.framework.container.factory;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.ejb.EJB3Desc;
import org.seasar.framework.util.StringUtil;

public class EJB3ComponentDefFactory implements ComponentDefFactory {

    public EJB3ComponentDefFactory() {
    }

    public ComponentDef createComponentDef(Class<?> componentClass,
            InstanceDef defaultInstanceDef, AutoBindingDef defaultAutoBindingDef) {
        final EJB3Desc ejb3Desc = EJB3Desc.getEJB3Desc(componentClass);
        if (!ejb3Desc.isEJB3()) {
            return null;
        }

        final ComponentDef componentDef = new ComponentDefImpl(componentClass);

        if (!StringUtil.isEmpty(ejb3Desc.getName())) {
            componentDef.setComponentName(ejb3Desc.getName());
        }

        componentDef
                .setInstanceDef(defaultInstanceDef != null ? defaultInstanceDef
                        : InstanceDefFactory.PROTOTYPE);
        componentDef
                .setAutoBindingDef(defaultAutoBindingDef != null ? defaultAutoBindingDef
                        : AutoBindingDefFactory.SEMIAUTO);
        return componentDef;
    }

}
