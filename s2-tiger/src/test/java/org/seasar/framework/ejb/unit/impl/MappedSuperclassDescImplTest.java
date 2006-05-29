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
import org.seasar.framework.ejb.unit.ToOneRelationshipStateDesc;

public class MappedSuperclassDescImplTest extends TestCase {

    public void testOverrideAttribute() {
        HashMap<String, PersistentColumn> overrides = new HashMap<String, PersistentColumn>();
        overrides.put("aaa", new PersistentColumn("111", "222"));
        MappedSuperclassDescImpl mapped = new MappedSuperclassDescImpl(
                Foo1.class, "PrimaryTable", false);
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
        MappedSuperclassDescImpl mapped = new MappedSuperclassDescImpl(
                Foo1.class, "PrimaryTable", false);
        mapped.overrideAssociations(overrides);
        ToOneRelationshipStateDesc toOne = (ToOneRelationshipStateDesc) mapped
                .getPersistentStateDesc("bbb");
        toOne.setupForeignKeys(new EntityClassDescImpl(Xxx.class));
        PersistentColumn fkColumn = toOne.getForeignKeys().get(0).getColumn();

        assertEquals("1", "111", fkColumn.getName());
        assertEquals("2", "222", fkColumn.getTable());
    }

    public void testOverrideAttributeForParent() {
        Foo2 entity = new Foo2();
        EntityIntrospector i = new EntityIntrospector(entity, true,
                DefaultProxiedObjectResolver.INSTANCE);
        PersistentClassDesc classDesc = i.getEntityClassDesc(entity);
        PersistentStateDesc stateDesc = classDesc.getPersistentStateDesc(
                Foo1.class, "aaa");
        assertEquals("1", "111", stateDesc.getColumn().getName());
    }

    public void testOverrideAsstociationForParent() {
        Foo2 entity = new Foo2();
        EntityIntrospector i = new EntityIntrospector(entity, true,
                DefaultProxiedObjectResolver.INSTANCE);
        PersistentClassDesc classDesc = i.getEntityClassDesc(entity);
        ToOneRelationshipStateDesc toOne = (ToOneRelationshipStateDesc) classDesc
                .getPersistentStateDesc(Foo1.class, "bbb");
        PersistentColumn fkColumn = toOne.getForeignKeys().get(0).getColumn();

        assertEquals("1", "222", fkColumn.getName());
        assertEquals("2", "foo2", fkColumn.getTable().toLowerCase());
    }

    public void testOverrideAttributeForParentExcludingNonEntity() {
        Bar3 entity = new Bar3();
        EntityIntrospector i = new EntityIntrospector(entity, true,
                DefaultProxiedObjectResolver.INSTANCE);
        PersistentClassDesc classDesc = i.getEntityClassDesc(entity);
        PersistentStateDesc stateDesc = classDesc.getPersistentStateDesc(
                Bar1.class, "aaa");
        assertEquals("1", "111", stateDesc.getColumn().getName());
    }

    public void testOverrideAsstociationForParentExcludingNonEntity() {
        Bar3 entity = new Bar3();
        EntityIntrospector i = new EntityIntrospector(entity, true,
                DefaultProxiedObjectResolver.INSTANCE);
        PersistentClassDesc classDesc = i.getEntityClassDesc(entity);
        ToOneRelationshipStateDesc toOne = (ToOneRelationshipStateDesc) classDesc
                .getPersistentStateDesc(Bar1.class, "bbb");
        PersistentColumn fkColumn = toOne.getForeignKeys().get(0).getColumn();

        assertEquals("1", "222", fkColumn.getName());
        assertEquals("2", "bar3", fkColumn.getTable().toLowerCase());
    }

    public void testOverrideAttributeForGrandParent() {
        Hoge3 entity = new Hoge3();
        EntityIntrospector i = new EntityIntrospector(entity, true,
                DefaultProxiedObjectResolver.INSTANCE);
        PersistentClassDesc classDesc = i.getEntityClassDesc(entity);
        PersistentStateDesc stateDesc = classDesc.getPersistentStateDesc(
                Hoge1.class, "aaa");
        assertEquals("1", "111", stateDesc.getColumn().getName());
    }

    public void testOverrideAsstociationForGrandParent() {
        Hoge3 entity = new Hoge3();
        EntityIntrospector i = new EntityIntrospector(entity, true,
                DefaultProxiedObjectResolver.INSTANCE);
        PersistentClassDesc classDesc = i.getEntityClassDesc(entity);
        ToOneRelationshipStateDesc toOne = (ToOneRelationshipStateDesc) classDesc
                .getPersistentStateDesc(Hoge1.class, "bbb");
        PersistentColumn fkColumn = toOne.getForeignKeys().get(0).getColumn();

        assertEquals("1", "222", fkColumn.getName());
        assertEquals("2", "hoge3", fkColumn.getTable().toLowerCase());
    }

    public void testOverrideAttributeInTheMiddleOfHierarchy() {
        Foo4 entity = new Foo4();
        EntityIntrospector i = new EntityIntrospector(entity, true,
                DefaultProxiedObjectResolver.INSTANCE);
        PersistentClassDesc classDesc = i.getEntityClassDesc(entity);
        PersistentStateDesc stateDesc = classDesc.getPersistentStateDesc(
                Foo3.class, "ddd");
        assertEquals("1", "333", stateDesc.getColumn().getName());
    }

    public void testOverrideAssociationInTheMiddleOfHierarchy() {
        Foo4 entity = new Foo4();
        EntityIntrospector i = new EntityIntrospector(entity, true,
                DefaultProxiedObjectResolver.INSTANCE);
        PersistentClassDesc classDesc = i.getEntityClassDesc(entity);
        ToOneRelationshipStateDesc toOne = (ToOneRelationshipStateDesc) classDesc
                .getPersistentStateDesc(Foo3.class, "eee");
        PersistentColumn fkColumn = toOne.getForeignKeys().get(0).getColumn();

        assertEquals("1", "444", fkColumn.getName());
        assertEquals("2", "foo4", fkColumn.getTable().toLowerCase());
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
