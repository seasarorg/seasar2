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
package org.seasar.framework.container.cooldeploy;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

import org.seasar.framework.container.ComponentCreator;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.util.S2ContainerUtil;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.JarFileUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.URLUtil;
import org.seasar.framework.util.ZipFileUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;

/**
 * {@link NamingConvention}に一致するコンポーネントを自動登録するクラスです。
 * 
 * @author higa
 * 
 */
public class CoolComponentAutoRegister implements ClassHandler {

    /**
     * InitMethodアノテーションの定義です。
     */
    public static final String INIT_METHOD = "registerAll";

    /**
     * Bindingアノテーションの定義です。
     */
    public static final String container_BINDING = "bindingType=must";

    private S2Container container;

    private Map strategies = new HashMap();

    private ComponentCreator[] creators;

    private NamingConvention namingConvention;

    private Set registerdClasses = new HashSet();

    /**
     * {@link CoolComponentAutoRegister}を作成します。
     */
    public CoolComponentAutoRegister() {
        addStrategy("file", new FileSystemStrategy());
        addStrategy("jar", new JarFileStrategy());
        addStrategy("zip", new ZipFileStrategy());
        addStrategy("code-source", new CodeSourceFileStrategy());
    }

    /**
     * {@link S2Container}を返します。
     * 
     * @return {@link S2Container}
     */
    public S2Container getContainer() {
        return container;
    }

    /**
     * {@link S2Container}を設定します。
     * 
     * @param container
     */
    public void setContainer(S2Container container) {
        this.container = container;
    }

    /**
     * 登録されているストラテジを返します。
     * 
     * @return 登録されているストラテジ
     */
    public Map getStrategies() {
        return strategies;
    }

    /**
     * {@link Strategy} を返します。
     * 
     * @param protocol
     * @return {@link Strategy}
     */
    protected Strategy getStrategy(String protocol) {
        return (Strategy) strategies.get(URLUtil.toCanonicalProtocol(protocol));
    }

    /**
     * {@link Strategy}を追加します。
     * 
     * @param protocol
     * @param strategy
     */
    protected void addStrategy(String protocol, Strategy strategy) {
        strategies.put(protocol, strategy);
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
     * 自動登録を行います。
     */
    public void registerAll() {
        try {
            final String[] rootPackageNames = namingConvention
                    .getRootPackageNames();
            if (rootPackageNames != null) {
                for (int i = 0; i < rootPackageNames.length; ++i) {
                    final String rootDir = rootPackageNames[i]
                            .replace('.', '/');
                    for (final Iterator it = ClassLoaderUtil
                            .getResources(rootDir); it.hasNext();) {
                        final URL url = (URL) it.next();
                        final Strategy strategy = getStrategy(URLUtil
                                .toCanonicalProtocol(url.getProtocol()));
                        strategy.registerAll(rootDir, url);
                    }
                }
                webSphereClassLoaderFix();
            }
        } finally {
            registerdClasses.clear();
        }
    }

    /**
     * Jarファイルからコンポーネントの登録を行います。
     * <p>
     * WebSphere のクラスローダーはJarファイル中のディレクトリエントリを<code>ClassLoader#getResource()</code>で
     * 返してくれないので、 S2のJarと同じ場所にあるJarファイルからコンポーネントの登録を行います。
     * </p>
     */
    protected void webSphereClassLoaderFix() {
        final URL url = ResourceUtil.getResourceNoException(getClass()
                .getName().replace('.', '/')
                + ".class");
        if ("wsjar".equals(url.getProtocol())) {
            final File s2JarFile = new File(JarFileUtil.toJarFile(url)
                    .getName());
            final File libDir = s2JarFile.getParentFile();
            final File[] jarFiles = libDir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jar");
                }
            });
            for (int i = 0; i < jarFiles.length; ++i) {
                final JarFile jarFile = JarFileUtil.create(jarFiles[i]);
                ClassTraversal.forEach(jarFile, this);
            }
        }
    }

    public void processClass(String packageName, String shortClassName) {
        if (shortClassName.indexOf('$') != -1) {
            return;
        }
        String className = ClassUtil.concatName(packageName, shortClassName);
        if (!namingConvention.isTargetClassName(className)) {
            return;
        }
        Class clazz = ClassUtil.forName(className);
        if (namingConvention.isSkipClass(clazz)) {
            return;
        }
        if (container.getRoot().hasComponentDef(clazz)) {
            return;
        }
        ComponentDef cd = createComponentDef(clazz);
        if (cd == null) {
            return;
        }
        if (registerdClasses.contains(cd.getComponentClass())) {
            return;
        }
        container.getRoot().register(cd);
        registerdClasses.add(cd.getComponentClass());
        S2ContainerUtil.putRegisterLog(cd);
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
     * プロトコルに応じた自動登録を行なうストラテジです。
     * 
     */
    protected interface Strategy {
        /**
         * 自動登録を行います。
         * 
         * @param path
         * @param url
         */
        void registerAll(String path, URL url);
    }

    /**
     * ファイルシステム用の
     * {@link org.seasar.framework.container.cooldeploy.CoolComponentAutoRegister.Strategy}です。
     * 
     */
    protected class FileSystemStrategy implements Strategy {

        public void registerAll(String path, URL url) {
            File rootDir = getRootDir(path, url);
            String[] rootPackageNames = namingConvention.getRootPackageNames();
            for (int i = 0; i < rootPackageNames.length; ++i) {
                ClassTraversal.forEach(rootDir, rootPackageNames[i],
                        CoolComponentAutoRegister.this);
            }
        }

        /**
         * ルートディレクトリを返します。
         * 
         * @param path
         * @param url
         * @return ルートディレクトリ
         */
        protected File getRootDir(String path, URL url) {
            File file = URLUtil.toFile(url);
            String[] names = StringUtil.split(path, "/");
            for (int i = 0; i < names.length; ++i) {
                file = file.getParentFile();
            }
            return file;
        }
    }

    /**
     * jarファイル用の {@link CoolComponentAutoRegister.Strategy}です。
     * 
     */
    protected class JarFileStrategy implements Strategy {

        public void registerAll(String path, URL url) {
            JarFile jarFile = createJarFile(url);
            ClassTraversal.forEach(jarFile, CoolComponentAutoRegister.this);
        }

        /**
         * {@link JarFile}を作成します。
         * 
         * @param url
         * @return {@link JarFile}
         */
        protected JarFile createJarFile(URL url) {
            return JarFileUtil.toJarFile(url);
        }
    }

    /**
     * WebLogic固有の<code>zip:</code>プロトコルで表現されるURLをサポートするストラテジです。
     */
    protected class ZipFileStrategy implements Strategy {

        public void registerAll(String path, URL url) {
            final JarFile jarFile = createJarFile(url);
            ClassTraversal.forEach(jarFile, CoolComponentAutoRegister.this);
        }

        /**
         * {@link JarFile}を作成します。
         * 
         * @param url
         * @return {@link JarFile}
         */
        protected JarFile createJarFile(URL url) {
            final String jarFileName = ZipFileUtil.toZipFilePath(url);
            return JarFileUtil.create(new File(jarFileName));
        }
    }

    /**
     * OC4J固有の<code>code-source:</code>プロトコルで表現されるURLをサポートするストラテジです。
     */
    protected class CodeSourceFileStrategy implements Strategy {

        public void registerAll(String path, URL url) {
            final JarFile jarFile = createJarFile(url);
            ClassTraversal.forEach(jarFile, CoolComponentAutoRegister.this);
        }

        /**
         * {@link JarFile}を作成します。
         * 
         * @param url
         * @return {@link JarFile}
         */
        protected JarFile createJarFile(final URL url) {
            final URL jarUrl = URLUtil.create("jar:file:" + url.getPath());
            return JarFileUtil.toJarFile(jarUrl);
        }
    }
}