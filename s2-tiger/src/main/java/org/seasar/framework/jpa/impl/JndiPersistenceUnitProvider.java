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
package org.seasar.framework.jpa.impl;

import java.util.Map;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;

import org.seasar.extension.j2ee.JndiResourceLocator;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.DestroyMethod;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.exception.NamingRuntimeException;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.jpa.PersistenceUnitProvider;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author taedium
 * 
 */
public class JndiPersistenceUnitProvider implements PersistenceUnitProvider {

    protected PersistenceUnitManager persistenceUnitManager;

    protected Map<String, String> JndiNames = CollectionsUtil.newHashMap();

    @Binding(bindingType = BindingType.MUST)
    public void setPersistenceUnitManager(
            final PersistenceUnitManager persistenceUnitManager) {
        this.persistenceUnitManager = persistenceUnitManager;
    }

    @InitMethod
    public void register() {
        persistenceUnitManager.addProvider(this);
    }

    @DestroyMethod
    public void unregister() {
        persistenceUnitManager.removeProvider(this);
    }

    public void addJndiName(final String unitName, final String jndiName) {
        JndiNames.put(unitName, jndiName);
    }

    public EntityManagerFactory createEntityManagerFactory(final String unitName) {
        return createEntityManagerFactory(null, unitName);
    }

    public EntityManagerFactory createEntityManagerFactory(
            final String abstractUnitName, final String concreteUnitName) {
        final String name = JndiNames.get(concreteUnitName);
        try {
            return EntityManagerFactory.class.cast(JndiResourceLocator
                    .lookup(name));
        } catch (final NamingException e) {
            throw new NamingRuntimeException(e);
        }
    }

}
