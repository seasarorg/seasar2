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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import junit.framework.TestCase;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentJoinColumn;
import org.seasar.framework.ejb.unit.PersistentStateDesc;

public class MappedSuperclassDescTest extends TestCase {

    public void testOverrideAttribute() {
        HashMap<String, PersistentColumn> overrides = new HashMap<String, PersistentColumn>();
        overrides.put("aaa", new PersistentColumn("111", "222"));
        MappedSuperclassDesc mapped = new MappedSuperclassDesc(Foo1.class,
                "PrimaryTable", false);
        mapped.overrideAttributes(overrides);
        PersistentStateDesc stateDesc = mapped.getPersistentStateDesc("aaa");
        PersistentColumn column = stateDesc.getColumn();

        assertEquals("1", "111", column.getName());
        assertEquals("2", "222", column.getTable());
    }

    public void testOverrideAssociation() {
        HashMap<String, List<PersistentJoinColumn>> overrides = new HashMap<String, List<PersistentJoinColumn>>();
        List<PersistentJoinColumn> joinColumns = new ArrayList<PersistentJoinColumn>();
        joinColumns.add(new PersistentJoinColumn("111", "222", "id"));
        overrides.put("bbb", joinColumns);
        MappedSuperclassDesc mapped = new MappedSuperclassDesc(Foo1.class,
                "PrimaryTable", false);
        mapped.overrideAssociations(overrides);
        PersistentStateDesc stateDesc = mapped.getPersistentStateDesc("bbb");
        stateDesc.setupForeignKeyColumns(new EntityClassDesc(Xxx.class));
        List<PersistentJoinColumn> fkColumns = stateDesc.getForeignKeyColumns();
        PersistentJoinColumn fkColumn = fkColumns.get(0);

        assertEquals("1", "111", fkColumn.getName());
        assertEquals("2", "222", fkColumn.getTable());
        assertEquals("3", "id", fkColumn.getReferencedColumnName());
    }

    public void testOverrideAttributeForParent() {
        EntityIntrospector i = new EntityIntrospector(new Foo2(), null);
        PersistentClassDesc classDesc = i.getPersistentClassDesc(Foo2.class);
        PersistentStateDesc stateDesc = classDesc.getPersistentStateDesc(
                Foo1.class, "aaa");
        assertEquals("1", "111", stateDesc.getColumn().getName());
    }

    public void testOverrideAsstociationForParent() {
        EntityIntrospector i = new EntityIntrospector(new Foo2(), null);
        PersistentClassDesc classDesc = i.getPersistentClassDesc(Foo2.class);
        PersistentStateDesc stateDesc = classDesc.getPersistentStateDesc(
                Foo1.class, "bbb");
        List<PersistentJoinColumn> fkColumns = stateDesc.getForeignKeyColumns();
        PersistentJoinColumn fkColumn = fkColumns.get(0);

        assertEquals("1", "222", fkColumn.getName());
        assertEquals("2", "foo2", fkColumn.getTable().toLowerCase());
        assertEquals("3", "id", fkColumn.getReferencedColumnName());
    }

    public void testOverrideAttributeForParentExcludingNonEntity() {
        EntityIntrospector i = new EntityIntrospector(new Bar3(), null);
        PersistentClassDesc classDesc = i.getPersistentClassDesc(Bar3.class);
        PersistentStateDesc stateDesc = classDesc.getPersistentStateDesc(
                Bar1.class, "aaa");
        assertEquals("1", "111", stateDesc.getColumn().getName());
    }

    public void testOverrideAsstociationForParentExcludingNonEntity() {
        EntityIntrospector i = new EntityIntrospector(new Bar3(), null);
        PersistentClassDesc classDesc = i.getPersistentClassDesc(Bar3.class);
        PersistentStateDesc stateDesc = classDesc.getPersistentStateDesc(
                Bar1.class, "bbb");
        List<PersistentJoinColumn> fkColumns = stateDesc.getForeignKeyColumns();
        PersistentJoinColumn fkColumn = fkColumns.get(0);

        assertEquals("1", "222", fkColumn.getName());
        assertEquals("2", "bar3", fkColumn.getTable().toLowerCase());
        assertEquals("3", "id", fkColumn.getReferencedColumnName());
    }

    public void testOverrideAttributeForGrandParent() {
        EntityIntrospector i = new EntityIntrospector(new Hoge3(), null);
        PersistentClassDesc classDesc = i.getPersistentClassDesc(Hoge3.class);
        PersistentStateDesc stateDesc = classDesc.getPersistentStateDesc(
                Hoge1.class, "aaa");
        assertEquals("1", "111", stateDesc.getColumn().getName());
    }

    public void testOverrideAsstociationForGrandParent() {
        EntityIntrospector i = new EntityIntrospector(new Hoge3(), null);
        PersistentClassDesc classDesc = i.getPersistentClassDesc(Hoge3.class);
        PersistentStateDesc stateDesc = classDesc.getPersistentStateDesc(
                Hoge1.class, "bbb");
        List<PersistentJoinColumn> fkColumns = stateDesc.getForeignKeyColumns();
        PersistentJoinColumn fkColumn = fkColumns.get(0);

        assertEquals("1", "222", fkColumn.getName());
        assertEquals("2", "hoge3", fkColumn.getTable().toLowerCase());
        assertEquals("3", "id", fkColumn.getReferencedColumnName());
    }

    public void testOverrideAttributeInTheMiddleOfHierarchy() {
        EntityIntrospector i = new EntityIntrospector(new Foo4(), null);
        PersistentClassDesc classDesc = i.getPersistentClassDesc(Foo4.class);
        PersistentStateDesc stateDesc = classDesc.getPersistentStateDesc(
                Foo3.class, "ddd");
        assertEquals("1", "333", stateDesc.getColumn().getName());
    }

    public void testOverrideAssociationInTheMiddleOfHierarchy() {
        EntityIntrospector i = new EntityIntrospector(new Foo4(), null);
        PersistentClassDesc classDesc = i.getPersistentClassDesc(Foo4.class);
        PersistentStateDesc stateDesc = classDesc.getPersistentStateDesc(
                Foo3.class, "eee");
        List<PersistentJoinColumn> fkColumns = stateDesc.getForeignKeyColumns();
        PersistentJoinColumn fkColumn = fkColumns.get(0);

        assertEquals("1", "444", fkColumn.getName());
        assertEquals("2", "foo4", fkColumn.getTable().toLowerCase());
        assertEquals("3", "id", fkColumn.getReferencedColumnName());
    }

    @MappedSuperclass
    public static class Hoge1 {
        private String aaa;

        @ManyToOne
        private Xxx bbb;
    }

    @MappedSuperclass
    public static class Hoge2 extends Hoge1 {
        private String ccc;

        @ManyToOne
        private Xxx ddd;
    }

    @Entity(name = "Hoge3")
    @AttributeOverride(name = "aaa", column = @Column(name = "111"))
    @AssociationOverride(name = "bbb", joinColumns = { @JoinColumn(name = "222") })
    public static class Hoge3 extends Hoge2 {
        @Id
        private String eee;
    }

    @MappedSuperclass
    public static class Foo1 {
        private String aaa;

        @ManyToOne
        private Xxx bbb;
    }

    @Entity(name = "Foo2")
    @AttributeOverride(name = "aaa", column = @Column(name = "111"))
    @AssociationOverride(name = "bbb", joinColumns = { @JoinColumn(name = "222") })
    public static class Foo2 extends Foo1 {
        @Id
        private String ccc;
    }

    @MappedSuperclass
    public static class Foo3 extends Foo2 {
        private String ddd;

        @ManyToOne
        private Xxx eee;
    }

    @Entity(name = "Foo4")
    @AttributeOverride(name = "ddd", column = @Column(name = "333"))
    @AssociationOverride(name = "eee", joinColumns = { @JoinColumn(name = "444") })
    public static class Foo4 extends Foo3 {
        @Id
        private String ddd;
    }

    @MappedSuperclass
    public static class Bar1 {
        private String aaa;

        @ManyToOne
        private Xxx bbb;
    }

    public static class Bar2 extends Bar1 {
        private String ccc;
    }

    @Entity(name = "Bar3")
    @AttributeOverride(name = "aaa", column = @Column(name = "111"))
    @AssociationOverride(name = "bbb", joinColumns = { @JoinColumn(name = "222") })
    public static class Bar3 extends Bar2 {
        @Id
        private String ddd;
    }

    @Entity
    public static class Xxx {
        @Id
        private String id;
    }

}
