/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.ExternalContextComponentDefRegister;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.util.SmartDeployUtil;
import org.seasar.framework.env.Env;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.JarDuplicatedException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.InputStreamUtil;

/**
 * 唯一の{@link org.seasar.framework.container.S2Container S2コンテナ}
 * を提供するためのファクトリクラスです。
 * <p>
 * シングルトンS2コンテナファクトリは、設定ファイルに基づいてS2コンテナを生成・初期化し、それを保持します。
 * 保持されるS2コンテナは、このクラスをロードしたクラスローダで一意になります。
 * </p>
 * <p>
 * デフォルトの設定ファイル名は<code>app.dicon</code>となります。
 * </p>
 * <p>
 * シングルトンS2コンテナファクトリの標準的な利用方法としては、 アプリケーション開始時に{@link #init()}を呼び出して、
 * {@link #getContainer()}でS2コンテナを取得し、 アプリケーション終了時に{@link #destroy()}を呼び出します。
 * Webアプリケーションであれば
 * {@link javax.servlet.Servlet#init(javax.servlet.ServletConfig)}で
 * {@link #init()}を、 {@link javax.servlet.Servlet#destroy()}で{@link #destroy()}
 * を呼び出します。
 * </p>
 * 
 * @author koichik
 * @author goto
 */
public class SingletonS2ContainerFactory {

    private static final Logger logger = Logger
            .getLogger(SingletonS2ContainerFactory.class);

    private static String configPath = "app.dicon";

    private static ExternalContext externalContext;

    private static ExternalContextComponentDefRegister externalContextComponentDefRegister;

    private static S2Container container;

    private SingletonS2ContainerFactory() {
    }

    /**
     * 設定ファイルのファイルパスを返します。
     * 
     * @return 設定ファイルのファイルパス
     */
    public static String getConfigPath() {
        return configPath;
    }

    /**
     * 設定ファイルのファイルパスを設定します。
     * 
     * @param path
     *            設定ファイルのファイルパス
     */
    public static void setConfigPath(String path) {
        configPath = path;
    }

    /**
     * {@link org.seasar.framework.container.ExternalContext 外部コンテキスト}を返します。
     * 
     * @return 外部コンテキスト
     */
    public static ExternalContext getExternalContext() {
        return externalContext;
    }

    /**
     * {@link org.seasar.framework.container.ExternalContext 外部コンテキスト}を設定します。
     * 
     * @param extCtx
     *            外部コンテキスト
     */
    public static void setExternalContext(ExternalContext extCtx) {
        externalContext = extCtx;
    }

    /**
     * {@link org.seasar.framework.container.ExternalContextComponentDefRegister
     * 外部コンテキストコンポーネント定義レジスタ}を返します。
     * 
     * @return 外部コンテキストコンポーネント定義レジスタ
     */
    public static ExternalContextComponentDefRegister getExternalContextComponentDefRegister() {
        return externalContextComponentDefRegister;
    }

    /**
     * {@link org.seasar.framework.container.ExternalContextComponentDefRegister
     * 外部コンテキストコンポーネント定義レジスタ}を設定します。
     * 
     * @param extCtxComponentDefRegister
     *            外部コンテキストコンポーネント定義レジスタ
     */
    public static void setExternalContextComponentDefRegister(
            ExternalContextComponentDefRegister extCtxComponentDefRegister) {
        externalContextComponentDefRegister = extCtxComponentDefRegister;
    }

    /**
     * 設定ファイルに基づいてS2コンテナを生成・初期化し、それを保持します。 既にS2コンテナが保持されている場合は何もしません。
     * <p>
     * S2コンテナを生成した後、 初期化を行なう前に必要に応じて、
     * 外部コンテキストおよび外部コンテキストコンポーネント定義レジスタをS2コンテナに設定します。
     * </p>
     * 
     * @see S2ContainerFactory#create(String)
     * @see org.seasar.framework.container.ExternalContext
     * @see org.seasar.framework.container.ExternalContextComponentDefRegister
     */
    public static void init() {
        if (container != null) {
            return;
        }
        checkVersions();
        container = S2ContainerFactory.create(configPath);
        if (container.getExternalContext() == null) {
            if (externalContext != null) {
                container.setExternalContext(externalContext);
            }
        } else if (container.getExternalContext().getApplication() == null
                && externalContext != null) {
            container.getExternalContext().setApplication(
                    externalContext.getApplication());
        }
        if (container.getExternalContextComponentDefRegister() == null
                && externalContextComponentDefRegister != null) {
            container
                    .setExternalContextComponentDefRegister(externalContextComponentDefRegister);
        }
        container.init();
        logger.info("Running on [ENV]" + Env.getValue() + ", [DEPLOY MODE]"
                + SmartDeployUtil.getDeployMode(container));
    }

    /**
     * S2コンテナやその他の終了処理を行ないます。
     */
    public static void destroy() {
        if (container == null) {
            return;
        }
        container.destroy();
        container = null;
        DisposableUtil.dispose();
    }

    /**
     * 唯一のS2コンテナを返します。 S2コンテナが保持されていない場合、
     * {@link org.seasar.framework.exception.EmptyRuntimeException}をスローします。
     * 
     * @return S2コンテナ
     */
    public static S2Container getContainer() {
        if (container == null) {
            throw new EmptyRuntimeException("S2Container");
        }
        return container;
    }

    /**
     * 保持するS2コンテナを設定します。
     * 
     * @param c
     *            S2コンテナ
     */
    public static void setContainer(S2Container c) {
        container = c;
    }

    /**
     * S2コンテナを保持しているかどうかを返します。
     * 
     * @return S2コンテナを保持している場合は<code>true</code>、そうでない場合は<code>false</code>
     */
    public static boolean hasContainer() {
        return container != null;
    }

    private static void checkVersions() {
        checkVersion("s2-framework");
        checkVersion("s2-extension");
        checkVersion("s2-tiger");
    }

    private static void checkVersion(final String artifactId) {
        final Map versions = getVersions(artifactId);
        if (versions.isEmpty()) {
            return;
        }
        if (versions.size() > 1) {
            throw new JarDuplicatedException(artifactId, versions);
        }
        final String version = (String) versions.keySet().iterator().next();
        final List urlList = (List) versions.values().iterator().next();
        if (urlList.size() > 1) {
            logger.log("WSSR0016",
                    new Object[] { artifactId, version, urlList });
            return;
        }
        logger.log("ISSR0009", new Object[] { artifactId, version });
    }

    private static Map getVersions(final String artifactId) {
        final Map versions = new HashMap();
        try {
            final String name = "META-INF/maven/org.seasar.container/"
                    + artifactId + "/pom.properties";
            final Enumeration urls = Thread.currentThread()
                    .getContextClassLoader().getResources(name);
            while (urls.hasMoreElements()) {
                URL url = (URL) urls.nextElement();
                final InputStream is = url.openStream();
                try {
                    final Properties props = new Properties();
                    props.load(is);
                    String version = props.getProperty("version");
                    final List urlList;
                    if (versions.containsKey(version)) {
                        urlList = (List) versions.get(version);
                    } else {
                        urlList = new ArrayList();
                        versions.put(version, urlList);
                    }
                    urlList.add(url.toExternalForm());
                } finally {
                    InputStreamUtil.close(is);
                }
            }
        } catch (final Exception ignore) {
        }
        return versions;
    }

}
