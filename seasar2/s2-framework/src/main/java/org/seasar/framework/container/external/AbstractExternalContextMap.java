/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.container.ExternalContext;

/**
 * {@link ExternalContext}用の抽象 {@link Map}です。
 * 
 * @author shot
 * @author higa
 */
public abstract class AbstractExternalContextMap extends AbstractMap {

    private Set entrySet;

    private Set keySet;

    private Collection values;

    /**
     * {@link AbstractExternalContextMap}を作成します。
     */
    public AbstractExternalContextMap() {
    }

    public void clear() {
        // avoid ConcurrentModificationException
        List list = new ArrayList();
        for (Iterator it = getAttributeNames(); it.hasNext();) {
            String key = (String) it.next();
            list.add(key);
        }
        clearReally(list);
    }

    private void clearReally(List keys) {
        for (Iterator itr = keys.iterator(); itr.hasNext();) {
            String key = (String) itr.next();
            removeAttribute(key);
        }
    }

    public boolean containsKey(Object key) {
        return (getAttribute(key.toString()) != null);
    }

    public boolean containsValue(Object value) {
        if (value != null) {
            for (Iterator it = getAttributeNames(); it.hasNext();) {
                String key = (String) it.next();
                Object attributeValue = getAttribute(key);
                if (value.equals(attributeValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet(this);
        }
        return entrySet;
    }

    public Object get(Object key) {
        return getAttribute(key.toString());
    }

    public Object put(Object key, Object value) {
        String keyStr = key.toString();
        Object o = getAttribute(keyStr);
        setAttribute(keyStr, value);
        return o;
    }

    public void putAll(Map map) {
        for (Iterator itr = map.entrySet().iterator(); itr.hasNext();) {
            Map.Entry entry = (Map.Entry) itr.next();
            String key = (String) entry.getKey();
            setAttribute(key, entry.getValue());
        }
    }

    public boolean isEmpty() {
        return !getAttributeNames().hasNext();
    }

    public Set keySet() {
        if (keySet == null) {
            keySet = new KeySet(this);
        }
        return keySet;
    }

    public Object remove(Object key) {
        String keyStr = key.toString();
        Object o = getAttribute(keyStr);
        removeAttribute(keyStr);
        return o;
    }

    public Collection values() {
        if (values == null) {
            values = new ValuesCollection(this);
        }
        return values;
    }

    /**
     * 属性の値を返します。
     * 
     * @param key
     * @return 属性の値
     */
    protected abstract Object getAttribute(String key);

    /**
     * 属性の値を設定します。
     * 
     * @param key
     * @param value
     */
    protected abstract void setAttribute(String key, Object value);

    /**
     * 属性名の {@link Iterator}を返します。
     * 
     * @return
     */
    protected abstract Iterator getAttributeNames();

    /**
     * 属性を削除します。
     * 
     * @param key
     */
    protected abstract void removeAttribute(String key);

    abstract class AbstractExternalContextSet extends AbstractSet {

        public int size() {
            int size = 0;
            for (Iterator itr = iterator(); itr.hasNext(); size++) {
                itr.next();
            }
            return size;
        }

    }

    class EntrySet extends AbstractExternalContextSet {

        private AbstractExternalContextMap contextMap;

        /**
         * {@link EntrySet}を作成します。
         * 
         * @param contextMap
         */
        public EntrySet(AbstractExternalContextMap contextMap) {
            this.contextMap = contextMap;
        }

        public Iterator iterator() {
            return new EntryIterator(contextMap);
        }

        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) o;
            Object returnObj = contextMap.remove(entry.getKey());
            return (returnObj != null);
        }
    }

    class KeySet extends AbstractExternalContextSet {

        private AbstractExternalContextMap contextMap;

        /**
         * {@link KeySet}を作成します。
         * 
         * @param contextMap
         */
        public KeySet(AbstractExternalContextMap contextMap) {
            this.contextMap = contextMap;
        }

        public Iterator iterator() {
            return new KeyIterator(contextMap);
        }

        public boolean remove(Object o) {
            if (!(o instanceof String)) {
                return false;
            }
            String s = (String) o;
            Object returnObj = contextMap.remove(s);
            return (returnObj != null);
        }
    }

    class ValuesCollection extends AbstractCollection {

        private AbstractExternalContextMap contextMap;

        /**
         * {@link ValuesCollection}を作成します。
         * 
         * @param contextMap
         */
        public ValuesCollection(AbstractExternalContextMap contextMap) {
            this.contextMap = contextMap;
        }

        public int size() {
            int size = 0;
            for (Iterator itr = iterator(); itr.hasNext(); size++) {
                itr.next();
            }
            return size;
        }

        public Iterator iterator() {
            return new ValuesIterator(contextMap);
        }

    }

    abstract class AbstractExternalContextIterator implements Iterator {

        private final Iterator iterator;

        private final AbstractExternalContextMap contextMap;

        private String currentKey;

        private boolean removeCalled = false;

        /**
         * {@link AbstractExternalContextIterator}を作成します。
         * 
         * @param contextMap
         */
        public AbstractExternalContextIterator(
                final AbstractExternalContextMap contextMap) {
            iterator = contextMap.getAttributeNames();
            this.contextMap = contextMap;
        }

        public boolean hasNext() {
            return iterator.hasNext();
        }

        public Object next() {
            currentKey = (String) iterator.next();
            try {
                return doNext();
            } finally {
                removeCalled = false;
            }
        }

        public void remove() {
            if (currentKey != null && !removeCalled) {
                doRemove();
                removeCalled = true;
            } else {
                throw new IllegalStateException();
            }
        }

        /**
         * 現在のキーを返します。
         * 
         * @return 現在のキー
         */
        protected String getCurrentKey() {
            return currentKey;
        }

        /**
         * 値を返します。
         * 
         * @param key
         * @return 値
         */
        protected Object getValueFromMap(String key) {
            return contextMap.get(key);
        }

        /**
         * キーに対するエントリを削除します。
         * 
         * @param key
         */
        protected void removeKeyFromMap(String key) {
            contextMap.remove(key);
        }

        /**
         * 値に対するエントリを削除します。
         * 
         * @param value
         */
        protected void removeValueFromMap(Object value) {
            if (containsValue(value)) {
                for (Iterator itr = entrySet().iterator(); itr.hasNext();) {
                    Map.Entry e = (Map.Entry) itr.next();
                    if (value.equals(e.getValue())) {
                        contextMap.remove(e.getKey());
                    }
                }
            }

        }

        /**
         * 次の要素に移動します。
         * 
         * @return 次の要素
         */
        protected abstract Object doNext();

        /**
         * 現在の要素を削除します。
         */
        protected abstract void doRemove();
    }

    class EntryIterator extends AbstractExternalContextIterator {

        /**
         * {@link EntryIterator}を作成します。
         * 
         * @param contextMap
         */
        public EntryIterator(AbstractExternalContextMap contextMap) {
            super(contextMap);
        }

        protected Object doNext() {
            String key = getCurrentKey();
            return new ImmutableEntry(key, getValueFromMap(key));
        }

        protected void doRemove() {
            String key = getCurrentKey();
            removeKeyFromMap(key);
        }

    }

    class KeyIterator extends AbstractExternalContextIterator {

        /**
         * {@link KeyIterator}を作成します。
         * 
         * @param contextMap
         */
        public KeyIterator(AbstractExternalContextMap contextMap) {
            super(contextMap);
        }

        protected Object doNext() {
            return getCurrentKey();
        }

        protected void doRemove() {
            removeKeyFromMap(getCurrentKey());
        }
    }

    class ValuesIterator extends AbstractExternalContextIterator {

        /**
         * {@link ValuesIterator}を作成します。
         * 
         * @param contextMap
         */
        public ValuesIterator(AbstractExternalContextMap contextMap) {
            super(contextMap);
        }

        protected Object doNext() {
            String key = getCurrentKey();
            return getValueFromMap(key);
        }

        protected void doRemove() {
            String key = getCurrentKey();
            Object value = getValueFromMap(key);
            removeValueFromMap(value);
        }

    }

    /**
     * 変化しない {@link java.util.Map.Entry}です。
     * 
     */
    protected static class ImmutableEntry implements Map.Entry {

        private final Object key;

        private final Object value;

        /**
         * {@link AbstractExternalContextMap.ImmutableEntry}を作成します。
         * 
         * @param key
         * @param value
         */
        public ImmutableEntry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Object setValue(Object arg0) {
            throw new UnsupportedOperationException("Immutable entry.");
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof ImmutableEntry)) {
                return false;
            }
            ImmutableEntry entry = (ImmutableEntry) obj;
            Object k = entry.getKey();
            Object v = entry.getValue();

            return (k == key || (k != null && k.equals(key)))
                    && (v == value || (v != null && v.equals(value)));
        }

        public int hashCode() {
            return ((key != null) ? key.hashCode() : 0)
                    ^ ((value != null) ? value.hashCode() : 0);
        }
    }

}
