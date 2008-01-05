/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.impl;

import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;

import org.seasar.framework.jpa.Dialect;
import org.seasar.framework.jpa.DialectManager;
import org.seasar.framework.jpa.exception.DialectNotFoundException;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * {@link DialectManager}の実装クラスです。
 * 
 * @author koichik
 */
public class DialectManagerImpl implements DialectManager {

    /** JPA実装が提供する{@link EntityManager}の実装クラスをキー、{@link Dialect}を値とするマップ */
    protected Map<Class<?>, Dialect> dialects = CollectionsUtil
            .newLinkedHashMap();

    public void addDialect(final Class<?> delegateClass, final Dialect dialect) {
        dialects.put(delegateClass, dialect);
    }

    public void removeDialect(final Class<?> delegateClass) {
        dialects.remove(delegateClass);
    }

    public Dialect getDialect(final EntityManager em) {
        final Object delegate = em.getDelegate();
        for (final Entry<Class<?>, Dialect> entry : dialects.entrySet()) {
            if (entry.getKey().isInstance(delegate)) {
                return entry.getValue();
            }
        }
        throw new DialectNotFoundException(em);
    }

}
