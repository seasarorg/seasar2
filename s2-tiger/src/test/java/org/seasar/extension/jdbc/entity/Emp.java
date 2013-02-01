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
package org.seasar.extension.jdbc.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author higa
 * 
 */
@Entity
public class Emp {

    /**
     * 
     */
    @Id
    public Integer empno;

    /**
     * 
     */
    public String ename;

    /**
     * 
     */
    public String job;

    /**
     * 
     */
    public Integer mgr;

    /**
     * 
     */
    @Temporal(TemporalType.DATE)
    public Date hiredate;

    /**
     * 
     */
    public BigDecimal sal;

    /**
     * 
     */
    public BigDecimal comm;

    /**
     * 
     */
    public Integer deptno;

    /**
     * 
     */
    @ManyToOne
    @JoinColumn(name = "deptno", referencedColumnName = "deptno")
    public Dept dept;
}