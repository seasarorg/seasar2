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
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.TemporalType;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.model.PropertyModel;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.TextUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import static junit.framework.Assert.*;

/**
 * @author taedium
 * 
 */
public class EntityCodeGeneratorTest {

    private EntityCodeGenerator generator;

    private Writer writer;

    @Before
    public void before() throws Exception {
        File file = ResourceUtil.getResourceAsFile("templates");
        Configuration cfg = new Configuration();
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setDirectoryForTemplateLoading(file);
        Template template = cfg.getTemplate("entityCode.ftl");
        writer = new StringWriter();
        generator = new EntityCodeGenerator("hoge", "foo", template, "UTF-8",
                new File("dest")) {

            @Override
            protected Writer openWriter(EntityModel entityModel) {
                return writer;
            }
        };
    }

    @Test
    public void testGenerate() throws Exception {
        PropertyModel id = new PropertyModel();
        id.setName("id");
        id.setId(true);
        id.setPropertyClass(int.class);

        EntityModel entityModel = new EntityModel();
        entityModel.setName("Hoge");
        entityModel.addPropertyModel(id);

        generator.generate(entityModel);
        String path = EntityCodeGeneratorTest.class.getName().replace(".", "/")
                + "_testGenerate.txt";
        assertEquals(TextUtil.readUTF8(path), writer.toString());
    }

    @Test
    public void testCreateImportNames() throws Exception {
        PropertyModel date = new PropertyModel();
        date.setName("date");
        date.setTemporalType(TemporalType.DATE);
        date.setPropertyClass(java.util.Date.class);

        EntityModel entityModel = new EntityModel();
        entityModel.setName("Hoge");
        entityModel.addPropertyModel(date);

        Set<String> importNames = generator.getImports(entityModel);
        assertEquals(2, importNames.size());
        assertTrue(importNames.contains(Entity.class.getName()));
        assertTrue(importNames.contains("foo.AbstractHoge"));
    }
}
