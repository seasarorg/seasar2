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
package org.seasar.extension.jdbc.id;

import javax.persistence.TableGenerator;
import javax.sql.DataSource;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.SqlLogger;
import org.seasar.extension.jdbc.exception.IdGenerationFailedRuntimeException;
import org.seasar.extension.jdbc.handler.ObjectResultSetHandler;
import org.seasar.extension.jdbc.impl.BasicSelectHandler;
import org.seasar.extension.jdbc.impl.BasicUpdateHandler;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.types.LongType;
import org.seasar.extension.tx.TransactionCallback;
import org.seasar.extension.tx.TransactionManagerAdapter;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.StringUtil;

/**
 * @author koichik
 */
public class TableIdGenerator extends AbstractPreAllocateIdGenerator {

    /** デフォルトの採番テーブル名 */
    public static final String DEFAULT_TABLE = "ID_GENERATOR";

    /** デフォルトの採番テーブルの識別子のカラム名 */
    public static final String DEFAULT_PK_COLUMN_NAME = "PK";

    /** デフォルトの採番テーブルの値のカラム名 */
    public static final String DEFAULT_VALUE_COLUMN_NAME = "VALUE";

    /** 識別子に付けられたアノテーション */
    protected TableGenerator tableGenerator;

    /** 採番テーブルのカタログ名 */
    protected String catalog;

    /** 採番テーブルのスキーマ名 */
    protected String schema;

    /** 採番テーブル名 */
    protected String table;

    /** 採番テーブルの識別子のカラム名 */
    protected String pkColumnName;

    /** 採番テーブルの識別子の値 */
    protected String pkColumnValue;

    /** 採番テーブルの値のカラム名 */
    protected String valueColumnName;

    /** 採番テーブルを更新するSQL */
    protected String updateSql;

    /** 採番テーブルから値を取得するするSQL */
    protected String selectSql;

    /**
     * インスタンスを構築します。
     * 
     * @param entityMeta
     *            エンティティのメタデータ
     * @param propertyMeta
     *            識別子を表すプロパティのメタデータ
     * @param tableGenerator
     *            識別子に付けられたアノテーション
     */
    public TableIdGenerator(final EntityMeta entityMeta,
            final PropertyMeta propertyMeta, final TableGenerator tableGenerator) {
        super(entityMeta, propertyMeta, tableGenerator.allocationSize());
        this.tableGenerator = tableGenerator;
        catalog = getCatalog();
        schema = getSchema();
        table = getTable();
        pkColumnName = getPkColumnName();
        pkColumnValue = getPkColumnValue();
        valueColumnName = getValueColumnName();
        updateSql = createUpdateSql();
        selectSql = createSelectSql();
    }

    @Override
    protected long getNewInitialValue(final JdbcManagerImplementor jdbcManager,
            final SqlLogger sqlLogger) {
        try {
            final TransactionManagerAdapter txAdapter = SingletonS2Container
                    .getComponent(TransactionManagerAdapter.class);
            final Object result = txAdapter
                    .requiresNew(new TransactionCallback() {

                        public Object execute(
                                final TransactionManagerAdapter adapter)
                                throws Throwable {
                            return updateIdTable(jdbcManager);
                        }

                    });
            return Number.class.cast(result).longValue() - allocationSize;
        } catch (final IdGenerationFailedRuntimeException e) {
            throw e;
        } catch (final Throwable t) {
            throw new IdGenerationFailedRuntimeException(entityMeta.getName(),
                    propertyMeta.getName(), t);
        }
    }

    /**
     * 採番テーブルを更新して次の識別子の値を返します。
     * <p>
     * このメソッドはエンティティのINSERTとは独立したトランザクションで実行されます。
     * </p>
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @return 次の識別子の値
     */
    protected Number updateIdTable(final JdbcManagerImplementor jdbcManager) {
        final DataSource ds = jdbcManager.getDataSource();
        final BasicUpdateHandler updateHandler = new BasicUpdateHandler(ds,
                updateSql);
        final int rows = updateHandler.execute(new Object[] { allocationSize,
                pkColumnValue });
        if (rows != 1) {
            throw new IdGenerationFailedRuntimeException(entityMeta.getName(),
                    propertyMeta.getName());
        }

        final BasicSelectHandler selectHandler = new BasicSelectHandler(ds,
                selectSql,
                new ObjectResultSetHandler(new LongType(), selectSql));
        final Object result = selectHandler.execute(
                new Object[] { pkColumnValue }, new Class[] { String.class });
        if (result == null || !Number.class.isInstance(result)) {
            throw new IdGenerationFailedRuntimeException(entityMeta.getName(),
                    propertyMeta.getName());
        }
        return Number.class.cast(result);
    }

    /**
     * 採番テーブルのカタログ名を返します。
     * 
     * @return 採番テーブルのカタログ名
     */
    protected String getCatalog() {
        final String catalog = tableGenerator.catalog();
        if (!StringUtil.isEmpty(catalog)) {
            return catalog;
        }
        return entityMeta.getTableMeta().getCatalog();
    }

    /**
     * 採番テーブルのスキーマ名を返します。
     * 
     * @return 採番テーブルのスキーマ名
     */
    protected String getSchema() {
        final String schema = tableGenerator.schema();
        if (!StringUtil.isEmpty(schema)) {
            return schema;
        }
        return entityMeta.getTableMeta().getSchema();
    }

    /**
     * 採番テーブル名を返します。
     * 
     * @return 採番テーブル名
     */
    protected String getTable() {
        final String table = tableGenerator.table();
        if (!StringUtil.isEmpty(table)) {
            return table;
        }
        return DEFAULT_TABLE;
    }

    /**
     * 採番テーブルの識別子のカラム名を返します。
     * 
     * @return 採番テーブルの識別子のカラム名
     */
    protected String getPkColumnName() {
        final String pkColumnName = tableGenerator.pkColumnName();
        if (!StringUtil.isEmpty(pkColumnName)) {
            return pkColumnName;
        }
        return DEFAULT_PK_COLUMN_NAME;
    }

    /**
     * 採番テーブルの識別子の値を返します。
     * 
     * @return 採番テーブルの識別子の値
     */
    protected String getPkColumnValue() {
        final String pkColumnValue = tableGenerator.pkColumnValue();
        if (!StringUtil.isEmpty(pkColumnValue)) {
            return pkColumnValue;
        }
        return entityMeta.getTableMeta().getName() + "_"
                + propertyMeta.getColumnMeta().getName();
    }

    /**
     * 採番テーブルの値のカラム名を返します。
     * 
     * @return 採番テーブルの値のカラム名
     */
    protected String getValueColumnName() {
        final String valueColumnName = tableGenerator.valueColumnName();
        if (!StringUtil.isEmpty(valueColumnName)) {
            return valueColumnName;
        }
        return DEFAULT_VALUE_COLUMN_NAME;
    }

    /**
     * 採番テーブルを更新するSQLを作成して返します。
     * 
     * @return 採番テーブルを更新するSQL
     */
    protected String createUpdateSql() {
        final StringBuilder buf = new StringBuilder(100);
        buf.append("update ");
        if (!StringUtil.isEmpty(catalog)) {
            buf.append(catalog).append('.');
        }
        if (!StringUtil.isEmpty(schema)) {
            buf.append(schema).append('.');
        }
        buf.append(table).append(" set ").append(valueColumnName).append(" = ")
                .append(valueColumnName).append(" + ? where ").append(
                        pkColumnName).append(" = ?");
        return new String(buf);
    }

    /**
     * 採番テーブルから値を取得するするSQLを作成して返します。
     * 
     * @return 採番テーブルから値を取得するするSQL
     */
    protected String createSelectSql() {
        final StringBuilder buf = new StringBuilder(100);
        buf.append("select ").append(valueColumnName).append(" from ");
        if (!StringUtil.isEmpty(catalog)) {
            buf.append(catalog).append('.');
        }
        if (!StringUtil.isEmpty(schema)) {
            buf.append(schema).append('.');
        }
        buf.append(table).append(" where ").append(pkColumnName).append(" = ?");
        return new String(buf);
    }

}
