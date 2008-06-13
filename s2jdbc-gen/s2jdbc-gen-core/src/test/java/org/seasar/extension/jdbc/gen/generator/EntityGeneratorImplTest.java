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
import org.seasar.extension.jdbc.gen.factory.EntityBaseCodeFactoryImpl;
import org.seasar.extension.jdbc.gen.factory.EntityCodeFactoryImpl;
import org.seasar.extension.jdbc.gen.model.AttributeDesc;
import org.seasar.extension.jdbc.gen.model.EntityBaseCode;
import org.seasar.extension.jdbc.gen.model.EntityCode;
import org.seasar.extension.jdbc.gen.model.EntityDesc;
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
public class EntityGeneratorImplTest {

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

        EntityGeneratorImpl generator = new EntityGeneratorImpl(configuration) {

            @Override
            protected boolean exists(File file) {
                return false;
            }

            @Override
            protected void makeDirsIfNecessary(File dir) {
            }

            @Override
            protected Writer openWriter(GenerationContext context) {
                return writer;
            }
        };

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
    public void testGenerateEntity() throws Exception {
        File file = ResourceUtil.getResourceAsFile("templates");
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setDirectoryForTemplateLoading(file);

        EntityGeneratorImpl generator = new EntityGeneratorImpl(configuration) {

            @Override
            protected boolean exists(File file) {
                return false;
            }

            @Override
            protected void makeDirsIfNecessary(File dir) {
            }

            @Override
            protected Writer openWriter(GenerationContext context) {
                return writer;
            }
        };

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

        EntityCodeFactoryImpl factory = new EntityCodeFactoryImpl();
        EntityCode code = factory.getEntityCode(entityDesc, "hoge.Foo",
                "bar.AbstractFoo");

        GenerationContext context = new GenerationContext();
        context.setDir(new File("dir"));
        context.setFile(new File("file"));
        context.setEncoding("UTF-8");
        context.setModel(code);
        context.setTemplateName("entity.ftl");

        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_entity.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerateEntityBase() throws Exception {
        File file = ResourceUtil.getResourceAsFile("templates");
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setDirectoryForTemplateLoading(file);

        EntityGeneratorImpl generator = new EntityGeneratorImpl(configuration) {

            @Override
            protected boolean exists(File file) {
                return false;
            }

            @Override
            protected void makeDirsIfNecessary(File dir) {
            }

            @Override
            protected Writer openWriter(GenerationContext context) {
                return writer;
            }
        };

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

        EntityBaseCodeFactoryImpl factory = new EntityBaseCodeFactoryImpl();
        EntityBaseCode code = factory.getEntityBaseCode(entityDesc,
                "bar.AbstractFoo");

        GenerationContext context = new GenerationContext();
        context.setDir(new File("dir"));
        context.setFile(new File("file"));
        context.setEncoding("UTF-8");
        context.setModel(code);
        context.setTemplateName("entityBase.ftl");

        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_entityBase.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testGenerateEntityBase_compositeId() throws Exception {
        File file = ResourceUtil.getResourceAsFile("templates");
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setDirectoryForTemplateLoading(file);

        EntityGeneratorImpl generator = new EntityGeneratorImpl(configuration) {

            @Override
            protected boolean exists(File file) {
                return false;
            }

            @Override
            protected void makeDirsIfNecessary(File dir) {
            }

            @Override
            protected Writer openWriter(GenerationContext context) {
                return writer;
            }
        };

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

        EntityBaseCodeFactoryImpl factory = new EntityBaseCodeFactoryImpl();
        EntityBaseCode code = factory.getEntityBaseCode(entityDesc,
                "bar.AbstractFoo");

        GenerationContext context = new GenerationContext();
        context.setDir(new File("dir"));
        context.setFile(new File("file"));
        context.setEncoding("UTF-8");
        context.setModel(code);
        context.setTemplateName("entityBase.ftl");

        generator.generate(context);
        String path = getClass().getName().replace(".", "/")
                + "_entityBase_compositeId.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
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
