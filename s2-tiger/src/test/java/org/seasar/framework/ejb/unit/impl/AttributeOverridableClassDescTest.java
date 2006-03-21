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

import java.util.HashMap;

import javax.persistence.Column;

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.PersistentClassDesc;

/**
 * @author taedium
 *
 */
public class AttributeOverridableClassDescTest extends TestCase {

	public void testEmbeddableClass() {
		PersistentClassDesc pc = new AttributeOverridableClassDesc(EmployeePeriod.class,
				"EMPLOYEE2", true, new HashMap<String, Column>());
		assertEquals("1", 2, pc.getPersistentStateDescSize());
		assertNotNull("2", pc.getPersistentStateDesc("EmployeePeriod.startDate"));
		assertNotNull("3", pc.getPersistentStateDesc("EmployeePeriod.endDate"));
	}

    public void testMappedSuperClass() {
        PersistentClassDesc pc = new AttributeOverridableClassDesc(Employee6.class,
                "EMPLOYEE2", true, new HashMap<String, Column>());
        assertEquals("1", 3, pc.getPersistentStateDescSize());
        assertNotNull("2", pc.getPersistentStateDesc("Employee6.empid"));
        assertNotNull("3", pc.getPersistentStateDesc("Employee6.version"));
        assertNotNull("4", pc.getPersistentStateDesc("Employee6.address"));
    }
    
}
