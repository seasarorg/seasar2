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
package org.seasar.framework.container.external;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.hotdeploy.HotdeployClassLoader;
import org.seasar.framework.container.hotdeploy.HotdeployUtil;

/**
 * HOT deploy時に値を再構築可能な{@link ExternalContext 外部コンテキスト}用の抽象{@link Map}です。
 * <p>
 * HOT deploy中はリクエストごとにクラスが変わってしまうので、 セッションなどに入れたデータを別のリクエストで取り出すと
 * {@link ClassCastException}が起きます。 これを防ぐために最新のクラスで元のオブジェクトを再作成します。
 * </p>
 * <p>
 * HOT deployは単一スレッドで実行されることが前提なので、このクラスはスレッドセーフではありません。
 * </p>
 * 
 * @author koichik
 * @see HotdeployUtil#rebuildValue(Object)
 */
public abstract class RebuildableExternalContextMap extends
        AbstractExternalContextMap {

    /** {@link HotdeployClassLoader} */
    protected static WeakReference hotdeployClassLoader = new WeakReference(
            null);

    /** {@link #hotdeployClassLoader}の元で再構築したコンポーネント名の{@link Set} */
    protected static Set rebuiltNames = new HashSet(64);

    public Object get(final Object key) {
        final Object value = getAttribute(key.toString());
        if (value == null || !isHotdeployMode()) {
            return value;
        }
        if (rebuiltNames.contains(key)) {
            return value;
        }
        final Object rebuiltValue = HotdeployUtil.rebuildValue(value);
        rebuiltNames.add(key);
        setAttribute(key.toString(), rebuiltValue);
        return rebuiltValue;
    }

    public Object put(Object key, Object value) {
        final Object oldValue = super.put(key, value);
        if (isHotdeployMode()) {
            rebuiltNames.add(key);
        }
        return oldValue;
    }

    public void putAll(Map map) {
        for (final Iterator it = map.entrySet().iterator(); it.hasNext();) {
            final Entry entry = (Entry) it.next();
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * HOT deployモードの場合は<code>true</code>を返します。
     * <p>
     * HOT deployモードの場合で、{@link HotdeployClassLoader}が前回のチェック以降に切り替わっていた場合は
     * 保持しているリビルド済みオブジェクトの名前をクリアします。
     * </p>
     * 
     * @return HOT deployモードの場合は<code>true</code>
     */
    protected boolean isHotdeployMode() {
        if (!HotdeployUtil.isHotdeploy()) {
            return false;
        }
        final ClassLoader currentLoader = Thread.currentThread()
                .getContextClassLoader();
        if (!(currentLoader instanceof HotdeployClassLoader)) {
            return false;
        }
        if (currentLoader != hotdeployClassLoader.get()) {
            hotdeployClassLoader = new WeakReference(currentLoader);
            rebuiltNames.clear();
        }
        return true;
    }

}
