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

import org.seasar.framework.ejb.unit.AnnotationNotFoundException;
import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.ejb.unit.PersistentStateNotFoundException;

/**
 * @author taedium
 * 
 */
public class EntityClassDescTest extends TestCase {

    public void testIsPropertyAccessed() {
        PersistentClassDesc fieldAccess = new EntityClassDesc(Hoge.class);
        assertFalse("1", fieldAccess.isPropertyAccessed());
        PersistentClassDesc propertyAccess = new EntityClassDesc(Hoge2.class);
        assertTrue("2", propertyAccess.isPropertyAccessed());
    }

    public void testGetPersistentClass() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        assertEquals("1", Hoge.class, pc.getPersistentClass());
    }

    public void testGetPersistentState() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        assertEquals("1", 4, pc.getPersistentStateDescSize());
    }

    public void testGetPersistentStateForException() {
        PersistentClassDesc fieldAccess = new EntityClassDesc(Hoge.class);
        try {
            fieldAccess.getPersistentStateDesc("invalidStateName");
            fail("1");
        } catch (PersistentStateNotFoundException expected) {
            System.out.println(expected);
        }
    }

    public void testGetTableName() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge3.class);
        assertEquals("1", 3, pc.getTableSize());
        assertEquals("2", "FOO1", pc.getTableName(0));
        assertEquals("3", "FOO2", pc.getTableName(1));
        assertEquals("4", "FOO3", pc.getTableName(2));
    }

    public void testInvalidClass() {
        try {
            new EntityClassDesc(String.class);
            fail("1");
        } catch (AnnotationNotFoundException expected) {
            System.out.println(expected);
        }
    }

    public void testMappedSuperclass() {
        PersistentClassDesc fullTime = new EntityClassDesc(FTEmployee.class);
        assertEquals("1", 5, fullTime.getPersistentStateDescSize());

        PersistentClassDesc partTime = new EntityClassDesc(
                PartTimeEmployee2.class);
        assertEquals("2", 5, partTime.getPersistentStateDescSize());
        assertNotNull("3", partTime.getPersistentStateDesc("Employee6.address"));
    }

    public void testMultiMappedSuperclass() {
        PersistentClassDesc hoge7 = new EntityClassDesc(Hoge7.class);
        assertEquals("1", 5, hoge7.getPersistentStateDescSize());
    }

    public void testSingleTableStrategy() {
        PersistentClassDesc vc = new EntityClassDesc(ValuedCustomer.class);

        assertEquals("1", 4, vc.getPersistentStateDescSize());
        PersistentStateDesc disc = vc
                .getPersistentStateDesc("ValuedCustomer.$DISC");
        assertEquals("2", "VCUSTOMER", disc.getValue(null));
        assertEquals("3", "CUST", disc.getColumn().getTableName());

        PersistentClassDesc customer = new EntityClassDesc(Customer.class);
        assertEquals("4", 3, customer.getPersistentStateDescSize());
        PersistentStateDesc disc2 = customer
                .getPersistentStateDesc("Customer.$DISC");
        assertEquals("5", "CUSTOMER", disc2.getValue(null));
        assertEquals("6", "CUST", disc2.getTableName());
    }

    public void testTablePerClassStrategy() {
        PersistentClassDesc vc = new EntityClassDesc(ValuedCustomer2.class);

        assertEquals("1", 3, vc.getPersistentStateDescSize());
        for (int i = 0; i < vc.getPersistentStateDescSize(); i++) {
            PersistentColumn column = vc.getPersistentStateDesc(i).getColumn();
            assertEquals("2", "VALUEDCUSTOMER2", column.getTableName());
        }

        PersistentClassDesc customer = new EntityClassDesc(Customer2.class);
        assertEquals("3", 2, customer.getPersistentStateDescSize());
        for (int i = 0; i < customer.getPersistentStateDescSize(); i++) {
            PersistentColumn column = customer.getPersistentStateDesc(i)
                    .getColumn();
            assertEquals("4", "CUST", column.getTableName());
        }
    }

    public void testJoineStrategy() {
        PersistentClassDesc vc = new EntityClassDesc(ValuedCustomer3.class);

        assertEquals("1", 4, vc.getPersistentStateDescSize());
        assertEquals("2", "CUST", vc.getPersistentStateDesc("Customer3.id")
                .getColumn().getTableName());
        assertEquals("3", "CUST", vc.getPersistentStateDesc("Customer3.name")
                .getColumn().getTableName());
        assertEquals("4", "VALUEDCUSTOMER3", vc.getPersistentStateDesc(
                "ValuedCustomer3.id").getColumn().getTableName());
        assertEquals("5", "VALUEDCUSTOMER3", vc.getPersistentStateDesc(
                "ValuedCustomer3.rank").getColumn().getTableName());

        PersistentClassDesc customer = new EntityClassDesc(Customer3.class);
        assertEquals("6", 2, customer.getPersistentStateDescSize());
        assertEquals("7", "CUST", customer.getPersistentStateDesc(
                "Customer3.id").getColumn().getTableName());
        assertEquals("8", "CUST", customer.getPersistentStateDesc(
                "Customer3.name").getColumn().getTableName());
    }

}
