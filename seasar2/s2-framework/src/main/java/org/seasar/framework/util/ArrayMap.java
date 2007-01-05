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
package org.seasar.framework.util;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class ArrayMap extends AbstractMap implements Map, Cloneable,
        Externalizable {

    static final long serialVersionUID = 1L;

    private static final int INITIAL_CAPACITY = 17;

    private static final float LOAD_FACTOR = 0.75f;

    private transient int threshold;

    private transient Entry[] mapTable;

    private transient Entry[] listTable;

    private transient int size = 0;

    private transient Set entrySet = null;

    public ArrayMap() {
        this(INITIAL_CAPACITY);
    }

    public ArrayMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            initialCapacity = INITIAL_CAPACITY;
        }
        mapTable = new Entry[initialCapacity];
        listTable = new Entry[initialCapacity];
        threshold = (int) (initialCapacity * LOAD_FACTOR);
    }

    public ArrayMap(Map map) {
        this((int) (map.size() / LOAD_FACTOR) + 1);
        putAll(map);
    }

    public final int size() {
        return size;
    }

    public final boolean isEmpty() {
        return size == 0;
    }

    public final boolean containsValue(Object value) {
        return indexOf(value) >= 0;
    }

    public final int indexOf(Object value) {
        if (value != null) {
            for (int i = 0; i < size; i++) {
                if (value.equals(listTable[i].value_)) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (listTable[i].value_ == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean containsKey(final Object key) {
        Entry[] tbl = mapTable;
        if (key != null) {
            int hashCode = key.hashCode();
            int index = (hashCode & 0x7FFFFFFF) % tbl.length;
            for (Entry e = tbl[index]; e != null; e = e.next_) {
                if (e.hashCode_ == hashCode && key.equals(e.key_)) {
                    return true;
                }
            }
        } else {
            for (Entry e = tbl[0]; e != null; e = e.next_) {
                if (e.key_ == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public Object get(final Object key) {
        Entry[] tbl = mapTable;
        if (key != null) {
            int hashCode = key.hashCode();
            int index = (hashCode & 0x7FFFFFFF) % tbl.length;
            for (Entry e = tbl[index]; e != null; e = e.next_) {
                if (e.hashCode_ == hashCode && key.equals(e.key_)) {
                    return e.value_;
                }
            }
        } else {
            for (Entry e = tbl[0]; e != null; e = e.next_) {
                if (e.key_ == null) {
                    return e.value_;
                }
            }
        }
        return null;
    }

    public final Object get(final int index) {
        return getEntry(index).value_;
    }

    public final Object getKey(final int index) {
        return getEntry(index).key_;
    }

    public final Entry getEntry(final int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index:" + index + ", Size:"
                    + size);
        }
        return listTable[index];
    }

    public Object put(final Object key, final Object value) {
        int hashCode = 0;
        int index = 0;

        if (key != null) {
            hashCode = key.hashCode();
            index = (hashCode & 0x7FFFFFFF) % mapTable.length;
            for (Entry e = mapTable[index]; e != null; e = e.next_) {
                if ((e.hashCode_ == hashCode) && key.equals(e.key_)) {
                    return swapValue(e, value);
                }
            }
        } else {
            for (Entry e = mapTable[0]; e != null; e = e.next_) {
                if (e.key_ == null) {
                    return swapValue(e, value);
                }
            }
        }
        ensureCapacity();
        index = (hashCode & 0x7FFFFFFF) % mapTable.length;
        Entry e = new Entry(hashCode, key, value, mapTable[index]);
        mapTable[index] = e;
        listTable[size++] = e;
        return null;
    }

    public final void set(final int index, final Object value) {
        getEntry(index).setValue(value);
    }

    public Object remove(final Object key) {
        Entry e = removeMap(key);
        if (e != null) {
            Object value = e.value_;
            removeList(indexOf(e));
            e.clear();
            return value;
        }
        return null;
    }

    public final Object remove(int index) {
        Entry e = removeList(index);
        Object value = e.value_;
        removeMap(e.key_);
        e.value_ = null;
        return value;
    }

    public void putAll(Map map) {
        for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
            Map.Entry e = (Map.Entry) i.next();
            put(e.getKey(), e.getValue());
        }
    }

    public final void clear() {
        for (int i = 0; i < mapTable.length; i++) {
            mapTable[i] = null;
        }
        for (int i = 0; i < listTable.length; i++) {
            listTable[i] = null;
        }
        size = 0;
    }

    public final Object[] toArray() {
        Object[] array = new Object[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = get(i);
        }
        return array;
    }

    public final Object[] toArray(final Object proto[]) {
        Object[] array = proto;
        if (proto.length < size) {
            array = (Object[]) Array.newInstance(proto.getClass()
                    .getComponentType(), size);
        }
        for (int i = 0; i < array.length; i++) {
            array[i] = get(i);
        }
        if (array.length > size) {
            array[size] = null;
        }
        return array;
    }

    public final boolean equals(Object o) {
        if (!getClass().isInstance(o)) {
            return false;
        }
        ArrayMap e = (ArrayMap) o;
        if (size != e.size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!listTable[i].equals(e.listTable[i])) {
                return false;
            }
        }
        return true;
    }

    public final Set entrySet() {
        if (entrySet == null) {
            entrySet = new AbstractSet() {
                public Iterator iterator() {
                    return new ArrayMapIterator();
                }

                public boolean contains(Object o) {
                    if (!(o instanceof Entry)) {
                        return false;
                    }
                    Entry entry = (Entry) o;
                    int index = (entry.hashCode_ & 0x7FFFFFFF)
                            % mapTable.length;
                    for (Entry e = mapTable[index]; e != null; e = e.next_) {
                        if (e.equals(entry)) {
                            return true;
                        }
                    }
                    return false;
                }

                public boolean remove(Object o) {
                    if (!(o instanceof Entry)) {
                        return false;
                    }
                    Entry entry = (Entry) o;
                    return ArrayMap.this.remove(entry.key_) != null;
                }

                public int size() {
                    return size;
                }

                public void clear() {
                    ArrayMap.this.clear();
                }
            };
        }
        return entrySet;
    }

    public final void writeExternal(final ObjectOutput out) throws IOException {
        out.writeInt(listTable.length);
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
            out.writeObject(listTable[i].key_);
            out.writeObject(listTable[i].value_);
        }
    }

    public final void readExternal(final ObjectInput in) throws IOException,
            ClassNotFoundException {

        int num = in.readInt();
        mapTable = new Entry[num];
        listTable = new Entry[num];
        threshold = (int) (num * LOAD_FACTOR);
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            Object key = in.readObject();
            Object value = in.readObject();
            put(key, value);
        }
    }

    public Object clone() {
        ArrayMap copy = new ArrayMap();
        copy.threshold = threshold;
        copy.mapTable = mapTable;
        copy.listTable = listTable;
        copy.size = size;
        return copy;
    }

    private final int indexOf(final Entry entry) {
        for (int i = 0; i < size; i++) {
            if (listTable[i] == entry) {
                return i;
            }
        }
        return -1;
    }

    private final Entry removeMap(Object key) {
        int hashCode = 0;
        int index = 0;

        if (key != null) {
            hashCode = key.hashCode();
            index = (hashCode & 0x7FFFFFFF) % mapTable.length;
            for (Entry e = mapTable[index], prev = null; e != null; prev = e, e = e.next_) {
                if ((e.hashCode_ == hashCode) && key.equals(e.key_)) {
                    if (prev != null) {
                        prev.next_ = e.next_;
                    } else {
                        mapTable[index] = e.next_;
                    }
                    return e;
                }
            }
        } else {
            for (Entry e = mapTable[index], prev = null; e != null; prev = e, e = e.next_) {
                if ((e.hashCode_ == hashCode) && e.key_ == null) {
                    if (prev != null) {
                        prev.next_ = e.next_;
                    } else {
                        mapTable[index] = e.next_;
                    }
                    return e;
                }
            }
        }
        return null;
    }

    private final Entry removeList(int index) {
        Entry e = listTable[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(listTable, index + 1, listTable, index, numMoved);
        }
        listTable[--size] = null;
        return e;
    }

    private final void ensureCapacity() {
        if (size >= threshold) {
            Entry[] oldTable = listTable;
            int newCapacity = oldTable.length * 2 + 1;
            Entry[] newMapTable = new Entry[newCapacity];
            Entry[] newListTable = new Entry[newCapacity];
            threshold = (int) (newCapacity * LOAD_FACTOR);
            System.arraycopy(oldTable, 0, newListTable, 0, size);
            for (int i = 0; i < size; i++) {
                Entry old = oldTable[i];
                int index = (old.hashCode_ & 0x7FFFFFFF) % newCapacity;
                Entry e = old;
                old = old.next_;
                e.next_ = newMapTable[index];
                newMapTable[index] = e;
            }
            mapTable = newMapTable;
            listTable = newListTable;
        }
    }

    private final Object swapValue(final Entry entry, final Object value) {
        Object old = entry.value_;
        entry.value_ = value;
        return old;
    }

    private class ArrayMapIterator implements Iterator {

        private int current_ = 0;

        private int last_ = -1;

        public boolean hasNext() {
            return current_ != size;
        }

        public Object next() {
            try {
                Object n = listTable[current_];
                last_ = current_++;
                return n;
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            if (last_ == -1) {
                throw new IllegalStateException();
            }
            ArrayMap.this.remove(last_);
            if (last_ < current_) {
                current_--;
            }
            last_ = -1;
        }
    }

    private static class Entry implements Map.Entry, Externalizable {

        private static final long serialVersionUID = -6625980241350717177L;

        transient int hashCode_;

        transient Object key_;

        transient Object value_;

        transient Entry next_;

        public Entry(final int hashCode, final Object key, final Object value,
                final Entry next) {

            hashCode_ = hashCode;
            key_ = key;
            value_ = value;
            next_ = next;
        }

        public Object getKey() {
            return key_;
        }

        public Object getValue() {
            return value_;
        }

        public Object setValue(final Object value) {
            Object oldValue = value_;
            value_ = value;
            return oldValue;
        }

        public void clear() {
            key_ = null;
            value_ = null;
            next_ = null;
        }

        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            Entry e = (Entry) o;
            return (key_ != null ? key_.equals(e.key_) : e.key_ == null)
                    && (value_ != null ? value_.equals(e.value_)
                            : e.value_ == null);
        }

        public int hashCode() {
            return hashCode_;
        }

        public String toString() {
            return key_ + "=" + value_;
        }

        public void writeExternal(final ObjectOutput s) throws IOException {
            s.writeInt(hashCode_);
            s.writeObject(key_);
            s.writeObject(value_);
            s.writeObject(next_);
        }

        public void readExternal(final ObjectInput s) throws IOException,
                ClassNotFoundException {

            hashCode_ = s.readInt();
            key_ = s.readObject();
            value_ = s.readObject();
            next_ = (Entry) s.readObject();
        }
    }
}