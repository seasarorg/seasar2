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
package org.seasar.framework.convention;

/**
 * @author higa
 * @author shot
 */
public interface NamingConvention {

    String getViewRootPath();

    String getViewExtension();

    String getImplementationSuffix();

    String getPageSuffix();

    String getActionSuffix();

    String getServiceSuffix();

    String getDxoSuffix();

    String getLogicSuffix();

    String getDaoSuffix();

    String getHelperSuffix();

    String getInterceptorSuffix();

    String getValidatorSuffix();

    String getConverterSuffix();

    String getDtoSuffix();

    String getSubApplicationRootPackageName();

    String getImplementationPackageName();

    String getDxoPackageName();

    String getLogicPackageName();

    String getDaoPackageName();

    String getEntityPackageName();

    String getDtoPackageName();

    String getServicePackageName();

    String getInterceptorPackageName();

    String getValidatorPackageName();

    String getConverterPackageName();

    String getHelperPackageName();

    String[] getRootPackageNames();

    String fromSuffixToPackageName(String suffix);

    String fromClassNameToShortComponentName(String className);

    String fromClassNameToComponentName(String className);

    Class fromComponentNameToClass(String componentName);

    String toImplementationClassName(String className);

    String toInterfaceClassName(String className);

    Class toCompleteClass(Class clazz);

    String fromComponentNameToPartOfClassName(String componentName);

    String fromComponentNameToSuffix(String componentName);

    String fromClassNameToSuffix(String className);

    String fromPathToPageName(String path);

    String fromPathToActionName(String path);

    String fromPageNameToPath(String pageName);

    String fromActionNameToPath(String actionName);

    String fromActionNameToPageName(String actionName);

    boolean isTargetClassName(String className, String suffix);

    boolean isTargetClassName(String className);
}
