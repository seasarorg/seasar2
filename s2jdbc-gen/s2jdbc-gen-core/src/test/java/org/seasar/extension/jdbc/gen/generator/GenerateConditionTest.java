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
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.ConditionModel;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.extension.jdbc.gen.model.ConditionAttributeModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.ConditionMethodModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.ConditionModelFactoryImpl;
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
        ConditionAttributeModelFactoryImpl camf = new ConditionAttributeModelFactoryImpl();
        ConditionMethodModelFactoryImpl cmmf = new ConditionMethodModelFactoryImpl(
                "Condition");
        conditionModelfactory = new ConditionModelFactoryImpl(camf, cmmf);
        generator = new GeneratorImplStub("UTF-8");
    }

    @Test
    public void testManyToOne() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Foo.class);
        ConditionModel model = conditionModelfactory.getConditionModel(
                entityMeta, "hoge.condition.FooCondition");
        GenerationContext context = new GenerationContext(model,
                new File("dir"), new File("file"), "java/condition.ftl",
                "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_ManyToOne.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    @Test
    public void testOneToOne() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Bar.class);
        ConditionModel model = conditionModelfactory.getConditionModel(
                entityMeta, "hoge.condition.BarCondition");
        GenerationContext context = new GenerationContext(model,
                new File("dir"), new File("file"), "java/condition.ftl",
                "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + "_OneToMany.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    @Entity
    public static class Foo {

        @Id
        @Column(nullable = false)
        public Integer id;

        @Column(nullable = false)
        public String name;

        @Lob
        @Column(nullable = false)
        public byte[] lob;

        @Temporal(TemporalType.DATE)
        public Date date;

        @Transient
        public String temp;

        @Version
        @Column(nullable = false)
        public Integer version;

        @Column(nullable = false)
        public Integer barId;

        @ManyToOne
        public Bar bar;
    }

    @Entity
    public static class Bar {

        @Id
        @Column(nullable = false)
        public Integer id;

        @Column(nullable = true)
        public String name;

        @OneToMany(mappedBy = "bar")
        public List<Foo> foos;
    }
}
