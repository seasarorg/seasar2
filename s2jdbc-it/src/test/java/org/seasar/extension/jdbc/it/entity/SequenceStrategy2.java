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
package org.seasar.extension.jdbc.it.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * @author taedium
 * 
 */
@Entity
public class SequenceStrategy2 {

    /** */
    @Id
    @SequenceGenerator(name = "aaa", sequenceName = "MY_SEQUENCE_STRATEGY_ID", initialValue = 100, allocationSize = 30)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aaa")
    public Integer id;

    /** */
    public String value;
}
