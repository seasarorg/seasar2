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
package org.seasar.extension.jdbc.gen.converter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.UniqueKeyModelConverter;
import org.seasar.extension.jdbc.gen.model.TableModel;
import org.seasar.extension.jdbc.gen.model.UniqueKeyModel;

/**
 * @author taedium
 * 
 */
public class UniqueKeyModelConverterImpl implements UniqueKeyModelConverter {

    public List<UniqueKeyModel> convert(EntityMeta entityMeta,
            TableModel tableModel) {
        List<UniqueKeyModel> uniqueKeyModelList = new ArrayList<UniqueKeyModel>();
        Class<?> entityClass = entityMeta.getEntityClass();
        Table table = entityClass.getAnnotation(Table.class);
        if (table != null) {
            for (UniqueConstraint uc : table.uniqueConstraints()) {
                if (uc.columnNames().length > 0) {
                    UniqueKeyModel uniqueKeyModel = new UniqueKeyModel();
                    doName(tableModel, uniqueKeyModel);
                    doColumnName(tableModel, uniqueKeyModel, uc);
                    uniqueKeyModelList.add(uniqueKeyModel);
                }
            }
        }
        return uniqueKeyModelList;
    }

    protected void doName(TableModel tableModel, UniqueKeyModel uniqueKeyModel) {
        String name = tableModel.getName() + "UQ"
                + tableModel.getUniqueKeyModelList().size();
        uniqueKeyModel.setName(name);
    }

    protected void doColumnName(TableModel tableModel,
            UniqueKeyModel uniqueKeyModel, UniqueConstraint uniqueConstraint) {
        for (String columnName : uniqueConstraint.columnNames()) {
            uniqueKeyModel.addColumnName(columnName);
        }
    }

}
