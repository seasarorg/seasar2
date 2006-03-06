package org.seasar.framework.ejb.unit.impl;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.ejb.unit.PersistentState;
import org.seasar.framework.ejb.unit.PersistentClass;

/**
 * @author taedium
 *
 */
public class PersistentPropertyTest extends S2TestCase {

	public void testInstanceOf() {
		PersistentClass pc = new EntityClass(Hoge2.class);
		PersistentState aaa = pc.getPersistentState("aaa");
		assertTrue("1", aaa instanceof PersistentProperty);
	}

	public void testNoReadMethod() {
		PersistentClass pc = new EntityClass(Hoge4.class);
		PersistentState bbb = pc.getPersistentState("bbb");
		assertEquals("1", "HOGE4", bbb.getTableName());
		assertEquals("2", "BBB", bbb.getColumnName());
	}

	public void testGetValue() {
		Hoge2 hoge2 = new Hoge2();
		hoge2.setBbb(10);
		PersistentClass pc = new EntityClass(Hoge2.class);
		PersistentState bbb = pc.getPersistentState("bbb");
		assertEquals("1", 10, bbb.getValue(hoge2));
	}

	public void testGetValue2() {
		Hoge4 hoge4 = new Hoge4();
		hoge4.setBbb(10);
		PersistentClass pc = new EntityClass(Hoge2.class);
		PersistentState bbb = pc.getPersistentState("bbb");
		try {
			bbb.getValue(hoge4);
			fail();
		} catch (Exception e) {
		}
	}

	public void testSetValue() {
		Hoge2 hoge2 = new Hoge2();
		PersistentClass pc = new EntityClass(Hoge2.class);
		PersistentState bbb = pc.getPersistentState("bbb");
		bbb.setValue(hoge2, 10);
		assertEquals("1", new Integer(10), hoge2.getBbb());
	}

	public void testSetValue2() {
		Hoge4 hoge4 = new Hoge4();
		PersistentClass pc = new EntityClass(Hoge4.class);
		PersistentState ccc = pc.getPersistentState("ccc");
		try {
			ccc.setValue(hoge4, new java.util.Date());
			fail();
		} catch (Exception e) {
		}
	}

}
