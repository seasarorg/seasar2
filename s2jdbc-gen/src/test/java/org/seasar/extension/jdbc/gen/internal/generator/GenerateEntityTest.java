/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.generator;

import java.io.File;
import java.math.BigDecimal;

import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.desc.CompositeUniqueConstraintDesc;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.desc.EntitySetDesc;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.internal.desc.AssociationResolver;
import org.seasar.extension.jdbc.gen.internal.desc.PluralFormDictinary;
import org.seasar.extension.jdbc.gen.internal.model.AssociationModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.AttributeModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.CompositeUniqueConstraintModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.EntityModelFactoryImpl;
import org.seasar.extension.jdbc.gen.meta.DbForeignKeyMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.TextUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GenerateEntityTest {

    private EntityModelFactoryImpl factory;

    private GeneratorImplStub generator;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        factory = new EntityModelFactoryImpl("hoge.entity", null,
                new AttributeModelFactoryImpl(false, true, true,
                        new PersistenceConventionImpl()),
                new AssociationModelFactoryImpl(false),
                new CompositeUniqueConstraintModelFactoryImpl(), false, false,
                true, true, false);
        generator = new GeneratorImplStub();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testSingleId() throws Exception {
        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setGenerationType(GenerationType.SEQUENCE);
        id.setInitialValue(100);
        id.setAllocationSize(50);
        id.setAttributeClass(int.class);
        id.setColumnName("ID");
        id.setColumnDefinition("integer");
        id.setNullable(false);

        AttributeDesc name = new AttributeDesc();
        name.setName("name");
        name.setAttributeClass(String.class);
        name.setColumnName("NAME");
        name.setColumnDefinition("varchar(10)");
        name.setNullable(false);
        name.setUnique(true);

        AttributeDesc sal = new AttributeDesc();
        sal.setName("sal");
        sal.setAttributeClass(BigDecimal.class);
        sal.setColumnName("SAL");
        sal.setColumnDefinition("decimal(15,5)");
        sal.setNullable(false);

        AttributeDesc lob = new AttributeDesc();
        lob.setName("lob");
        lob.setLob(true);
        lob.setAttributeClass(byte[].class);
        lob.setColumnName("LOB");
        lob.setColumnDefinition("blob");
        lob.setNullable(true);

        AttributeDesc date = new AttributeDesc();
        date.setName("date");
        date.setAttributeClass(java.sql.Date.class);
        date.setColumnName("DATE");
        date.setColumnDefinition("date");
        date.setNullable(true);

        AttributeDesc temp = new AttributeDesc();
        temp.setName("temp");
        temp.setTransient(true);
        temp.setAttributeClass(String.class);
        temp.setColumnName("TEMP");
        temp.setNullable(false);

        AttributeDesc version = new AttributeDesc();
        version.setName("version");
        version.setVersion(true);
        version.setAttributeClass(Integer.class);
        version.setColumnName("VERSION");
        version.setColumnDefinition("integer");
        version.setNullable(false);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");
        entityDesc.addAttributeDesc(id);
        entityDesc.addAttributeDesc(name);
        entityDesc.addAttributeDesc(sal);
        entityDesc.addAttributeDesc(lob);
        entityDesc.addAttributeDesc(date);
        entityDesc.addAttributeDesc(temp);
        entityDesc.addAttributeDesc(version);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/") + "_SingleId.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testSingleIdString() throws Exception {
        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setAttributeClass(String.class);
        id.setColumnName("ID");
        id.setColumnDefinition("varchar");
        id.setNullable(false);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");
        entityDesc.addAttributeDesc(id);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_SingleIdString.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCompositeId() throws Exception {
        AttributeDesc id1 = new AttributeDesc();
        id1.setName("id1");
        id1.setId(true);
        id1.setAttributeClass(int.class);
        id1.setColumnName("ID1");
        id1.setColumnDefinition("integer");
        id1.setNullable(false);

        AttributeDesc id2 = new AttributeDesc();
        id2.setName("id2");
        id2.setId(true);
        id2.setAttributeClass(int.class);
        id2.setColumnName("ID2");
        id2.setColumnDefinition("integer");
        id2.setNullable(false);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");
        entityDesc.addAttributeDesc(id1);
        entityDesc.addAttributeDesc(id2);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_CompositeId.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCopyright() throws Exception {
        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        String packageName = ClassUtil.getPackageName(getClass());
        File dir = ResourceUtil
                .getResourceAsFile(packageName.replace('.', '/'));
        GeneratorImplStub generator = new GeneratorImplStub("UTF-8", dir);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/") + "_Copyright.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testLib() throws Exception {
        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        String packageName = ClassUtil.getPackageName(getClass());
        File dir = ResourceUtil
                .getResourceAsFile(packageName.replace('.', '/'));
        GeneratorImplStub generator = new GeneratorImplStub("UTF-8", new File(
                dir, "lib"));
        generator.generate(context);

        String path = getClass().getName().replace(".", "/") + "_Lib.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testUnsupportedColumnType() throws Exception {
        AttributeDesc name = new AttributeDesc();
        name.setName("name");
        name.setAttributeClass(String.class);
        name.setColumnName("NAME");
        name.setColumnTypeName("hoge");
        name.setUnsupportedColumnType(true);
        name.setNullable(false);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");
        entityDesc.addAttributeDesc(name);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_UnsupportedColumnType.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testSingleIdAssociation() throws Exception {
        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");

        EntityDesc entityDesc2 = new EntityDesc();
        entityDesc2.setCatalogName("AAA");
        entityDesc2.setSchemaName("BBB");
        entityDesc2.setTableName("HOGE");
        entityDesc2.setName("Hoge");

        EntitySetDesc entitySetDesc = new EntitySetDesc();
        entitySetDesc.addEntityDesc(entityDesc);
        entitySetDesc.addEntityDesc(entityDesc2);

        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setCatalogName("AAA");
        tableMeta.setSchemaName("BBB");
        tableMeta.setName("FOO");

        DbForeignKeyMeta fkMeta = new DbForeignKeyMeta();
        fkMeta.setPrimaryKeyCatalogName("AAA");
        fkMeta.setPrimaryKeySchemaName("BBB");
        fkMeta.setPrimaryKeyTableName("HOGE");
        fkMeta.addPrimaryKeyColumnName("PK");
        fkMeta.addForeignKeyColumnName("HOGE_ID");

        AssociationResolver resolver = new AssociationResolver(entitySetDesc,
                new PluralFormDictinary(), new PersistenceConventionImpl());
        resolver.resolve(tableMeta, fkMeta);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_SingleId_Association_Foo.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
        generator.clear();

        model = factory.getEntityModel(entityDesc2);
        context = new GenerationContextImpl(model, new File("file"),
                "java/entity.ftl", "UTF-8", false);
        generator.generate(context);
        path = getClass().getName().replace(".", "/")
                + "_SingleId_Association_Hoge.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testSingleIdAssociationDefaultMapping() throws Exception {
        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");

        EntityDesc entityDesc2 = new EntityDesc();
        entityDesc2.setCatalogName("AAA");
        entityDesc2.setSchemaName("BBB");
        entityDesc2.setTableName("HOGE");
        entityDesc2.setName("Hoge");

        EntitySetDesc entitySetDesc = new EntitySetDesc();
        entitySetDesc.addEntityDesc(entityDesc);
        entitySetDesc.addEntityDesc(entityDesc2);

        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setCatalogName("AAA");
        tableMeta.setSchemaName("BBB");
        tableMeta.setName("FOO");

        DbForeignKeyMeta fkMeta = new DbForeignKeyMeta();
        fkMeta.setPrimaryKeyCatalogName("AAA");
        fkMeta.setPrimaryKeySchemaName("BBB");
        fkMeta.setPrimaryKeyTableName("HOGE");
        fkMeta.addPrimaryKeyColumnName("ID");
        fkMeta.addForeignKeyColumnName("HOGE_ID");

        AssociationResolver resolver = new AssociationResolver(entitySetDesc,
                new PluralFormDictinary(), new PersistenceConventionImpl());
        resolver.resolve(tableMeta, fkMeta);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_SingleId_Association_DefaultMapping_Foo.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
        generator.clear();

        model = factory.getEntityModel(entityDesc2);
        context = new GenerationContextImpl(model, new File("file"),
                "java/entity.ftl", "UTF-8", false);
        generator.generate(context);
        path = getClass().getName().replace(".", "/")
                + "_SingleId_Association_DefaultMapping_Hoge.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCompositeIdAssociation() throws Exception {
        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");

        EntityDesc entityDesc2 = new EntityDesc();
        entityDesc2.setCatalogName("AAA");
        entityDesc2.setSchemaName("BBB");
        entityDesc2.setTableName("HOGE");
        entityDesc2.setName("Hoge");

        EntitySetDesc entitySetDesc = new EntitySetDesc();
        entitySetDesc.addEntityDesc(entityDesc);
        entitySetDesc.addEntityDesc(entityDesc2);

        DbTableMeta tableMeta = new DbTableMeta();
        tableMeta.setCatalogName("AAA");
        tableMeta.setSchemaName("BBB");
        tableMeta.setName("FOO");

        DbForeignKeyMeta fkMeta = new DbForeignKeyMeta();
        fkMeta.setPrimaryKeyCatalogName("AAA");
        fkMeta.setPrimaryKeySchemaName("BBB");
        fkMeta.setPrimaryKeyTableName("HOGE");
        fkMeta.addPrimaryKeyColumnName("ID1");
        fkMeta.addPrimaryKeyColumnName("ID2");
        fkMeta.addForeignKeyColumnName("HOGE_ID1");
        fkMeta.addForeignKeyColumnName("HOGE_ID2");

        AssociationResolver resolver = new AssociationResolver(entitySetDesc,
                new PluralFormDictinary(), new PersistenceConventionImpl());
        resolver.resolve(tableMeta, fkMeta);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_CompositeId_Association_Foo.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
        generator.clear();

        model = factory.getEntityModel(entityDesc2);
        context = new GenerationContextImpl(model, new File("file"),
                "java/entity.ftl", "UTF-8", false);
        generator.generate(context);
        path = getClass().getName().replace(".", "/")
                + "_CompositeId_Association_Hoge.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testUniqueConstraint() throws Exception {
        CompositeUniqueConstraintDesc uniqueConstraintDesc1 = new CompositeUniqueConstraintDesc();
        uniqueConstraintDesc1.addColumnName("CCC");
        uniqueConstraintDesc1.addColumnName("DDD");

        CompositeUniqueConstraintDesc uniqueConstraintDesc2 = new CompositeUniqueConstraintDesc();
        uniqueConstraintDesc2.addColumnName("EEE");
        uniqueConstraintDesc2.addColumnName("FFF");

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");
        entityDesc.addCompositeUniqueConstraintDesc(uniqueConstraintDesc1);
        entityDesc.addCompositeUniqueConstraintDesc(uniqueConstraintDesc2);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_UniqueConstraint.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testSuperclass() throws Exception {
        factory = new EntityModelFactoryImpl("hoge.entity", Eee.class,
                new AttributeModelFactoryImpl(false, true, true,
                        new PersistenceConventionImpl()),
                new AssociationModelFactoryImpl(false),
                new CompositeUniqueConstraintModelFactoryImpl(), false, false,
                true, true, false);

        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setGenerationType(GenerationType.SEQUENCE);
        id.setInitialValue(100);
        id.setAllocationSize(50);
        id.setAttributeClass(int.class);
        id.setColumnName("ID");
        id.setColumnDefinition("integer");
        id.setNullable(false);

        AttributeDesc name = new AttributeDesc();
        name.setName("name");
        name.setAttributeClass(String.class);
        name.setColumnName("NAME");
        name.setColumnDefinition("varchar(10)");
        name.setNullable(false);
        name.setUnique(true);

        AttributeDesc sal = new AttributeDesc();
        sal.setName("sal");
        sal.setAttributeClass(BigDecimal.class);
        sal.setColumnName("SAL");
        sal.setColumnDefinition("decimal(15,5)");
        sal.setNullable(false);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");
        entityDesc.addAttributeDesc(id);
        entityDesc.addAttributeDesc(name);
        entityDesc.addAttributeDesc(sal);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_Superclass.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testAccessor() throws Exception {
        factory = new EntityModelFactoryImpl("hoge.entity", null,
                new AttributeModelFactoryImpl(false, true, true,
                        new PersistenceConventionImpl()),
                new AssociationModelFactoryImpl(false),
                new CompositeUniqueConstraintModelFactoryImpl(), true, false,
                true, true, false);

        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setGenerationType(GenerationType.SEQUENCE);
        id.setInitialValue(100);
        id.setAllocationSize(50);
        id.setAttributeClass(int.class);
        id.setColumnName("ID");
        id.setColumnDefinition("integer");
        id.setNullable(false);

        AttributeDesc man = new AttributeDesc();
        man.setName("man");
        man.setAttributeClass(Boolean.class);
        man.setColumnName("man");
        man.setColumnDefinition("bool");
        man.setNullable(false);
        man.setUnique(true);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");
        entityDesc.addAttributeDesc(id);
        entityDesc.addAttributeDesc(man);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/") + "_Accessor.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testComment() throws Exception {
        factory = new EntityModelFactoryImpl("hoge.entity", null,
                new AttributeModelFactoryImpl(false, true, true,
                        new PersistenceConventionImpl()),
                new AssociationModelFactoryImpl(false),
                new CompositeUniqueConstraintModelFactoryImpl(), false, true,
                true, true, false);

        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setGenerationType(GenerationType.SEQUENCE);
        id.setInitialValue(100);
        id.setAllocationSize(50);
        id.setAttributeClass(int.class);
        id.setColumnName("ID");
        id.setColumnDefinition("integer");
        id.setNullable(false);
        id.setComment("識別子");

        AttributeDesc man = new AttributeDesc();
        man.setName("man");
        man.setAttributeClass(Boolean.class);
        man.setColumnName("man");
        man.setColumnDefinition("bool");
        man.setNullable(false);
        man.setUnique(true);
        man.setComment("性別を表す。男ならばtrue");

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");
        entityDesc.addAttributeDesc(id);
        entityDesc.addAttributeDesc(man);
        entityDesc.setComment("FOOテーブル");

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/") + "_Comment.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testTemporal() throws Exception {
        factory = new EntityModelFactoryImpl("hoge.entity", null,
                new AttributeModelFactoryImpl(false, true, false,
                        new PersistenceConventionImpl()),
                new AssociationModelFactoryImpl(false),
                new CompositeUniqueConstraintModelFactoryImpl(), false, true,
                true, true, false);

        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setAttributeClass(int.class);
        id.setColumnName("ID");
        id.setColumnDefinition("integer");
        id.setNullable(false);

        AttributeDesc date = new AttributeDesc();
        date.setName("date");
        date.setAttributeClass(java.sql.Date.class);
        date.setColumnName("DATE");
        date.setColumnDefinition("date");
        date.setNullable(true);

        AttributeDesc time = new AttributeDesc();
        time.setName("time");
        time.setAttributeClass(java.sql.Time.class);
        time.setColumnName("TIME");
        time.setColumnDefinition("time");
        time.setNullable(true);

        AttributeDesc timestamp = new AttributeDesc();
        timestamp.setName("timestamp");
        timestamp.setAttributeClass(java.sql.Timestamp.class);
        timestamp.setColumnName("TIMESTAMP");
        timestamp.setColumnDefinition("timestamp");
        timestamp.setNullable(true);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");
        entityDesc.addAttributeDesc(id);
        entityDesc.addAttributeDesc(date);
        entityDesc.addAttributeDesc(time);
        entityDesc.addAttributeDesc(timestamp);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/") + "_Temporal.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testTemporal_useTemporalType() throws Exception {
        factory = new EntityModelFactoryImpl("hoge.entity", null,
                new AttributeModelFactoryImpl(false, true, true,
                        new PersistenceConventionImpl()),
                new AssociationModelFactoryImpl(false),
                new CompositeUniqueConstraintModelFactoryImpl(), false, true,
                true, true, false);

        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setAttributeClass(int.class);
        id.setColumnName("ID");
        id.setColumnDefinition("integer");
        id.setNullable(false);

        AttributeDesc date = new AttributeDesc();
        date.setName("date");
        date.setAttributeClass(java.sql.Date.class);
        date.setColumnName("DATE");
        date.setColumnDefinition("date");
        date.setNullable(true);

        AttributeDesc time = new AttributeDesc();
        time.setName("time");
        time.setAttributeClass(java.sql.Time.class);
        time.setColumnName("TIME");
        time.setColumnDefinition("time");
        time.setNullable(true);

        AttributeDesc timestamp = new AttributeDesc();
        timestamp.setName("timestamp");
        timestamp.setAttributeClass(java.sql.Timestamp.class);
        timestamp.setColumnName("TIMESTAMP");
        timestamp.setColumnDefinition("timestamp");
        timestamp.setNullable(true);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");
        entityDesc.addAttributeDesc(id);
        entityDesc.addAttributeDesc(date);
        entityDesc.addAttributeDesc(time);
        entityDesc.addAttributeDesc(timestamp);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_Temporal_useTemporalType.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testTemporal_primaryTemporalType() throws Exception {
        factory = new EntityModelFactoryImpl("hoge.entity", null,
                new AttributeModelFactoryImpl(false, true, false,
                        new PersistenceConventionImpl()),
                new AssociationModelFactoryImpl(false),
                new CompositeUniqueConstraintModelFactoryImpl(), false, true,
                true, true, false);

        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setAttributeClass(int.class);
        id.setColumnName("ID");
        id.setColumnDefinition("integer");
        id.setNullable(false);

        AttributeDesc oracleDate = new AttributeDesc();
        oracleDate.setName("oracleDate");
        oracleDate.setAttributeClass(java.sql.Timestamp.class);
        oracleDate.setColumnName("ORACLE_DATE");
        oracleDate.setColumnDefinition("date");
        oracleDate.setNullable(true);
        oracleDate.setPrimaryTemporalType(TemporalType.TIMESTAMP);

        AttributeDesc timestamp = new AttributeDesc();
        timestamp.setName("timestamp");
        timestamp.setAttributeClass(java.sql.Timestamp.class);
        timestamp.setColumnName("TIMESTAMP");
        timestamp.setColumnDefinition("timestamp");
        timestamp.setNullable(true);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");
        entityDesc.addAttributeDesc(id);
        entityDesc.addAttributeDesc(oracleDate);
        entityDesc.addAttributeDesc(timestamp);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_Temporal_primaryTemporalType.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

}
