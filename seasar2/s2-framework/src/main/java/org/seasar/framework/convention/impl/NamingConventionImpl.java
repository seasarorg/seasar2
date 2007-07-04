/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.MapUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.URLUtil;
import org.seasar.framework.util.ZipFileUtil;

/**
 * {@link NamingConvention}の実装クラスです。
 * 
 * @author higa
 * @author shot
 */
public class NamingConventionImpl implements NamingConvention, Disposable {

    private static final char PACKAGE_SEPARATOR = '_';

    private static final String PACKAGE_SEPARATOR_STR = "_";

    private boolean initialized;

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

    private String connectorSuffix = "Connector";

    private String subApplicationRootPackageName = "web";

    private String entityPackageName = "entity";

    private String[] rootPackageNames = new String[0];

    private String[] ignorePackageNames = new String[0];

    private Map existCheckerArrays = MapUtil.createHashMap();

    private Map interfaceToImplementationMap = new HashMap();

    private Map implementationToInterfaceMap = new HashMap();

    /**
     * {@link NamingConventionImpl}を作成します。
     */
    public NamingConventionImpl() {
        initialize();
    }

    /**
     * 初期化します。
     */
    public void initialize() {
        if (!initialized) {
            for (int i = 0; i < rootPackageNames.length; ++i) {
                addExistChecker(rootPackageNames[i]);
            }
            DisposableUtil.add(this);
            initialized = true;
        }
    }

    public void dispose() {
        existCheckerArrays.clear();
        initialized = false;
    }

    public String getPageSuffix() {
        return pageSuffix;
    }

    /**
     * <code>Page</code>の<code>suffix</code>を設定します。
     * 
     * @param pageSuffix
     */
    public void setPageSuffix(String pageSuffix) {
        this.pageSuffix = pageSuffix;
    }

    public String getActionSuffix() {
        return actionSuffix;
    }

    /**
     * <code>Action</code>の<code>suffix</code>を設定します。
     * 
     * @param actionSuffix
     */
    public void setActionSuffix(String actionSuffix) {
        this.actionSuffix = actionSuffix;
    }

    public String getConnectorSuffix() {
        return connectorSuffix;
    }

    /**
     * <code>Connector</code>の<code>suffix</code>を設定します。
     * 
     * @param connectorSuffix
     */
    public void setConnectorSuffix(String connectorSuffix) {
        this.connectorSuffix = connectorSuffix;
    }

    public String getDaoSuffix() {
        return daoSuffix;
    }

    /**
     * <code>Dao</code>の<code>suffix</code>を設定します。
     * 
     * @param daoSuffix
     */
    public void setDaoSuffix(String daoSuffix) {
        this.daoSuffix = daoSuffix;
    }

    public String getDtoSuffix() {
        return dtoSuffix;
    }

    /**
     * <code>Dto</code>の<code>suffix</code>を設定します。
     * 
     * @param dtoSuffix
     */
    public void setDtoSuffix(String dtoSuffix) {
        this.dtoSuffix = dtoSuffix;
    }

    public String getDxoSuffix() {
        return dxoSuffix;
    }

    /**
     * <code>Dxo</code>の<code>suffix</code>を設定します。
     * 
     * @param dxoSuffix
     */
    public void setDxoSuffix(String dxoSuffix) {
        this.dxoSuffix = dxoSuffix;
    }

    public String getHelperSuffix() {
        return helperSuffix;
    }

    /**
     * <code>Helper</code>の<code>suffix</code>を設定します。
     * 
     * @param helperSuffix
     */
    public void setHelperSuffix(String helperSuffix) {
        this.helperSuffix = helperSuffix;
    }

    public String getInterceptorSuffix() {
        return interceptorSuffix;
    }

    /**
     * <code>Interceptor</code>の<code>suffix</code>を設定します。
     * 
     * @param interceptorSuffix
     */
    public void setInterceptorSuffix(String interceptorSuffix) {
        this.interceptorSuffix = interceptorSuffix;
    }

    public String getLogicSuffix() {
        return logicSuffix;
    }

    /**
     * <code>Logic</code>の<code>suffix</code>を設定します。
     * 
     * @param logicSuffix
     */
    public void setLogicSuffix(String logicSuffix) {
        this.logicSuffix = logicSuffix;
    }

    public String getServiceSuffix() {
        return serviceSuffix;
    }

    /**
     * <code>Service</code>の<code>suffix</code>を設定します。
     * 
     * @param serviceSuffix
     */
    public void setServiceSuffix(String serviceSuffix) {
        this.serviceSuffix = serviceSuffix;
    }

    public String getValidatorSuffix() {
        return validatorSuffix;
    }

    /**
     * <code>Validator</code>の<code>suffix</code>を設定します。
     * 
     * @param validatorSuffix
     */
    public void setValidatorSuffix(String validatorSuffix) {
        this.validatorSuffix = validatorSuffix;
    }

    public String getConverterSuffix() {
        return converterSuffix;
    }

    /**
     * <code>Converter</code>の<code>suffix</code>を設定します。
     * 
     * @param converterSuffix
     */
    public void setConverterSuffix(String converterSuffix) {
        this.converterSuffix = converterSuffix;
    }

    public String getImplementationSuffix() {
        return implementationSuffix;
    }

    /**
     * 実装クラスの<code>suffix</code>を設定します。
     * 
     * @param implementationSuffix
     */
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

    public String getConnectorPackageName() {
        return fromSuffixToPackageName(connectorSuffix);
    }

    public String getImplementationPackageName() {
        return fromSuffixToPackageName(implementationSuffix);
    }

    public String getEntityPackageName() {
        return entityPackageName;
    }

    /**
     * <code>Entity</code>のパッケージを設定します。
     * 
     * @param entityPackage
     */
    public void setEntityPackage(String entityPackage) {
        this.entityPackageName = entityPackage;
    }

    public String getViewExtension() {
        return viewExtension;
    }

    /**
     * <code>View</code>の拡張子を設定します。
     * 
     * @param viewExtension
     */
    public void setViewExtension(String viewExtension) {
        this.viewExtension = viewExtension;
    }

    public String getViewRootPath() {
        return viewRootPath;
    }

    /**
     * <code>View</code>のルートパスを設定します。
     * 
     * @param viewRootPath
     */
    public void setViewRootPath(String viewRootPath) {
        this.viewRootPath = viewRootPath;
    }

    public String adjustViewRootPath() {
        return viewRootPath == "/" ? "" : viewRootPath;
    }

    public String getSubApplicationRootPackageName() {
        return subApplicationRootPackageName;
    }

    /**
     * サブアプリケーションのルートパッケージ名を設定します。
     * 
     * @param subApplicationRootPackageName
     */
    public void setSubApplicationRootPackageName(
            String subApplicationRootPackageName) {
        this.subApplicationRootPackageName = subApplicationRootPackageName;
    }

    public String[] getRootPackageNames() {
        return rootPackageNames;
    }

    /**
     * ルートパッケージ名を追加します。
     * 
     * @param rootPackageName
     */
    public void addRootPackageName(String rootPackageName) {
        rootPackageNames = (String[]) ArrayUtil.add(rootPackageNames,
                rootPackageName);
        addExistChecker(rootPackageName);
    }

    public String[] getIgnorePackageNames() {
        return ignorePackageNames;
    }

    /**
     * 無視するパッケージ名を追加します。
     * 
     * @param ignorePackageName
     */
    public void addIgnorePackageName(String ignorePackageName) {
        ignorePackageNames = (String[]) ArrayUtil.add(ignorePackageNames,
                ignorePackageName);
    }

    /**
     * インターフェース名と実装クラス名の関連を追加します。
     * 
     * @param interfaceName
     * @param implementationClassName
     */
    public void addInterfaceToImplementationClassName(
            final String interfaceName, final String implementationClassName) {
        interfaceToImplementationMap
                .put(interfaceName, implementationClassName);
        implementationToInterfaceMap
                .put(implementationClassName, interfaceName);
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

    /**
     * サブアプリケーションサフィックスかどうかを返します。
     * 
     * @param suffix
     *            サフィックス
     * @return サブアプリケーションサフィックスかどうか
     */
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

    /**
     * クラスを探します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     * @param middlePackageName
     *            ミドルパッケージ名
     * @param partOfClassName
     *            クラス名の一部
     * @return クラス
     */
    protected Class findClass(final String rootPackageName,
            final String middlePackageName, final String partOfClassName) {
        initialize();

        final String backPartOfClassName = ClassUtil.concatName(
                middlePackageName, partOfClassName);
        final String className = ClassUtil.concatName(rootPackageName,
                backPartOfClassName);
        final String backPartOfImplClassName = toImplementationClassName(backPartOfClassName);
        final String implClassName = ClassUtil.concatName(rootPackageName,
                backPartOfImplClassName);

        if (!isIgnoreClassName(implClassName)
                && isExist(rootPackageName, backPartOfImplClassName)) {
            return ClassUtil.forName(implClassName);
        }

        if (!isIgnoreClassName(className)
                && isExist(rootPackageName, backPartOfClassName)) {
            return ClassUtil.forName(className);
        }
        return null;
    }

    public String toImplementationClassName(String className) {
        String implementationClassName = (String) interfaceToImplementationMap
                .get(className);
        if (implementationClassName != null) {
            return implementationClassName;
        }
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
        String interfaceClassName = (String) implementationToInterfaceMap
                .get(className);
        if (interfaceClassName != null) {
            return interfaceClassName;
        }
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

    public boolean isSkipClass(final Class clazz) {
        if (clazz.isInterface()) {
            return false;
        }
        for (final Iterator it = interfaceToImplementationMap.entrySet()
                .iterator(); it.hasNext();) {
            final Entry entry = (Entry) it.next();
            final Class interfaceClass = ClassUtil.forName((String) entry
                    .getKey());
            if (interfaceClass.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
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
        String[] names = StringUtil.split(componentName, PACKAGE_SEPARATOR_STR);
        StringBuffer buf = new StringBuffer(50);
        for (int i = 0; i < names.length; ++i) {
            if (i == names.length - 1) {
                buf.append(StringUtil.capitalize(names[i]));
            } else {
                buf.append(names[i]).append(".");
            }
        }
        return buf.toString();
    }

    public String fromComponentNameToSuffix(String componentName) {
        return fromNameToSuffix(componentName);
    }

    public String fromClassNameToSuffix(String componentName) {
        return fromNameToSuffix(componentName);
    }

    /**
     * 名前をサフィックスに変換します。
     * 
     * @param name
     *            名前
     * @return サフィックス
     */
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

    public boolean isValidViewRootPath(String path) {
        if (!path.startsWith(viewRootPath) || !path.endsWith(viewExtension)) {
            return false;
        }
        return true;
    }

    public String fromPathToPageName(String path) {
        return fromPathToComponentName(path, pageSuffix);
    }

    /**
     * パスをコンポーネント名に変換します。
     * 
     * @param path
     *            パス
     * @param nameSuffix
     *            サフィックス
     * @return コンポーネント名
     */
    protected String fromPathToComponentName(String path, String nameSuffix) {
        if (!path.startsWith(viewRootPath) || !path.endsWith(viewExtension)) {
            throw new IllegalArgumentException(path);
        }
        String componentName = (path.substring(viewRootPath.length() + 1, path
                .length()
                - viewExtension.length()) + nameSuffix).replace('/', '_');
        int pos = componentName.lastIndexOf('_');
        if (pos == -1) {
            return StringUtil.decapitalize(componentName);
        }
        return componentName.substring(0, pos + 1)
                + StringUtil.decapitalize(componentName.substring(pos + 1));
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
        return adjustViewRootPath() + "/"
                + name.replace(PACKAGE_SEPARATOR, '/') + viewExtension;
    }

    public String fromPageClassToPath(Class pageClass) {
        String componentName = fromClassNameToComponentName(pageClass.getName());
        return fromPageNameToPath(componentName);
    }

    public String fromActionNameToPath(String actionName) {
        if (!actionName.endsWith(actionSuffix)) {
            throw new IllegalArgumentException(actionName);
        }
        String name = actionName.substring(0, actionName.length()
                - actionSuffix.length());
        return adjustViewRootPath() + "/"
                + name.replace(PACKAGE_SEPARATOR, '/') + viewExtension;
    }

    public String fromActionNameToPageName(String actionName) {
        if (!actionName.endsWith(actionSuffix)) {
            throw new IllegalArgumentException(actionName);
        }
        return actionName.substring(0, actionName.length()
                - actionSuffix.length())
                + pageSuffix;
    }

    public boolean isTargetClassName(final String className, final String suffix) {
        if (isIgnoreClassName(className)) {
            return false;
        }
        if (!StringUtil.trimSuffix(className, implementationSuffix).endsWith(
                suffix)) {
            return false;
        }
        final String shortClassName = ClassUtil.getShortClassName(className);
        if (className.endsWith(implementationSuffix)
                && !className.endsWith("." + getImplementationPackageName()
                        + "." + shortClassName)) {
            return false;
        }
        final String middlePkgName = StringUtil.decapitalize(suffix);
        for (int i = 0; i < rootPackageNames.length; ++i) {
            if (className.startsWith(rootPackageNames[i] + "." + middlePkgName
                    + ".")) {
                return true;
            }
            if (className.startsWith(rootPackageNames[i] + "."
                    + subApplicationRootPackageName + ".")) {
                return true;
            }
        }
        return false;
    }

    public boolean isTargetClassName(final String className) {
        if (isIgnoreClassName(className)) {
            return false;
        }
        for (int i = 0; i < rootPackageNames.length; ++i) {
            if (className.startsWith(rootPackageNames[i] + ".")) {
                return true;
            }
        }
        return false;
    }

    public boolean isIgnoreClassName(String className) {
        for (int i = 0; i < ignorePackageNames.length; ++i) {
            if (className.startsWith(ignorePackageNames[i] + ".")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 存在するかどうかを返します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     * @param lastClassName
     *            クラス名の最後
     * @return 存在するかどうか
     */
    protected boolean isExist(final String rootPackageName,
            final String lastClassName) {
        final ExistChecker[] checkerArray = getExistCheckerArray(rootPackageName);
        for (int i = 0; i < checkerArray.length; ++i) {
            if (checkerArray[i].isExist(lastClassName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 存在チェッカの配列を返します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     * @return 存在チェッカの配列
     */
    protected ExistChecker[] getExistCheckerArray(final String rootPackageName) {
        return (ExistChecker[]) existCheckerArrays.get(rootPackageName);
    }

    /**
     * 存在チェッカを追加します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     */
    protected void addExistChecker(final String rootPackageName) {
        ExistChecker[] checkerArray = createExistCheckerArray(rootPackageName);
        existCheckerArrays.put(rootPackageName, checkerArray);
    }

    /**
     * 存在チェッカの配列を作成します。
     * 
     * @param rootPackageName
     *            ルートパッケージ名
     * @return 存在チェッカの配列
     */
    protected ExistChecker[] createExistCheckerArray(
            final String rootPackageName) {
        if (StringUtil.isEmpty(rootPackageName)) {
            return new ExistChecker[0];
        }
        final String s = rootPackageName.replace('.', '/');
        final List list = new ArrayList();
        for (final Iterator it = ClassLoaderUtil.getResources(this.getClass(),
                s); it.hasNext();) {
            final URL url = (URL) it.next();
            final String protocol = URLUtil.toCanonicalProtocol(url
                    .getProtocol());
            if ("file".equals(protocol)) {
                list.add(new FileExistChecker(url));
            } else if ("jar".equals(protocol)) {
                list.add(new JarExistChecker(url, rootPackageName));
            } else if ("zip".equals(protocol)) {
                list.add(new ZipExistChecker(url, rootPackageName));
            } else if ("code-source".equals(protocol)) {
                list.add(new CodeSourceExistChecker(url, rootPackageName));
            }
        }
        return (ExistChecker[]) list.toArray(new ExistChecker[list.size()]);
    }

    /**
     * パス名を返します。
     * 
     * @param lastClassName
     *            クラス名の最後
     * @return パス名
     */
    protected static String getPathName(final String lastClassName) {
        return lastClassName.replace('.', '/') + ".class";
    }

    /**
     * 存在チェッカのインターフェースです。
     * 
     */
    protected static interface ExistChecker {
        /**
         * クラスが存在するかどうかを返します。
         * 
         * @param lastClassName
         * @return クラスが存在するかどうか
         */
        boolean isExist(String lastClassName);

        /**
         * リソースを開放します。
         */
        void close();
    }

    /**
     * ファイル用の存在チェッカです。
     * 
     */
    protected static class FileExistChecker implements ExistChecker {
        private File rootFile;

        /**
         * {@link FileExistChecker}を作成します。
         * 
         * @param rootUrl
         *            ルートURL
         */
        protected FileExistChecker(final URL rootUrl) {
            rootFile = URLUtil.toFile(rootUrl);
        }

        public boolean isExist(final String lastClassName) {
            final File file = new File(rootFile, getPathName(lastClassName));
            return file.exists();
        }

        public void close() {
        }
    }

    /**
     * jar用の存在チェッカです。
     * 
     */
    protected static class JarExistChecker implements ExistChecker {
        private JarFile jarFile;

        private String rootPath;

        /**
         * {@link JarExistChecker}を作成します。
         * 
         * @param jarUrl
         *            jar URL
         * @param rootPackageName
         *            ルートパッケージ名
         */
        protected JarExistChecker(final URL jarUrl, final String rootPackageName) {
            jarFile = JarFileUtil.toJarFile(jarUrl);
            this.rootPath = rootPackageName.replace('.', '/') + "/";
        }

        public boolean isExist(final String lastClassName) {
            return jarFile.getEntry(rootPath + getPathName(lastClassName)) != null;
        }

        public void close() {
            JarFileUtil.close(jarFile);
        }
    }

    /**
     * zip用の存在チェッカです。
     * 
     */
    protected static class ZipExistChecker implements ExistChecker {
        private ZipFile zipFile;

        private String rootPath;

        /**
         * {@link ZipExistChecker}を作成します。
         * 
         * @param zipUrl
         *            zip URL
         * @param rootPackageName
         *            ルートパッケージ名
         */
        protected ZipExistChecker(final URL zipUrl, final String rootPackageName) {
            zipFile = ZipFileUtil.toZipFile(zipUrl);
            this.rootPath = rootPackageName.replace('.', '/') + "/";
        }

        public boolean isExist(final String lastClassName) {
            return zipFile.getEntry(rootPath + getPathName(lastClassName)) != null;
        }

        public void close() {
            ZipFileUtil.close(zipFile);
        }
    }

    /**
     * OC4J用の存在チェッカです。
     * 
     */
    protected static class CodeSourceExistChecker implements ExistChecker {
        private JarFile jarFile;

        private String rootPath;

        /**
         * {@link CodeSourceExistChecker}を作成します。
         * 
         * @param url
         *            URL
         * @param rootPackageName
         *            ルートパッケージ名
         */
        protected CodeSourceExistChecker(final URL url,
                final String rootPackageName) {
            final URL jarUrl = URLUtil.create("jar:file:" + url.getPath());
            jarFile = JarFileUtil.toJarFile(jarUrl);
            this.rootPath = rootPackageName.replace('.', '/') + "/";
        }

        public boolean isExist(final String lastClassName) {
            return jarFile.getEntry(rootPath + getPathName(lastClassName)) != null;
        }

        public void close() {
            JarFileUtil.close(jarFile);
        }
    }

}
