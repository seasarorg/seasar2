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

import static javax.persistence.EnumType.STRING;
import static org.seasar.framework.ejb.unit.PersistentStateType.BASIC;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.persistence.Temporal;

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class BasicStateDescTest extends TestCase {

    public void testGetColumn() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        Field field = ClassUtil.getDeclaredField(Hoge.class, "aaa");
        FieldAccessor accessor = new FieldAccessor(field);
        BasicStateDesc basicDesc = new BasicStateDesc(pc, "hoge", accessor);
        PersistentColumn column = basicDesc.getColumn();

        assertEquals("1", "hoge", column.getTable().toLowerCase());
        assertEquals("2", "aaa", column.getName().toLowerCase());
    }

    public void testGetColumn2() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge2.class);
        Field field = ClassUtil.getDeclaredField(Hoge2.class, "aaa");
        FieldAccessor accessor = new FieldAccessor(field);
        BasicStateDesc basicDesc = new BasicStateDesc(pc, "hoge", accessor);
        PersistentColumn column = basicDesc.getColumn();

        assertEquals("1", "foo2", column.getTable().toLowerCase());
        assertEquals("2", "foo1aaa", column.getName().toLowerCase());
    }

    public void testHasColumnReturnsTrue() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge2.class);
        Field field = ClassUtil.getDeclaredField(Hoge2.class, "aaa");
        FieldAccessor accessor = new FieldAccessor(field);
        BasicStateDesc basicDesc = new BasicStateDesc(pc, "hoge", accessor);

        assertEquals("1", true, basicDesc.hasColumn("foo1aaa"));
    }

    public void testHasColumnReturnsFalse() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge2.class);
        Field field = ClassUtil.getDeclaredField(Hoge2.class, "aaa");
        FieldAccessor accessor = new FieldAccessor(field);
        BasicStateDesc basicDesc = new BasicStateDesc(pc, "hoge", accessor);

        assertEquals("1", false, basicDesc.hasColumn("aaa"));
    }

    public void testGetPersistenceTargetClass() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        Field field = ClassUtil.getDeclaredField(Hoge.class, "aaa");
        FieldAccessor accessor = new FieldAccessor(field);
        BasicStateDesc basicDesc = new BasicStateDesc(pc, "hoge", accessor);
        
        assertEquals("1", Long.class, basicDesc.getPersistenceTargetClass());
    }

    public void testIsIdentifierReturnsTrue() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        Field field = ClassUtil.getDeclaredField(Hoge.class, "aaa");
        FieldAccessor accessor = new FieldAccessor(field);
        BasicStateDesc basicDesc = new BasicStateDesc(pc, "hoge", accessor); 
        
        assertEquals("1", true, basicDesc.isIdentifier());
    }

    public void testIsIdentifierReturnsFalse() {
        PersistentClassDesc pc = new EntityClassDesc(Hoge.class);
        Field field = ClassUtil.getDeclaredField(Hoge.class, "bbb");
        FieldAccessor accessor = new FieldAccessor(field);
        BasicStateDesc basicDesc = new BasicStateDesc(pc, "hoge", accessor);         
        
        assertEquals("1", false, basicDesc.isIdentifier());
    }

    public void testGetEnumValueByOrdinal() {
        Hoge3 hoge3 = new Hoge3();
        PersistentClassDesc pc = new EntityClassDesc(Hoge3.class);
        Field field = ClassUtil.getDeclaredField(Hoge3.class, "bbb");
        field.setAccessible(true);
        FieldAccessor accessor = new FieldAccessor(field);
        BasicStateDesc basicDesc = new BasicStateDesc(pc, "hoge", accessor);
        
        assertEquals("1", 1, basicDesc.getValue(hoge3));
        assertEquals("2", int.class, basicDesc.getPersistenceTargetClass());
    }

    public void testGetEnumValueByString() {
        Hoge3 hoge3 = new Hoge3();
        PersistentClassDesc pc = new EntityClassDesc(Hoge3.class);
        Field field = ClassUtil.getDeclaredField(Hoge3.class, "ccc");
        field.setAccessible(true);
        FieldAccessor accessor = new FieldAccessor(field);
        BasicStateDesc basicDesc = new BasicStateDesc(pc, "hoge", accessor);
        
        assertEquals("1", "BBB", basicDesc.getValue(hoge3));
        assertEquals("2", String.class, basicDesc.getPersistenceTargetClass());
    }

    @Entity(name = "Hoge")
    public static class Hoge {
        @Id
        private Long aaa;

        private String bbb;
    }

    @Entity
    @Table(name = "Foo1")
    @SecondaryTable(name = "Foo2")
    public static class Hoge2 {
        @Id
        @Column(name = "Foo1aaa", table = "Foo2")
        private Long aaa;
    }

    @Entity(name = "Hoge3")
    public static class Hoge3 {
        @Id
        private Long aaa;

        private HogeType bbb = HogeType.BBB;

        @Enumerated(STRING)
        private HogeType ccc = HogeType.BBB;
        
        private java.util.Date d;
    }

    public static enum HogeType {
        AAA, BBB
    }
}
