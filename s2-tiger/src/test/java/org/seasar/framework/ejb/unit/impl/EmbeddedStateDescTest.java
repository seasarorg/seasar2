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

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class EmbeddedStateDescTest extends TestCase {

    public void testGetEmbeddedStateDescs() {
        PersistentClassDesc pc = new EntityClassDesc(Employee.class);
        Field field = ClassUtil.getDeclaredField(Employee.class, "period");
        FieldAccessor accessor = new FieldAccessor(field);
        EmbeddedStateDesc embeddedDesc = new EmbeddedStateDesc(pc, "hoge", accessor);
        
        List<PersistentStateDesc> embeddedStates = embeddedDesc.getEmbeddedStateDescs();
        assertEquals("1", 2, embeddedStates.size());
    }

    public void testIsIdentifierReturnsTrueWithEmbeddedIdAnnotation() {
        PersistentClassDesc pc = new EntityClassDesc(Employee2.class);
        Field field = ClassUtil.getDeclaredField(Employee2.class, "period");
        FieldAccessor accessor = new FieldAccessor(field);
        EmbeddedStateDesc embeddedDesc = new EmbeddedStateDesc(pc, "hoge", accessor);
        
        assertEquals("1", true, embeddedDesc.isIdentifier());
    }

    public void testIsIdentifierReturnsTrueWithIdAnnotation() {
        PersistentClassDesc pc = new EntityClassDesc(Employee3.class);
        Field field = ClassUtil.getDeclaredField(Employee3.class, "period");
        FieldAccessor accessor = new FieldAccessor(field);
        EmbeddedStateDesc embeddedDesc = new EmbeddedStateDesc(pc, "hoge", accessor);

        assertEquals("1", true, embeddedDesc.isIdentifier());
    }
    
    public void testIsIdentifierReturnsFalse() {
        PersistentClassDesc pc = new EntityClassDesc(Employee.class);
        Field field = ClassUtil.getDeclaredField(Employee.class, "period");
        FieldAccessor accessor = new FieldAccessor(field);
        EmbeddedStateDesc embeddedDesc = new EmbeddedStateDesc(pc, "hoge", accessor);

        assertEquals("1", false, embeddedDesc.isIdentifier());
    }

    @Entity(name = "Employee")
    public static class Employee {

        @Id
        private Long id;

        @Embedded
        private EmployeePeriod period;

    }

    @Entity(name = "Employee2")
    public static class Employee2 {

        @EmbeddedId
        private EmployeePeriod2 period;

    }

    @Entity(name = "Employee3")
    public static class Employee3 {

        @Id
        private EmployeePeriod3 period;

    }

    @Embeddable
    public static class EmployeePeriod {

        private java.util.Date startDate;

        private java.util.Date endDate;
    }
    
    public static class EmployeePeriod2 {

        private java.util.Date startDate;

        private java.util.Date endDate;
    }
    
    @Embeddable
    public static class EmployeePeriod3 {

        private java.util.Date startDate;

        private java.util.Date endDate;
    }
}
