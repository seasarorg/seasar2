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
package org.seasar.framework.container.factory;

import javax.ejb.Local;
import javax.ejb.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Local( { IHoge11A.class, IHoge11B.class })
public class Hoge11 implements IHoge11A, IHoge11B {
    String foo;

    @PersistenceContext
    EntityManager em1;

    @PersistenceContext(name = "em")
    EntityManager em2;

    @PersistenceContext(unitName = "emf")
    EntityManager em3;

    EntityManager em4;

    EntityManager em5;

    EntityManager em6;

    @PersistenceContext
    public void setEm4(EntityManager em) {
        em4 = em;
    }

    @PersistenceContext(name = "em")
    public void setEm5(EntityManager em) {
        em5 = em;
    }

    @PersistenceContext(unitName = "emf")
    public void setEm6(EntityManager em) {
        em6 = em;
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public void mandatory() {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void required() {
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void requiresNew() {
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void supports() {
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void notSupported() {
    }

    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void never() {
    }

    @TransactionAttribute
    public void defaultValue() {
    }

    public void notAnnotated() {
    }

    @PostConstruct
    private void initialize() {
        foo = "FOO";
    }
}
