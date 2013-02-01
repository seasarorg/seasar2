/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

/**
 * 
 */
@Stateless
@Local( { IHoge11A.class, IHoge11B.class })
public class Hoge11 implements IHoge11A, IHoge11B {

    String foo;

    @PersistenceContext
    EntityManager em1;

    @PersistenceContext(name = "emf")
    EntityManager em2;

    @PersistenceContext(unitName = "hibernate")
    EntityManager em3;

    EntityManager em4;

    EntityManager em5;

    EntityManager em6;

    @PersistenceUnit
    EntityManagerFactory emf1;

    @PersistenceUnit(name = "emf")
    EntityManagerFactory emf2;

    @PersistenceUnit(unitName = "hibernate")
    EntityManagerFactory emf3;

    EntityManagerFactory emf4;

    EntityManagerFactory emf5;

    EntityManagerFactory emf6;

    @Resource
    DataSource ds1;

    @Resource(name = "java:comp/env/jdbc/DataSource")
    DataSource ds2;

    DataSource ds3;

    DataSource ds4;

    /**
     * @param em
     */
    @PersistenceContext
    public void setEm4(EntityManager em) {
        em4 = em;
    }

    /**
     * @param em
     */
    @PersistenceContext(name = "emf")
    public void setEm5(EntityManager em) {
        em5 = em;
    }

    /**
     * @param em
     */
    @PersistenceContext(unitName = "hibernate")
    public void setEm6(EntityManager em) {
        em6 = em;
    }

    /**
     * @return
     */
    public EntityManagerFactory getEmf4() {
        return emf4;
    }

    /**
     * @param emf4
     */
    @PersistenceUnit
    public void setEmf4(EntityManagerFactory emf4) {
        this.emf4 = emf4;
    }

    /**
     * @return
     */
    public EntityManagerFactory getEmf5() {
        return emf5;
    }

    /**
     * @param emf5
     */
    @PersistenceUnit(name = "emf")
    public void setEmf5(EntityManagerFactory emf5) {
        this.emf5 = emf5;
    }

    /**
     * @return
     */
    public EntityManagerFactory getEmf6() {
        return emf6;
    }

    /**
     * @param emf6
     */
    @PersistenceUnit(unitName = "hibernate")
    public void setEmf6(EntityManagerFactory emf6) {
        this.emf6 = emf6;
    }

    /**
     * @param ds3
     */
    @Resource
    public void setDs3(DataSource ds3) {
        this.ds3 = ds3;
    }

    /**
     * @param ds4
     */
    @Resource(name = "DataSource")
    public void setDs4(DataSource ds4) {
        this.ds4 = ds4;
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

    @SuppressWarnings("unused")
    @PostConstruct
    private void initialize() {
        foo = "FOO";
    }
}
