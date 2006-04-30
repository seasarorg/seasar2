package org.seasar.framework.container.hotdeploy;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.InstanceDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.AnnotationHandlerFactory;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;

public class DefaultComponentFilter implements ComponentFilter {

    public final String instanceDef_BINDING = "bindingType=may";
    
    private InstanceDef instanceDef;
    
    public final String autoBindingDef_BINDING = "bindingType=may";
    
    private AutoBindingDef autoBindingDef;
    
    private String implementationPackageName = "impl";
    
    private String implementationSuffix = "Impl";
    
    public InstanceDef getInstanceDef() {
        return instanceDef;
    }

    public void setInstanceDef(InstanceDef instanceDef) {
        this.instanceDef = instanceDef;
    }

    public AutoBindingDef getAutoBindingDef() {
        return autoBindingDef;
    }

    public void setAutoBindingDef(AutoBindingDef autoBindingDef) {
        this.autoBindingDef = autoBindingDef;
    }

    public String getImplementationPackageName() {
        return implementationPackageName;
    }

    public void setImplementationPackageName(String implementationPackageName) {
        this.implementationPackageName = implementationPackageName;
    }

    public String getImplementationSuffix() {
        return implementationSuffix;
    }

    public void setImplementationSuffix(String implementationSuffix) {
        this.implementationSuffix = implementationSuffix;
    }

    public ComponentDef createComponentDef(S2Container container, Class clazz) {
        return doCreateComponentDef(container, clazz);
    }

    protected ComponentDef doCreateComponentDef(S2Container container, Class clazz) {
        Class targetClass = getTargetClass(clazz);
        AnnotationHandler handler = AnnotationHandlerFactory.getAnnotationHandler();
        return handler.createComponentDef(targetClass, instanceDef, autoBindingDef);
    }
    
    protected Class getTargetClass(Class clazz) {
        if (!clazz.isInterface()) {
            return clazz;
        }
        String packageName = clazz.getPackage().getName();
        String targetClassName = packageName + "." + implementationPackageName + "." + ClassUtil.getShortClassName(clazz) + implementationSuffix;
        if (ResourceUtil.getResourceAsFileNoException(ClassUtil.getResourcePath(targetClassName)) != null) {
            try {
                return Thread.currentThread().getContextClassLoader().loadClass(targetClassName);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return clazz;
    }
}
