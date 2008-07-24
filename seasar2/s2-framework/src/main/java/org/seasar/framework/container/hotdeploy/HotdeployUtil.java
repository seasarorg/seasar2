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
package org.seasar.framework.container.hotdeploy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.seasar.framework.container.impl.S2ContainerBehavior;
import org.seasar.framework.container.impl.S2ContainerBehavior.Provider;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;

/**
 * HOT deploy用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class HotdeployUtil {

    /** {@link org.seasar.framework.container.hotdeploy.HotdeployUtil.RrebuilderImpl}のクラス名です。 */
    public static final String REBUILDER_CLASS_NAME = "org.seasar.framework.container.hotdeploy.HotdeployUtil$RebuilderImpl";

    private static Boolean hotdeploy;

    protected HotdeployUtil() {
    }

    /**
     * デバッグ用にHOT deployかどうかを設定します。
     * <p>
     * 通常は {@link S2ContainerBehavior#getProvider()}が何かによって自動的に判定されます。
     * </p>
     * 
     * @param hotdeploy
     */
    public static void setHotdeploy(boolean hotdeploy) {
        HotdeployUtil.hotdeploy = Boolean.valueOf(hotdeploy);
    }

    /**
     * デバッグ用のHOT deployかどうかの設定をクリアします。
     */
    public static void clearHotdeploy() {
        hotdeploy = null;
    }

    /**
     * HOT deployかどうかを返します。
     * 
     * @return HOT deployかどうか
     */
    public static boolean isHotdeploy() {
        if (hotdeploy != null) {
            return hotdeploy.booleanValue();
        }
        Provider provider = S2ContainerBehavior.getProvider();
        return provider instanceof HotdeployBehavior;
    }

    /**
     * HOT deployを開始します。
     */
    public static void start() {
        if (isHotdeploy()) {
            ((HotdeployBehavior) S2ContainerBehavior.getProvider()).start();
        }
    }

    /**
     * HOT deployを終了します。
     */
    public static void stop() {
        if (isHotdeploy()) {
            ((HotdeployBehavior) S2ContainerBehavior.getProvider()).stop();
        }
    }

    /**
     * HOT deploy中は、リクエストごとにクラスが変わってしまうので、 セッションなどに入れたデータを別のリクエストで取り出すと
     * {@link ClassCastException}が起きます。 これを防ぐために最新のクラスで元のオブジェクトを再作成します。
     * 
     * @param value
     * @return 再作成されたオブジェクト
     * @see #rebuildValueInternal(Object)
     */
    public static Object rebuildValue(Object value) {
        if (isHotdeploy()) {
            return rebuildValueInternal(value);
        }
        return value;
    }

    /**
     * 値を再作成するために内部的に呼び出されるメソッドです。
     * 
     * @param value
     * @return 再作成されたオブジェクト
     */
    protected static Object rebuildValueInternal(Object value) {
        if (value == null) {
            return null;
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class rebuilderClass = ClassLoaderUtil.loadClass(loader,
                REBUILDER_CLASS_NAME);
        Rebuilder rebuilder = (Rebuilder) ClassUtil.newInstance(rebuilderClass);
        return rebuilder.rebuild(value);
    }

    /**
     * 値を再構成するためのインターフェースです。
     */
    public interface Rebuilder {
        /**
         * 値を再構成します。
         * 
         * @param value
         *            値
         * @return 再構成されたオブジェクト
         */
        Object rebuild(Object value);
    }

    /**
     * 値を再構成するための実装クラスです。
     * <p>
     * このクラスは常に{@link HotdeployClassLoader}からロードされます。 これにより、 デシリアライズされたオブジェクトは{@link HotdeployClassLoader}からロードされたものになります。
     * </p>
     */
    public static class RebuilderImpl implements Rebuilder {

        public Object rebuild(Object value) {
            try {
                final ByteArrayOutputStream baos = new ByteArrayOutputStream(
                        1024);
                final ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(value);
                oos.close();

                final ByteArrayInputStream bais = new ByteArrayInputStream(baos
                        .toByteArray());
                final ObjectInputStream ois = new ObjectInputStream(bais);
                return ois.readObject();
            } catch (final Throwable t) {
                return value;
            }
        }

    }

}
