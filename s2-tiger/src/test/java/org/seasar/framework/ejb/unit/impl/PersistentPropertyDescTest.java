package org.seasar.framework.ejb.unit.impl;

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateDesc;

/**
 * @author taedium
 *
 */
public class PersistentPropertyDescTest extends TestCase {

	public void testIsProperty() {
		PersistentClassDesc pc = new EntityClassDesc(Hoge2.class);
		PersistentStateDesc aaa = pc.getStateDesc("aaa");
		assertTrue("1", aaa.isProperty());
	}

	public void testNoReadMethod() {
		PersistentClassDesc pc = new EntityClassDesc(Hoge4.class);
		PersistentStateDesc bbb = pc.getStateDesc("bbb");
		assertEquals("1", "HOGE4", bbb.getTableName());
		assertEquals("2", "BBB", bbb.getColumnName());
	}

	public void testGetValue() {
		Hoge2 hoge2 = new Hoge2();
		hoge2.setBbb(10);
		PersistentClassDesc pc = new EntityClassDesc(Hoge2.class);
		PersistentStateDesc bbb = pc.getStateDesc("bbb");
		assertEquals("1", 10, bbb.getValue(hoge2));
	}

	public void testGetValue2() {
		Hoge4 hoge4 = new Hoge4();
		hoge4.setBbb(10);
		PersistentClassDesc pc = new EntityClassDesc(Hoge2.class);
		PersistentStateDesc bbb = pc.getStateDesc("bbb");
		try {
			bbb.getValue(hoge4);
			fail();
		} catch (Exception e) {
		}
	}

	public void testSetValue() {
		Hoge2 hoge2 = new Hoge2();
		PersistentClassDesc pc = new EntityClassDesc(Hoge2.class);
		PersistentStateDesc bbb = pc.getStateDesc("bbb");
		bbb.setValue(hoge2, 10);
		assertEquals("1", new Integer(10), hoge2.getBbb());
	}

	public void testSetValue2() {
		Hoge4 hoge4 = new Hoge4();
		PersistentClassDesc pc = new EntityClassDesc(Hoge4.class);
		PersistentStateDesc ccc = pc.getStateDesc("ccc");
		try {
			ccc.setValue(hoge4, new java.util.Date());
			fail();
		} catch (Exception e) {
		}
	}

}
