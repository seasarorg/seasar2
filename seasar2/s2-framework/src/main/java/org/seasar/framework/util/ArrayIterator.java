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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 配列を{@link Iterator}にするAdaptorです。
 * 
 * @author shot
 */
public class ArrayIterator implements Iterator {

    private Object[] items_;

    private int index_ = 0;

    /**
     * {@link ArrayIterator}を作成します。
     * 
     * @param items
     */
    public ArrayIterator(Object items[]) {
        items_ = items;
    }

    public boolean hasNext() {
        return index_ < items_.length;
    }

    public Object next() {
        try {
            Object o = items_[index_];
            index_++;
            return o;
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("index=" + index_);
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("remove");
    }

}
