/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;

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

    /**
     * {@link org.seasar.framework.container.hotdeploy.HotdeployUtil.RrebuilderImpl}
     * のクラス名です。
     */
    public static final String REBUILDER_CLASS_NAME = "org.seasar.framework.container.hotdeploy.HotdeployUtil$RebuilderImpl";

    private static Boolean hotdeploy;

    /**
     * インスタンスを構築します。
     */
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
     * HOT deploy対応の{@link HttpSession}を返します。
     * <p>
     * {@link HttpSessionListener}や{@link HttpSessionAttributeListener}等、 HOT
     * deploy下でない環境で、HOT deploy環境で設定されたセッションの属性にアクセスする場合に使用してください。
     * </p>
     * 
     * @param session
     *            オリジナルの{@link HttpSession}
     * @return HOT deploy対応の{@link HttpSession}
     */
    public static HttpSession getHotdeployAwareSession(final HttpSession session) {
        if (!isHotdeploy()) {
            return session;
        }
        return new HotdeployHttpSession(session);
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
     * バイト列をデシリアライズするために内部的に呼び出されるメソッドです。
     * 
     * @param bytes
     *            バイト列
     * @return デシリアライズされたオブジェクト
     * @throws Exception
     *             デシリアライズで例外が発せした場合
     */
    protected static Object deserializeInternal(final byte[] bytes)
            throws Exception {
        if (bytes == null) {
            return null;
        }
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class rebuilderClass = ClassLoaderUtil.loadClass(loader,
                REBUILDER_CLASS_NAME);
        Rebuilder rebuilder = (Rebuilder) ClassUtil.newInstance(rebuilderClass);
        return rebuilder.deserialize(bytes);
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

        /**
         * バイト列をデシリアイラズします。
         * 
         * @param bytes
         *            バイト列
         * @return デシリアライズされたオブジェクト
         * @throws Exception
         *             デシリアライズで例外が発せした場合
         */
        Object deserialize(byte[] bytes) throws Exception;
    }

    /**
     * 値を再構成するための実装クラスです。
     * <p>
     * このクラスは常に{@link HotdeployClassLoader}からロードされます。 これにより、 デシリアライズされたオブジェクトは
     * {@link HotdeployClassLoader}からロードされたものになります。
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

        public Object deserialize(final byte[] bytes) throws Exception {
            final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            final ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }

    }

}
