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

import static javax.persistence.InheritanceType.JOINED;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Inheritance;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;

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

    public void testConstructor() throws Exception {
        try {
            new EntityClassDesc(String.class);
            fail("1");
        } catch (AnnotationNotFoundException expected) {
            System.out.println(expected);
        }
    }

    public void testIsPropertyAccessed() throws Exception {
        EntityClassDesc entityDesc = new EntityClassDesc(Hoge.class);
        assertEquals("1", false, entityDesc.isPropertyAccessed());
        entityDesc = new EntityClassDesc(Hoge2.class);
        assertEquals("2", true, entityDesc.isPropertyAccessed());
    }

    public void testGetPersistentClass() throws Exception {
        EntityClassDesc entityDesc = new EntityClassDesc(Hoge.class);
        assertEquals("1", Hoge.class, entityDesc.getPersistentClass());
    }

    public void testGetPersistentState() throws Exception {
        EntityClassDesc entityDesc = new EntityClassDesc(Hoge.class);
        assertEquals("1", 4, entityDesc.getPersistentStateDescs().size());
        assertNotNull("2", entityDesc.getPersistentStateDesc("aaa"));
        assertNotNull("3", entityDesc.getPersistentStateDesc("bbb"));
        assertNotNull("4", entityDesc.getPersistentStateDesc("ccc"));
        assertNotNull("5", entityDesc.getPersistentStateDesc("$DTYPE"));
    }

    public void testGetPersistentState2() throws Exception {
        EntityClassDesc entityDesc = new EntityClassDesc(Hoge5.class);
        assertNotNull("2", entityDesc
                .getPersistentStateDesc(Hoge4.class, "bbb"));
    }

    public void testGetPersistentStateForException() throws Exception {
        EntityClassDesc entityDesc = new EntityClassDesc(Hoge5.class);
        try {
            entityDesc.getPersistentStateDesc("bbb");
            fail("1");
        } catch (PersistentStateNotFoundException expected) {
            System.out.println(expected);
        }
    }

    public void testGetTableName() throws Exception {
        EntityClassDesc entityDescs = new EntityClassDesc(Hoge3.class);
        List<String> tableNames = entityDescs.getTableNames();
        assertEquals("1", 3, tableNames.size());
        assertEquals("2", "foo1", tableNames.get(0).toLowerCase());
        assertEquals("3", "foo2", tableNames.get(1).toLowerCase());
        assertEquals("4", "foo3", tableNames.get(2).toLowerCase());
    }

    public void testGetIdentifier() throws Exception {
        EntityClassDesc entityDesc = new EntityClassDesc(Hoge.class);
        List<PersistentStateDesc> identifiers = entityDesc.getIdentifiers();
        assertEquals("1", 1, identifiers.size());
        assertEquals("2", "aaa", identifiers.get(0).getName());
    }

    public void testGetIdentifierForJoinedSubclass() throws Exception {
        EntityClassDesc entityDesc = new EntityClassDesc(Hoge5.class);
        List<PersistentStateDesc> identifiers = entityDesc.getIdentifiers();
        assertEquals("1", 1, identifiers.size());
        assertEquals("2", "aaa", identifiers.get(0).getName());
    }

    public void testGetIdentifierForEmbeddedId() throws Exception {
        EntityClassDesc entityDesc = new EntityClassDesc(Hoge6.class);
        List<PersistentStateDesc> identifiers = entityDesc.getIdentifiers();
        assertEquals("1", 1, identifiers.size());
        assertEquals("2", "pk", identifiers.get(0).getName());
    }

    public void testGetIdentifierForIdClass() throws Exception {
        EntityClassDesc entityDesc = new EntityClassDesc(Hoge7.class);
        List<PersistentStateDesc> identifiers = entityDesc.getIdentifiers();
        assertEquals("1", 2, identifiers.size());
    }

    public void testGetRoot() throws Exception {
        EntityClassDesc entityDesc = new EntityClassDesc(Hoge5.class);
        assertEquals("1", Hoge4.class, entityDesc.getRoot()
                .getPersistentClass());
    }

    public void testGetPersistentStateDescsByTableName() {
        List<PersistentStateDesc> list = null;
        PersistentStateDesc stateDesc = null;

        EntityClassDesc ecd = new EntityClassDesc(SpecialValuedCustomer.class);

        assertEquals("1", 6, ecd.getPersistentStateDescs().size());

        list = ecd.getPersistentStateDescsByTableName("CUST");
        stateDesc = ecd.getPersistentStateDesc(Customer.class, "id");
        assertEquals("2", true, list.contains(stateDesc));
        stateDesc = ecd.getPersistentStateDesc(Customer.class, "name");
        assertEquals("3", true, list.contains(stateDesc));

        list = ecd.getPersistentStateDescsByTableName("ValuedCustomer");
        stateDesc = ecd.getPersistentStateDesc(ValuedCustomer.class, "id");
        assertEquals("4", true, list.contains(stateDesc));
        stateDesc = ecd.getPersistentStateDesc(ValuedCustomer.class, "rank");
        assertEquals("5", true, list.contains(stateDesc));

        list = ecd.getPersistentStateDescsByTableName("SpecialValuedCustomer");
        stateDesc = ecd.getPersistentStateDesc("id");
        assertEquals("6", true, list.contains(stateDesc));
        stateDesc = ecd.getPersistentStateDesc("specialRank");
        assertEquals("7", true, list.contains(stateDesc));
    }

    public void testPrimaryKeyJoinColumnWithoutReferencedColumnName() {
        EntityClassDesc ecd = new EntityClassDesc(ValuedCustomer2.class);
        PersistentColumn column = ecd.getPersistentStateDesc("id").getColumn();
        assertEquals("1", "aaa", column.getName().toLowerCase());
        assertEquals("2", "valuedcustomer2", column.getTable().toLowerCase());
    }

    public void testPrimaryKeyJoinColumnWithReferencedColumnName() {
        EntityClassDesc ecd = new EntityClassDesc(SpecialValuedCustomer2.class);
        PersistentColumn column = ecd.getPersistentStateDesc("id").getColumn();
        assertEquals("1", "aaa", column.getName().toLowerCase());
        assertEquals("2", "specialvaluedcustomer2", column.getTable().toLowerCase());
    }

    public void testPrimaryKeyJoinColumnsWithoutReferencedColumnName() {
        EntityClassDesc ecd = new EntityClassDesc(ValuedCustomer3.class);
        PersistentClassDesc embedded = ecd.getPersistentStateDesc("id")
                .getEmbeddedClassDesc();
        PersistentColumn column = embedded.getPersistentStateDesc("id1")
                .getColumn();
        assertEquals("1", "aaa", column.getName().toLowerCase());
        assertEquals("2", "valuedcustomer3", column.getTable().toLowerCase());
        column = embedded.getPersistentStateDesc("id2").getColumn();
        assertEquals("3", "bbb", column.getName().toLowerCase());
        assertEquals("4", "valuedcustomer3", column.getTable().toLowerCase());
    }

    public void testPrimaryKeyJoinColumnsWithReferencedColumnName() {
        EntityClassDesc ecd = new EntityClassDesc(SpecialValuedCustomer3.class);
        PersistentClassDesc embedded = ecd.getPersistentStateDesc("id")
                .getEmbeddedClassDesc();
        PersistentColumn column = embedded.getPersistentStateDesc("id1")
                .getColumn();
        assertEquals("1", "aaa", column.getName().toLowerCase());
        assertEquals("2", "specialvaluedcustomer3", column.getTable().toLowerCase());
        column = embedded.getPersistentStateDesc("id2").getColumn();
        assertEquals("3", "bbb", column.getName().toLowerCase());
        assertEquals("4", "specialvaluedcustomer3", column.getTable().toLowerCase());
    }

    @Entity
    public static class Hoge {

        @Id
        private Long aaa;

        private Integer bbb;

        private java.util.Date ccc;
    }

    @Entity(name = "Foo")
    public static class Hoge2 {

        @Id
        public Long getAaa() {
            return null;
        }

        public Integer getBbb() {
            return null;
        }

        public java.util.Date getCcc() {
            return null;
        }
    }

    @Entity
    @Table(name = "Foo1")
    @SecondaryTable(name = "Foo2")
    @SecondaryTables( { @SecondaryTable(name = "Foo3") })
    public static class Hoge3 {

        @Id
        @Column(name = "Foo1aaa", table = "Foo1")
        private Long aaa;

        @Column(name = "Foo2bbb", table = "Foo2")
        private Integer bbb;

        @Column(name = "Foo3ccc", table = "Foo3")
        private java.util.Date ccc;
    }

    @Entity
    @Inheritance(strategy = JOINED)
    public static class Hoge4 {
        @Id
        private Long aaa;

        private Integer bbb;
    }

    @Entity
    public static class Hoge5 extends Hoge4 {
        private String ccc;
    }

    @Entity
    public static class Hoge6 {
        @EmbeddedId
        private PK pk;
    }

    @Entity
    @IdClass(PK.class)
    public static class Hoge7 {
        @Id
        private String aaa;

        @Id
        private String bbb;
    }

    public static class PK {

        private String aaa;

        private String bbb;
    }

    @Entity
    @Table(name = "CUST")
    @Inheritance(strategy = JOINED)
    public static class Customer {
        @Id
        protected Long id;

        protected String name;
    }

    @Entity
    @Table(name = "CUST2")
    @Inheritance(strategy = JOINED)
    public static class Customer2 {
        @Id
        protected Long id;

        protected String name;
    }

    @Entity
    @Table(name = "CUST3")
    @Inheritance(strategy = JOINED)
    public static class Customer3 {
        @EmbeddedId
        protected Custosmer3PK id;

        protected String name;
    }

    public static class Custosmer3PK implements Serializable {
        private Long id1;

        private Long id2;
    }

    @Entity(name = "ValuedCustomer")
    public static class ValuedCustomer extends Customer {
        protected Integer rank;
    }

    @Entity(name = "ValuedCustomer2")
    @PrimaryKeyJoinColumn(name = "aaa", referencedColumnName = "id")
    public static class ValuedCustomer2 extends Customer2 {
        protected Integer rank;
    }

    @Entity(name = "ValuedCustomer3")
    @PrimaryKeyJoinColumns( { @PrimaryKeyJoinColumn(name = "aaa"),
            @PrimaryKeyJoinColumn(name = "bbb") })
    public static class ValuedCustomer3 extends Customer3 {
        protected Integer rank;
    }

    @Entity(name = "SpecialValuedCustomer")
    public static class SpecialValuedCustomer extends ValuedCustomer {
        protected Integer specialRank;
    }

    @Entity(name = "SpecialValuedCustomer2")
    @PrimaryKeyJoinColumn(name = "aaa", referencedColumnName = "id")
    public static class SpecialValuedCustomer2 extends ValuedCustomer2 {
        protected Integer specialRank;
    }

    @Entity(name = "SpecialValuedCustomer3")
    @PrimaryKeyJoinColumns( {
            @PrimaryKeyJoinColumn(name = "aaa", referencedColumnName = "id1"),
            @PrimaryKeyJoinColumn(name = "bbb", referencedColumnName = "id2") })
    public static class SpecialValuedCustomer3 extends ValuedCustomer3 {
        protected Integer specialRank;
    }
}
