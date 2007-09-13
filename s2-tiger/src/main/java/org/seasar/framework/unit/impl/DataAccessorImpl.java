/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.unit.impl;

import javax.persistence.EntityManager;
import javax.transaction.TransactionManager;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.unit.DataAccessor;
import org.seasar.framework.util.TransactionManagerUtil;

/**
 * {@link EntityManager}のキャッシュに影響されない{@link DataAccessor}の実装です。
 * <p>
 * 必要に応じて{@link EntityManager#flush()}を呼び出すことで、キャッシュの影響を受けずにデータの取得、更新をします。
 * このクラスは<code>EntityManager</code>の利用の有無に関わらず使用することができます。
 * </p>
 * 
 * @author taedium
 */
public class DataAccessorImpl extends SimpleDataAccessor {

    /** トランザクションマネジャー */
    protected TransactionManager tm;

    /** エンティティマネジャー */
    protected EntityManager em;

    /**
     * トランザクションマネジャーを設定します。
     * 
     * @param tm
     *            トランザクションマネジャー
     */
    @Binding(bindingType = BindingType.SHOULD)
    public void setTransactionManager(final TransactionManager tm) {
        this.tm = tm;
    }

    /**
     * エンティティマネジャーを設定します。
     * 
     * @param em
     *            エンティティマネジャー
     */
    @Binding(bindingType = BindingType.MAY)
    public void setEntityManager(final EntityManager em) {
        this.em = em;
    }

    @Override
    public void writeDb(DataSet dataSet) {
        flushIfNecessary();
        super.writeDb(dataSet);
    }

    @Override
    public DataSet readDb(DataSet dataSet) {
        flushIfNecessary();
        return super.readDb(dataSet);
    }

    @Override
    public DataTable readDbByTable(String table) {
        flushIfNecessary();
        return super.readDbByTable(table);
    }

    @Override
    public DataTable readDbByTable(String table, String condition) {
        flushIfNecessary();
        return super.readDbByTable(table, condition);
    }

    @Override
    public DataTable readDbBySql(String sql, String tableName) {
        flushIfNecessary();
        return super.readDbBySql(sql, tableName);
    }

    @Override
    public void readXlsWriteDb(String path, boolean trimString) {
        flushIfNecessary();
        super.readXlsWriteDb(path, trimString);
    }

    @Override
    public void readXlsReplaceDb(String path, boolean trimString) {
        flushIfNecessary();
        super.readXlsReplaceDb(path, trimString);
    }

    @Override
    public void readXlsAllReplaceDb(String path, boolean trimString) {
        flushIfNecessary();
        super.readXlsAllReplaceDb(path, trimString);
    }

    @Override
    public DataSet reload(DataSet dataSet) {
        flushIfNecessary();
        return super.reload(dataSet);
    }

    @Override
    public DataTable reload(DataTable table) {
        flushIfNecessary();
        return super.reload(table);
    }

    @Override
    public DataSet reloadOrReadDb(DataSet dataSet) {
        flushIfNecessary();
        return super.reloadOrReadDb(dataSet);
    }

    @Override
    public void deleteDb(DataSet dataSet) {
        flushIfNecessary();
        super.deleteDb(dataSet);
    }

    @Override
    public void deleteTable(String tableName) {
        flushIfNecessary();
        super.deleteTable(tableName);
    }

    /**
     * 必要ならば{@link EntityManager#flush()}}を実行します。
     */
    protected void flushIfNecessary() {
        if (em != null && TransactionManagerUtil.isActive(tm)) {
            em.flush();
        }
    }

}
