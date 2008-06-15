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

import javax.persistence.TemporalType;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.desc.AttributeDesc;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.model.EntityBaseModel;
import org.seasar.extension.jdbc.gen.model.EntityConditionBaseModel;
import org.seasar.extension.jdbc.gen.model.EntityConditionModel;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.model.factory.EntityBaseModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.factory.EntityConditionBaseModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.factory.EntityConditionModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.factory.EntityModelFactoryImpl;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.TextUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GeneratorImplTest {

    private Writer writer = new StringWriter();

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerate() throws Exception {
        String packageName = ClassUtil.getPackageName(getClass());
        File file = ResourceUtil.getResourceAsFile(packageName
                .replace('.', '/'));
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setDirectoryForTemplateLoading(file);
        GeneratorImpl generator = new GeneratorImplStub(configuration);

        GenerationContext context = new GenerationContext();
        context.setDir(new File("dir"));
        context.setFile(new File("file"));
        context.setEncoding("UTF-8");
        context.setModel(new MyModel("hoge"));
        context.setTemplateName(getClass().getSimpleName() + "_hoge.ftl");

        generator.generate(context);
        assertEquals("hoge", writer.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerate_s2jdbcEntity() throws Exception {
        File file = ResourceUtil.getResourceAsFile("templates");
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setDirectoryForTemplateLoading(file);
        GeneratorImpl generator = new GeneratorImplStub(configuration);

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
        entityDesc.setName("Foo");
        entityDesc.addAttribute(id);
        entityDesc.addAttribute(name);
        entityDesc.addAttribute(lob);
        entityDesc.addAttribute(date);
        entityDesc.addAttribute(temp);
        entityDesc.addAttribute(version);

        EntityModelFactoryImpl factory = new EntityModelFactoryImpl();
        EntityModel model = factory.getEntityModel(entityDesc, "hoge.Foo",
                "bar.AbstractFoo");

        GenerationContext context = new GenerationContext();
        context.setDir(new File("dir"));
        context.setFile(new File("file"));
        context.setEncoding("UTF-8");
        context.setModel(model);
        context.setTemplateName("s2jdbc-entity.ftl");

        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_s2jdbc-entity.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerate_s2jdbcEntityBase() throws Exception {
        File file = ResourceUtil.getResourceAsFile("templates");
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setDirectoryForTemplateLoading(file);
        GeneratorImpl generator = new GeneratorImplStub(configuration);

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
        entityDesc.setName("Foo");
        entityDesc.addAttribute(id);
        entityDesc.addAttribute(name);
        entityDesc.addAttribute(lob);
        entityDesc.addAttribute(date);
        entityDesc.addAttribute(temp);
        entityDesc.addAttribute(version);

        EntityBaseModelFactoryImpl factory = new EntityBaseModelFactoryImpl();
        EntityBaseModel model = factory.getEntityBaseModel(entityDesc,
                "bar.AbstractFoo");

        GenerationContext context = new GenerationContext();
        context.setDir(new File("dir"));
        context.setFile(new File("file"));
        context.setEncoding("UTF-8");
        context.setModel(model);
        context.setTemplateName("s2jdbc-entityBase.ftl");

        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_s2jdbc-entityBase.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerate_s2jdbcEntityBase_compositeId() throws Exception {
        File file = ResourceUtil.getResourceAsFile("templates");
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setDirectoryForTemplateLoading(file);
        GeneratorImpl generator = new GeneratorImplStub(configuration);

        AttributeDesc id1 = new AttributeDesc();
        id1.setName("id1");
        id1.setId(true);
        id1.setAttributeClass(int.class);

        AttributeDesc id2 = new AttributeDesc();
        id2.setName("id2");
        id2.setId(true);
        id2.setAttributeClass(int.class);

        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setName("Foo");
        entityDesc.addAttribute(id1);
        entityDesc.addAttribute(id2);

        EntityBaseModelFactoryImpl factory = new EntityBaseModelFactoryImpl();
        EntityBaseModel model = factory.getEntityBaseModel(entityDesc,
                "bar.AbstractFoo");

        GenerationContext context = new GenerationContext();
        context.setDir(new File("dir"));
        context.setFile(new File("file"));
        context.setEncoding("UTF-8");
        context.setModel(model);
        context.setTemplateName("s2jdbc-entityBase.ftl");

        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_s2jdbc-entityBase_compositeId.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerate_s2jdbcEntityCondition() throws Exception {
        File file = ResourceUtil.getResourceAsFile("templates");
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setDirectoryForTemplateLoading(file);
        GeneratorImpl generator = new GeneratorImplStub(configuration);

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

        EntityConditionModelFactoryImpl factory = new EntityConditionModelFactoryImpl();
        EntityConditionModel model = factory.getEntityConditionModel(
                entityDesc, "hoge.FooCondition", "bar.AbstractFooCondition");

        GenerationContext context = new GenerationContext();
        context.setDir(new File("dir"));
        context.setFile(new File("file"));
        context.setEncoding("UTF-8");
        context.setModel(model);
        context.setTemplateName("s2jdbc-entityCondition.ftl");

        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_s2jdbc-entityCondition.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerate_s2jdbcEntityConditionBase() throws Exception {
        File file = ResourceUtil.getResourceAsFile("templates");
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setDirectoryForTemplateLoading(file);
        GeneratorImpl generator = new GeneratorImplStub(configuration);

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

        EntityConditionBaseModelFactoryImpl factory = new EntityConditionBaseModelFactoryImpl();
        EntityConditionBaseModel model = factory.getEntityConditionBaseModel(
                entityDesc, "bar.AbstractFooCondition");

        GenerationContext context = new GenerationContext();
        context.setDir(new File("dir"));
        context.setFile(new File("file"));
        context.setEncoding("UTF-8");
        context.setModel(model);
        context.setTemplateName("s2jdbc-entityConditionBase.ftl");

        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_s2jdbc-entityConditionBase.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerate_jpaEntity() throws Exception {
        File file = ResourceUtil.getResourceAsFile("templates");
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setDirectoryForTemplateLoading(file);
        GeneratorImpl generator = new GeneratorImplStub(configuration);

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
        entityDesc.setName("Foo");
        entityDesc.addAttribute(id);
        entityDesc.addAttribute(name);
        entityDesc.addAttribute(lob);
        entityDesc.addAttribute(date);
        entityDesc.addAttribute(temp);
        entityDesc.addAttribute(version);

        EntityModelFactoryImpl factory = new EntityModelFactoryImpl();
        EntityModel model = factory.getEntityModel(entityDesc, "hoge.Foo",
                "bar.AbstractFoo");

        GenerationContext context = new GenerationContext();
        context.setDir(new File("dir"));
        context.setFile(new File("file"));
        context.setEncoding("UTF-8");
        context.setModel(model);
        context.setTemplateName("jpa-entity.ftl");

        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_jpa-entity.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerate_jpaEntityBase() throws Exception {
        File file = ResourceUtil.getResourceAsFile("templates");
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setDirectoryForTemplateLoading(file);
        GeneratorImpl generator = new GeneratorImplStub(configuration);

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
        entityDesc.setName("Foo");
        entityDesc.addAttribute(id);
        entityDesc.addAttribute(name);
        entityDesc.addAttribute(lob);
        entityDesc.addAttribute(date);
        entityDesc.addAttribute(temp);
        entityDesc.addAttribute(version);

        EntityBaseModelFactoryImpl factory = new EntityBaseModelFactoryImpl();
        EntityBaseModel model = factory.getEntityBaseModel(entityDesc,
                "bar.AbstractFoo");

        GenerationContext context = new GenerationContext();
        context.setDir(new File("dir"));
        context.setFile(new File("file"));
        context.setEncoding("UTF-8");
        context.setModel(model);
        context.setTemplateName("jpa-entityBase.ftl");

        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_jpa-entityBase.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
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
        public GeneratorImplStub(Configuration configuration) {
            super(configuration);
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
