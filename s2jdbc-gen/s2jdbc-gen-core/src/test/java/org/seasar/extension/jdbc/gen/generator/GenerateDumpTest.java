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
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.DumpModel;
import org.seasar.extension.jdbc.gen.GenerationContext;
import org.seasar.framework.util.TextUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GenerateDumpTest {

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
    public void test() throws Exception {
        DumpModel model = new DumpModel();
        model.setDelimiter(',');
        model.addColumnName("\"column1\"");
        model.addColumnName("\"column2\"");
        model.addColumnName("\"column3\"");
        model.addRow(Arrays.asList("\"hoge\"", "\"100\"", "\"200\""));
        model.addRow(Arrays.asList("\"foo\"", null, "\"2000\""));
        GenerationContext context = new GenerationContextImpl(model, new File(
                "dir"), new File("file"), "data/dump.ftl", "UTF-8", false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + ".txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

}
