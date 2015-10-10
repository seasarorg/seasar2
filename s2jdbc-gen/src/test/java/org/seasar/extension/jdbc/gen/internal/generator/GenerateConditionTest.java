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
import org.seasar.extension.jdbc.gen.internal.model.ConditionAssociationModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.ConditionAttributeModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.ConditionModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.ConditionModel;
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
public class GenerateConditionTest {

    private EntityMetaFactoryImpl entityMetaFactory;

    private ConditionModelFactoryImpl conditionModelfactory;

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
        ConditionAttributeModelFactoryImpl attrFactory = new ConditionAttributeModelFactoryImpl();
        ConditionAssociationModelFactoryImpl assoFactory = new ConditionAssociationModelFactoryImpl(
                "Condition");
        conditionModelfactory = new ConditionModelFactoryImpl(attrFactory,
                assoFactory, "hoge.condition", "Condition");
        generator = new GeneratorImplStub();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testManyToOne() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        ConditionModel model = conditionModelfactory
                .getConditionModel(entityMeta);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/condition.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_ManyToOne.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testOneToOne() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Bbb.class);
        ConditionModel model = conditionModelfactory
                .getConditionModel(entityMeta);
        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/condition.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_OneToMany.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }
}
