/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.util;

import java.io.Externalizable;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.StringUtil;

/**
 * {@link S2Container}の用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class S2ContainerUtil implements ContainerConstants {

    private static final Logger logger = Logger
            .getLogger(S2ContainerUtil.class);

    private static Set notAssignableClasses = new HashSet();

    static {
        notAssignableClasses.add(Cloneable.class);
        notAssignableClasses.add(Comparable.class);
        notAssignableClasses.add(Serializable.class);
        notAssignableClasses.add(Externalizable.class);
        notAssignableClasses.add(ContainerConstants.class);
    }

    private S2ContainerUtil() {
    }

    /**
     * アサイン可能なクラスを返します。
     * 
     * @param componentClass
     * @return アサイン可能なクラス
     */
    public static Class[] getAssignableClasses(Class componentClass) {
        Set classes = new HashSet();
        for (Class clazz = componentClass; clazz != Object.class
                && clazz != null; clazz = clazz.getSuperclass()) {

            addAssignableClasses(classes, clazz);
        }
        return (Class[]) classes.toArray(new Class[classes.size()]);
    }

    private static void addAssignableClasses(Set classes, Class clazz) {
        if (notAssignableClasses.contains(clazz)) {
            return;
        }
        classes.add(clazz);
        Class[] interfaces = clazz.getInterfaces();
        for (int i = 0; i < interfaces.length; ++i) {
            addAssignableClasses(classes, interfaces[i]);
        }
    }

    /**
     * {@link ComponentDef}を登録したときのログを出力します。
     * 
     * @param cd
     */
    public static void putRegisterLog(final ComponentDef cd) {
        if (logger.isDebugEnabled()) {
            final StringBuffer buf = new StringBuffer(100);
            final Class componentClass = cd.getComponentClass();
            if (componentClass != null) {
                buf.append(componentClass.getName());
            }
            final String componentName = cd.getComponentName();
            if (!StringUtil.isEmpty(componentName)) {
                buf.append("[").append(componentName).append("]");
            }
            logger.log("DSSR0105", new Object[] { new String(buf) });
        }

    }
}