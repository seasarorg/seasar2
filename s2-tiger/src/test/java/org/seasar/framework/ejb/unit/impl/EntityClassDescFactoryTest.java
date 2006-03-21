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

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.PersistentStateDesc;

public class EntityClassDescFactoryTest extends TestCase {
    
    public void test() {
        EntityClassDesc entityDesc =  EntityClassDescFactory.getEntityClassDesc(Employee.class);
        PersistentStateDesc stateDesc = entityDesc.getPersistentStateDesc("Employee.department");
        assertNotNull("1", stateDesc.getRelationshipClassDesc());
        assertTrue("2", stateDesc.getForeignKeyColumnSize() > 0);
    }

}
