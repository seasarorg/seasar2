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

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.ejb.unit.PersistentStateNotFoundException;

/**
 * @author taedium
 * 
 */
public class PersistentPropertyDescTest extends TestCase {

    public void testIsProperty() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge2.class);
        PersistentStateDesc aaa = pc.getPersistentStateDesc("Hoge2.aaa");
        assertTrue("1", aaa.isProperty());
    }

    public void testNoReadMethod() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge4.class);
        try {
            pc.getPersistentStateDesc("Hoge4.bbb");
            fail("1");
        } catch (PersistentStateNotFoundException expected) {
            System.out.println(expected);
        }
    }

    public void testGetValue() {
        Hoge2 hoge2 = new Hoge2();
        hoge2.setBbb(10);
        PersistentClassDesc pc = new EntityClassDesc(Hoge2.class);
        PersistentStateDesc bbb = pc.getPersistentStateDesc("Hoge2.bbb");
        assertEquals("1", 10, bbb.getValue(hoge2));
    }

    public void testGetValue2() {
        Hoge4 hoge4 = new Hoge4();
        hoge4.setBbb(10);
        PersistentClassDesc pc = new EntityClassDesc(Hoge2.class);
        PersistentStateDesc bbb = pc.getPersistentStateDesc("Hoge2.bbb");
        try {
            bbb.getValue(hoge4);
            fail();
        } catch (Exception e) {
        }
    }

    public void testSetValue() {
        Hoge2 hoge2 = new Hoge2();
        PersistentClassDesc pc = new EntityClassDesc(Hoge2.class);
        PersistentStateDesc bbb = pc.getPersistentStateDesc("Hoge2.bbb");
        bbb.setValue(hoge2, 10);
        assertEquals("1", new Integer(10), hoge2.getBbb());
    }

    public void testSetValue2() {
        Hoge4 hoge4 = new Hoge4();
        PersistentClassDesc pc = new EntityClassDesc(Hoge4.class);
        PersistentStateDesc ccc = pc.getPersistentStateDesc("Hoge4.ccc");
        try {
            ccc.setValue(hoge4, new java.util.Date());
            fail();
        } catch (Exception e) {
        }
    }

}
