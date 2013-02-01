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
package example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author taedium
 * 
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "FIRST_NAME",
        "LAST_NAME" }) })
public class Employee {

    /** */
    @Id
    @GeneratedValue
    public Integer id;

    /** */
    @Column(nullable = false)
    public String firstName;

    /** */
    @Column(nullable = false)
    public String lastName;

    /** */
    public Integer departmentId;

    /** */
    public Integer addressId;

    /** */
    @ManyToOne
    public Department department;

    /** */
    @OneToOne
    public Address address;
}
