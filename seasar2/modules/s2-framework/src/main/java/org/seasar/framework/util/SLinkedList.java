/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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

public class SLinkedList implements Cloneable, Externalizable {

    static final long serialVersionUID = 1L;
    private transient Entry header_ = new Entry(null, null, null);
    private transient int size_ = 0;

    public SLinkedList() {
        header_._next = header_._previous = header_;
    }

    public Entry getFirstEntry() {
        if (isEmpty()) {
            return null;
        }
        return header_._next;
    }

    public Object getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getFirstEntry()._element;
    }

    public Entry getLastEntry()  {
        if (isEmpty()) {
            return null;
        }
        return header_._previous;
    }

    public Object getLast()  {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return getLastEntry()._element;
    }

    public Object removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Object first = header_._next._element;
        header_._next.remove();
        return first;
    }

    public Object removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Object last = header_._previous._element;
        header_._previous.remove();
        return last;
    }

    public void addFirst(final Object o) {
        header_._next.addBefore(o);
    }

    public void addLast(final Object o) {
        header_.addBefore(o);
    }

    public void add(final int index, final Object element) {
        getEntry(index).addBefore(element);
    }

    public int size() {
        return size_;
    }

    public boolean isEmpty() {
        return size_ == 0;
    }

    public boolean contains(final Object o) {
        return indexOf(o) != -1;
    }

    public boolean remove(final Object o) {
        if (o == null) {
            for (Entry e = header_._next; e != header_; e = e._next) {
                if (e._element == null) {
                   e.remove();
                    return true;
                }
            }
        } else {
            for (Entry e = header_._next; e != header_; e = e._next) {
                if (o.equals(e._element)) {
                    e.remove();
                    return true;
                }
            }
        }
        return false;
    }

    public Object remove(final int index) {
        Entry e = getEntry(index);
        e.remove();
        return e._element;
    }

    public void clear() {
        header_._next = header_._previous = header_;
        size_ = 0;
    }

    public Entry getEntry(final int index) {
        if (index < 0 || index >= size_) {
            throw new IndexOutOfBoundsException(
                "Index: " + index + ", Size: " + size_);
        }
        Entry e = header_;
        if (index < size_ / 2) {
            for (int i = 0; i <= index; i++) {
                e = e._next;
            }
        } else {
            for (int i = size_; i > index; i--) {
                e = e._previous;
            }
        }
        return e;
    }

    public Object get(final int index) {
        return getEntry(index)._element;
    }

    public Object set(final int index, final Object element) {
        Entry e = getEntry(index);
        Object oldVal = e._element;
        e._element = element;
        return oldVal;
    }

    public int indexOf(final Object o) {
        int index = 0;
        if (o == null) {
            for (Entry e = header_._next; e != header_; e = e._next) {
                if (e._element == null) {
                    return index;
                }
                index++;
            }
        } else {
            for (Entry e = header_._next; e != header_; e = e._next) {
                if (o.equals(e._element)) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    public void writeExternal(final ObjectOutput s) throws IOException {
        s.writeInt(size_);
        for (Entry e = header_._next; e != header_; e = e._next) {
            s.writeObject(e._element);
        }
    }

    public void readExternal(ObjectInput s)
            throws IOException, ClassNotFoundException {

        int size = s.readInt();
        header_ = new Entry(null, null, null);
        header_._next = header_._previous = header_;
        for (int i = 0; i < size; i++) {
            addLast(s.readObject());
        }
    }

    public Object clone() {
        SLinkedList copy = new SLinkedList();
        for (Entry e = header_._next; e != header_; e = e._next) {
            copy.addLast(e._element);
        }
        return copy;
    }

    public Object[] toArray() {
        Object[] result = new Object[size_];
        int i = 0;
        for (Entry e = header_._next; e != header_; e = e._next) {
            result[i++] = e._element;
        }
        return result;
    }

    public final class Entry {

        private Object _element;
        private Entry _next;
        private Entry _previous;

        Entry(final Object element, final Entry next, final Entry previous) {
            _element = element;
            _next = next;
            _previous = previous;
        }

        public Object getElement() {
            return _element;
        }

        public Entry getNext() {
            if (_next != SLinkedList.this.header_) {
                return _next;
            }
            return null;
        }

        public Entry getPrevious() {
            if (_previous != SLinkedList.this.header_) {
                return _previous;
            }
            return null;
        }

        public void remove() {
            _previous._next = _next;
            _next._previous = _previous;
            SLinkedList.this.size_--;
        }

        public Entry addBefore(final Object o) {
            Entry newEntry = new Entry(o, this, _previous);
            _previous._next = newEntry;
            _previous = newEntry;
            SLinkedList.this.size_++;
            return newEntry;
        }
    }
}