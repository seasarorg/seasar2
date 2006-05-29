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
package org.seasar.framework.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CaseInsensitiveSet extends AbstractSet implements Set,
        Serializable {

    static final long serialVersionUID = 0L;

    private transient Map map;

    private static final Object PRESENT = new Object();

    public CaseInsensitiveSet() {
        map = new CaseInsensitiveMap();
    }

    public CaseInsensitiveSet(Collection c) {
        map = new CaseInsensitiveMap(Math.max((int) (c.size() / .75f) + 1, 16));
        addAll(c);
    }

    public CaseInsensitiveSet(int initialCapacity) {
        map = new CaseInsensitiveMap(initialCapacity);
    }

    public Iterator iterator() {
        return map.keySet().iterator();
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public boolean add(Object o) {
        return map.put(o, PRESENT) == null;
    }

    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }

    public void clear() {
        map.clear();
    }
}
