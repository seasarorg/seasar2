/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.impl;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.util.EnumerationIterator;

/**
 * @author Shinpei Ohtani
 * @author higa
 */
public abstract class AbstractExternalContextMap extends AbstractMap {

    private Set entrySet;

    private Set keySet;

    private Collection values;

    public AbstractExternalContextMap() {
    }

    public void clear() {
        //avoid ConcurrentModificationException
        List list = new ArrayList();
        for (Enumeration e = getAttributeNames(); e.hasMoreElements();) {
            String key = (String) e.nextElement();
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
            for (Enumeration e = getAttributeNames(); e.hasMoreElements();) {
                String key = (String) e.nextElement();
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
        return !getAttributeNames().hasMoreElements();
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

    protected abstract Object getAttribute(String key);

    protected abstract void setAttribute(String key, Object value);

    protected abstract Enumeration getAttributeNames();

    protected abstract void removeAttribute(String key);

    abstract class AbstractExternalContextSet extends AbstractSet {

        /* (non-Javadoc)
         * @see java.util.AbstractCollection#size()
         */
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

        public EntrySet(AbstractExternalContextMap contextMap) {
            this.contextMap = contextMap;
        }

        /* (non-Javadoc)
         * @see java.util.AbstractCollection#iterator()
         */
        public Iterator iterator() {
            return new EntryIterator(contextMap);
        }

        /* (non-Javadoc)
         * @see java.util.AbstractCollection#remove(java.lang.Object)
         */
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

        public KeySet(AbstractExternalContextMap contextMap) {
            this.contextMap = contextMap;
        }

        /* (non-Javadoc)
         * @see java.util.AbstractCollection#iterator()
         */
        public Iterator iterator() {
            return new KeyIterator(contextMap);
        }

        /* (non-Javadoc)
         * @see java.util.AbstractCollection#remove(java.lang.Object)
         */
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

    abstract class AbstractExternalContextIterator extends EnumerationIterator {

        private final AbstractExternalContextMap contextMap;

        private String currentKey;

        private boolean removeCalled = false;

        public AbstractExternalContextIterator(
                final AbstractExternalContextMap contextMap) {
            super(contextMap.getAttributeNames());
            this.contextMap = contextMap;
        }

        public Object next() {
            currentKey = (String) super.next();
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

        protected String getCurrentKey() {
            return currentKey;
        }

        protected Object getValueFromMap(String key) {
            return contextMap.get(key);
        }

        protected void removeKeyFromMap(String key) {
            contextMap.remove(key);
        }

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

        protected abstract Object doNext();

        protected abstract void doRemove();
    }

    class EntryIterator extends AbstractExternalContextIterator {

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

    protected static class ImmutableEntry implements Map.Entry {

        private final Object key;

        private final Object value;

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
                    && (v == value || (v != null && v
                            .equals(value)));
        }

        public int hashCode() {
            return ((key != null) ? key.hashCode() : 0)
                    ^ ((value != null) ? value.hashCode() : 0);
        }
    }

}
