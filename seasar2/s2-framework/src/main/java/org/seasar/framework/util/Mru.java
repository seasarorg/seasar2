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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author higa
 * 
 */
public class Mru implements Serializable {

    private static final long serialVersionUID = 1L;

    private LinkedList list = new LinkedList();

    private Map map = new HashMap();

    private int limitSize;

    public Mru(int limitSize) {
        this.limitSize = limitSize;
    }

    public synchronized Object get(Object key) {
        list.remove(key);
        list.addFirst(key);
        return map.get(key);
    }

    public synchronized Iterator getKeyIterator() {
        return list.iterator();
    }

    public synchronized void put(Object key, Object value) {
        if (map.size() >= limitSize) {
            Object lastKey = list.removeLast();
            map.remove(lastKey);
        }
        list.remove(key);
        list.addFirst(key);
        map.put(key, value);
    }

    public synchronized int getSize() {
        return map.size();
    }

}
