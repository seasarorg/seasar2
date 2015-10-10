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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.internal.model.SqlFileTestModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.SqlFileTestModel;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.TextUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GenerateSqlFileTestTest {

    private GeneratorImplStub generator;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        generator = new GeneratorImplStub();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testSqlFileSet() throws Exception {
        File classpathDir = ResourceUtil.getBuildDir(getClass());
        String basePath = getClass().getPackage().getName().replace(".", "/");
        Set<File> sqlFileSet = new HashSet<File>();
        sqlFileSet.add(ResourceUtil.getResourceAsFile(basePath + "/aaa.sql"));
        sqlFileSet.add(ResourceUtil.getResourceAsFile(basePath + "/bbb.sql"));
        SqlFileTestModelFactoryImpl sqlFileTestModelFactory = new SqlFileTestModelFactoryImpl(
                classpathDir, sqlFileSet, "s2jdbc.dicon", "jdbcManager",
                "hoge", "SqlFileTest", false);
        SqlFileTestModel model = sqlFileTestModelFactory.getSqlFileTestModel();

        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/sqlfiletest.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_SqlFileSet.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testSqlFileSet_s2junit4() throws Exception {
        File classpathDir = ResourceUtil.getBuildDir(getClass());
        String basePath = getClass().getPackage().getName().replace(".", "/");
        Set<File> sqlFileSet = new HashSet<File>();
        sqlFileSet.add(ResourceUtil.getResourceAsFile(basePath + "/aaa.sql"));
        sqlFileSet.add(ResourceUtil.getResourceAsFile(basePath + "/bbb.sql"));
        SqlFileTestModelFactoryImpl sqlFileTestModelFactory = new SqlFileTestModelFactoryImpl(
                classpathDir, sqlFileSet, "s2jdbc.dicon", "jdbcManager",
                "hoge", "SqlFileTest", true);
        SqlFileTestModel model = sqlFileTestModelFactory.getSqlFileTestModel();

        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/sqlfiletest.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/")
                + "_SqlFileSet_s2junit4.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testNoSqlFile() throws Exception {
        File classpathDir = ResourceUtil.getBuildDir(getClass());
        SqlFileTestModelFactoryImpl sqlFileTestModelFactory = new SqlFileTestModelFactoryImpl(
                classpathDir, Collections.<File> emptySet(), "s2jdbc.dicon",
                "jdbcManager", "hoge", "SqlFileTest", false);
        SqlFileTestModel model = sqlFileTestModelFactory.getSqlFileTestModel();

        GenerationContext context = new GenerationContextImpl(model, new File(
                "file"), "java/sqlfiletest.ftl", "UTF-8", false);
        generator.generate(context);

        String path = getClass().getName().replace(".", "/") + "_NoSqlFile.txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

}
