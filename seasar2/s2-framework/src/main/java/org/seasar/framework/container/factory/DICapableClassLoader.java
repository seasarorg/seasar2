/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory;

import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.autoregister.ClassPattern;
import org.seasar.framework.util.StringUtil;

public class DICapableClassLoader extends ClassLoader {
    public static final String INJECT_DEPENDENCY = "INJECT_DEPENDENCY";

    protected S2Container container;

    protected List classPatterns = new ArrayList();

    protected List ignoreClassPatterns = new ArrayList();

    protected ClassPool classPool;

    public DICapableClassLoader(final ClassLoader parent) {
        super(parent);
        classPool = new ClassPool();
        classPool.appendClassPath(new LoaderClassPath(parent));
    }

    public S2Container getContainer() {
        return container;
    }

    public void setContainer(final S2Container container) {
        this.container = container;
    }

    public synchronized void addClassPattern(final String packageName,
            final String shortClassNames) {
        addClassPattern(new ClassPattern(packageName, shortClassNames));
    }

    public synchronized void addClassPattern(final ClassPattern classPattern) {
        classPatterns.add(classPattern);
    }

    public synchronized void addIgnoreClassPattern(final String packageName,
            final String shortClassNames) {
        addIgnoreClassPattern(new ClassPattern(packageName, shortClassNames));
    }

    public synchronized void addIgnoreClassPattern(
            final ClassPattern classPattern) {
        ignoreClassPatterns.add(classPattern);
    }

    public synchronized void reset() {
        classPatterns.clear();
        ignoreClassPatterns.clear();
    }

    protected synchronized Class loadClass(final String name,
            final boolean resolve) throws ClassNotFoundException {
        try {
            return resolveClassIfNecessary(Class.forName(name, false, null),
                    resolve);
        } catch (final ClassNotFoundException ignore) {
        }

        if (!isTargetClass(name)) {
            return resolveClassIfNecessary(getParent().loadClass(name), resolve);
        }

        Class clazz = findLoadedClass(name);
        if (clazz == null) {
            clazz = findClass(name);
        }
        return resolveClassIfNecessary(clazz, resolve);
    }

    protected Class resolveClassIfNecessary(final Class clazz,
            final boolean resolve) {
        if (resolve) {
            resolveClass(clazz);
        }
        return clazz;
    }

    protected synchronized Class findClass(final String name)
            throws ClassNotFoundException {
        try {
            final CtClass ctClass = classPool.get(name);
            enhanceClass(ctClass);
            final byte[] bytes = ctClass.toBytecode();
            return defineClass(name, bytes, 0, bytes.length);
        } catch (final Exception e) {
            throw new ClassNotFoundException(name, e);
        }
    }

    protected boolean isTargetClass(final String name) {
        return isMatch(name, classPatterns)
                && !isMatch(name, ignoreClassPatterns);
    }

    protected boolean isMatch(final String name, final List patterns) {
        if (patterns.isEmpty()) {
            return false;
        }
        final int pos = name.lastIndexOf('.');
        final String packageName = name.substring(0, pos);
        final String shortName = name.substring(pos + 1);
        for (int i = 0; i < patterns.size(); ++i) {
            final ClassPattern pattern = (ClassPattern) patterns.get(i);
            if (pattern.isAppliedPackageName(packageName)
                    && pattern.isAppliedShortClassName(shortName)) {
                return true;
            }
        }
        return false;
    }

    protected void enhanceClass(final CtClass ctClass)
            throws CannotCompileException {
        if (ctClass.isInterface()
                || (ctClass.getModifiers() & Modifier.ABSTRACT) != 0) {
            return;
        }

        final String defaultComponentName = getDefaultComponentName(ctClass);
        enhanceConstructors(ctClass, defaultComponentName);
    }

    protected void enhanceConstructors(final CtClass clazz,
            final String defaultComponentName) throws CannotCompileException {
        final CtConstructor[] ctors = clazz.getConstructors();
        for (int i = 0; i < ctors.length; ++i) {
            final String componentName = getComponentName(ctors[i],
                    defaultComponentName);
            if (componentName == null) {
                continue;
            } else if (StringUtil.isEmpty(componentName)) {
                enhanceConstructor(ctors[i], "");
            } else {
                enhanceConstructor(ctors[i], ", \"" + componentName + "\"");
            }
        }
    }

    protected void enhanceConstructor(final CtConstructor constructor,
            final String arg) throws CannotCompileException {
        constructor
                .insertAfter("ClassLoader loader = getClass().getClassLoader();"
                        + "Class clazz = loader.getClass();"
                        + "java.lang.reflect.Method method = "
                        + "  org.seasar.framework.util.ClassUtil"
                        + "    .getMethod(clazz, \"getContainer\", null);"
                        + "org.seasar.framework.container.S2Container container = "
                        + "  (org.seasar.framework.container.S2Container)"
                        + "    org.seasar.framework.util.MethodUtil.invoke(method, loader, null);"
                        + "container.injectDependency(this" + arg + ");");
    }

    protected String getDefaultComponentName(final CtClass ctClass) {
        String componentName = getDefaultComponentNameFromAnnotation(ctClass);
        if (componentName == null) {
            componentName = getDefaultComponentNameFromConstant(ctClass);
        }
        return componentName;
    }

    protected String getDefaultComponentNameFromConstant(final CtClass ctClass) {
        try {
            final CtField field = ctClass.getField(INJECT_DEPENDENCY);
            final String componentName = (String) field.getConstantValue();
            return componentName;
        } catch (final NotFoundException ignore) {
        }
        return null;
    }

    protected String getDefaultComponentNameFromAnnotation(final CtClass ctClass) {
        return null;
    }

    protected String getComponentName(final CtConstructor ctClass,
            final String defaultValue) {
        return defaultValue;
    }
}
