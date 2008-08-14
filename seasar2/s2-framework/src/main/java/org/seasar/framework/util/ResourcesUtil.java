/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;

/**
 * ファイルシステム上やJarファイル中に展開されているリソースの集まりを扱うユーティリティクラスです。
 * 
 * @author koichik
 */
public class ResourcesUtil {

    /** 空の{@link Resources}の配列です。 */
    protected static final Resources[] EMPTY_ARRAY = new Resources[0];

    private static final Logger logger = Logger.getLogger(ResourcesUtil.class);

    /** URLのプロトコルをキー、{@link ResourceTypeFactory}を値とするマッピングです。 */
    protected static final Map resourcesTypeFactories = new HashMap();
    static {
        addResourcesTypeFactory("file", new ResourcesFactory() {
            public Resources create(final URL url, final String rootPackage,
                    final String baseName) {
                return new FileSystemResources(getBaseDir(url, baseName),
                        rootPackage, baseName);
            }
        });
        addResourcesTypeFactory("jar", new ResourcesFactory() {
            public Resources create(final URL url, final String rootPackage,
                    final String baseName) {
                return new JarFileResources(url, rootPackage, baseName);
            }
        });
        addResourcesTypeFactory("zip", new ResourcesFactory() {
            public Resources create(final URL url, final String rootPackage,
                    final String baseName) {
                return new JarFileResources(JarFileUtil.create(new File(
                        ZipFileUtil.toZipFilePath(url))), rootPackage, baseName);
            }
        });
        addResourcesTypeFactory("code-source", new ResourcesFactory() {
            public Resources create(final URL url, final String rootPackage,
                    final String baseName) {
                return new JarFileResources(URLUtil.create("jar:file:"
                        + url.getPath()), rootPackage, baseName);
            }
        });
    }

    /**
     * {@link ResourcesFactory}を追加します。
     * 
     * @param protocol
     *            URLのプロトコル
     * @param factory
     *            プロトコルに対応する{@link Resources}のファクトリ
     */
    public static void addResourcesTypeFactory(final String protocol,
            ResourcesFactory factory) {
        resourcesTypeFactories.put(protocol, factory);
    }

    /**
     * 指定のクラスを基点とするリソースの集まりを扱う{@link ResourceType}を返します。
     * 
     * @param referenceClass
     *            基点となるクラス
     * @return 指定のクラスを基点とするリソースの集まりを扱う{@link ResourceType}
     */
    public static Resources getResourcesType(final Class referenceClass) {
        final URL url = ResourceUtil.getResource(toClassFile(referenceClass
                .getName()));
        final String path[] = referenceClass.getName().split("\\.");
        String baseUrl = url.toExternalForm();
        for (int i = 0; i < path.length; ++i) {
            int pos = baseUrl.lastIndexOf('/');
            baseUrl = baseUrl.substring(0, pos);
        }
        return getResourcesType(URLUtil.create(baseUrl + '/'), null, null);
    }

    /**
     * 指定のディレクトリを基点とするリソースの集まりを扱う{@link ResourceType}を返します。
     * 
     * @param baseDir
     *            基点となるディレクトリ
     * @return 指定のディレクトリを基点とするリソースの集まりを扱う{@link ResourceType}
     */
    public static Resources getResourcesType(final String baseDir) {
        final URL url = ResourceUtil
                .getResource(baseDir.endsWith("/") ? baseDir : baseDir + '/');
        return getResourcesType(url, null, baseDir);
    }

    /**
     * 指定のルートパッケージを基点とするリソースの集まりを扱う{@link ResourceType}の配列を返します。
     * 
     * @param rootPackage
     *            ルートパッケージ
     * @return 指定のルートパッケージを基点とするリソースの集まりを扱う{@link ResourceType}の配列
     */
    public static Resources[] getResourcesTypes(final String rootPackage) {
        if (StringUtil.isEmpty(rootPackage)) {
            return EMPTY_ARRAY;
        }

        final String baseName = toDirectoryName(rootPackage);
        final List list = new ArrayList();
        for (final Iterator it = ClassLoaderUtil.getResources(baseName); it
                .hasNext();) {
            final URL url = (URL) it.next();
            final Resources resourcesType = getResourcesType(url, rootPackage,
                    baseName);
            if (resourcesType != null) {
                list.add(resourcesType);
            }
        }
        if (list.isEmpty()) {
            logger.log("WSSR0014", new Object[] { rootPackage });
            return EMPTY_ARRAY;
        }
        return (Resources[]) list.toArray(new Resources[list.size()]);
    }

    /**
     * URLを扱う{@link Resources}を作成して返します。
     * <p>
     * URLのプロトコルが未知の場合は<code>null</code>を返します。
     * </p>
     * 
     * @param url
     *            リソースのURL
     * @param rootPackage
     *            ルートパッケージ
     * @param baseName
     *            ベース名
     * @return URLを扱う{@link Resources}
     */
    protected static Resources getResourcesType(final URL url,
            final String rootPackage, final String baseName) {
        final ResourcesFactory factory = (ResourcesFactory) resourcesTypeFactories
                .get(URLUtil.toCanonicalProtocol(url.getProtocol()));
        if (factory != null) {
            return factory.create(url, rootPackage, baseName);
        }
        logger.log("WSSR0013", new Object[] { rootPackage, url });
        return null;
    }

    /**
     * パッケージ名をディレクトリ名に変換して返します。
     * 
     * @param packageName
     *            パッケージ名
     * @return ディレクトリ名
     */
    protected static String toDirectoryName(final String packageName) {
        if (StringUtil.isEmpty(packageName)) {
            return null;
        }
        return packageName.replace('.', '/') + '/';
    }

    /**
     * クラス名をクラスファイルのパス名に変換して返します。
     * 
     * @param className
     *            クラス名
     * @return クラスファイルのパス名
     */
    protected static String toClassFile(final String className) {
        return className.replace('.', '/') + ".class";
    }

    /**
     * ファイルを表すURLからルートパッケージの上位となるベースディレクトリを求めて返します。
     * 
     * @param url
     *            ファイルを表すURL
     * @param baseName
     *            ベース名
     * @return ルートパッケージの上位となるベースディレクトリ
     */
    protected static File getBaseDir(final URL url, final String baseName) {
        File file = URLUtil.toFile(url);
        final String[] paths = StringUtil.split(baseName, "/");
        for (int i = 0; i < paths.length; ++i) {
            file = file.getParentFile();
        }
        return file;
    }

    /**
     * {@link Resources}のインスタンスを作成するファクトリです。
     * 
     * @author koichik
     */
    public interface ResourcesFactory {
        /**
         * {@link Resources}のインスタンスを作成して返します。
         * 
         * @param url
         *            リソースを表すURL
         * @param rootPackage
         *            ルートパッケージ
         * @param baseName
         *            ベース名
         * @return URLで表されたリソースを扱う{@link Resources}
         */
        Resources create(URL url, String rootPackage, String baseName);
    }

    /**
     * リソースの集まりを表すオブジェクトです。
     * 
     * @author koichik
     */
    public interface Resources {

        /**
         * 指定されたクラス名に対応するクラスファイルがこのインスタンスが扱うリソースの中に存在すれば<code>true</code>を返します。
         * <p>
         * インスタンス構築時にルートパッケージが指定されている場合、 指定されたクラス名はルートパッケージからの相対名として解釈されます。
         * </p>
         * 
         * @param className
         *            クラス名
         * @return 指定されたクラス名に対応するクラスファイルがこのインスタンスが扱うリソースの中に存在すれば
         *         <code>true</code>
         */
        boolean isExistClass(final String className);

        /**
         * このインスタンスが扱うリソースの中に存在するクラスを探して
         * {@link ClassHandler#processClass(String, String) ハンドラ}をコールバックします。
         * <p>
         * インスタンス構築時にルートパッケージが指定されている場合は、 ルートパッケージ以下のクラスのみが対象となります。
         * </p>
         * 
         * @param handler
         *            ハンドラ
         */
        void forEach(ClassHandler handler);

        /**
         * このインスタンスが扱うリソースを探して
         * {@link ResourceHandler#processResource(String, java.io.InputStream)
         * ハンドラ}をコールバックします。
         * <p>
         * インスタンス構築時にベース名が指定されている場合は、 ベース名以下のリソースのみが対象となります。
         * </p>
         * 
         * @param handler
         *            ハンドラ
         */
        void forEach(ResourceHandler handler);

        /**
         * リソースの後処理を行います。
         */
        void close();

    }

    /**
     * ファイルシステム上のリソースの集まりを扱うオブジェクトです。
     * 
     * @author koichik
     */
    public static class FileSystemResources implements Resources {

        /** ベースディレクトリです。 */
        protected final File baseDir;

        /** ルートパッケージです。 */
        protected final String rootPackage;

        /** エントリのベース名です。 */
        protected final String baseName;

        /**
         * インスタンスを構築します。
         * 
         * @param baseDir
         *            ベースディレクトリ
         * @param rootPackage
         *            ルートパッケージ
         * @param baseName
         *            ベース名
         */
        public FileSystemResources(final File baseDir,
                final String rootPackage, final String baseName) {
            this.baseDir = baseDir;
            this.rootPackage = rootPackage;
            this.baseName = baseName;
        }

        /**
         * インスタンスを構築します。
         * 
         * @param url
         *            ディレクトリを表すURL
         * @param rootPackage
         *            ルートパッケージ
         * @param baseName
         *            ベース名
         */
        public FileSystemResources(final URL url, final String rootPackage,
                final String baseName) {
            this(URLUtil.toFile(url), rootPackage, baseName);
        }

        public boolean isExistClass(final String className) {
            final File file = new File(baseDir, toClassFile(ClassUtil
                    .concatName(rootPackage, className)));
            return file.exists();
        }

        public void forEach(final ClassHandler handler) {
            ClassTraversal.forEach(baseDir, rootPackage, handler);
        }

        public void forEach(final ResourceHandler handler) {
            ResourceTraversal.forEach(baseDir, baseName, handler);
        }

        public void close() {
        }

    }

    /**
     * Jarファイル中のリソースの集まりを扱うオブジェクトです。
     * 
     * @author koichik
     */
    public static class JarFileResources implements Resources {

        /** Jarファイルです。 */
        protected final JarFile jarFile;

        /** ルートパッケージです。 */
        protected final String rootPackage;

        /** エントリのベース名です。 */
        final protected String baseName;

        /**
         * インスタンスを構築します。
         * 
         * @param jarFile
         *            Jarファイル
         * @param rootPackage
         *            ルートパッケージ
         * @param baseName
         *            ベース名
         */
        public JarFileResources(final JarFile jarFile,
                final String rootPackage, final String baseName) {
            this.jarFile = jarFile;
            this.rootPackage = rootPackage;
            this.baseName = baseName;
        }

        /**
         * インスタンスを構築します。
         * 
         * @param url
         *            Jarファイルを表すURL
         * @param rootPackage
         *            ルートパッケージ
         * @param baseName
         *            ベース名
         */
        public JarFileResources(final URL url, final String rootPackage,
                final String baseName) {
            this(JarFileUtil.toJarFile(url), rootPackage, baseName);
        }

        public boolean isExistClass(final String className) {
            return jarFile.getEntry(toClassFile(ClassUtil.concatName(
                    rootPackage, className))) != null;
        }

        public void forEach(final ClassHandler handler) {
            ClassTraversal.forEach(jarFile, new ClassHandler() {
                public void processClass(String packageName,
                        String shortClassName) {
                    if (rootPackage == null
                            || packageName.startsWith(rootPackage)) {
                        handler.processClass(packageName, shortClassName);
                    }
                }
            });
        }

        public void forEach(final ResourceHandler handler) {
            ResourceTraversal.forEach(jarFile, new ResourceHandler() {
                public void processResource(String path, InputStream is) {
                    if (baseName == null || path.startsWith(baseName)) {
                        handler.processResource(path, is);
                    }
                }
            });
        }

        public void close() {
            JarFileUtil.close(jarFile);
        }

    }

}
