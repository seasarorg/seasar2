/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.container.hotdeploy;

import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.container.impl.S2ContainerBehavior.DefaultProvider;
import org.seasar.framework.container.util.S2ContainerUtil;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.DisposableUtil;

/**
 * HOT deployのための
 * {@link org.seasar.framework.container.impl.S2ContainerBehavior.Provider}です。
 * <p>
 * このクラスをs2container.diconに登録するとHOT deployで動作するようになります。
 * </p>
 * 
 * @author higa
 * 
 */
public class HotdeployBehavior extends DefaultProvider {

    private static final Logger logger = Logger
            .getLogger(HotdeployBehavior.class);

    private ClassLoader originalClassLoader;

    private HotdeployClassLoader hotdeployClassLoader;

    private Map componentDefCache = new HashMap();

    private NamingConvention namingConvention;

    private ComponentCreator[] creators = new ComponentCreator[0];

    /** keepプロパティのバインディングタイプアノテーションです。 */
    public static final String keep_BINDING = "bindingType=may";

    private boolean keep;

    /**
     * {@link NamingConvention}を返します。
     * 
     * @return {@link NamingConvention}
     */
    public NamingConvention getNamingConvention() {
        return namingConvention;
    }

    /**
     * {@link NamingConvention}を設定します。
     * 
     * @param namingConvention
     */
    public void setNamingConvention(NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    /**
     * {@link ComponentCreator}の配列を返します。
     * 
     * @return {@link ComponentCreator}の配列
     */
    public ComponentCreator[] getCreators() {
        return creators;
    }

    /**
     * {@link ComponentCreator}の配列を設定します。
     * 
     * @param creators
     */
    public void setCreators(ComponentCreator[] creators) {
        this.creators = creators;
    }

    /**
     * {@link #start()}/{@link #stop()}の度にクラスローダをキープするかどうかを設定します。
     * 
     * @param keep
     *            クラスローダをキープする場合<code>true</code>
     */
    public void setKeep(boolean keep) {
        this.keep = keep;
        if (hotdeployClassLoader != null) {
            finish();
        }
    }

    /**
     * HOT deployを開始します。
     */
    public void start() {
        originalClassLoader = Thread.currentThread().getContextClassLoader();
        if (!keep || hotdeployClassLoader == null) {
            if (logger.isDebugEnabled()) {
                logger.log("DSSR0108", null);
            }
            hotdeployClassLoader = new HotdeployClassLoader(
                    originalClassLoader, namingConvention);
        }
        Thread.currentThread().setContextClassLoader(hotdeployClassLoader);
        S2ContainerImpl container = (S2ContainerImpl) SingletonS2ContainerFactory
                .getContainer();
        container.setClassLoader(hotdeployClassLoader);
    }

    /**
     * HOT deployを終了します。
     * <p>
     * {@link #keep}プロパティが<code>true</code>の場合、HOT deployクラスローダは破棄せず、 次の
     * {@link #start()}～{@link #stop()}でも同じクラスローダが使用されます。
     * </p>
     */
    public void stop() {
        if (!keep) {
            finish();
        }
        S2ContainerImpl container = (S2ContainerImpl) SingletonS2ContainerFactory
                .getContainer();
        container.setClassLoader(originalClassLoader);
        Thread.currentThread().setContextClassLoader(originalClassLoader);
        originalClassLoader = null;
    }

    /**
     * HOT deployクラスローダを破棄します。
     */
    public void finish() {
        componentDefCache.clear();
        hotdeployClassLoader = null;
        DisposableUtil.dispose();
        if (logger.isDebugEnabled()) {
            logger.log("DSSR0109", null);
        }
    }

    protected ComponentDef getComponentDef(S2Container container, Object key) {
        ComponentDef cd = super.getComponentDef(container, key);
        if (cd != null) {
            return cd;
        }
        if (container != container.getRoot()) {
            return null;
        }
        cd = getComponentDefFromCache(key);
        if (cd != null) {
            return cd;
        }
        if (key instanceof Class) {
            cd = createComponentDef((Class) key);
        } else if (key instanceof String) {
            cd = createComponentDef((String) key);
            if (cd != null && !key.equals(cd.getComponentName())) {
                logger.log("WSSR0011",
                        new Object[] { key, cd.getComponentClass().getName(),
                                cd.getComponentName() });
                cd = null;
            }
        } else {
            throw new IllegalArgumentException("key");
        }
        if (cd != null) {
            register(cd);
            S2ContainerUtil.putRegisterLog(cd);
            cd.init();
        }
        return cd;
    }

    /**
     * キャッシュにある {@link ComponentDef}を返します。
     * 
     * @param key
     * @return {@link ComponentDef}
     */
    protected ComponentDef getComponentDefFromCache(Object key) {
        return (ComponentDef) componentDefCache.get(key);
    }

    /**
     * {@link ComponentDef}を作成します。
     * 
     * @param componentClass
     * @return {@link ComponentDef}
     */
    protected ComponentDef createComponentDef(Class componentClass) {
        for (int i = 0; i < creators.length; ++i) {
            ComponentCreator creator = creators[i];
            ComponentDef cd = creator.createComponentDef(componentClass);
            if (cd != null) {
                return cd;
            }
        }
        return null;
    }

    /**
     * {@link ComponentDef}を作成します。
     * 
     * @param componentName
     * @return {@link ComponentDef}
     */
    protected ComponentDef createComponentDef(String componentName) {
        for (int i = 0; i < creators.length; ++i) {
            ComponentCreator creator = creators[i];
            ComponentDef cd = creator.createComponentDef(componentName);
            if (cd != null) {
                return cd;
            }
        }
        return null;
    }

    /**
     * {@link ComponentDef}を登録します。
     * 
     * @param componentDef
     */
    protected void register(ComponentDef componentDef) {
        componentDef.setContainer(SingletonS2ContainerFactory.getContainer());
        registerByClass(componentDef);
        registerByName(componentDef);
    }

    /**
     * {@link ComponentDef}をクラスをキーにして登録します。
     * 
     * @param componentDef
     */
    protected void registerByClass(ComponentDef componentDef) {
        Class[] classes = S2ContainerUtil.getAssignableClasses(componentDef
                .getComponentClass());
        for (int i = 0; i < classes.length; ++i) {
            registerMap(classes[i], componentDef);
        }
    }

    /**
     * {@link ComponentDef}を名前をキーにして登録します。
     * 
     * @param componentDef
     */
    protected void registerByName(ComponentDef componentDef) {
        String componentName = componentDef.getComponentName();
        if (componentName != null) {
            registerMap(componentName, componentDef);
        }
    }

    /**
     * {@link ComponentDef}をキャッシュに登録します。
     * <p>
     * キャッシュは基本的にリクエストごとに破棄されます
     * </p>
     * 
     * @param key
     * @param componentDef
     */
    protected void registerMap(Object key, ComponentDef componentDef) {
        ComponentDef previousCd = (ComponentDef) componentDefCache.get(key);
        if (previousCd == null) {
            componentDefCache.put(key, componentDef);
        } else {
            ComponentDef tmrcd = S2ContainerImpl.createTooManyRegistration(key,
                    previousCd, componentDef);
            componentDefCache.put(key, tmrcd);
        }
    }
}