/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.seasar.framework.container.ExtensionNotFoundRuntimeException;
import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.ExternalContextComponentDefRegister;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.assembler.AssemblerFactory;
import org.seasar.framework.container.deployer.ComponentDeployerFactory;
import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

/**
 * {@link org.seasar.framework.container.S2Container S2コンテナ}を構築するためのファクトリクラスです。
 * <p>
 * S2コンテナファクトリは、 diconファイルなどの設定ファイルから新たにS2コンテナを構築する機能を提供します。 S2コンテナの構築時には、
 * 指定されたクラスローダをスレッドのコンテキストクラスローダとして設定します。 そのため、
 * S2コンテナに登録されるクラスはこのクラスローダによってロードされます。
 * </p>
 * <p>
 * 実際にS2コンテナを構築する処理は、 {@link S2ContainerFactory.Provider}を実装したクラスに委譲します。
 * デフォルトの実装として、 {@link S2ContainerFactory.DefaultProvider}を提供します。 別の実装を使いたい場合は、
 * コンフィグレーションdiconファイルに{@link S2ContainerFactory.Provider}を実装したクラスをコンポーネントとして登録します。
 * </p>
 * <p>
 * コンフィグレーションdiconファイルとは、 S2コンテナおよびS2コンテナファクトリの振る舞いを変更するためのものです。 デフォルトでは、
 * クラスパス上の<code>s2container.dicon</code>を読み込みます。 システムプロパティの<code>org.seasar.framework.container.factory.config</code>にdiconファイルを指定することで、
 * そのファイルをコンフィグレーションdiconファイルとして使うことが出来ます。
 * </p>
 * <p>
 * コンフィグレーションdiconファイルは、 {@link S2ContainerFactory.Configurator}を実装したクラスによって処理されます。
 * デフォルトの実装として{@link S2ContainerFactory.DefaultConfigurator}を提供します。
 * コンフィグレーションdiconファイルに{@link S2ContainerFactory.Configurator}を実装したクラスをコンポーネントとして登録することで、
 * コンフィグレーションdiconファイルの処理方法を変更することが出来ます。
 * </p>
 * 
 * @author higa
 * @author jundu
 */
public class S2ContainerFactory {

    /**
     * コンフィグレーションdiconファイルを指定するためのシステムプロパティ名を表す定数です。
     */
    public static final String FACTORY_CONFIG_KEY = "org.seasar.framework.container.factory.config";

    /**
     * コンフィグレーションdiconファイルのデフォルトのファイル名を表す定数です。
     */
    public static final String FACTORY_CONFIG_PATH = "s2container.dicon";

    /**
     * コンフィグレーションdiconファイルに登録する、 デフォルトの{@link S2ContainerBuilder S2コンテナビルダ}のコンポーネント名を表す定数です。
     */
    public static final String DEFAULT_BUILDER_NAME = "defaultBuilder";

    /**
     * S2コンテナファクトリが、 初期化済みかどうかを表します。
     */
    protected static boolean initialized;

    /**
     * S2コンテナファクトリが、 コンフィグレーション中かどうかを表します。
     */
    protected static boolean configuring = false;

    /**
     * コンフィグレーションdiconファイルから構築された、 コンフィグレーションS2コンテナです。
     */
    protected static S2Container configurationContainer;

    /**
     * {@link S2ContainerFactory.Provider ファクトリプロバイダ}です。
     */
    protected static Provider provider;

    /**
     * デフォルトの{@link S2ContainerBuilder S2コンテナビルダ}です。
     */
    protected static S2ContainerBuilder defaultBuilder;

    /**
     * S2コンテナの構築中に処理中の、 設定ファイルパスを表します。
     */
    protected static ThreadLocal processingPaths = new ThreadLocal();

    private static final Logger logger = Logger
            .getLogger(S2ContainerFactory.class);

    static {
        configure();
    }

    /**
     * 指定された設定ファイルに基づき、 S2コンテナを構築して返します。
     * <p>
     * S2コンテナの構築には、 スレッドのコンテキストクラスローダを使用します。
     * </p>
     * 
     * @param path
     *            設定ファイルのパス
     * @return 構築したS2コンテナ
     * @throws EmptyRuntimeException
     *             <code>path</code>が<code>null</code>または空文字列の場合
     * 
     * @see S2ContainerFactory.Provider#create(String)
     */
    public static synchronized S2Container create(final String path) {
        if (StringUtil.isEmpty(path)) {
            throw new EmptyRuntimeException("path");
        }
        if (!initialized) {
            configure();
        }
        return getProvider().create(path);
    }

    /**
     * 指定された設定ファイルに基づき、 指定されたクラスローダを使用してS2コンテナを構築して返します。
     * 
     * @param path
     *            設定ファイルのパス
     * @param classLoader
     *            S2コンテナの構築に使用するクラスローダ
     * @return 構築したS2コンテナ
     * @throws EmptyRuntimeException
     *             <code>path</code>が<code>null</code>または空文字列の場合
     * 
     * @see S2ContainerFactory.Provider#create(String, ClassLoader)
     */
    public static synchronized S2Container create(final String path,
            final ClassLoader classLoader) {
        if (StringUtil.isEmpty(path)) {
            throw new EmptyRuntimeException("path");
        }
        if (!initialized) {
            configure();
        }
        return getProvider().create(path, classLoader);
    }

    /**
     * 設定ファイルを使用せず、 空のS2コンテナを構築して返します。
     * 
     * @return 構築したS2コンテナ
     * 
     * @see S2ContainerFactory.Provider#create()
     */
    public static synchronized S2Container create() {
        if (!initialized) {
            configure();
        }
        return getProvider().create();
    }

    /**
     * 指定された設定ファイルからS2コンテナを構築し、 親S2コンテナに対してインクルードします。
     * 
     * @param parent
     *            親となるS2コンテナ
     * @param path
     *            設定ファイルのパス
     * @return 構築したS2コンテナ
     * 
     * @see S2ContainerFactory.Provider#include(S2Container, String)
     */
    public static S2Container include(final S2Container parent,
            final String path) {
        if (!initialized) {
            configure();
        }
        return getProvider().include(parent, path);
    }

    /**
     * コンフィグレーションdiconファイルに基づいて、 S2コンテナファクトリを構成します。
     * <p>
     * コンフィグレーションdiconファイルとして、 クラスパス上の<code>s2container.dicon</code>を使用します。
     * ただし、 システムプロパティ<code>org.seasar.framework.container.factory.config</code>にdiconファイルが指定されていた場合、
     * そのdiconファイルを使用します。
     * </p>
     */
    public static void configure() {
        final String configFile = System.getProperty(FACTORY_CONFIG_KEY,
                FACTORY_CONFIG_PATH);
        configure(configFile);
    }

    /**
     * 指定されたコンフィグレーションdiconファイルに基づき、 S2コンテナファクトリを構成します。
     * 
     * @param configFile
     *            コンフィグレーションdiconファイル
     */
    public static synchronized void configure(final String configFile) {
        if (configuring) {
            return;
        }
        configuring = true;
        if (provider == null) {
            provider = new DefaultProvider();
        }
        if (defaultBuilder == null) {
            defaultBuilder = new XmlS2ContainerBuilder();
        }
        if (ResourceUtil.isExist(configFile)) {
            final S2ContainerBuilder builder = new XmlS2ContainerBuilder();
            configurationContainer = builder.build(configFile);
            configurationContainer.init();
            Configurator configurator;
            if (configurationContainer.hasComponentDef(Configurator.class)) {
                configurator = (Configurator) configurationContainer
                        .getComponent(Configurator.class);
            } else {
                configurator = new DefaultConfigurator();
            }
            configurator.configure(configurationContainer);
        }
        DisposableUtil.add(new Disposable() {
            public void dispose() {
                S2ContainerFactory.destroy();
            }
        });
        configuring = false;
        initialized = true;
    }

    /**
     * S2コンテナファクトリの構成をクリアして、 初期化前の状態に戻します。
     */
    public static synchronized void destroy() {
        defaultBuilder = null;
        provider = null;
        if (configurationContainer != null) {
            configurationContainer.destroy();
        }
        configurationContainer = null;
        initialized = false;
    }

    /**
     * コンフィグレーションS2コンテナを返します。
     * 
     * @return 現在のコンフィグレーションS2コンテナ
     */
    public static synchronized S2Container getConfigurationContainer() {
        return configurationContainer;
    }

    /**
     * ファクトリプロバイダを返します。
     * 
     * @return ファクトリプロバイダ
     */
    protected static Provider getProvider() {
        return provider;
    }

    /**
     * ファクトリプロバイダを設定します。
     * 
     * @param p
     *            ファクトリプロバイダ
     */
    protected static void setProvider(final Provider p) {
        provider = p;
    }

    /**
     * デフォルトS2コンテナビルダを返します。
     * 
     * @return デフォルトS2コンテナビルダ
     */
    protected static S2ContainerBuilder getDefaultBuilder() {
        return defaultBuilder;
    }

    /**
     * デフォルトS2コンテナビルダを設定します。
     * 
     * @param builder
     *            デフォルトS2コンテナビルダ
     */
    protected static void setDefaultBuilder(final S2ContainerBuilder builder) {
        defaultBuilder = builder;
    }

    /**
     * 処理中の設定ファイルパスを保持し、 循環インクルードを検出できるようにします。
     * <p>
     * もしも既に処理中のパスが指定された場合、 循環インクルードとして{@link org.seasar.framework.container.factory.CircularIncludeRuntimeException}をスローします。
     * </p>
     * 
     * @param path
     *            処理中の設定ファイルパス
     */
    protected static void enter(final String path) {
        final Set paths = getProcessingPaths();
        if (paths.contains(path)) {
            throw new CircularIncludeRuntimeException(path, paths);
        }
        paths.add(path);
    }

    /**
     * 処理中として保持していた設定ファイルのパスを取り除きます。
     * 
     * @param path
     *            取り除く設定ファイルのパス
     */
    protected static void leave(final String path) {
        final Set paths = getProcessingPaths();
        paths.remove(path);
    }

    /**
     * S2コンテナの構築中に処理中の、 設定ファイルパスを返します。
     * 
     * @return S2コンテナの構築中に処理中の、 設定ファイルパス
     */
    protected static Set getProcessingPaths() {
        Set paths = (Set) processingPaths.get();
        if (paths == null) {
            paths = new LinkedHashSet();
            processingPaths.set(paths);
        }
        return paths;
    }

    /**
     * 指定されたパスが、 指定されたS2コンテナおよび、その上位コンテナに含まれていないことを確認します。
     * 
     * @param container
     *            基点となるS2コンテナ
     * @param path
     *            設定ファイルのパス
     */
    protected static void assertCircularInclude(final S2Container container,
            final String path) {
        assertCircularInclude(container, path, new LinkedList());
    }

    /**
     * 指定されたパスが、 指定されたS2コンテナおよび、その上位コンテナに含まれていないことを確認します。
     * 
     * @param container
     *            確認対象のS2コンテナ
     * @param path
     *            設定ファイルのパス
     * @param paths
     *            パスのインクルード経路
     */
    protected static void assertCircularInclude(final S2Container container,
            final String path, LinkedList paths) {
        paths.addFirst(container.getPath());
        try {
            if (path.equals(container.getPath())) {
                throw new CircularIncludeRuntimeException(path, new ArrayList(
                        paths));
            }
            for (int i = 0; i < container.getParentSize(); ++i) {
                assertCircularInclude(container.getParent(i), path, paths);
            }
        } finally {
            paths.removeFirst();
        }
    }

    /**
     * {@link S2ContainerFactory S2コンテナファクトリ}の振る舞いを提供します。
     * <p>
     * S2コンテナファクトリは、 {@link org.seasar.framework.container.S2Container S2コンテナ}の構築時に、
     * このインターフェースを実装したクラスに処理を委譲します。
     * </p>
     * 
     * @author jundu
     */
    public interface Provider {

        /**
         * 指定された設定ファイルに基づき、 S2コンテナを構築して返します。
         * <p>
         * S2コンテナの構築には、 スレッドのコンテキストクラスローダを使用します。
         * </p>
         * 
         * @param path
         *            設定ファイルのパス
         * @return 構築したS2コンテナ
         */
        S2Container create(String path);

        /**
         * 指定された設定ファイルに基づき、 指定されたクラスローダを使用してS2コンテナを構築して返します。
         * 
         * @param path
         *            設定ファイルのパス
         * @param classLoader
         *            S2コンテナの構築に使用するクラスローダ
         * @return 構築したS2コンテナ
         */
        S2Container create(String path, ClassLoader classLoader);

        /**
         * 設定ファイルを使用せず、 空のS2コンテナを構築して返します。
         * 
         * @return 構築したS2コンテナ
         */
        S2Container create();

        /**
         * 指定された設定ファイルからS2コンテナを構築し、 親S2コンテナに対してインクルードします。
         * 
         * @param parent
         *            親となるS2コンテナ
         * @param path
         *            設定ファイルのパス
         * @return 構築したS2コンテナ
         */
        S2Container include(S2Container parent, String path);
    }

    /**
     * {@link S2ContainerFactory S2コンテナファクトリ}の振る舞いを提供する、 デフォルトの実装クラスです。
     * <p>
     * このクラスでは、 以下の手順に従って{@link org.seasar.framework.container.S2Container S2コンテナ}を構築します。
     * </p>
     * <ol>
     * <li>設定ファイルの拡張子を取り出す。
     * <li>拡張子と同じ名前を持つ{@link S2ContainerBuilder S2コンテナビルダ}をコンフィグレーションS2コンテナから取得する。
     * <li>取得できた場合は、 そのS2コンテナビルダを使ってS2コンテナを構築する。
     * <li>取得できなかった場合は、 デフォルトのS2コンテナビルダを使ってS2コンテナを構築する。
     * </ol>
     * <p>
     * デフォルトのS2コンテナビルダとして、 <code>defaultBuilder</code>という名前でコンフィグレーションS2コンテナに登録されたコンポーネントを使用します。
     * </p>
     * <p>
     * S2コンテナを構築した後、
     * {@link org.seasar.framework.container.ExternalContext 外部コンテキスト}および{@link org.seasar.framework.container.ExternalContextComponentDefRegister 外部コンテキストコンポーネント定義レジスタ}をコンテナへ登録します。
     * </p>
     * 
     * @author jundu
     */
    public static class DefaultProvider implements Provider {

        /**
         * プロパティ<code>pathResolver</code>のための定数アノテーションです。
         */
        public static final String pathResolver_BINDING = "bindingType=may";

        /**
         * プロパティ<code>externalContext</code>のための定数アノテーションです。
         */
        public static final String externalContext_BINDING = "bindingType=may";

        /**
         * プロパティ<code>externalContextComponentDefRegister</code>のための定数アノテーションです。
         */
        public static final String externalContextComponentDefRegister_BINDING = "bindingType=may";

        /**
         * 論理パスから物理（リアル）パスを取得するためのパスリゾルバです。
         */
        protected PathResolver pathResolver = new SimplePathResolver();

        /**
         * 外部コンテキストです。
         */
        protected ExternalContext externalContext;

        /**
         * 外部コンテキストコンポーネント定義レジスタです。
         */
        protected ExternalContextComponentDefRegister externalContextComponentDefRegister;

        /**
         * 論理パスから物理（リアル）パスを取得するためのパスリゾルバを返します。
         * 
         * @return パスリゾルバ
         */
        public PathResolver getPathResolver() {
            return pathResolver;
        }

        /**
         * 論理パスから物理（リアル）パスを取得するためのパスリゾルバを設定します。
         * 
         * @param pathResolver
         *            パスリゾルバ
         */
        public void setPathResolver(final PathResolver pathResolver) {
            this.pathResolver = pathResolver;
        }

        /**
         * 外部コンテキストを返します。
         * 
         * @return 外部コンテキスト
         */
        public ExternalContext getExternalContext() {
            return externalContext;
        }

        /**
         * 外部コンテキストを設定します。
         * 
         * @param externalContext
         *            外部コンテキスト
         */
        public void setExternalContext(ExternalContext externalContext) {
            this.externalContext = externalContext;
        }

        /**
         * 外部コンテキストコンポーネント定義レジスタを返します。
         * 
         * @return 外部コンテキストコンポーネント定義レジスタ
         */
        public ExternalContextComponentDefRegister getExternalContextComponentDefRegister() {
            return externalContextComponentDefRegister;
        }

        /**
         * 外部コンテキストコンポーネント定義レジスタを設定します。
         * 
         * @param externalContextComponentDefRegister
         *            外部コンテキストコンポーネント定義レジスタ
         */
        public void setExternalContextComponentDefRegister(
                ExternalContextComponentDefRegister externalContextComponentDefRegister) {
            this.externalContextComponentDefRegister = externalContextComponentDefRegister;
        }

        public S2Container create(final String path) {
            ClassLoader classLoader;
            if (configurationContainer != null
                    && configurationContainer
                            .hasComponentDef(ClassLoader.class)) {
                classLoader = (ClassLoader) configurationContainer
                        .getComponent(ClassLoader.class);
            } else {
                classLoader = Thread.currentThread().getContextClassLoader();
            }
            S2Container container = StringUtil.isEmpty(path) ? new S2ContainerImpl()
                    : build(path, classLoader);
            if (container.isInitializeOnCreate()) {
                container.init();
            }
            return container;
        }

        public S2Container create(final String path,
                final ClassLoader classLoader) {
            final ClassLoader oldLoader = Thread.currentThread()
                    .getContextClassLoader();
            try {
                if (classLoader != null) {
                    Thread.currentThread().setContextClassLoader(classLoader);
                }
                return create(path);
            } finally {
                Thread.currentThread().setContextClassLoader(oldLoader);
            }
        }

        public S2Container create() {
            return create(null);
        }

        public S2Container include(final S2Container parent, final String path) {
            final String realPath = pathResolver.resolvePath(parent.getPath(),
                    path);
            assertCircularInclude(parent, realPath);
            enter(realPath);
            try {
                final S2Container root = parent.getRoot();
                S2Container child = null;
                synchronized (root) {
                    if (root.hasDescendant(realPath)) {
                        child = root.getDescendant(realPath);
                        parent.include(child);
                    } else {
                        putCreationStartLog(path, realPath);
                        final String ext = getExtension(realPath);
                        final S2ContainerBuilder builder = getBuilder(ext);
                        child = builder.include(parent, realPath);
                        root.registerDescendant(child);
                        if (child.isInitializeOnCreate()) {
                            child.init();
                        }
                        putCreationEndLog(path, realPath);
                    }
                }
                return child;
            } finally {
                leave(realPath);
            }
        }

        /**
         * S2コンテナを構築して返します。
         * <p>
         * 指定されたクラスローダを使用してS2コンテナを構築し、
         * 外部コンテキストおよび外部コンテキストコンポーネント定義レジスタを設定して返します。
         * </p>
         * 
         * @param path
         *            設定ファイルのパス
         * @param classLoader
         *            S2コンテナの構築に使用するクラスローダ
         * @return 構築したS2コンテナ
         */
        protected S2Container build(final String path,
                final ClassLoader classLoader) {
            final String realPath = pathResolver.resolvePath(null, path);
            putCreationStartLog(path, realPath);
            enter(realPath);
            try {
                final String ext = getExtension(realPath);
                final S2Container container = getBuilder(ext).build(realPath,
                        classLoader);
                container.setExternalContext(externalContext);
                container
                        .setExternalContextComponentDefRegister(externalContextComponentDefRegister);
                putCreationEndLog(path, realPath);
                return container;
            } finally {
                leave(realPath);
            }
        }

        /**
         * 指定されたパスから、 ファイル名の拡張子部分を取り出して返します。
         * 
         * @param path
         *            対象のファイルパス
         * @return 取り出した拡張子
         */
        protected String getExtension(final String path) {
            final String ext = ResourceUtil.getExtension(path);
            if (ext == null) {
                throw new ExtensionNotFoundRuntimeException(path);
            }
            return ext;
        }

        /**
         * 指定された拡張子に対応するS2コンテナビルダを返します。
         * <p>
         * コンフィグレーションS2コンテナから、 拡張子と同じ名前を持つコンポーネントを取得します。 取得できなかった場合は、
         * デフォルトのS2コンテナビルダを返します。
         * </p>
         * 
         * @param ext
         *            対象の拡張子
         * @return 拡張子に対応するS2コンテナビルダ
         */
        protected S2ContainerBuilder getBuilder(final String ext) {
            if (configurationContainer != null
                    && configurationContainer.hasComponentDef(ext)) {
                return (S2ContainerBuilder) configurationContainer
                        .getComponent(ext);
            }
            return defaultBuilder;
        }

        /**
         * S2コンテナの作成開始を示すログを出力します。
         * 
         * @param path
         *            設定ファイルの論理パス
         * @param realPath
         *            設定ファイルの物理パス
         */
        protected void putCreationStartLog(final String path,
                final String realPath) {
            if (logger.isDebugEnabled()) {
                if (path.equals(realPath)) {
                    logger.log("DSSR0106", new Object[] { path });
                } else {
                    logger.log("DSSR0110", new Object[] { path, realPath });
                }
            }
        }

        /**
         * S2コンテナの作成終了を示すログを出力します。
         * 
         * @param path
         *            設定ファイルの論理パス
         * @param realPath
         *            設定ファイルの物理パス
         */
        protected void putCreationEndLog(final String path,
                final String realPath) {
            if (logger.isDebugEnabled()) {
                if (path.equals(realPath)) {
                    logger.log("DSSR0107", new Object[] { path });
                } else {
                    logger.log("DSSR0111", new Object[] { path, realPath });
                }
            }
        }

    }

    /**
     * {@link org.seasar.framework.container.S2Container S2コンテナ}および{@link S2ContainerFactory S2コンテナファクトリ}の振る舞いを構成します。
     * <p>
     * {@link S2ContainerFactory#configure}から呼び出されて、
     * S2コンテナおよびS2コンテナファクトリを構成します。
     * </p>
     * 
     * @author jundu
     */
    public interface Configurator {

        /**
         * S2コンテナおよびS2コンテナファクトリの構成をします。
         * <p>
         * 引数には、 コンフィグレーション用のコンポーネントを含むS2コンテナを指定します。
         * </p>
         * 
         * @param configurationContainer
         *            コンフィグレーションS2コンテナ
         */
        void configure(S2Container configurationContainer);
    }

    /**
     * {@link org.seasar.framework.container.S2Container S2コンテナ}および{@link S2ContainerFactory S2コンテナファクトリ}の振る舞いを構成するデフォルトの実装クラスです。
     * <p>
     * コンフィグレーションS2コンテナに登録されているコンポーネントに基づき、 S2コンテナおよびS2コンテナファクトリを構成します。
     * S2コンテナについては、 以下の設定を行います。
     * </p>
     * <dl>
     * <dt>ビヘイビアプロバイダ</dt>
     * <dd>{@link org.seasar.framework.container.impl.S2ContainerBehavior.Provider}を実装したコンポーネントがあれば、
     * ビヘイビアプロバイダとして設定します。</dd>
     * <dt>デプロイヤファクトリプロバイダ</dt>
     * <dd>{@link org.seasar.framework.container.deployer.ComponentDeployerFactory.Provider}を実装したコンポーネントがあれば、
     * デプロイヤファクトリプロバイダとして設定します。</dd>
     * <dt>アセンブラファクトリプロバイダ</dt>
     * <dd>{@link org.seasar.framework.container.assembler.AssemblerFactory.Provider}を実装したコンポーネントがあれば、
     * アセンブラファクトリプロバイダとして設定します。</dd>
     * </dl>
     * <p>
     * また、 S2コンテナファクトリについては、 以下の設定を行います。
     * </p>
     * <dl>
     * <dt>ファクトリプロバイダ</dt>
     * <dd>{@link S2ContainerFactory.Provider}を実装したコンポーネントがあれば、
     * それをファクトリプロバイダとして設定します。 それ以外の場合は、
     * {@link S2ContainerFactory.DefaultProvider デフォルトのファクトリプロバイダ}を設定します。</dd>
     * <dt>パスリゾルバ</dt>
     * <dd>デフォルトのファクトリプロバイダが使用されている場合、
     * {@link org.seasar.framework.container.factory.PathResolver}を実装したコンポーネントがあれば、
     * パスリゾルバとして設定します。</dd>
     * <dt>外部コンテキスト</dt>
     * <dd>デフォルトのファクトリプロバイダが使用されている場合、
     * {@link org.seasar.framework.container.ExternalContext}を実装したコンポーネントがあれば、
     * 外部コンテキストとして設定します。</dd>
     * <dt>外部コンテキストコンポーネント定義レジスタ</dt>
     * <dd>デフォルトのファクトリプロバイダが使用されている場合、
     * {@link org.seasar.framework.container.ExternalContextComponentDefRegister}を実装したコンポーネントがあれば、
     * 外部コンテキストコンポーネント定義レジスタとして設定します。</dd>
     * <dt>デフォルトのS2コンテナビルダ</dt>
     * <dd><code>defaultBuilder</code>という名前のコンポーネントがあれば、
     * デフォルトのS2コンテナビルダとして設定します。</dd>
     * <dt>リソースリゾルバ</dt>
     * <dd>defaultBuilderというコンポーネントがなく{@link AbstractS2ContainerBuilder}を継承したS2コンテナビルダが設定されてる場合は、
     * {@link org.seasar.framework.container.factory.ResourceResolver}を実装したコンポーネントがあればリソースリゾルバとして設定します。</dd>
     * </dl>
     * 
     * @author jundu
     */
    public static class DefaultConfigurator implements Configurator {

        public void configure(final S2Container configurationContainer) {
            provider = createProvider(configurationContainer);
            defaultBuilder = createDefaultBuilder(configurationContainer);
            setupBehavior(configurationContainer);
            setupDeployer(configurationContainer);
            setupAssembler(configurationContainer);
        }

        /**
         * プロバイダを作成します。
         * 
         * @param configurationContainer
         * @return プロバイダ
         */
        protected Provider createProvider(
                final S2Container configurationContainer) {
            if (configurationContainer.hasComponentDef(Provider.class)) {
                return (Provider) configurationContainer
                        .getComponent(Provider.class);
            } else if (provider instanceof DefaultProvider) {
                DefaultProvider dp = (DefaultProvider) provider;
                if (configurationContainer.hasComponentDef(PathResolver.class)) {
                    dp.setPathResolver((PathResolver) configurationContainer
                            .getComponent(PathResolver.class));
                }
                if (configurationContainer
                        .hasComponentDef(ExternalContext.class)) {
                    dp
                            .setExternalContext((ExternalContext) configurationContainer
                                    .getComponent(ExternalContext.class));
                }
                if (configurationContainer
                        .hasComponentDef(ExternalContextComponentDefRegister.class)) {
                    dp
                            .setExternalContextComponentDefRegister((ExternalContextComponentDefRegister) configurationContainer
                                    .getComponent(ExternalContextComponentDefRegister.class));
                }
            }
            return provider;
        }

        /**
         * コンテナのビルダを作成します。
         * 
         * @param configurationContainer
         * @return コンテナのビルダ
         */
        protected S2ContainerBuilder createDefaultBuilder(
                final S2Container configurationContainer) {
            if (configurationContainer.hasComponentDef(DEFAULT_BUILDER_NAME)) {
                return (S2ContainerBuilder) configurationContainer
                        .getComponent(DEFAULT_BUILDER_NAME);
            } else if (configurationContainer
                    .hasComponentDef(ResourceResolver.class)
                    && defaultBuilder instanceof AbstractS2ContainerBuilder) {
                ((AbstractS2ContainerBuilder) defaultBuilder)
                        .setResourceResolver((ResourceResolver) configurationContainer
                                .getComponent(ResourceResolver.class));
            }
            return defaultBuilder;
        }

        /**
         * ビヘイビアを設定します。
         * 
         * @param configurationContainer
         */
        protected void setupBehavior(final S2Container configurationContainer) {
            if (configurationContainer
                    .hasComponentDef(S2ContainerBehavior.Provider.class)) {
                S2ContainerBehavior
                        .setProvider((S2ContainerBehavior.Provider) configurationContainer
                                .getComponent(S2ContainerBehavior.Provider.class));
            }
        }

        /**
         * デプロイヤを設定します。
         * 
         * @param configurationContainer
         */
        protected void setupDeployer(final S2Container configurationContainer) {
            if (configurationContainer
                    .hasComponentDef(ComponentDeployerFactory.Provider.class)) {
                ComponentDeployerFactory
                        .setProvider((ComponentDeployerFactory.Provider) configurationContainer
                                .getComponent(ComponentDeployerFactory.Provider.class));
            }
        }

        /**
         * アセンブラを設定します。
         * 
         * @param configurationContainer
         */
        protected void setupAssembler(final S2Container configurationContainer) {
            if (configurationContainer
                    .hasComponentDef(AssemblerFactory.Provider.class)) {
                AssemblerFactory
                        .setProvider((AssemblerFactory.Provider) configurationContainer
                                .getComponent(AssemblerFactory.Provider.class));
            }
        }
    }
}
