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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.NoSuchElementException;

/**
 * Seasar2用の連結リストです。
 * 
 * @author higa
 * 
 */
public class SLinkedList implements Cloneable, Externalizable {

    static final long serialVersionUID = 1L;

    private transient Entry header = new Entry(null, null, null);

    private transient int size = 0;

    /**
     * {@link SLinkedList}を作成します。
     */
    public SLinkedList() {
        header._next = header._previous = header;
    }

    /**
     * 最初のエントリを返します。
     * 
     * @return 最初のエントリ
     */
    public Entry getFirstEntry() {
        if (isEmpty()) {
            return null;
        }
        return header._next;
    }

    /**
     * 最初の要素を返します。
     * 
     * @return 最初の要素
     */
    public Object getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getFirstEntry()._element;
    }

    /**
     * 最後のエントリを返します。
     * 
     * @return 最後のエントリ
     */
    public Entry getLastEntry() {
        if (isEmpty()) {
            return null;
        }
        return header._previous;
    }

    /**
     * 最後の要素を返します。
     * 
     * @return 最後の要素
     */
    public Object getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getLastEntry()._element;
    }

    /**
     * 最初の要素を削除します。
     * 
     * @return 最初の要素
     */
    public Object removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Object first = header._next._element;
        header._next.remove();
        return first;
    }

    /**
     * 最後の要素を削除します。
     * 
     * @return 最後の要素
     */
    public Object removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Object last = header._previous._element;
        header._previous.remove();
        return last;
    }

    /**
     * 先頭に追加します。
     * 
     * @param o
     *            追加するオブジェクト
     */
    public void addFirst(final Object o) {
        header._next.addBefore(o);
    }

    /**
     * 最後に追加します。
     * 
     * @param o
     *            追加するオブジェクト
     */
    public void addLast(final Object o) {
        header.addBefore(o);
    }

    /**
     * 指定した位置にオブジェクトを追加します。
     * 
     * @param index
     *            位置
     * @param element
     *            要素
     */
    public void add(final int index, final Object element) {
        getEntry(index).addBefore(element);
    }

    /**
     * 要素の数を返します。
     * 
     * @return 要素の数
     */
    public int size() {
        return size;
    }

    /**
     * 空かどうかを返します。
     * 
     * @return 空かどうか
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 要素が含まれているかどうかを返します。
     * 
     * @param o
     *            要素
     * @return 要素が含まれているかどうか
     */
    public boolean contains(final Object o) {
        return indexOf(o) != -1;
    }

    /**
     * 要素を削除します。
     * 
     * @param o
     * @return 削除されたかどうか
     */
    public boolean remove(final Object o) {
        if (o == null) {
            for (Entry e = header._next; e != header; e = e._next) {
                if (e._element == null) {
                    e.remove();
                    return true;
                }
            }
        } else {
            for (Entry e = header._next; e != header; e = e._next) {
                if (o.equals(e._element)) {
                    e.remove();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 指定した位置の要素を削除します。
     * 
     * @param index
     *            位置
     * @return 削除された要素
     */
    public Object remove(final int index) {
        Entry e = getEntry(index);
        e.remove();
        return e._element;
    }

    /**
     * 要素を空にします。
     */
    public void clear() {
        header._next = header._previous = header;
        size = 0;
    }

    /**
     * エントリを返します。
     * 
     * @param index
     * @return エントリ
     */
    public Entry getEntry(final int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: "
                    + size);
        }
        Entry e = header;
        if (index < size / 2) {
            for (int i = 0; i <= index; i++) {
                e = e._next;
            }
        } else {
            for (int i = size; i > index; i--) {
                e = e._previous;
            }
        }
        return e;
    }

    /**
     * 要素を返します。
     * 
     * @param index
     *            位置
     * @return 要素
     */
    public Object get(final int index) {
        return getEntry(index)._element;
    }

    /**
     * 要素を設定します。
     * 
     * @param index
     * @param element
     * @return 元の要素
     */
    public Object set(final int index, final Object element) {
        Entry e = getEntry(index);
        Object oldVal = e._element;
        e._element = element;
        return oldVal;
    }

    /**
     * 位置を返します。
     * 
     * @param o
     *            要素
     * @return 位置
     */
    public int indexOf(final Object o) {
        int index = 0;
        if (o == null) {
            for (Entry e = header._next; e != header; e = e._next) {
                if (e._element == null) {
                    return index;
                }
                index++;
            }
        } else {
            for (Entry e = header._next; e != header; e = e._next) {
                if (o.equals(e._element)) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    public void writeExternal(final ObjectOutput s) throws IOException {
        s.writeInt(size);
        for (Entry e = header._next; e != header; e = e._next) {
            s.writeObject(e._element);
        }
    }

    public void readExternal(ObjectInput s) throws IOException,
            ClassNotFoundException {

        int size = s.readInt();
        header = new Entry(null, null, null);
        header._next = header._previous = header;
        for (int i = 0; i < size; i++) {
            addLast(s.readObject());
        }
    }

    public Object clone() {
        SLinkedList copy = new SLinkedList();
        for (Entry e = header._next; e != header; e = e._next) {
            copy.addLast(e._element);
        }
        return copy;
    }

    /**
     * 配列に変換します。
     * 
     * @return 配列
     */
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (Entry e = header._next; e != header; e = e._next) {
            result[i++] = e._element;
        }
        return result;
    }

    /**
     * 要素を格納するエントリです。
     * 
     */
    public class Entry {

        private Object _element;

        private Entry _next;

        private Entry _previous;

        Entry(final Object element, final Entry next, final Entry previous) {
            _element = element;
            _next = next;
            _previous = previous;
        }

        /**
         * 要素を返します。
         * 
         * @return 要素
         */
        public Object getElement() {
            return _element;
        }

        /**
         * 次のエントリを返します。
         * 
         * @return 次のエントリ
         */
        public Entry getNext() {
            if (_next != SLinkedList.this.header) {
                return _next;
            }
            return null;
        }

        /**
         * 前のエントリを返します。
         * 
         * @return 前のエントリ
         */
        public Entry getPrevious() {
            if (_previous != SLinkedList.this.header) {
                return _previous;
            }
            return null;
        }

        /**
         * 要素を削除します。
         */
        public void remove() {
            _previous._next = _next;
            _next._previous = _previous;
            SLinkedList.this.size--;
        }

        /**
         * 前に追加します。
         * 
         * @param o
         *            要素
         * @return 追加されたエントリ
         */
        public Entry addBefore(final Object o) {
            Entry newEntry = new Entry(o, this, _previous);
            _previous._next = newEntry;
            _previous = newEntry;
            SLinkedList.this.size++;
            return newEntry;
        }
    }
}