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
package org.seasar.framework.convention.impl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 * @author shot
 */
public class NamingConventionImpl implements NamingConvention {

    private static final char PACKAGE_SEPARATOR = '_';

    private String viewRootPath = "/view";

    private String viewExtension = ".html";

    private String implementationSuffix = "Impl";

    private String pageSuffix = "Page";

    private String actionSuffix = "Action";

    private String serviceSuffix = "Service";

    private String dxoSuffix = "Dxo";

    private String logicSuffix = "Logic";

    private String daoSuffix = "Dao";

    private String helperSuffix = "Helper";

    private String interceptorSuffix = "Interceptor";

    private String validatorSuffix = "Validator";

    private String converterSuffix = "Converter";

    private String dtoSuffix = "Dto";

    private String subApplicationRootPackageName = "web";

    private String entityPackageName = "entity";

    private String[] rootPackageNames = new String[0];

    private Map existCheckers = new HashMap();

    public String getPageSuffix() {
        return pageSuffix;
    }

    public void setPageSuffix(String pageSuffix) {
        this.pageSuffix = pageSuffix;
    }

    public String getActionSuffix() {
        return actionSuffix;
    }

    public void setActionSuffix(String actionSuffix) {
        this.actionSuffix = actionSuffix;
    }

    public String getDaoSuffix() {
        return daoSuffix;
    }

    public void setDaoSuffix(String daoSuffix) {
        this.daoSuffix = daoSuffix;
    }

    public String getDtoSuffix() {
        return dtoSuffix;
    }

    public void setDtoSuffix(String dtoSuffix) {
        this.dtoSuffix = dtoSuffix;
    }

    public String getDxoSuffix() {
        return dxoSuffix;
    }

    public void setDxoSuffix(String dxoSuffix) {
        this.dxoSuffix = dxoSuffix;
    }

    public String getHelperSuffix() {
        return helperSuffix;
    }

    public void setHelperSuffix(String helperSuffix) {
        this.helperSuffix = helperSuffix;
    }

    public String getInterceptorSuffix() {
        return interceptorSuffix;
    }

    public void setInterceptorSuffix(String interceptorSuffix) {
        this.interceptorSuffix = interceptorSuffix;
    }

    public String getLogicSuffix() {
        return logicSuffix;
    }

    public void setLogicSuffix(String logicSuffix) {
        this.logicSuffix = logicSuffix;
    }

    public String getServiceSuffix() {
        return serviceSuffix;
    }

    public void setServiceSuffix(String serviceSuffix) {
        this.serviceSuffix = serviceSuffix;
    }

    public String getValidatorSuffix() {
        return validatorSuffix;
    }

    public void setValidatorSuffix(String validatorSuffix) {
        this.validatorSuffix = validatorSuffix;
    }

    public String getConverterSuffix() {
        return converterSuffix;
    }

    public void setConverterSuffix(String converterSuffix) {
        this.converterSuffix = converterSuffix;
    }

    public String getImplementationSuffix() {
        return implementationSuffix;
    }

    public void setImplementationSuffix(String implementationSuffix) {
        this.implementationSuffix = implementationSuffix;
    }

    public String getDaoPackageName() {
        return fromSuffixToPackageName(daoSuffix);
    }

    public String getDtoPackageName() {
        return fromSuffixToPackageName(dtoSuffix);
    }

    public String getDxoPackageName() {
        return fromSuffixToPackageName(dxoSuffix);
    }

    public String getHelperPackageName() {
        return fromSuffixToPackageName(helperSuffix);
    }

    public String getInterceptorPackageName() {
        return fromSuffixToPackageName(interceptorSuffix);
    }

    public String getLogicPackageName() {
        return fromSuffixToPackageName(logicSuffix);
    }

    public String getServicePackageName() {
        return fromSuffixToPackageName(serviceSuffix);
    }

    public String getValidatorPackageName() {
        return fromSuffixToPackageName(validatorSuffix);
    }

    public String getConverterPackageName() {
        return fromSuffixToPackageName(converterSuffix);
    }

    public String getImplementationPackageName() {
        return fromSuffixToPackageName(implementationSuffix);
    }

    public String getEntityPackageName() {
        return entityPackageName;
    }

    public void setEntityPackage(String entityPackage) {
        this.entityPackageName = entityPackage;
    }

    public String getViewExtension() {
        return viewExtension;
    }

    public void setViewExtension(String viewExtension) {
        this.viewExtension = viewExtension;
    }

    public String getViewRootPath() {
        return viewRootPath;
    }

    public void setViewRootPath(String viewRootPath) {
        this.viewRootPath = viewRootPath;
    }

    public String getSubApplicationRootPackageName() {
        return subApplicationRootPackageName;
    }

    public void setSubApplicationRootPackageName(
            String subApplicationRootPackageName) {
        this.subApplicationRootPackageName = subApplicationRootPackageName;
    }

    public String[] getRootPackageNames() {
        return rootPackageNames;
    }

    public void addRootPackageName(String rootPackageName) {
        rootPackageNames = (String[]) ArrayUtil.add(rootPackageNames,
                rootPackageName);
        addExistChecker(rootPackageName);
    }

    public String fromSuffixToPackageName(String suffix) {
        if (StringUtil.isEmpty(suffix)) {
            throw new EmptyRuntimeException("suffix");
        }
        return StringUtil.decapitalize(suffix);
    }

    public String fromClassNameToShortComponentName(String className) {
        if (StringUtil.isEmpty(className)) {
            throw new EmptyRuntimeException("className");
        }
        String s = StringUtil.decapitalize(ClassUtil
                .getShortClassName(className));
        if (s.endsWith(implementationSuffix)) {
            return s.substring(0, s.length() - implementationSuffix.length());
        }
        return s;
    }

    public String fromClassNameToComponentName(final String className) {
        if (StringUtil.isEmpty(className)) {
            throw new EmptyRuntimeException("className");
        }
        String cname = toInterfaceClassName(className);
        String suffix = fromClassNameToSuffix(cname);
        String middlePackageName = fromSuffixToPackageName(suffix);
        String key = "." + middlePackageName + ".";
        int index = cname.indexOf(key);
        String name = null;
        if (index > 0) {
            name = cname.substring(index + key.length());
        } else {
            key = "." + subApplicationRootPackageName + ".";
            index = cname.indexOf(key);
            if (index < 0) {
                return fromClassNameToShortComponentName(className);
            }
            name = cname.substring(index + key.length());
        }
        String[] array = StringUtil.split(name, ".");
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            if (i == array.length - 1) {
                buf.append(StringUtil.decapitalize(array[i]));
            } else {
                buf.append(array[i]);
                buf.append('_');
            }
        }
        return buf.toString();
    }

    public Class fromComponentNameToClass(String componentName) {
        if (StringUtil.isEmpty(componentName)) {
            throw new EmptyRuntimeException("componentName");
        }
        String suffix = fromComponentNameToSuffix(componentName);
        if (suffix == null) {
            return null;
        }
        String middlePackageName = fromSuffixToPackageName(suffix);
        String partOfClassName = fromComponentNameToPartOfClassName(componentName);
        boolean subAppSuffix = isSubApplicationSuffix(suffix);
        for (int i = 0; i < rootPackageNames.length; ++i) {
            String rootPackageName = rootPackageNames[i];
            if (subAppSuffix) {
                Class clazz = findClass(rootPackageName,
                        subApplicationRootPackageName, partOfClassName);
                if (clazz != null) {
                    return clazz;
                }
                clazz = findClass(rootPackageName, middlePackageName,
                        partOfClassName);
                if (clazz != null) {
                    return clazz;
                }
            } else {
                Class clazz = findClass(rootPackageName, middlePackageName,
                        partOfClassName);
                if (clazz != null) {
                    return clazz;
                }
                clazz = findClass(rootPackageName,
                        subApplicationRootPackageName, partOfClassName);
                if (clazz != null) {
                    return clazz;
                }
            }
        }
        return null;
    }

    protected boolean isSubApplicationSuffix(String suffix) {
        if (pageSuffix.equals(suffix)) {
            return true;
        }
        if (dxoSuffix.equals(suffix)) {
            return true;
        }
        if (actionSuffix.equals(suffix)) {
            return true;
        }
        if (serviceSuffix.equals(suffix)) {
            return true;
        }
        return false;
    }

    protected Class findClass(String rootPackageName, String middlePackageName,
            String partOfClassName) {
        String lastClassName = ClassUtil.concatName(middlePackageName,
                partOfClassName);
        if (isExist(rootPackageName, lastClassName)) {
            String className = ClassUtil.concatName(rootPackageName,
                    lastClassName);
            Class clazz = ClassUtil.forName(className);
            if (clazz.isInterface()) {
                String lastImplClassName = toImplementationClassName(lastClassName);
                if (isExist(rootPackageName, lastImplClassName)) {
                    className = ClassUtil.concatName(rootPackageName,
                            lastImplClassName);
                    return ClassUtil.forName(className);
                }
            }
            return clazz;
        }
        return null;
    }

    public String toImplementationClassName(String className) {
        int index = className.lastIndexOf('.');
        if (index < 0) {
            return getImplementationPackageName() + "." + className
                    + implementationSuffix;
        }
        return className.substring(0, index) + "."
                + getImplementationPackageName() + "."
                + className.substring(index + 1) + implementationSuffix;
    }

    public String toInterfaceClassName(String className) {
        if (!className.endsWith(implementationSuffix)) {
            return className;
        }
        String key = "." + getImplementationPackageName() + ".";
        int index = className.lastIndexOf(key);
        if (index < 0) {
            throw new IllegalArgumentException(className);
        }
        return className.substring(0, index)
                + "."
                + className.substring(index + key.length(), className.length()
                        - implementationSuffix.length());
    }

    public Class toCompleteClass(Class clazz) {
        if (!clazz.isInterface()) {
            return clazz;
        }
        String className = toImplementationClassName(clazz.getName());
        if (ResourceUtil.isExist(ClassUtil.getResourcePath(className))) {
            return ClassUtil.forName(className);
        }
        return clazz;
    }

    public String fromComponentNameToPartOfClassName(String componentName) {
        if (componentName == null) {
            throw new EmptyRuntimeException("componentName");
        }
        int index = componentName.indexOf(PACKAGE_SEPARATOR);
        if (index < 0) {
            return StringUtil.capitalize(componentName);
        }
        return componentName.substring(0, index) + "."
                + StringUtil.capitalize(componentName.substring(index + 1));
    }

    public String fromComponentNameToSuffix(String componentName) {
        return fromNameToSuffix(componentName);
    }

    public String fromClassNameToSuffix(String componentName) {
        return fromNameToSuffix(componentName);
    }

    protected String fromNameToSuffix(String name) {
        if (StringUtil.isEmpty(name)) {
            throw new EmptyRuntimeException("name");
        }
        for (int i = name.length() - 1; i >= 0; --i) {
            if (Character.isUpperCase(name.charAt(i))) {
                return name.substring(i);
            }
        }
        return null;
    }

    public String fromPathToPageName(String path) {
        return fromPathToComponentName(path, pageSuffix);
    }

    protected String fromPathToComponentName(String path, String nameSuffix) {
        if (!path.startsWith(viewRootPath) || !path.endsWith(viewExtension)) {
            throw new IllegalArgumentException(path);
        }
        String componentName = path.substring(viewRootPath.length() + 1, path
                .length()
                - viewExtension.length())
                + nameSuffix;
        return componentName.replace('/', '_');
    }

    public String fromPathToActionName(String path) {
        return fromPathToComponentName(path, actionSuffix);
    }

    public String fromPageNameToPath(String pageName) {
        if (!pageName.endsWith(pageSuffix)) {
            throw new IllegalArgumentException(pageName);
        }
        String name = pageName.substring(0, pageName.length()
                - pageSuffix.length());
        return viewRootPath + "/" + name.replace(PACKAGE_SEPARATOR, '/')
                + viewExtension;
    }

    public String fromActionNameToPath(String actionName) {
        if (!actionName.endsWith(actionSuffix)) {
            throw new IllegalArgumentException(actionName);
        }
        String name = actionName.substring(0, actionName.length()
                - actionSuffix.length());
        return viewRootPath + "/" + name.replace(PACKAGE_SEPARATOR, '/')
                + viewExtension;
    }

    public String fromActionNameToPageName(String actionName) {
        if (!actionName.endsWith(actionSuffix)) {
            throw new IllegalArgumentException(actionName);
        }
        return actionName.substring(0, actionName.length()
                - actionSuffix.length())
                + pageSuffix;
    }

    public boolean isTargetClassName(String className, String suffix) {
        String name = StringUtil.trimSuffix(className, implementationSuffix);
        return name.endsWith(suffix);
    }

    public boolean isTargetClassName(String className) {
        for (int i = 0; i < rootPackageNames.length; ++i) {
            if (className.startsWith(rootPackageNames[i])) {
                return true;
            }
        }
        return false;
    }

    protected boolean isExist(String rootPackageName, String lastClassName) {
        ExistChecker checker = getExistChecker(rootPackageName);
        if (checker != null) {
            return checker.isExist(lastClassName);
        }
        return false;
    }

    protected ExistChecker getExistChecker(String rootPackageName) {
        return (ExistChecker) existCheckers.get(rootPackageName);
    }

    protected void addExistChecker(String rootPackageName) {
        ExistChecker checker = createExistChecker(rootPackageName);
        existCheckers.put(rootPackageName, checker);
    }

    protected ExistChecker createExistChecker(String rootPackageName) {
        if (StringUtil.isEmpty(rootPackageName)) {
            return null;
        }
        String s = rootPackageName.replace('.', '/');
        String[] rootFiles = new String[0];
        List list = new ArrayList();
        for (Iterator itr = ClassLoaderUtil.getResources(this.getClass(), s); itr
                .hasNext();) {
            URL url = (URL) itr.next();
            list.add(url.getPath());
        }
        rootFiles = (String[]) list.toArray(new String[list.size()]);
        if (rootFiles.length != 0) {
            return new FileExistChecker(rootFiles, rootPackageName);
        }
        return new JarExistChecker(rootPackageName);
    }

    protected static interface ExistChecker {

        boolean isExist(String lastClassName);
    }

    protected static class FileExistChecker implements ExistChecker {

        private String[] rootFiles;

        private String rootPath;

        private Map cache = Collections.synchronizedMap(new HashMap());

        private Map jarCache = Collections.synchronizedMap(new HashMap());

        protected FileExistChecker(String[] rootFiles, String rootPath) {
            this.rootFiles = rootFiles;
            this.rootPath = rootPath;
        }

        public boolean isExist(String lastClassName) {
            if (jarCache.containsKey(lastClassName)) {
                Boolean b = (Boolean) jarCache.get(lastClassName);
                return b.booleanValue();
            }
            String pathName = getPathName(lastClassName);
            File file = (File) cache.get(lastClassName);
            if (file == null) {
                for (int i = 0; i < rootFiles.length; i++) {
                    file = createFile(rootFiles[i], lastClassName);
                    if (file.exists()) {
                        cache.put(lastClassName, file);
                        return true;
                    }
                }
                boolean ret = ResourceUtil.isExist(rootPath + "." + pathName);
                jarCache.put(lastClassName, new Boolean(ret));
                if (ret) {
                    return true;
                }
            }
            return file.exists();
        }

        protected File createFile(String rootFileName, String lastClassName) {
            String pathName = getPathName(lastClassName);
            return new File(rootFileName, pathName);
        }

        private static String getPathName(String lastClassName) {
            return lastClassName.replace('.', '/') + ".class";
        }
    }

    protected static class JarExistChecker implements ExistChecker {

        private String rootPath;

        protected JarExistChecker(String rootPackageName) {
            this.rootPath = rootPackageName.replace('.', '/') + "/";
        }

        public boolean isExist(String lastClassName) {
            return ResourceUtil.isExist(rootPath
                    + lastClassName.replace('.', '/') + ".class");
        }
    }
}