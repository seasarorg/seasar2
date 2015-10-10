/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;

/**
 * ファイルシステム上やJarファイル中に展開されているリソースの集まりを扱うユーティリティクラスです。
 * <p>
 * 次のプロトコルをサポートしています。
 * </p>
 * <ul>
 * <li><code>file</code></li>
 * <li><code>jar</code></li>
 * <li><code>wsjar</code>(WebShpere独自プロトコル、<code>jar</code>の別名)</li>
 * <li><code>zip</code>(WebLogic独自プロトコル)</li>
 * <li><code>code-source</code>(Oracle AS(OC4J)独自プロトコル)</li>
 * <li><code>vfsfile</code>(JBossAS5独自プロトコル、<code>file</code>の別名)</li>
 * <li><code>vfszip</code>(JBossAS5独自プロトコル)</li>
 * </ul>
 * 
 * @author koichik
 * @see URLUtil#toCanonicalProtocol(String)
 */
public class ResourcesUtil {

    /** 空の{@link Resources}の配列です。 */
    protected static final Resources[] EMPTY_ARRAY = new Resources[0];

    private static final Logger logger = Logger.getLogger(ResourcesUtil.class);

    /** URLのプロトコルをキー、{@link ResourceTypeFactory}を値とするマッピングです。 */
    protected static final Map resourcesTypeFactories = new HashMap();
    static {
        addResourcesFactory("file", new ResourcesFactory() {
            public Resources create(final URL url, final String rootPackage,
                    final String rootDir) {
                return new FileSystemResources(getBaseDir(url, rootDir),
                        rootPackage, rootDir);
            }
        });
        addResourcesFactory("jar", new ResourcesFactory() {
            public Resources create(final URL url, final String rootPackage,
                    final String rootDir) {
                return new JarFileResources(url, rootPackage, rootDir);
            }
        });
        addResourcesFactory("zip", new ResourcesFactory() {
            public Resources create(final URL url, final String rootPackage,
                    final String rootDir) {
                return new JarFileResources(JarFileUtil.create(new File(
                        ZipFileUtil.toZipFilePath(url))), rootPackage, rootDir);
            }
        });
        addResourcesFactory("code-source", new ResourcesFactory() {
            public Resources create(final URL url, final String rootPackage,
                    final String rootDir) {
                return new JarFileResources(URLUtil.create("jar:file:"
                        + url.getPath()), rootPackage, rootDir);
            }
        });
        addResourcesFactory("vfszip", new ResourcesFactory() {
            public Resources create(final URL url, final String rootPackage,
                    final String rootDir) {
                return new VfsZipResources(url, rootPackage, rootDir);
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
    public static void addResourcesFactory(final String protocol,
            ResourcesFactory factory) {
        resourcesTypeFactories.put(protocol, factory);
    }

    /**
     * 指定のクラスを基点とするリソースの集まりを扱う{@link Resources}を返します。
     * <p>
     * このメソッドが返す{@link Resources}は、指定されたクラスをFQNで参照可能なパスをルートとします。 例えば指定されたクラスが
     * <code>foo.Bar</code>で、そのクラスファイルが<code>classes/foo/Bar.class</code>の場合、
     * このメソッドが返す{@link Resources}は<code>classes</code>ディレクトリ以下のリソースの集合を扱います。
     * </p>
     * 
     * @param referenceClass
     *            基点となるクラス
     * @return 指定のクラスを基点とするリソースの集まりを扱う{@link Resources}
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
     * @param rootDir
     *            ルートディレクトリ
     * @return 指定のディレクトリを基点とするリソースの集まりを扱う{@link ResourceType}
     */
    public static Resources getResourcesType(final String rootDir) {
        final URL url = ResourceUtil
                .getResource(rootDir.endsWith("/") ? rootDir : rootDir + '/');
        return getResourcesType(url, null, rootDir);
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
     * @param rootDir
     *            ルートディレクトリ
     * @return URLを扱う{@link Resources}
     */
    protected static Resources getResourcesType(final URL url,
            final String rootPackage, final String rootDir) {
        final ResourcesFactory factory = (ResourcesFactory) resourcesTypeFactories
                .get(URLUtil.toCanonicalProtocol(url.getProtocol()));
        if (factory != null) {
            return factory.create(url, rootPackage, rootDir);
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
         * @param rootDir
         *            ルートディレクトリ
         * @return URLで表されたリソースを扱う{@link Resources}
         */
        Resources create(URL url, String rootPackage, String rootDir);
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
         * インスタンス構築時にルートディレクトリが指定されている場合は、 ルートディレクトリ以下のリソースのみが対象となります。
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

        /** ルートディレクトリです。 */
        protected final String rootDir;

        /**
         * インスタンスを構築します。
         * 
         * @param baseDir
         *            ベースディレクトリ
         * @param rootPackage
         *            ルートパッケージ
         * @param rootDir
         *            ルートディレクトリ
         */
        public FileSystemResources(final File baseDir,
                final String rootPackage, final String rootDir) {
            this.baseDir = baseDir;
            this.rootPackage = rootPackage;
            this.rootDir = rootDir;
        }

        /**
         * インスタンスを構築します。
         * 
         * @param url
         *            ディレクトリを表すURL
         * @param rootPackage
         *            ルートパッケージ
         * @param rootDir
         *            ルートディレクトリ
         */
        public FileSystemResources(final URL url, final String rootPackage,
                final String rootDir) {
            this(URLUtil.toFile(url), rootPackage, rootDir);
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
            ResourceTraversal.forEach(baseDir, rootDir, handler);
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

        /** ルートディレクトリです。 */
        final protected String rootDir;

        /**
         * インスタンスを構築します。
         * 
         * @param jarFile
         *            Jarファイル
         * @param rootPackage
         *            ルートパッケージ
         * @param rootDir
         *            ルートディレクトリ
         */
        public JarFileResources(final JarFile jarFile,
                final String rootPackage, final String rootDir) {
            this.jarFile = jarFile;
            this.rootPackage = rootPackage;
            this.rootDir = rootDir;
        }

        /**
         * インスタンスを構築します。
         * 
         * @param url
         *            Jarファイルを表すURL
         * @param rootPackage
         *            ルートパッケージ
         * @param rootDir
         *            ルートディレクトリ
         */
        public JarFileResources(final URL url, final String rootPackage,
                final String rootDir) {
            this(JarFileUtil.toJarFile(url), rootPackage, rootDir);
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
                            || (packageName != null && packageName
                                    .startsWith(rootPackage))) {
                        handler.processClass(packageName, shortClassName);
                    }
                }
            });
        }

        public void forEach(final ResourceHandler handler) {
            ResourceTraversal.forEach(jarFile, new ResourceHandler() {
                public void processResource(String path, InputStream is) {
                    if (rootDir == null || path.startsWith(rootDir)) {
                        handler.processResource(path, is);
                    }
                }
            });
        }

        public void close() {
            JarFileUtil.close(jarFile);
        }

    }

    /**
     * JBossAS5のvfszipプロトコルで表されるリソースの集まりを扱うオブジェクトです。
     * 
     * @author koichik
     */
    public static class VfsZipResources implements Resources {

        /** WAR内の.classファイルの接頭辞です。 */
        protected static final String WAR_CLASSES_PREFIX = "/WEB-INF/CLASSES/";

        /** ルートパッケージです。 */
        protected final String rootPackage;

        /** ルートディレクトリです。 */
        final protected String rootDir;

        /** ZipのURLです。 */
        protected final URL zipUrl;

        /** Zip内のエントリの接頭辞です。 */
        protected final String prefix;

        /** Zip内のエントリ名の{@link Set}です。 */
        protected final Set entryNames = new HashSet();

        /**
         * インスタンスを構築します。
         * 
         * @param url
         *            ルートを表すURL
         * @param rootPackage
         *            ルートパッケージ
         * @param rootDir
         *            ルートディレクトリ
         */
        public VfsZipResources(final URL url, final String rootPackage,
                final String rootDir) {
            URL zipUrl = url;
            String prefix = "";
            if (rootPackage != null) {
                final String[] paths = rootPackage.split("\\.");
                for (int i = 0; i < paths.length; ++i) {
                    zipUrl = URLUtil.create(zipUrl, "..");
                }
            }
            loadFromZip(zipUrl);
            if (entryNames.isEmpty()) {
                final String zipUrlString = zipUrl.toExternalForm();
                if (zipUrlString.toUpperCase().endsWith(WAR_CLASSES_PREFIX)) {
                    final URL warUrl = URLUtil.create(zipUrl, "../..");
                    final String path = warUrl.getPath();
                    zipUrl = FileUtil.toURL(new File(path.substring(0, path
                            .length() - 1)));
                    prefix = zipUrlString.substring(warUrl.toExternalForm()
                            .length());
                    loadFromZip(zipUrl);
                }
            }

            this.rootPackage = rootPackage;
            this.rootDir = rootDir;
            this.zipUrl = zipUrl;
            this.prefix = prefix;
        }

        private void loadFromZip(final URL zipUrl) {
            final ZipInputStream zis = new ZipInputStream(URLUtil
                    .openStream(zipUrl));
            try {
                ZipEntry entry = null;
                while ((entry = ZipInputStreamUtil.getNextEntry(zis)) != null) {
                    entryNames.add(entry.getName());
                    ZipInputStreamUtil.closeEntry(zis);
                }
            } finally {
                InputStreamUtil.close(zis);
            }
        }

        public boolean isExistClass(final String className) {
            final String entryName = prefix
                    + toClassFile(ClassUtil.concatName(rootPackage, className));
            return entryNames.contains(entryName);
        }

        public void forEach(final ClassHandler handler) {
            final ZipInputStream zis = new ZipInputStream(URLUtil
                    .openStream(zipUrl));
            try {
                ClassTraversal.forEach(zis, prefix, new ClassHandler() {
                    public void processClass(String packageName,
                            String shortClassName) {
                        if (rootPackage == null
                                || (packageName != null && packageName
                                        .startsWith(rootPackage))) {
                            handler.processClass(packageName, shortClassName);
                        }
                    }
                });
            } finally {
                InputStreamUtil.close(zis);
            }
        }

        public void forEach(final ResourceHandler handler) {
            final ZipInputStream zis = new ZipInputStream(URLUtil
                    .openStream(zipUrl));
            try {
                ResourceTraversal.forEach(zis, prefix, new ResourceHandler() {
                    public void processResource(String path, InputStream is) {
                        if (rootDir == null || path.startsWith(rootDir)) {
                            handler.processResource(path, is);
                        }
                    }
                });
            } finally {
                InputStreamUtil.close(zis);
            }
        }

        public void close() {
        }

    }

}
