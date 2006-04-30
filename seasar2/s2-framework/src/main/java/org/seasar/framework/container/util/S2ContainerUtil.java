/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import org.seasar.framework.container.ContainerConstants;

/**
 * @author higa
 *
 */
public final class S2ContainerUtil implements ContainerConstants {

    private static Set notAssignableClasses = new HashSet();
    
    static {
        notAssignableClasses.add(Cloneable.class);
        notAssignableClasses.add(Comparable.class);
        notAssignableClasses.add(Serializable.class);
        notAssignableClasses.add(Externalizable.class);
        notAssignableClasses.add(ContainerConstants.class);
    }
    
	protected S2ContainerUtil() {
	}

    public static Class[] getAssignableClasses(Class componentClass) {
        Set classes = new HashSet();
        for (Class clazz = componentClass; clazz != Object.class
                && clazz != null; clazz = clazz.getSuperclass()) {

            addAssignableClasses(classes, clazz);
        }
        return (Class[]) classes.toArray(new Class[classes.size()]);
    }

    public static void addAssignableClasses(Set classes, Class clazz) {
        if (notAssignableClasses.contains(clazz)) {
            return;
        }
        classes.add(clazz);
        Class[] interfaces = clazz.getInterfaces();
        for (int i = 0; i < interfaces.length; ++i) {
            addAssignableClasses(classes, interfaces[i]);
        }
    }
}