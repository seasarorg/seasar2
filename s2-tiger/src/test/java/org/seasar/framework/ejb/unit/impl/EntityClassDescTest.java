package org.seasar.framework.ejb.unit.impl;

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.AnnotationNotFoundException;
import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateDesc;

/**
 * @author taedium
 * 
 */
public class EntityClassDescTest extends TestCase {

    public void testIsPropertyAccessed() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        assertFalse("1", pc.isPropertyAccessed());
        PersistentClassDesc psh2 = new EntityClassDesc(Hoge2.class);
        assertTrue("2", psh2.isPropertyAccessed());
    }

    public void testGetName() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        assertEquals("1", "Hoge", pc.getName());
        PersistentClassDesc psh2 = new EntityClassDesc(Hoge2.class);
        assertEquals("2", "Foo", psh2.getName());
    }

    public void testGetPersistentClass() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        assertEquals("1", Hoge.class, pc.getPersistentClass());
    }

    public void testGetPersistentState() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        assertEquals("1", 4, pc.getStateDescSize());
    }

    public void testGetTableName() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge3.class);
        assertEquals("1", 3, pc.getTableSize());
        assertEquals("2", "Foo1", pc.getTableName(0));
        assertEquals("3", "Foo2", pc.getTableName(1));
        assertEquals("4", "Foo3", pc.getTableName(2));
    }

    public void testInvalidClass() {
        try {
            new EntityClassDesc(String.class);
            fail();
        } catch (AnnotationNotFoundException e) {
        }
    }

    public void testMappedSuperclassStrategy() {
        PersistentClassDesc fullTime = new EntityClassDesc(FTEmployee.class);
        assertEquals("1", 5, fullTime.getStateDescSize());

        PersistentClassDesc partTime = new EntityClassDesc(PartTimeEmployee2.class);
        assertEquals("2", 5, partTime.getStateDescSize());
        PersistentStateDesc address = partTime.getStateDesc("address");
        assertEquals("3", "ADDR_ID", address.getColumnName());
    }

    public void testSingleTableStrategy() {
        PersistentClassDesc vc = new EntityClassDesc(ValuedCustomer.class);
        
        assertEquals("1", 4, vc.getStateDescSize());
        PersistentStateDesc disc = vc.getStateDesc("$DISC");
        assertEquals("2", "VCUSTOMER", disc.getValue(null));
        assertEquals("3", "CUST", disc.getTableName());

        PersistentClassDesc customer = new EntityClassDesc(Customer.class);
        assertEquals("4", 3, customer.getStateDescSize());
        PersistentStateDesc disc2 = customer.getStateDesc("$DISC");
        assertEquals("5", "CUSTOMER", disc2.getValue(null));
        assertEquals("6", "CUST", disc2.getTableName());
    }
    
}
