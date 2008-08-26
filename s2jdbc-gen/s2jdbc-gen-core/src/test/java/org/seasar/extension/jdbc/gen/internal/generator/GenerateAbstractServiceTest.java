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

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.internal.model.AbstServiceModelFactoryImpl;
import org.seasar.extension.jdbc.gen.model.AbstServiceModel;
import org.seasar.framework.util.TextUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class GenerateAbstractServiceTest {

    private AbstServiceModelFactoryImpl abstServiceModelFactory;

    private GeneratorImplStub generator;

    /**
     * 
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        abstServiceModelFactory = new AbstServiceModelFactoryImpl(
                "hoge.service", "Service");
        generator = new GeneratorImplStub();
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        AbstServiceModel model = abstServiceModelFactory.getAbstServiceModel();
        GenerationContext context = new GenerationContextImpl(model, new File(
                "dir"), new File("file"), "java/abstract-service.ftl", "UTF-8",
                false);
        generator.generate(context);
        String path = getClass().getName().replace(".", "/") + ".txt";
        assertEquals(TextUtil.readUTF8(path), generator.getResult());
    }

}
