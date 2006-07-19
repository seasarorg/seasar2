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
 * 
 */
public interface NamingConvention {

    String getViewRootPath();

    String getViewExtension();

    String getImplementationSuffix();

    String getImplementationPackageName();

    String getPageSuffix();

    String getActionSuffix();

    String getServiceSuffix();

    String getDxoSuffix();

    String getLogicSuffix();

    String getDaoSuffix();

    String getHelperSuffix();

    String getInterceptorSuffix();

    String getValidatorSuffix();

    String getDtoSuffix();

    String getWebPackageName();

    String getDxoPackageName();

    String getLogicPackageName();

    String getDaoPackageName();

    String getEntityPackageName();

    String getDtoPackageName();

    String getServicePackageName();

    String getInterceptorPackageName();

    String getValidatorPackageName();

    String getHelperPackageName();

    String fromClassNameToComponentName(String className);

    String fromPathToPageName(String path);

    String fromPathToActionName(String path);

    String fromPageNameToPath(String pageName);

    String fromActionNameToPath(String actionName);
}
