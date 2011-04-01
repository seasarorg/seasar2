/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.extension.httpsession;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.util.EnumerationAdapter;
import org.seasar.framework.util.SerializeUtil;

/**
 * セッションの状態をあらわすクラスです。
 * 
 * @author higa
 * 
 */
public class SessionState {

    private Map binaryData;

    private long lastAccessedTime;

    private Map accessedData;

    private Map persistedData;

    /**
     * {@link SessionState}を作成します。
     * 
     * @param binaryData
     *            バイナリデータ
     */
    public SessionState(Map binaryData) {
        this(binaryData, System.currentTimeMillis());
    }

    /**
     * {@link SessionState}を作成します。
     * 
     * @param binaryData
     *            バイナリデータ
     * @param lastAccessedTime
     *            最後にアクセスされた時刻
     * @since 2.4.43
     */
    public SessionState(Map binaryData, long lastAccessedTime) {
        this.binaryData = binaryData;
        this.lastAccessedTime = lastAccessedTime;
        int size = Math.max(binaryData.size(), 20);
        accessedData = new HashMap(size);
        persistedData = new HashMap(size);
    }

    /**
     * 属性の値を返します。
     * 
     * @param name
     *            名前
     * @return 属性の値
     */
    public synchronized Object getAttribute(String name) {
        if (accessedData.containsKey(name)) {
            return accessedData.get(name);
        }
        if (persistedData.containsKey(name)) {
            Object value = persistedData.get(name);
            accessedData.put(name, value);
            return value;
        }
        if (binaryData.containsKey(name)) {
            byte[] binary = (byte[]) binaryData.get(name);
            Object value = SerializeUtil.fromBinaryToObject(binary);
            accessedData.put(name, value);
            return value;
        }
        return null;
    }

    /**
     * セッションで管理されているすべての属性名を返します。
     * 
     * @return セッションで管理されているすべての属性名
     */
    public Enumeration getAttributeNames() {
        Set set = new LinkedHashSet();
        for (Iterator i = accessedData.keySet().iterator(); i.hasNext();) {
            set.add(i.next());
        }
        for (Iterator i = binaryData.keySet().iterator(); i.hasNext();) {
            set.add(i.next());
        }
        return new EnumerationAdapter(set.iterator());
    }

    /**
     * アクセスされたすべての属性名を返します。
     * 
     * @return アクセスされたすべての属性名
     */
    public Enumeration getAccessedAttributeNames() {
        return new EnumerationAdapter(accessedData.keySet().iterator());
    }

    /**
     * 属性の値を設定します。
     * 
     * @param name
     *            名前
     * @param value
     *            値
     */
    public void setAttribute(String name, Object value) {
        accessedData.put(name, value);
    }

    /**
     * 最後にアクセスされた時刻を返します。
     * 
     * @return 最後にアクセスされた時刻
     * @since 2.4.43
     */
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    /**
     * データを永続化した後に呼び出されます。
     */
    public synchronized void persisted() {
        for (Iterator i = accessedData.keySet().iterator(); i.hasNext();) {
            Object key = i.next();
            Object value = accessedData.get(key);
            if (value == null) {
                persistedData.remove(key);
                binaryData.remove(key);
            } else {
                persistedData.put(key, value);
            }
        }
        accessedData.clear();
    }

    /**
     * insertする必要があるかどうかを返します。
     * 
     * @param name
     *            名前
     * @return insertする必要があるかどうか
     */
    public boolean needInsert(String name) {
        if (!accessedData.containsKey(name)) {
            return false;
        }
        Object value = accessedData.get(name);
        return value != null && !binaryData.containsKey(name)
                && !persistedData.containsKey(name);
    }

    /**
     * updateする必要があるかどうかを返します。
     * 
     * @param name
     *            名前
     * @return updateする必要があるかどうか
     */
    public boolean needUpdate(String name) {
        if (!accessedData.containsKey(name)) {
            return false;
        }
        Object value = accessedData.get(name);
        return value != null
                && (binaryData.containsKey(name) || persistedData
                        .containsKey(name));
    }

    /**
     * deleteする必要があるかどうかを返します。
     * 
     * @param name
     *            名前
     * @return deleteする必要があるかどうか
     */
    public boolean needDelete(String name) {
        if (!accessedData.containsKey(name)) {
            return false;
        }
        Object value = accessedData.get(name);
        return value == null
                && (binaryData.containsKey(name) || persistedData
                        .containsKey(name));
    }

    /**
     * 永続化された属性の値を返します。
     * 
     * @param name
     *            名前
     * @return 永続化された属性の値
     */
    protected Object getPersistedAttribute(String name) {
        return persistedData.get(name);
    }
}