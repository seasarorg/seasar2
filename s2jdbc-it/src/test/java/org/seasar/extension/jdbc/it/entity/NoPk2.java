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
package org.seasar.extension.jdbc.it.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author taedium
 * 
 */
@Entity
@Table(name = "NO_PK")
public class NoPk2 {

    /** */
    public int column1;

    /** */
    public int column2;

    /** */
    @ManyToOne
    @JoinColumn(name = "column2", referencedColumnName = "column1")
    public NoPk2 noPk2;

    /** */
    @OneToMany(mappedBy = "noPk2")
    public List<NoPk2> noPk2s;
}
