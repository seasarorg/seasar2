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

import java.sql.Types;
import java.util.List;

import org.seasar.extension.jdbc.gen.DbModel;
import org.seasar.extension.jdbc.gen.DbModelFactory;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SequenceDesc;
import org.seasar.extension.jdbc.gen.TableDesc;

/**
 * {@link DbModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class DbModelFactoryImpl implements DbModelFactory {

    /** 方言 */
    protected GenDialect dialect;

    /** SQLステートメントの区切り文字 */
    protected char statementDelimiter;

    /** スキーマ情報を格納するテーブル名 */
    protected String schemaInfoFullTableName;

    /** スキーマのバージョン番号を格納するカラム名 */
    protected String schemaInfoColumnName;

    /**
     * インスタンスを構築します。
     * 
     * @param dialect
     *            方言
     * @param statementDelimiter
     *            SQLステートメントの区切り文字
     * @param schemaInfoFullTableName
     *            スキーマ情報を格納するテーブル名
     * @param schemaInfoColumnName
     *            スキーマのバージョン番号を格納するカラム名
     * 
     */
    public DbModelFactoryImpl(GenDialect dialect, char statementDelimiter,
            String schemaInfoFullTableName, String schemaInfoColumnName) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (schemaInfoFullTableName == null) {
            throw new NullPointerException("schemaInfoFullTableName");
        }
        if (schemaInfoColumnName == null) {
            throw new NullPointerException("schemaInfoColumnName");
        }
        this.dialect = dialect;
        this.statementDelimiter = statementDelimiter;
        this.schemaInfoFullTableName = schemaInfoFullTableName;
        this.schemaInfoColumnName = schemaInfoColumnName;
    }

    public DbModel getDbModel(List<TableDesc> tableDescList, int versionNo) {
        DbModel model = new DbModel();
        model.setDialect(dialect);
        model.setDelimiter(statementDelimiter);
        for (TableDesc tableDesc : tableDescList) {
            model.addTableDesc(tableDesc);
            for (SequenceDesc sequenceDesc : tableDesc.getSequenceDescList()) {
                model.addSequenceDesc(sequenceDesc);
            }
            for (TableDesc idTableDesc : tableDesc.getIdTableDescList()) {
                model.addTableDesc(idTableDesc);
            }
        }
        model.setSchemaInfoFullTableName(schemaInfoFullTableName);
        model.setSchemaInfoColumnName(schemaInfoColumnName);
        model.setVersionNo(versionNo);
        String columnDefinition = dialect.getType(Types.INTEGER)
                .getColumnDefinition(0, 10, 0, null);
        model.setSchemaInfoColumnDefinition(columnDefinition);
        return model;
    }
}
