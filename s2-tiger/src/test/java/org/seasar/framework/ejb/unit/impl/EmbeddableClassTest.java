package org.seasar.framework.ejb.unit.impl;

import java.util.HashMap;

import javax.persistence.Column;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.ejb.unit.AnnotationNotFoundRuntimeException;
import org.seasar.framework.ejb.unit.PersistentClass;

/**
 * @author taedium
 *
 */
public class EmbeddableClassTest extends S2TestCase {

	public void test() {
		PersistentClass pc = new EmbeddableClass(EmployeePeriod.class,
				"EMPLOYEE2", true, new HashMap<String, Column>());
		assertEquals("1", 2, pc.getPersistentStateSize());
		assertNotNull("2", pc.getPersistentState("startDate"));
		assertNotNull("3", pc.getPersistentState("endDate"));
	}

	public void testInvalidClass() {
		try {
			new EmbeddableClass(String.class, "STRING",
					true, new HashMap<String, Column>());
			fail();
		} catch (AnnotationNotFoundRuntimeException e) {
		}
	}
}
