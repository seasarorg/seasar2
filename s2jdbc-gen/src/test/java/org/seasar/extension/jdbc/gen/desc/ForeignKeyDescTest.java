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
package org.seasar.extension.jdbc.gen.desc;

import junitx.framework.Assert;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class ForeignKeyDescTest {

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testEquals() throws Exception {
        ForeignKeyDesc foreignKeyDesc = new ForeignKeyDesc();
        foreignKeyDesc.addColumnName("AAA");
        foreignKeyDesc.setReferencedCatalogName("BBB");
        foreignKeyDesc.setReferencedSchemaName("CCC");
        foreignKeyDesc.setReferencedTableName("DDD");
        foreignKeyDesc.addReferencedColumnName("EEE");

        ForeignKeyDesc foreignKeyDesc2 = new ForeignKeyDesc();
        foreignKeyDesc2.addColumnName("aaa");
        foreignKeyDesc2.setReferencedCatalogName("bbb");
        foreignKeyDesc2.setReferencedSchemaName("ccc");
        foreignKeyDesc2.setReferencedTableName("ddd");
        foreignKeyDesc2.addReferencedColumnName("eee");

        ForeignKeyDesc foreignKeyDesc3 = new ForeignKeyDesc();
        foreignKeyDesc3.addColumnName("XXX");

        assertEquals(foreignKeyDesc, foreignKeyDesc2);
        assertEquals(foreignKeyDesc.hashCode(), foreignKeyDesc2.hashCode());
        Assert.assertNotEquals(foreignKeyDesc, foreignKeyDesc3);
        Assert.assertNotEquals(foreignKeyDesc.hashCode(), foreignKeyDesc3
                .hashCode());
    }

}
