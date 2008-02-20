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
package org.seasar.extension.jdbc.gen.javacode;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.model.PropertyModel;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class EntityBaseCodeTest {

    @Test
    public void test() throws Exception {
        PropertyModel aaa = new PropertyModel();
        aaa.setName("aaa");
        aaa.setPropertyClass(BigDecimal.class);
        PropertyModel bbb = new PropertyModel();
        bbb.setName("bbb");
        bbb.setPropertyClass(String.class);
        EntityModel model = new EntityModel();
        model.addPropertyModel(aaa);
        model.addPropertyModel(bbb);

        EntityBaseCode code = new EntityBaseCode(model, "bar.AbstractFoo",
                "entityBaseCode.ftl");

        assertEquals(model, code.getEntityModel());
        assertEquals("bar", code.getPackageName());

        Set<String> imports = code.getImportPackageNames();
        assertEquals(2, imports.size());
        Iterator<String> it = imports.iterator();
        assertEquals("java.math.BigDecimal", it.next());
        assertEquals("javax.persistence.MappedSuperclass", it.next());

        assertEquals("AbstractFoo", code.getShortClassName());

        assertEquals("entityBaseCode.ftl", code.getTemplateName());
    }
}
