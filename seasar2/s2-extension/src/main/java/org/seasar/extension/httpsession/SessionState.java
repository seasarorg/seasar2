/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
 * HttpSessionの状態をあらわすクラスです。
 * 
 * @author higa
 * 
 */
public class SessionState {

    private Map binaryData;

    private Map accessedData;

    /**
     * <code>SessionState</code>を作成します。
     * 
     * @param binaryData
     */
    public SessionState(Map binaryData) {
        this.binaryData = binaryData;
        accessedData = new HashMap(Math.max(binaryData.size(), 20));
    }

    /**
     * 指定された名前に対する値を返します。
     * 
     * @param name
     * @return
     */
    public synchronized Object getAttribute(String name) {
        if (accessedData.containsKey(name)) {
            return accessedData.get(name);
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
     * @return
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
     * @return
     */
    public Enumeration getAccessedAttributeNames() {
        return new EnumerationAdapter(accessedData.keySet().iterator());
    }

    /**
     * 名前に対する値を設定します。
     * 
     * @param name
     * @param value
     */
    public void setAttribute(String name, Object value) {
        accessedData.put(name, value);
    }

    /**
     * insertする必要があるかどうか返します。
     * 
     * @param name
     * @return
     */
    public boolean needInsert(String name) {
        if (!accessedData.containsKey(name)) {
            return false;
        }
        Object value = accessedData.get(name);
        return value != null && !binaryData.containsKey(name);
    }

    /**
     * updateする必要があるかどうか返します。
     * 
     * @param name
     * @return
     */
    public boolean needUpdate(String name) {
        if (!accessedData.containsKey(name)) {
            return false;
        }
        Object value = accessedData.get(name);
        return value != null && binaryData.containsKey(name);
    }

    /**
     * deleteする必要があるかどうか返します。
     * 
     * @param name
     * @return
     */
    public boolean needDelete(String name) {
        if (!accessedData.containsKey(name)) {
            return false;
        }
        Object value = accessedData.get(name);
        return value == null && binaryData.containsKey(name);
    }
}