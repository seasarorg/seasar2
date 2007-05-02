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
package org.seasar.framework.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * @author koichik
 */
public interface PersistenceUnitManager {

    EntityManagerFactory getEntityManagerFactory(String unitName);

    EntityManagerFactory getEntityManagerFactory(String unitName,
            PersistenceUnitProvider provider);

    EntityManagerFactory getEntityManagerFactory(String abstractUnitName,
            String concreteUnitName);

    EntityManagerFactory getEntityManagerFactory(String abstractUnitName,
            String concreteUnitName, PersistenceUnitProvider provider);

    PersistenceUnitContext getPersistenceUnitContext(EntityManagerFactory emf);

    String getPersistenceUnitName(Class<?> entityClass);

    String getPersistenceUnitName(String mappingFile);

}
