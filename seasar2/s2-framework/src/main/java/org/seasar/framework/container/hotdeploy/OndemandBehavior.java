package org.seasar.framework.container.hotdeploy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerBehavior.DefaultProvider;
import org.seasar.framework.container.util.S2ContainerUtil;
import org.seasar.framework.exception.EmptyRuntimeException;

public class OndemandBehavior extends DefaultProvider implements HotdeployListener  {

    private ClassLoader originalClassLoader;
    
    private HotdeployClassLoader hotdeployClassLoader;
    
    private List componentFilters = new ArrayList();
    
    private String rootPackageName;
    
    private Map componentDefCache = new HashMap();
    
    public ComponentFilter getComponentFilter(int index) {
        return (ComponentFilter) componentFilters.get(index);
    }
    
    public int getComponentFilterSize() {
        return componentFilters.size();
    }
    
    public void addComponentFilter(ComponentFilter componentFilter) {
        componentFilters.add(componentFilter);
    }
    
    public String getRootPackageName() {
        return rootPackageName;
    }

    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    public void start() {
        if (rootPackageName == null) {
            throw new EmptyRuntimeException("rootPackageName");
        }
        originalClassLoader = Thread.currentThread().getContextClassLoader();
        hotdeployClassLoader = new HotdeployClassLoader(originalClassLoader);
        hotdeployClassLoader.setPackageName(rootPackageName);
        hotdeployClassLoader.addHotdeployListener(this);
        Thread.currentThread().setContextClassLoader(hotdeployClassLoader);
    }
    
    public void stop() {
        Thread.currentThread().setContextClassLoader(originalClassLoader);
        hotdeployClassLoader = null;
        originalClassLoader = null;
        BeanDescFactory.clear();
        componentDefCache.clear();
    }
    
    
    public void definedClass(Class clazz) {
        ComponentDef cd = createComponentDef(SingletonS2ContainerFactory.getContainer(), clazz);
        if (cd != null) {
            register(cd);
        }
    }

    protected ComponentDef getComponentDef(S2Container container, Object key) {
        ComponentDef cd = getComponentDefFromCache(key);
        if (cd != null) {
            return cd;
        }
        cd = super.getComponentDef(container, key);
        if (cd != null) {
            return cd;
        }
        if (key instanceof Class) {
            cd = createComponentDef(container, (Class) key);
        } else if (key instanceof String) {
            return null;
        } else {
            throw new IllegalArgumentException("key");
        }
        register(cd);
        return cd;
    }
    
    protected ComponentDef getComponentDefFromCache(Object key) {
        return (ComponentDef) componentDefCache.get(key);
    }
    
    protected ComponentDef createComponentDef(S2Container container, Class clazz) {
        for (int i = 0; i < getComponentFilterSize(); ++i) {
            ComponentFilter filter = getComponentFilter(i);
            ComponentDef cd = filter.createComponentDef(container, clazz);
            if (cd != null) {
                return cd;
            }
        }
        return null;
    }
    
    protected void register(ComponentDef componentDef) {
        componentDef.setContainer(SingletonS2ContainerFactory.getContainer());
        registerByClass(componentDef);
        registerByName(componentDef);
    }

    protected void registerByClass(ComponentDef componentDef) {
        Class[] classes = S2ContainerUtil.getAssignableClasses(componentDef.getComponentClass());
        for (int i = 0; i < classes.length; ++i) {
            registerMap(classes[i], componentDef);
        }
    }

    protected void registerByName(ComponentDef componentDef) {
        String componentName = componentDef.getComponentName();
        if (componentName != null) {
            registerMap(componentName, componentDef);
        }
    }

    protected void registerMap(Object key, ComponentDef componentDef) {
        componentDefCache.put(key, componentDef);
    }
}
