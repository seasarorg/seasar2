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

import java.beans.Introspector;
import java.util.LinkedList;

import ognl.OgnlRuntime;

import org.seasar.framework.log.Logger;

/**
 * {@link org.seasar.framework.container.S2Container S2コンテナ}の終了時にリソースを破棄するためのユーティリティクラスです。
 * <p>
 * S2コンテナの終了時に破棄しなければならないリソースがある場合は、 {@link Disposable}を実装したクラスを作成し、
 * このクラスに登録します。 通常、
 * {@link org.seasar.framework.container.factory.SingletonS2ContainerFactory#destroy()}が実行される際に、
 * {@link #dispose()}メソッドが呼び出され、 登録されている{@link Disposable}の{@link Disposable#dispose()}メソッドが呼び出されます。
 * {@link org.seasar.framework.unit.S2FrameworkTestCase}のサブクラスであるテストケースでは、
 * テストメソッドを実行する毎に{@link #dispose()}メソッドが呼び出されます．
 * </p>
 * 
 * @author koichik
 */
public class DisposableUtil {

    /** 登録済みの{@link Disposable} */
    protected static final LinkedList disposables = new LinkedList();

    /**
     * 破棄可能なリソースを登録します。
     * 
     * @param disposable
     *            破棄可能なリソース
     */
    public static synchronized void add(final Disposable disposable) {
        disposables.add(disposable);
    }

    /**
     * 破棄可能なリソースを登録解除します。
     * 
     * @param disposable
     *            破棄可能なリソース
     */
    public static synchronized void remove(final Disposable disposable) {
        disposables.remove(disposable);
    }

    /**
     * 登録済みのリソースを全て破棄します。
     * <p>
     * 登録済みのリソースを全て破棄した後，{@link org.seasar.framework.log.Logger#dispose()}を呼び出します。
     * commons loggingがクラスローダへの参照を保持するため、この呼び出しが必要となります。
     * リソースの破棄中にログが出力される場合を考慮して、 リソースを破棄した後に{@link org.seasar.framework.log.Logger#dispose()}を呼び出します。
     * </p>
     */
    public static synchronized void dispose() {
        while (!disposables.isEmpty()) {
            final Disposable disposable = (Disposable) disposables.removeLast();
            try {
                disposable.dispose();
            } catch (final Throwable t) {
                t.printStackTrace(); // must not use Logger.
            }
        }
        disposables.clear();
        OgnlRuntime.clearCache();
        Introspector.flushCaches();
        Logger.dispose();
    }

    /**
     * {@link java.sql.DriverManager}に登録されている{@link java.sql.Driver}を 解除します。
     * <p>
     * このメソッドは互換性のために残されています。 バージョン2.4.10以降では、
     * {@link DriverManagerUtil#deregisterAllDrivers()}を使用してください。
     * </p>
     */
    public static void deregisterAllDrivers() {
        DriverManagerUtil.deregisterAllDrivers();
    }
}
