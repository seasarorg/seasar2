package org.seasar.framework.container.factory.component;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.assembler.AutoBindingDefFactory;
import org.seasar.framework.container.deployer.InstanceDefFactory;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.ComponentDefBuilder;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.ejb.EJB3Desc;
import org.seasar.framework.ejb.EJB3DescFactory;
import org.seasar.framework.util.StringUtil;

public class EJB3ComponentDefBuilder implements ComponentDefBuilder {

    public EJB3ComponentDefBuilder() {
    }

    public ComponentDef createComponentDef(
            final AnnotationHandler annotationHandler, Class<?> componentClass,
            InstanceDef defaultInstanceDef,
            AutoBindingDef defaultAutoBindingDef, boolean defaultExternalBinding) {
        final EJB3Desc ejb3Desc = EJB3DescFactory.getEJB3Desc(componentClass);
        if (ejb3Desc == null) {
            return null;
        }

        final ComponentDef componentDef = new ComponentDefImpl(componentClass);
        if (!StringUtil.isEmpty(ejb3Desc.getName())) {
            componentDef.setComponentName(ejb3Desc.getName());
        }
        componentDef.setInstanceDef(getInstanceDef(defaultInstanceDef));
        componentDef
                .setAutoBindingDef(getAutoBindingDef(defaultAutoBindingDef));
        componentDef.setExternalBinding(defaultExternalBinding);
        return componentDef;
    }

    protected InstanceDef getInstanceDef(final InstanceDef defaultInstanceDef) {
        return defaultInstanceDef != null ? defaultInstanceDef
                : InstanceDefFactory.PROTOTYPE;
    }

    protected AutoBindingDef getAutoBindingDef(
            final AutoBindingDef defaultAutoBindingDef) {
        return defaultAutoBindingDef != null ? defaultAutoBindingDef
                : AutoBindingDefFactory.SEMIAUTO;
    }

}