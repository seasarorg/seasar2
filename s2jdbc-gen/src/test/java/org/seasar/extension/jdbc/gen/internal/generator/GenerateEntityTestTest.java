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

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.internal.model.EntityTestModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.EntityTestModel;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;
import org.seasar.framework.util.TextUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GenerateEntityTestTest {

    private EntityMetaFactoryImpl entityMetaFactory;

    private GeneratorImplStub generator;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        PersistenceConventionImpl pc = new PersistenceConventionImpl();
        ColumnMetaFactoryImpl cmf = new ColumnMetaFactoryImpl();
        cmf.setPersistenceConvention(pc);
        PropertyMetaFactoryImpl propertyMetaFactory = new PropertyMetaFactoryImpl();
        propertyMetaFactory.setPersistenceConvention(pc);
        propertyMetaFactory.setColumnMetaFactory(cmf);
        TableMetaFactoryImpl tmf = new TableMetaFactoryImpl();
        tmf.setPersistenceConvention(pc);
        entityMetaFactory = new EntityMetaFactoryImpl();
        entityMetaFactory.setPersistenceConvention(pc);
        entityMetaFactory.setPropertyMetaFactory(propertyMetaFactory);
        entityMetaFactory.setTableMetaFactory(tmf);
        generator = new GeneratorImplStub();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testCompositeId() throws Exception {
        EntityTestModelFactoryImpl entityTestModelFactory = new EntityTestModelFactoryImpl(
                "s2jdbc.dicon", "jdbcManager", "Test",
                new NamesModelFactoryImpl("hoge.entity", "Names"), false, false);
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Ccc.class);
        EntityTestModel model = entityTestModelFactory
                .getEntityTestModel(entityMeta);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entitytest.ftl", "UTF-8", false);
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
    public void testCompositeId_s2junit4() throws Exception {
        EntityTestModelFactoryImpl entityTestModelFactory = new EntityTestModelFactoryImpl(
                "s2jdbc.dicon", "jdbcManager", "Test",
                new NamesModelFactoryImpl("hoge.entity", "Names"), false, true);
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Ccc.class);
        EntityTestModel model = entityTestModelFactory
                .getEntityTestModel(entityMeta);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entitytest.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_CompositeId_s2junit4.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testNoId() throws Exception {
        EntityTestModelFactoryImpl entityTestModelFactory = new EntityTestModelFactoryImpl(
                "s2jdbc.dicon", "jdbcManager", "Test",
                new NamesModelFactoryImpl("hoge.entity", "Names"), false, false);
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Ddd.class);
        EntityTestModel model = entityTestModelFactory
                .getEntityTestModel(entityMeta);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entitytest.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/") + "_NoId.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testLeftOuterJoin() throws Exception {
        EntityTestModelFactoryImpl entityTestModelFactory = new EntityTestModelFactoryImpl(
                "s2jdbc.dicon", "jdbcManager", "Test",
                new NamesModelFactoryImpl("hoge.entity", "Names"), false, false);
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        EntityTestModel model = entityTestModelFactory
                .getEntityTestModel(entityMeta);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entitytest.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_LeftOuterJoin.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testLeftOuterJoin_useNamesClass() throws Exception {
        EntityTestModelFactoryImpl entityTestModelFactory = new EntityTestModelFactoryImpl(
                "s2jdbc.dicon", "jdbcManager", "Test",
                new NamesModelFactoryImpl("hoge.entity", "Names"), true, false);
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        EntityTestModel model = entityTestModelFactory
                .getEntityTestModel(entityMeta);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/entitytest.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_LeftOuterJoin_useNamesClass.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

}
