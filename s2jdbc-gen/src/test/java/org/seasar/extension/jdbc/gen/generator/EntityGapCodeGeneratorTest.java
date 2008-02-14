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

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.model.PropertyModel;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.TextUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

import static junit.framework.Assert.*;

/**
 * @author taedium
 * 
 */
public class EntityGapCodeGeneratorTest {

    private Configuration config;

    @Before
    public void before() throws Exception {
        File file = ResourceUtil.getResourceAsFile("templates");
        config = new Configuration();
        config.setObjectWrapper(new DefaultObjectWrapper());
        config.setDirectoryForTemplateLoading(file);
    }

    @Test
    public void testGenerate() throws Exception {
        PropertyModel id = new PropertyModel();
        id.setName("id");
        id.setId(true);
        id.setPropertyClass(int.class);

        PropertyModel name = new PropertyModel();
        name.setName("name");
        name.setPropertyClass(String.class);

        PropertyModel lob = new PropertyModel();
        lob.setName("lob");
        lob.setLob(true);
        lob.setPropertyClass(byte[].class);

        PropertyModel date = new PropertyModel();
        date.setName("date");
        date.setTemporalType(TemporalType.DATE);
        date.setPropertyClass(java.util.Date.class);

        PropertyModel temp = new PropertyModel();
        temp.setName("temp");
        temp.setTrnsient(true);
        temp.setPropertyClass(String.class);

        PropertyModel version = new PropertyModel();
        version.setName("version");
        version.setVersion(true);
        version.setPropertyClass(Integer.class);

        EntityModel entityModel = new EntityModel();
        entityModel.setName("Hoge");
        entityModel.addPropertyModel(id);
        entityModel.addPropertyModel(name);
        entityModel.addPropertyModel(lob);
        entityModel.addPropertyModel(date);
        entityModel.addPropertyModel(temp);
        entityModel.addPropertyModel(version);

        final Writer writer = new StringWriter();
        EntityGapCodeGenerator generator = new EntityGapCodeGenerator(
                entityModel, "bar.Hoge", "foo.AbstractHoge",
                "entityGapCode.ftl", config, "UTF-8", new File("dest")) {

            @Override
            public Writer openWriter() {
                return writer;
            }
        };

        generator.generate();
        String path = EntityGapCodeGeneratorTest.class.getName().replace(".",
                "/")
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

        EntityGapCodeGenerator generator = new EntityGapCodeGenerator(
                entityModel, "bar.Hoge", "foo.AbstractHoge",
                "entityGapCode.ftl", config, "UTF-8", new File("dest"));
        Set<String> importNames = generator.getImports();
        assertEquals(4, importNames.size());
        assertTrue(importNames.contains(MappedSuperclass.class.getName()));
        assertTrue(importNames.contains(Temporal.class.getName()));
        assertTrue(importNames.contains(TemporalType.class.getName()));
        assertTrue(importNames.contains(java.util.Date.class.getName()));
    }
}
