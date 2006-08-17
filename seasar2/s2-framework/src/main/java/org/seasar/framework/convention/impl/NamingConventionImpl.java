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

import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 * @author shot
 */
public class NamingConventionImpl implements NamingConvention {

    private static final char PACKAGE_SEPARATOR = '_';

    private String viewRootPath = "/view";

    private String viewExtension = ".html";

    private String implementationPackageName = "impl";

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

    private String webPackageName = "web";

    private String dxoPackageName = "dxo";

    private String logicPackageName = "logic";

    private String daoPackageName = "dao";

    private String entityPackageName = "entity";

    private String dtoPackageName = "dto";

    private String servicePackageName = "service";

    private String interceptorPackageName = "interceptor";

    private String validatorPackageName = "validator";

    private String converterPackageName = "converter";

    private String helperPackageName = "helper";

    public String getActionSuffix() {
        return actionSuffix;
    }

    public void setActionSuffix(String actionSuffix) {
        this.actionSuffix = actionSuffix;
    }

    public String getDaoPackageName() {
        return daoPackageName;
    }

    public void setDaoPackage(String daoPackage) {
        this.daoPackageName = daoPackage;
    }

    public String getDaoSuffix() {
        return daoSuffix;
    }

    public void setDaoSuffix(String daoSuffix) {
        this.daoSuffix = daoSuffix;
    }

    public String getDtoPackageName() {
        return dtoPackageName;
    }

    public void setDtoPackage(String dtoPackage) {
        this.dtoPackageName = dtoPackage;
    }

    public String getDtoSuffix() {
        return dtoSuffix;
    }

    public void setDtoSuffix(String dtoSuffix) {
        this.dtoSuffix = dtoSuffix;
    }

    public String getDxoPackageName() {
        return dxoPackageName;
    }

    public void setDxoPackage(String dxoPackage) {
        this.dxoPackageName = dxoPackage;
    }

    public String getDxoSuffix() {
        return dxoSuffix;
    }

    public void setDxoSuffix(String dxoSuffix) {
        this.dxoSuffix = dxoSuffix;
    }

    public String getEntityPackageName() {
        return entityPackageName;
    }

    public void setEntityPackage(String entityPackage) {
        this.entityPackageName = entityPackage;
    }

    public String getHelperPackageName() {
        return helperPackageName;
    }

    public void setHelperPackageName(String helperPackageName) {
        this.helperPackageName = helperPackageName;
    }

    public String getHelperSuffix() {
        return helperSuffix;
    }

    public void setHelperSuffix(String helperSuffix) {
        this.helperSuffix = helperSuffix;
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

    public String getInterceptorPackageName() {
        return interceptorPackageName;
    }

    public void setInterceptorPackage(String interceptorPackage) {
        this.interceptorPackageName = interceptorPackage;
    }

    public String getInterceptorSuffix() {
        return interceptorSuffix;
    }

    public void setInterceptorSuffix(String interceptorSuffix) {
        this.interceptorSuffix = interceptorSuffix;
    }

    public String getLogicPackageName() {
        return logicPackageName;
    }

    public void setLogicPackage(String logicPackage) {
        this.logicPackageName = logicPackage;
    }

    public String getLogicSuffix() {
        return logicSuffix;
    }

    public void setLogicSuffix(String logicSuffix) {
        this.logicSuffix = logicSuffix;
    }

    public String getPageSuffix() {
        return pageSuffix;
    }

    public void setPageSuffix(String pageSuffix) {
        this.pageSuffix = pageSuffix;
    }

    public String getServicePackageName() {
        return servicePackageName;
    }

    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }

    public String getServiceSuffix() {
        return serviceSuffix;
    }

    public void setServiceSuffix(String serviceSuffix) {
        this.serviceSuffix = serviceSuffix;
    }

    public String getValidatorPackageName() {
        return validatorPackageName;
    }

    public void setValidatorPackage(String validatorPackage) {
        this.validatorPackageName = validatorPackage;
    }

    public String getValidatorSuffix() {
        return validatorSuffix;
    }

    public void setValidatorSuffix(String validatorSuffix) {
        this.validatorSuffix = validatorSuffix;
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

    public String getWebPackageName() {
        return webPackageName;
    }

    public void setWebPackageName(String webPackageName) {
        this.webPackageName = webPackageName;
    }

    public String fromClassNameToShortComponentName(String className) {
        if (className == null) {
            throw new EmptyRuntimeException("className");
        }
        String s = StringUtil.decapitalize(ClassUtil
                .getShortClassName(className));
        if (s.endsWith(implementationSuffix)) {
            return s.substring(0, s.length() - implementationSuffix.length());
        }
        return s;
    }

    public String fromClassNameToComponentName(String className) {
        if (className == null) {
            throw new EmptyRuntimeException("className");
        }
        String wwwkey = "." + webPackageName + ".";
        int index = className.lastIndexOf(wwwkey);
        if (index > 0) {
            String s = className.substring(index + wwwkey.length());
            if (s.endsWith(implementationSuffix)) {
                String implkey = "." + implementationPackageName + ".";
                index = s.lastIndexOf(implkey);
                if (index < 0) {
                    throw new IllegalArgumentException(className);
                }
                String packageName = s.substring(0, index).replace('.',
                        PACKAGE_SEPARATOR);
                String name = StringUtil.decapitalize(s.substring(index
                        + implkey.length(), s.length()
                        - implementationSuffix.length()));
                return packageName + PACKAGE_SEPARATOR + name;
            }
            index = s.lastIndexOf('.');
            if (index < 0) {
                throw new IllegalArgumentException(className);
            }
            String packageName = s.substring(0, index).replace('.',
                    PACKAGE_SEPARATOR);
            String name = StringUtil.decapitalize(s.substring(index + 1));
            return packageName + PACKAGE_SEPARATOR + name;
        }
        return fromClassNameToShortComponentName(className);
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

    public String getConverterSuffix() {
        return converterSuffix;
    }

    public void setConverterSuffix(String converterSuffix) {
        this.converterSuffix = converterSuffix;
    }

    public String getConverterPackageName() {
        return converterPackageName;
    }

    public void setConverterPackage(String converterPackage) {
        this.converterPackageName = converterPackage;
    }

}