/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.generator;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.TemporalType;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.AttributeDesc;
import org.seasar.extension.jdbc.gen.ColumnDesc;
import org.seasar.extension.jdbc.gen.EntityDesc;
import org.seasar.extension.jdbc.gen.ForeignKeyDesc;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.PrimaryKeyDesc;
import org.seasar.extension.jdbc.gen.SequenceDesc;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.extension.jdbc.gen.UniqueKeyDesc;
import org.seasar.extension.jdbc.gen.dialect.HsqlGenDialect;
import org.seasar.extension.jdbc.gen.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.model.ConditionBaseModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.ConditionModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.EntityBaseModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.EntityModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.SchemaModelFactoryImpl;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.TextUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GeneratorImplTest {

    private GeneratorImpl generator;

    private Writer writer;

    @Before
    public void before() throws Exception {
        File dir = ResourceUtil.getResourceAsFile("templates");
        generator = new GeneratorImplStub("UTF-8", dir);
        writer = new StringWriter();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerate() throws Exception {
        String packageName = ClassUtil.getPackageName(getClass());
        File dir = ResourceUtil
                .getResourceAsFile(packageName.replace('.', '/'));
        GeneratorImpl generator = new GeneratorImplStub("UTF-8", dir);

        GenerationContext context = new GenerationContext(new MyModel("hoge"),
                new File("dir"), new File("file"), getClass().getSimpleName()
                        + "_hoge.ftl", "UTF-8", false);
        generator.generate(context);
        assertEquals("hoge", writer.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerate_entity() throws Exception {
        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setAttributeClass(int.class);

        AttributeDesc name = new AttributeDesc();
        name.setName("name");
        name.setAttributeClass(String.class);

        AttributeDesc lob = new AttributeDesc();
        lob.setName("lob");
        lob.setLob(true);
        lob.setAttributeClass(byte[].class);

        AttributeDesc date = new AttributeDesc();
        date.setName("date");
        date.setTemporalType(TemporalType.DATE);
        date.setAttributeClass(java.util.Date.class);

        AttributeDesc temp = new AttributeDesc();
        temp.setName("temp");
        temp.setTransient(true);
        temp.setAttributeClass(String.class);

        AttributeDesc version = new AttributeDesc();
        version.setName("version");
        version.setVersion(true);
        version.setAttributeClass(Integer.class);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("FOO");
        entityDesc.setName("Foo");
        entityDesc.addAttribute(id);
        entityDesc.addAttribute(name);
        entityDesc.addAttribute(lob);
        entityDesc.addAttribute(date);
        entityDesc.addAttribute(temp);
        entityDesc.addAttribute(version);

        EntityModelFactoryImpl factory = new EntityModelFactoryImpl();
        Object model = factory.getEntityModel(entityDesc, "hoge.Foo",
                "bar.AbstractFoo");

        GenerationContext context = new GenerationContext(model,
                new File("dir"), new File("file"), "entity.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_entity.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerate_entityBase() throws Exception {
        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setAttributeClass(int.class);
        id.setColumnName("ID");
        id.setPrecision(10);
        id.setScale(0);
        id.setNullable(false);

        AttributeDesc name = new AttributeDesc();
        name.setName("name");
        name.setAttributeClass(String.class);
        name.setColumnName("NAME");
        name.setLength(20);
        name.setNullable(false);

        AttributeDesc sal = new AttributeDesc();
        sal.setName("sal");
        sal.setAttributeClass(BigDecimal.class);
        sal.setColumnName("SAL");
        sal.setPrecision(15);
        sal.setScale(5);
        sal.setNullable(false);

        AttributeDesc lob = new AttributeDesc();
        lob.setName("lob");
        lob.setLob(true);
        lob.setAttributeClass(byte[].class);
        lob.setColumnName("LOB");
        lob.setLength(10);
        lob.setNullable(true);

        AttributeDesc date = new AttributeDesc();
        date.setName("date");
        date.setTemporalType(TemporalType.DATE);
        date.setAttributeClass(java.util.Date.class);
        date.setColumnName("DATE");
        date.setLength(10);
        date.setNullable(true);

        AttributeDesc temp = new AttributeDesc();
        temp.setName("temp");
        temp.setTransient(true);
        temp.setAttributeClass(String.class);
        temp.setColumnName("TEMP");
        temp.setLength(10);
        temp.setNullable(false);

        AttributeDesc version = new AttributeDesc();
        version.setName("version");
        version.setVersion(true);
        version.setAttributeClass(Integer.class);
        version.setColumnName("VERSION");
        version.setPrecision(10);
        version.setScale(0);
        version.setNullable(false);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setName("Foo");
        entityDesc.addAttribute(id);
        entityDesc.addAttribute(name);
        entityDesc.addAttribute(sal);
        entityDesc.addAttribute(lob);
        entityDesc.addAttribute(date);
        entityDesc.addAttribute(temp);
        entityDesc.addAttribute(version);

        EntityBaseModelFactoryImpl factory = new EntityBaseModelFactoryImpl();
        Object model = factory
                .getEntityBaseModel(entityDesc, "bar.AbstractFoo");

        GenerationContext context = new GenerationContext(model,
                new File("dir"), new File("file"), "entity-base.ftl", "UTF-8",
                false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_entity-base.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerate_entityBase_compositeId() throws Exception {
        AttributeDesc id1 = new AttributeDesc();
        id1.setName("id1");
        id1.setId(true);
        id1.setAttributeClass(int.class);
        id1.setColumnName("ID1");
        id1.setPrecision(10);
        id1.setScale(0);
        id1.setNullable(false);

        AttributeDesc id2 = new AttributeDesc();
        id2.setName("id2");
        id2.setId(true);
        id2.setAttributeClass(int.class);
        id2.setColumnName("ID");
        id2.setPrecision(20);
        id2.setScale(0);
        id2.setNullable(false);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setName("Foo");
        entityDesc.addAttribute(id1);
        entityDesc.addAttribute(id2);

        EntityBaseModelFactoryImpl factory = new EntityBaseModelFactoryImpl();
        Object model = factory
                .getEntityBaseModel(entityDesc, "bar.AbstractFoo");

        GenerationContext context = new GenerationContext(model,
                new File("dir"), new File("file"), "entity-base.ftl", "UTF-8",
                false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_entity-base_compositeId.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerate_condition() throws Exception {
        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setAttributeClass(int.class);

        AttributeDesc name = new AttributeDesc();
        name.setName("name");
        name.setAttributeClass(String.class);

        AttributeDesc lob = new AttributeDesc();
        lob.setName("lob");
        lob.setLob(true);
        lob.setAttributeClass(byte[].class);

        AttributeDesc date = new AttributeDesc();
        date.setName("date");
        date.setTemporalType(TemporalType.DATE);
        date.setAttributeClass(java.util.Date.class);
        date.setNullable(true);

        AttributeDesc temp = new AttributeDesc();
        temp.setName("temp");
        temp.setTransient(true);
        temp.setAttributeClass(String.class);
        temp.setNullable(true);

        AttributeDesc version = new AttributeDesc();
        version.setName("version");
        version.setVersion(true);
        version.setAttributeClass(Integer.class);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setName("Foo");
        entityDesc.addAttribute(id);
        entityDesc.addAttribute(name);
        entityDesc.addAttribute(lob);
        entityDesc.addAttribute(date);
        entityDesc.addAttribute(temp);
        entityDesc.addAttribute(version);

        ConditionModelFactoryImpl factory = new ConditionModelFactoryImpl();
        Object model = factory.getConditionModel(entityDesc,
                "hoge.FooCondition", "bar.AbstractFooCondition");

        GenerationContext context = new GenerationContext(model,
                new File("dir"), new File("file"), "condition.ftl", "UTF-8",
                false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_condition.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerate_conditionBase() throws Exception {
        AttributeDesc id = new AttributeDesc();
        id.setName("id");
        id.setId(true);
        id.setAttributeClass(int.class);

        AttributeDesc name = new AttributeDesc();
        name.setName("name");
        name.setAttributeClass(String.class);

        AttributeDesc lob = new AttributeDesc();
        lob.setName("lob");
        lob.setLob(true);
        lob.setAttributeClass(byte[].class);

        AttributeDesc date = new AttributeDesc();
        date.setName("date");
        date.setTemporalType(TemporalType.DATE);
        date.setAttributeClass(java.util.Date.class);
        date.setNullable(true);

        AttributeDesc temp = new AttributeDesc();
        temp.setName("temp");
        temp.setTransient(true);
        temp.setAttributeClass(String.class);
        temp.setNullable(true);

        AttributeDesc version = new AttributeDesc();
        version.setName("version");
        version.setVersion(true);
        version.setAttributeClass(Integer.class);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setName("Foo");
        entityDesc.addAttribute(id);
        entityDesc.addAttribute(name);
        entityDesc.addAttribute(lob);
        entityDesc.addAttribute(date);
        entityDesc.addAttribute(temp);
        entityDesc.addAttribute(version);

        ConditionBaseModelFactoryImpl factory = new ConditionBaseModelFactoryImpl();
        Object model = factory.getConditionBaseModel(entityDesc,
                "bar.AbstractFooCondition");

        GenerationContext context = new GenerationContext(model,
                new File("dir"), new File("file"), "condition-base.ftl",
                "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_condition-base.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
    }

    @Test
    public void testGenerate_table() throws Exception {
        ColumnDesc no = new ColumnDesc();
        no.setName("no");
        no.setDefinition("integer");
        no.setNullable(false);
        no.setUnique(true);

        ColumnDesc name = new ColumnDesc();
        name.setName("name");
        name.setDefinition("varchar");
        name.setNullable(true);
        name.setUnique(false);

        TableDesc tableDesc = new TableDesc();
        tableDesc.setCatalogName("AAA");
        tableDesc.setSchemaName("BBB");
        tableDesc.setName("HOGE");
        tableDesc.addColumnDesc(no);
        tableDesc.addColumnDesc(name);

        TableDesc tableDesc2 = new TableDesc();
        tableDesc2.setCatalogName("AAA");
        tableDesc2.setSchemaName("BBB");
        tableDesc2.setName("FOO");
        tableDesc2.addColumnDesc(no);
        tableDesc2.addColumnDesc(name);

        SchemaModelFactoryImpl factory = new SchemaModelFactoryImpl(
                new StandardGenDialect());
        Object model = factory.getSchemaModel(Arrays.asList(tableDesc,
                tableDesc2));

        {
            GenerationContext context = new GenerationContext(model, new File(
                    "dir"), new File("file"), "create-table.ftl", "UTF-8",
                    false);
            generator.generate(context);
            String path = getClass().getName().replace(".", "/")
                    + "_create-table.txt";
            assertEquals(TextUtil.readUTF8(path), writer.toString());
        }
        writer = new StringWriter();
        {
            GenerationContext context = new GenerationContext(model, new File(
                    "dir"), new File("file"), "drop-table.ftl", "UTF-8", false);
            generator.generate(context);
            String path = getClass().getName().replace(".", "/")
                    + "_drop-table.txt";
            assertEquals(TextUtil.readUTF8(path), writer.toString());
        }
    }

    @Test
    public void testGenerate_sequence() throws Exception {
        SequenceDesc sequenceDesc = new SequenceDesc();
        sequenceDesc.setSequenceName("HOGE");
        sequenceDesc.setInitialValue(1);
        sequenceDesc.setAllocationSize(50);
        sequenceDesc.setDataType("integer");

        SequenceDesc sequenceDesc2 = new SequenceDesc();
        sequenceDesc2.setSequenceName("FOO");
        sequenceDesc2.setInitialValue(10);
        sequenceDesc2.setAllocationSize(20);
        sequenceDesc2.setDataType("integer");

        TableDesc tableDesc = new TableDesc();
        tableDesc.addSequenceDesc(sequenceDesc);

        TableDesc tableDesc2 = new TableDesc();
        tableDesc2.addSequenceDesc(sequenceDesc2);

        SchemaModelFactoryImpl factory = new SchemaModelFactoryImpl(
                new HsqlGenDialect());
        Object model = factory.getSchemaModel(Arrays.asList(tableDesc,
                tableDesc2));

        {
            GenerationContext context = new GenerationContext(model, new File(
                    "dir"), new File("file"), "create-sequence.ftl", "UTF-8",
                    false);
            generator.generate(context);
            String path = getClass().getName().replace(".", "/")
                    + "_create-sequence.txt";
            assertEquals(TextUtil.readUTF8(path), writer.toString());
        }
        writer = new StringWriter();
        {
            GenerationContext context = new GenerationContext(model, new File(
                    "dir"), new File("file"), "drop-sequence.ftl", "UTF-8",
                    false);
            generator.generate(context);
            String path = getClass().getName().replace(".", "/")
                    + "_drop-sequence.txt";
            assertEquals(TextUtil.readUTF8(path), writer.toString());
        }
    }

    @Test
    public void testGenerate_constraint() throws Exception {
        List<TableDesc> tableDescList = new ArrayList<TableDesc>();
        {
            PrimaryKeyDesc primaryKeyDesc = new PrimaryKeyDesc();
            primaryKeyDesc.addColumnName("PK1");
            primaryKeyDesc.addColumnName("PK2");

            UniqueKeyDesc uniqueKeyDesc = new UniqueKeyDesc();
            uniqueKeyDesc.addColumnName("UK1-1");
            uniqueKeyDesc.addColumnName("UK1-2");

            UniqueKeyDesc uniqueKeyDesc2 = new UniqueKeyDesc();
            uniqueKeyDesc2.addColumnName("UK2-1");
            uniqueKeyDesc2.addColumnName("UK2-2");

            ForeignKeyDesc foreignKeyDesc = new ForeignKeyDesc();
            foreignKeyDesc.addColumnName("FK1-1");
            foreignKeyDesc.addColumnName("FK1-2");
            foreignKeyDesc.setReferencedCatalogName("CCC");
            foreignKeyDesc.setReferencedSchemaName("DDD");
            foreignKeyDesc.setReferencedTableName("FOO");
            foreignKeyDesc.addReferencedColumnName("REF1-1");
            foreignKeyDesc.addReferencedColumnName("REF1-2");

            ForeignKeyDesc foreignKeyDesc2 = new ForeignKeyDesc();
            foreignKeyDesc2.addColumnName("FK2-1");
            foreignKeyDesc2.addColumnName("FK2-2");
            foreignKeyDesc2.setReferencedCatalogName("EEE");
            foreignKeyDesc2.setReferencedSchemaName("FFF");
            foreignKeyDesc2.setReferencedTableName("BAR");
            foreignKeyDesc2.addReferencedColumnName("REF2-1");
            foreignKeyDesc2.addReferencedColumnName("REF2-2");

            TableDesc tableDesc = new TableDesc();
            tableDesc.setCatalogName("AAA");
            tableDesc.setSchemaName("BBB");
            tableDesc.setName("HOGE");
            tableDesc.setPrimaryKeyDesc(primaryKeyDesc);
            tableDesc.addUniqueKeyDesc(uniqueKeyDesc);
            tableDesc.addUniqueKeyDesc(uniqueKeyDesc2);
            tableDesc.addForeignKeyDesc(foreignKeyDesc);
            tableDesc.addForeignKeyDesc(foreignKeyDesc2);

            tableDescList.add(tableDesc);
        }
        {
            PrimaryKeyDesc primaryKeyDesc = new PrimaryKeyDesc();
            primaryKeyDesc.addColumnName("PK1");
            primaryKeyDesc.addColumnName("PK2");

            UniqueKeyDesc uniqueKeyDesc = new UniqueKeyDesc();
            uniqueKeyDesc.addColumnName("UK1-1");
            uniqueKeyDesc.addColumnName("UK1-2");

            UniqueKeyDesc uniqueKeyDesc2 = new UniqueKeyDesc();
            uniqueKeyDesc2.addColumnName("UK2-1");
            uniqueKeyDesc2.addColumnName("UK2-2");

            ForeignKeyDesc foreignKeyDesc = new ForeignKeyDesc();
            foreignKeyDesc.addColumnName("FK1-1");
            foreignKeyDesc.addColumnName("FK1-2");
            foreignKeyDesc.setReferencedCatalogName("CCC");
            foreignKeyDesc.setReferencedSchemaName("DDD");
            foreignKeyDesc.setReferencedTableName("FOO");
            foreignKeyDesc.addReferencedColumnName("REF1-1");
            foreignKeyDesc.addReferencedColumnName("REF1-2");

            ForeignKeyDesc foreignKeyDesc2 = new ForeignKeyDesc();
            foreignKeyDesc2.addColumnName("FK2-1");
            foreignKeyDesc2.addColumnName("FK2-2");
            foreignKeyDesc2.setReferencedCatalogName("EEE");
            foreignKeyDesc2.setReferencedSchemaName("FFF");
            foreignKeyDesc2.setReferencedTableName("BAR");
            foreignKeyDesc2.addReferencedColumnName("REF2-1");
            foreignKeyDesc2.addReferencedColumnName("REF2-2");

            TableDesc tableDesc = new TableDesc();
            tableDesc.setCatalogName("AAA");
            tableDesc.setSchemaName("BBB");
            tableDesc.setName("FOO");
            tableDesc.setPrimaryKeyDesc(primaryKeyDesc);
            tableDesc.addUniqueKeyDesc(uniqueKeyDesc);
            tableDesc.addUniqueKeyDesc(uniqueKeyDesc2);
            tableDesc.addForeignKeyDesc(foreignKeyDesc);
            tableDesc.addForeignKeyDesc(foreignKeyDesc2);

            tableDescList.add(tableDesc);
        }

        SchemaModelFactoryImpl factory = new SchemaModelFactoryImpl(
                new HsqlGenDialect());
        Object model = factory.getSchemaModel(tableDescList);

        {
            GenerationContext context = new GenerationContext(model, new File(
                    "dir"), new File("file"), "create-constraint.ftl", "UTF-8",
                    false);
            generator.generate(context);
            String path = getClass().getName().replace(".", "/")
                    + "_create-constraint.txt";
            assertEquals(TextUtil.readUTF8(path), writer.toString());
        }
        writer = new StringWriter();
        {
            GenerationContext context = new GenerationContext(model, new File(
                    "dir"), new File("file"), "drop-constraint.ftl", "UTF-8",
                    false);
            generator.generate(context);
            String path = getClass().getName().replace(".", "/")
                    + "_drop-constraint.txt";
            assertEquals(TextUtil.readUTF8(path), writer.toString());
        }
    }

    /**
     * 
     * @author taedium
     * 
     */
    public class GeneratorImplStub extends GeneratorImpl {

        /**
         * 
         * @param configuration
         */
        public GeneratorImplStub(String templateFielEncoding, File templateDir) {
            super(templateFielEncoding, templateDir);
        }

        @Override
        protected boolean exists(File file) {
            return false;
        }

        @Override
        protected void mkdirs(File dir) {
        }

        @Override
        protected Writer openWriter(GenerationContext context) {
            return writer;
        }
    }

    /**
     * 
     * @author taedium
     */
    public static class MyModel {

        private String name;

        /**
         * 
         * @param name
         */
        public MyModel(String name) {
            this.name = name;
        }

        /**
         * 
         * @return
         */
        public String getName() {
            return name;
        }
    }
}
