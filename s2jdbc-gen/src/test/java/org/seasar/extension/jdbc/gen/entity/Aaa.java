/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author taedium
 * 
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "aaa", "bbb",
        "ccc" }) })
public class Aaa {

    /** */
    @Id
    public Integer id;

    /** */
    public Integer bbbId;

    /** */
    @ManyToOne
    public Bbb bbb;

    /** */
    @Column(name = "bar")
    public Integer bbb2Id;

    /** */
    @ManyToOne
    @JoinColumn(name = "bar")
    public Bbb bbb2;

    /** */
    @Column(name = "hoge")
    public Integer cccId1;

    /** */
    @Column(name = "foo")
    public Integer cccId2;

    /** */
    @OneToOne
    @JoinColumns( {
            @JoinColumn(name = "hoge", referencedColumnName = "hogehoge"),
            @JoinColumn(name = "foo", referencedColumnName = "foofoo") })
    public Ccc ccc;

}
