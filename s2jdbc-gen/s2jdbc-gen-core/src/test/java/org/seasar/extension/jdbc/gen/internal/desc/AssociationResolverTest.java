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
package org.seasar.extension.jdbc.gen.internal.desc;

import org.junit.Test;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.desc.EntitySetDesc;
import org.seasar.extension.jdbc.gen.meta.DbForeignKeyMeta;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class AssociationResolverTest {

    @Test
    public void test() throws Exception {
        EntityDesc entityDesc = new EntityDesc();
        entityDesc.setCatalogName("AAA");
        entityDesc.setSchemaName("BBB");
        entityDesc.setTableName("CCC");
        entityDesc.setFullTableName("AAA.BBB.CCC");

        EntityDesc entityDesc2 = new EntityDesc();
        entityDesc2.setCatalogName("DDD");
        entityDesc2.setSchemaName("EEE");
        entityDesc2.setTableName("FFF");
        entityDesc2.setFullTableName("DDD.EEE.FFF");

        EntitySetDesc entitySetDesc = new EntitySetDesc();
        entitySetDesc.addEntityDesc(entityDesc);
        entitySetDesc.addEntityDesc(entityDesc2);

        DbForeignKeyMeta fkMeta = new DbForeignKeyMeta();
        fkMeta.setPrimaryKeyCatalogName("AAA");
        fkMeta.setPrimaryKeySchemaName("BBB");
        fkMeta.setPrimaryKeyTableName("CCC");
        fkMeta.addPrimaryKeyColumnName("111");
        fkMeta.addPrimaryKeyColumnName("222");
        fkMeta.setForeignKeyCatalogName("DDD");
        fkMeta.setForeignKeySchemaName("EEE");
        fkMeta.setForeignKeyTableName("FFF");
        fkMeta.addForeignKeyColumnName("111");
        fkMeta.addForeignKeyColumnName("222");

        AssociationResolver resolver = new AssociationResolver(entitySetDesc);
        resolver.resolveRelationship(fkMeta);

        assertEquals(0, entityDesc.getAssociationDescList().size());
        assertEquals(1, entityDesc.getInverseAssociationDescList().size());
        assertEquals(1, entityDesc2.getAssociationDescList().size());
        assertEquals(0, entityDesc2.getInverseAssociationDescList().size());

    }
}
