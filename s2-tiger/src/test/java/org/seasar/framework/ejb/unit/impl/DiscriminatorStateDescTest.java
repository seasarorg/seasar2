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
package org.seasar.framework.ejb.unit.impl;

import static javax.persistence.DiscriminatorType.INTEGER;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;

import junit.framework.TestCase;

public class DiscriminatorStateDescTest extends TestCase {

    private DiscriminatorStateDesc discriminator;

    private DiscriminatorStateDesc defaultDisc;

    @Override
    public void setUp() throws Exception {
        EntityClassDesc entityDesc = new EntityClassDesc(Hoge.class);
        discriminator = (DiscriminatorStateDesc) entityDesc
                .getPersistentStateDesc("$foo");

        EntityClassDesc entityDesc2 = new EntityClassDesc(Hoge2.class);
        defaultDisc = (DiscriminatorStateDesc) entityDesc2
                .getPersistentStateDesc("$DTYPE");
    }

    public void testGetPersistentStateClass() throws Exception {
        assertEquals("1", Integer.class, discriminator
                .getPersistentStateClass());
        assertEquals("2", String.class, defaultDisc.getPersistentStateClass());
    }

    public void testGetGetValue() throws Exception {
        assertEquals("1", new Integer(111), discriminator.getValue(null));
        assertEquals("2", Hoge2.class.getName(), defaultDisc.getValue(null));
    }

    @Entity
    @DiscriminatorColumn(name = "foo", discriminatorType = INTEGER)
    @DiscriminatorValue("111")
    public static class Hoge {
        @Id
        private Long aaa;

        public Long getAaa() {
            return aaa;
        }
    }

    @Entity
    @DiscriminatorColumn
    public static class Hoge2 {
        @Id
        private Long aaa;

        public Long getAaa() {
            return aaa;
        }
    }
}
