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
package org.seasar.extension.jdbc.gen.internal.generator;

import java.io.File;
import java.math.BigDecimal;

import javax.persistence.TemporalType;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.internal.generator.GenerationContextImpl;
import org.seasar.extension.jdbc.gen.internal.model.AttributeModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.EntityModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.EntityModel;
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
        factory = new EntityModelFactoryImpl("hoge.entity",
                new AttributeModelFactoryImpl());
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
        date.setTemporalType(TemporalType.DATE);
        date.setAttributeClass(java.util.Date.class);
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
        entityDesc.setName("Foo");
        entityDesc.addAttribute(id);
        entityDesc.addAttribute(name);
        entityDesc.addAttribute(sal);
        entityDesc.addAttribute(lob);
        entityDesc.addAttribute(date);
        entityDesc.addAttribute(temp);
        entityDesc.addAttribute(version);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "dir"), new File("file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/") + "_SingleId.txt";
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
        id2.setColumnName("ID");
        id2.setColumnDefinition("integer");
        id2.setNullable(false);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setName("Foo");
        entityDesc.addAttribute(id1);
        entityDesc.addAttribute(id2);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "dir"), new File("file"), "java/entity.ftl", "UTF-8", false);
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
        entityDesc.setName("Foo");

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "dir"), new File("file"), "java/entity.ftl", "UTF-8", false);
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
    public void testNullColumnDefinition() throws Exception {
        AttributeDesc name = new AttributeDesc();
        name.setName("name");
        name.setAttributeClass(String.class);
        name.setColumnName("NAME");
        name.setColumnTypeName("hoge");
        name.setColumnDefinition(null);
        name.setNullable(false);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setName("Foo");
        entityDesc.addAttribute(name);

        EntityModel model = factory.getEntityModel(entityDesc);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "dir"), new File("file"), "java/entity.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_NullColumnDefinition.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
}
