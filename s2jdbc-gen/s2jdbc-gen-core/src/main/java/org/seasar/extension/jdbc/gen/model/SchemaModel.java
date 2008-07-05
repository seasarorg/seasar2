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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seasar.extension.jdbc.gen.ForeignKeyDesc;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SequenceDesc;
import org.seasar.extension.jdbc.gen.TableDesc;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class SchemaModel {

    protected GenDialect dialect;

    protected List<TableDesc> tableDescList = new ArrayList<TableDesc>();

    public GenDialect getDialect() {
        return dialect;
    }

    public void setDialect(GenDialect dialect) {
        this.dialect = dialect;
    }

    public void addTableDesc(TableDesc tableDesc) {
        tableDescList.add(tableDesc);
    }

    public List<TableDesc> getTableDescList() {
        return Collections.unmodifiableList(tableDescList);
    }

    public String getBeginQuote() {
        return dialect.getOpenQuote();
    }

    public String getEndQuote() {
        return dialect.getCloseQuote();
    }

    public String getSequenceDefinitionFragment(SequenceDesc sequenceDesc) {
        return dialect.getSequenceDefinitionFragment(
                sequenceDesc.getDataType(), sequenceDesc.getInitialValue(),
                sequenceDesc.getAllocationSize());
    }

    public String getQuotedTableName(TableDesc tableDesc) {
        StringBuilder buf = new StringBuilder();
        if (!StringUtil.isEmpty(tableDesc.getCatalogName())) {
            buf.append(quote(tableDesc.getCatalogName()));
            buf.append(".");
        }
        if (!StringUtil.isEmpty(tableDesc.getSchemaName())) {
            buf.append(quote(tableDesc.getSchemaName()));
            buf.append(".");
        }
        buf.append(quote(tableDesc.getName()));
        return buf.toString();
    }

    public String getQuotedReferencedTableName(ForeignKeyDesc foreignKeyDesc) {
        StringBuilder buf = new StringBuilder();
        if (!StringUtil.isEmpty(foreignKeyDesc.getReferencedCatalogName())) {
            buf.append(quote(foreignKeyDesc.getReferencedCatalogName()));
            buf.append(".");
        }
        if (!StringUtil.isEmpty(foreignKeyDesc.getReferencedSchemaName())) {
            buf.append(quote(foreignKeyDesc.getReferencedSchemaName()));
            buf.append(".");
        }
        buf.append(quote(foreignKeyDesc.getReferencedTableName()));
        return buf.toString();
    }

    public String quote(String value) {
        return dialect.getOpenQuote() + value + dialect.getCloseQuote();
    }
}
