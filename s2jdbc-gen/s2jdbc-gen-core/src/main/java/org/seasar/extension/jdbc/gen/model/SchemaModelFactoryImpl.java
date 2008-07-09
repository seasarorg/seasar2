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
package org.seasar.extension.jdbc.gen.model;

import java.util.List;

import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SchemaModel;
import org.seasar.extension.jdbc.gen.SchemaModelFactory;
import org.seasar.extension.jdbc.gen.SequenceDesc;
import org.seasar.extension.jdbc.gen.TableDesc;

/**
 * @author taedium
 * 
 */
public class SchemaModelFactoryImpl implements SchemaModelFactory {

    protected GenDialect dialect;

    /**
     * @param dialect
     */
    public SchemaModelFactoryImpl(GenDialect dialect) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        this.dialect = dialect;
    }

    public SchemaModel getSchemaModel(List<TableDesc> tableDescList) {
        SchemaModel model = new SchemaModel();
        model.setDialect(dialect);
        for (TableDesc tableDesc : tableDescList) {
            model.addTableDesc(tableDesc);
            for (SequenceDesc sequenceDesc : tableDesc.getSequenceDescList()) {
                model.addSequenceDesc(sequenceDesc);
            }
            for (TableDesc idTableDesc : tableDesc.getIdTableDescList()) {
                model.addTableDesc(idTableDesc);
            }
        }
        return model;
    }

}
