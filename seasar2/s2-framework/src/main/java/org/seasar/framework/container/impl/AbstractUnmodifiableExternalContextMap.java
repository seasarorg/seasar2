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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Shinpei Ohtani
 */
public abstract class AbstractUnmodifiableExternalContextMap extends
        AbstractExternalContextMap {

    public AbstractUnmodifiableExternalContextMap() {
    }

    public final Set entrySet() {
        return Collections.unmodifiableSet(super.entrySet());
    }

    public final Set keySet() {
        return Collections.unmodifiableSet(super.keySet());
    }

    public final Collection values() {
        return Collections.unmodifiableCollection(super.values());
    }

    public final void clear() {
        throw new UnsupportedOperationException();
    }

    public final Object put(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    public final void putAll(Map map) {
        throw new UnsupportedOperationException();
    }

    public final Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    protected final void setAttribute(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    protected final void removeAttribute(String key) {
        throw new UnsupportedOperationException();
    }
}
