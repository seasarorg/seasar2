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
package org.seasar.extension.jdbc.gen.desc;

import javax.persistence.UniqueConstraint;

import org.seasar.extension.jdbc.gen.ColumnDesc;
import org.seasar.extension.jdbc.gen.UniqueKeyDesc;
import org.seasar.extension.jdbc.gen.UniqueKeyDescFactory;

/**
 * {@link UniqueKeyDescFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class UniqueKeyDescFactoryImpl implements UniqueKeyDescFactory {

    /**
     * インスタンスを構築します。
     */
    public UniqueKeyDescFactoryImpl() {
    }

    public UniqueKeyDesc getCompositeUniqueKeyDesc(UniqueConstraint uniqueConstraint) {
        UniqueKeyDesc uniqueKeyDesc = new UniqueKeyDesc();
        for (String columnName : uniqueConstraint.columnNames()) {
            uniqueKeyDesc.addColumnName(columnName);
        }
        if (uniqueKeyDesc.getColumnNameList().isEmpty()) {
            return null;
        }
        return uniqueKeyDesc;
    }

    public UniqueKeyDesc getSingleUniqueKeyDesc(ColumnDesc columnDesc) {
        UniqueKeyDesc uniqueKeyDesc = new UniqueKeyDesc();
        if (columnDesc.isUnique()) {
            uniqueKeyDesc.addColumnName(columnDesc.getName());
        }
        if (uniqueKeyDesc.getColumnNameList().isEmpty()) {
            return null;
        }
        return uniqueKeyDesc;
    }
}
