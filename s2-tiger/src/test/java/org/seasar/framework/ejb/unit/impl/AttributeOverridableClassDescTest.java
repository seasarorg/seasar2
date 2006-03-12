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
		assertEquals("1", 2, pc.getStateDescSize());
		assertNotNull("2", pc.getStateDesc("startDate"));
		assertNotNull("3", pc.getStateDesc("endDate"));
	}

    public void testMappedSuperClass() {
        PersistentClassDesc pc = new AttributeOverridableClassDesc(Employee6.class,
                "EMPLOYEE2", true, new HashMap<String, Column>());
        assertEquals("1", 3, pc.getStateDescSize());
        assertNotNull("2", pc.getStateDesc("empid"));
        assertNotNull("3", pc.getStateDesc("version"));
        assertNotNull("4", pc.getStateDesc("address"));
    }

}
