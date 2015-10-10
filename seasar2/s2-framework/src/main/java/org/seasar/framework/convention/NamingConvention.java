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
package org.seasar.framework.convention;

/**
 * 命名規約のためのインターフェースです。
 * 
 * @author higa
 * @author shot
 */
public interface NamingConvention {

    /**
     * <code>view</code>のルートパスを返します。
     * 
     * @return
     */
    String getViewRootPath();

    /**
     * <code>view</code>のルートパスが/のみの場合に取り除きます。 例:"/" -> "", "/hoge" -> "/hoge"
     * 
     * @return
     */
    String adjustViewRootPath();

    /**
     * <code>view</code>の拡張子を返します。
     * 
     * @return
     */
    String getViewExtension();

    /**
     * 実装クラスの<code>suffix</code>を返します。
     * 
     * @return 実装クラスの<code>suffix</code>
     */
    String getImplementationSuffix();

    /**
     * <code>Page</code>クラスの<code>suffix</code>を返します。
     * 
     * @return <code>Page</code>クラスの<code>suffix</code>
     */
    String getPageSuffix();

    /**
     * <code>Action</code>クラスの<code>suffix</code>を返します。
     * 
     * @return <code>Action</code>クラスの<code>suffix</code>
     */
    String getActionSuffix();

    /**
     * <code>Service</code>クラスの<code>suffix</code>を返します。
     * 
     * @return <code>Service</code>クラスの<code>suffix</code>
     */
    String getServiceSuffix();

    /**
     * <code>Dxo</code>クラスの<code>suffix</code>を返します。
     * 
     * @return <code>Dxo</code>クラスの<code>suffix</code>
     */
    String getDxoSuffix();

    /**
     * <code>Logic</code>クラスの<code>suffix</code>を返します。
     * 
     * @return <code>Logic</code>クラスの<code>suffix</code>
     */
    String getLogicSuffix();

    /**
     * <code>Dao</code>クラスの<code>suffix</code>を返します。
     * 
     * @return <code>Dao</code>クラスの<code>suffix</code>
     */
    String getDaoSuffix();

    /**
     * <code>Helper</code>クラスの<code>suffix</code>を返します。
     * 
     * @return <code>Helper</code>クラスの<code>suffix</code>
     */
    String getHelperSuffix();

    /**
     * <code>Interceptor</code>クラスの<code>suffix</code>を返します。
     * 
     * @return <code>Interceptor</code>クラスの<code>suffix</code>
     */
    String getInterceptorSuffix();

    /**
     * <code>Validator</code>クラスの<code>suffix</code>を返します。
     * 
     * @return <code>Validator</code>クラスの<code>suffix</code>
     */
    String getValidatorSuffix();

    /**
     * <code>Converter</code>クラスの<code>suffix</code>を返します。
     * 
     * @return <code>Converter</code>クラスの<code>suffix</code>
     */
    String getConverterSuffix();

    /**
     * <code>Dto</code>クラスの<code>suffix</code>を返します。
     * 
     * @return <code>Dto</code>クラスの<code>suffix</code>
     */
    String getDtoSuffix();

    /**
     * <code>Connector</code>クラスの<code>suffix</code>を返します。
     * 
     * @return <code>Connector</code>クラスの<code>suffix</code>
     */
    String getConnectorSuffix();

    /**
     * サブアプリケーションのルートパッケージ名を返します。
     * 
     * @return サブアプリケーションのルートパッケージ名
     */
    String getSubApplicationRootPackageName();

    /**
     * 実装用のパッケージ名を返します。
     * 
     * @return 実装用のパッケージ名
     */
    String getImplementationPackageName();

    /**
     * <code>Dxo</code>クラスのパッケージ名を返します。
     * 
     * @return <code>Dxo</code>クラスのパッケージ名
     */
    String getDxoPackageName();

    /**
     * <code>Logic</code>クラスのパッケージ名を返します。
     * 
     * @return <code>Logic</code>クラスのパッケージ名
     */
    String getLogicPackageName();

    /**
     * <code>Dao</code>クラスのパッケージ名を返します。
     * 
     * @return <code>Dao</code>クラスのパッケージ名
     */
    String getDaoPackageName();

    /**
     * <code>Entity</code>クラスのパッケージ名を返します。
     * 
     * @return <code>Entity</code>クラスのパッケージ名
     */
    String getEntityPackageName();

    /**
     * <code>Dto</code>クラスのパッケージ名を返します。
     * 
     * @return <code>Dto</code>クラスのパッケージ名
     */
    String getDtoPackageName();

    /**
     * <code>Service</code>クラスのパッケージ名を返します。
     * 
     * @return <code>Service</code>クラスのパッケージ名
     */
    String getServicePackageName();

    /**
     * <code>Interceptor</code>クラスのパッケージ名を返します。
     * 
     * @return <code>Interceptor</code>クラスのパッケージ名
     */
    String getInterceptorPackageName();

    /**
     * <code>Validator</code>クラスのパッケージ名を返します。
     * 
     * @return <code>Validator</code>クラスのパッケージ名
     */
    String getValidatorPackageName();

    /**
     * <code>Converter</code>クラスのパッケージ名を返します。
     * 
     * @return <code>Converter</code>クラスのパッケージ名
     */
    String getConverterPackageName();

    /**
     * <code>Helper</code>クラスのパッケージ名を返します。
     * 
     * @return <code>Helper</code>クラスのパッケージ名
     */
    String getHelperPackageName();

    /**
     * <code>Connector</code>クラスのパッケージ名を返します。
     * 
     * @return <code>Connector</code>クラスのパッケージ名
     */
    String getConnectorPackageName();

    /**
     * ルートパッケージ名の配列を返します。
     * 
     * @return ルートパッケージ名の配列
     */
    String[] getRootPackageNames();

    /**
     * 無視するルートパッケージ名の配列を返します。
     * 
     * @return 無視するルートパッケージ名の配列
     */
    String[] getIgnorePackageNames();

    /**
     * <code>suffix</code>をパッケージ名に変換します。
     * 
     * @param suffix
     * @return パッケージ名
     */
    String fromSuffixToPackageName(String suffix);

    /**
     * クラス名を短いコンポーネント名に変換します。 短いコンポーネント名とは、"サブアプリケーション名_"がついていないコンポーネント名です。
     * 
     * @param className
     * @return 短いコンポーネント名
     */
    String fromClassNameToShortComponentName(String className);

    /**
     * クラス名をコンポーネント名に変換します。
     * 
     * @param className
     * @return コンポーネント名
     */
    String fromClassNameToComponentName(String className);

    /**
     * コンポーネント名を{@link Class}に変換します。
     * 
     * @param componentName
     * @return {@link Class}
     */
    Class fromComponentNameToClass(String componentName);

    /**
     * クラス名を実装クラス名に変換します。
     * 
     * @param className
     * @return 実装クラス名
     */
    String toImplementationClassName(String className);

    /**
     * クラス名をインターフェース名に変換します。
     * 
     * @param className
     * @return インターフェース名
     */
    String toInterfaceClassName(String className);

    /**
     * 規約に従っていないスキップすべきクラスかどうか返します。
     * 
     * @param clazz
     * @return 規約に従っていないスキップすべきクラス
     */
    boolean isSkipClass(Class clazz);

    /**
     * 最終的に利用されるクラスに変換します。 通常は、実装クラスですが、DaoのようにInterceptorで実体化される場合、
     * インターフェースの場合もあります。
     * 
     * @param clazz
     * @return 最終的に利用されるクラス
     */
    Class toCompleteClass(Class clazz);

    /**
     * コンポーネント名をクラス名の一部に変換します。 "_"は"."に"_"の後ろは大文字に変換されます。
     * 例えば、コンポーネント名がhoge_fooの場合、hoge.Fooになります。
     * 
     * @param componentName
     * @return クラス名の一部
     */
    String fromComponentNameToPartOfClassName(String componentName);

    /**
     * コンポーネント名を<code>suffix</code>に変換します。 コンポーネント名の最後から探して最初の大文字までを抽出して、
     * 先頭を小文字に変換したものが、 <code>suffix</code>になります。
     * 
     * @param componentName
     * @return <code>suffix</code>
     */
    String fromComponentNameToSuffix(String componentName);

    /**
     * クラス名を<code>suffix</code>に変換します。
     * 
     * @param className
     * @return <code>suffix</code>
     */
    String fromClassNameToSuffix(String className);

    /**
     * <code>View</code>のパスをページ名に変換します。
     * 
     * @param path
     * @return ページ名
     */
    String fromPathToPageName(String path);

    /**
     * <code>View</code>のパスをアクション名に変換します。
     * 
     * @param path
     * @return アクション名
     */
    String fromPathToActionName(String path);

    /**
     * ページ名を<code>View</code>のパスに変換します。
     * 
     * @param pageName
     * @return <code>View</code>のパス
     */
    String fromPageNameToPath(String pageName);

    /**
     * ページの{@link Class}を<code>View</code>のパスに変換します。
     * 
     * @param pageClass
     * @return <code>View</code>のパス
     */
    String fromPageClassToPath(Class pageClass);

    /**
     * アクション名を<code>View</code>のパスに変換します。
     * 
     * @param actionName
     * @return <code>View</code>のパス
     */
    String fromActionNameToPath(String actionName);

    /**
     * アクション名をページ名に変換します。
     * 
     * @param actionName
     * @return ページ名
     */
    String fromActionNameToPageName(String actionName);

    /**
     * ターゲットのクラス名かどうかを返します。
     * 
     * @param className
     * @param suffix
     * @return ターゲットのクラス名かどうか
     */
    boolean isTargetClassName(String className, String suffix);

    /**
     * ターゲットのクラス名かどうかを返します。
     * 
     * @param className
     * @return ターゲットのクラス名かどうか
     */
    boolean isTargetClassName(String className);

    /**
     * HOT deployのターゲットのクラス名かどうかを返します。
     * 
     * @param className
     * @return HOT deployのターゲットのクラス名かどうか
     */
    boolean isHotdeployTargetClassName(String className);

    /**
     * 無視するクラス名かどうかを返します。
     * 
     * @param className
     * @return 無視するクラス名かどうか
     */
    boolean isIgnoreClassName(String className);

    /**
     * 妥当な<code>View</code>のルートパスかどうかを返します。
     * 
     * @param path
     * @return 妥当な<code>View</code>のルートパスかどうか
     */
    boolean isValidViewRootPath(String path);
}
