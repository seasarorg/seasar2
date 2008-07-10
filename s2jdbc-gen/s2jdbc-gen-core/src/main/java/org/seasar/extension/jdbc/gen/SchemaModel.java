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
package org.seasar.extension.jdbc.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author taedium
 * 
 */
public class SchemaModel {

    protected GenDialect dialect;

    protected List<TableDesc> tableDescList = new ArrayList<TableDesc>();

    protected List<SequenceDesc> sequenceDescList = new ArrayList<SequenceDesc>();

    public GenDialect getDialect() {
        return dialect;
    }

    public void setDialect(GenDialect dialect) {
        this.dialect = dialect;
    }

    public void addTableDesc(TableDesc tableDesc) {
        if (tableDescList.contains(tableDesc)) {
            return;
        }
        tableDescList.add(tableDesc);
    }

    public List<TableDesc> getTableDescList() {
        return Collections.unmodifiableList(tableDescList);
    }

    public void addSequenceDesc(SequenceDesc sequenceDesc) {
        if (sequenceDescList.contains(sequenceDesc)) {
            return;
        }
        sequenceDescList.add(sequenceDesc);
    }

    public List<SequenceDesc> getSequenceDescList() {
        return Collections.unmodifiableList(sequenceDescList);
    }

    public String getSequenceDefinitionFragment(SequenceDesc sequenceDesc) {
        return dialect.getSequenceDefinitionFragment(
                sequenceDesc.getDataType(), sequenceDesc.getInitialValue(),
                sequenceDesc.getAllocationSize());
    }

}
