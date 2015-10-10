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

import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.SqlLogger;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * INSERT前に識別子を自動生成するIDジェネレータの抽象クラスです。
 * 
 * @author koichik
 */
public abstract class AbstractPreAllocateIdGenerator extends
        AbstractIdGenerator {

    /** {@link #idContextMap}に対するデフォルトのキー */
    protected static String DEFAULT_ID_CONTEXT_KEY = IdContext.class.getName();

    /** 割り当てサイズ */
    protected final long allocationSize;

    /** IDコンテキストの{@link Map} */
    protected ConcurrentMap<String, IdContext> idContextMap = CollectionsUtil
            .newConcurrentHashMap();

    /**
     * インスタンスを構築します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMeta
     *            プロパティメタデータ
     * @param allocationSize
     *            割り当てサイズ
     */
    public AbstractPreAllocateIdGenerator(final EntityMeta entityMeta,
            final PropertyMeta propertyMeta, final long allocationSize) {
        super(entityMeta, propertyMeta);
        this.allocationSize = allocationSize;
    }

    public boolean supportBatch(final JdbcManagerImplementor jdbcManager) {
        return true;
    }

    public boolean useGetGeneratedKeys(final JdbcManagerImplementor jdbcManager) {
        return false;
    }

    public boolean isInsertInto(final JdbcManagerImplementor jdbcManager) {
        return true;
    }

    public Object preInsert(final JdbcManagerImplementor jdbcManager,
            final Object entity, final SqlLogger sqlLogger) {
        final long id = getIdContext(jdbcManager).getNextValue(jdbcManager,
                sqlLogger);
        setId(entity, id);
        return Long.valueOf(id);
    }

    public void postInsert(final JdbcManagerImplementor jdbcManager,
            final Object entity, final Statement statement,
            final SqlLogger sqlLogger) {
    }

    /**
     * IDコンテキストを返します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @return IDコンテキスト
     */
    protected IdContext getIdContext(final JdbcManagerImplementor jdbcManager) {
        final String dsName = jdbcManager.getSelectableDataSourceName();
        final String key = dsName != null ? dsName : DEFAULT_ID_CONTEXT_KEY;
        final IdContext context = idContextMap.get(key);
        if (context != null) {
            return context;
        }
        return CollectionsUtil.putIfAbsent(idContextMap, key, new IdContext());
    }

    /**
     * 次の初期値を返します。
     * 
     * @param jdbcManager
     *            内部的なJDBCマネージャ
     * @param sqlLogger
     *            SQLロガー
     * @return 次の初期値
     */
    protected abstract long getNewInitialValue(
            JdbcManagerImplementor jdbcManager, SqlLogger sqlLogger);

    /**
     * 自動生成される識別子のコンテキスト情報を保持するクラスです。
     * 
     * @author koichik
     */
    public class IdContext {

        /** 初期値 */
        protected long initialValue;

        /** 割り当て済みの値 */
        protected long allocated = Long.MAX_VALUE;

        /**
         * 自動生成された識別子の値を返します。
         * 
         * @param jdbcManager
         *            内部的なJDBCマネージャ
         * @param sqlLogger
         *            SQLロガー
         * @return 自動生成された識別子の値
         */
        public synchronized long getNextValue(
                final JdbcManagerImplementor jdbcManager,
                final SqlLogger sqlLogger) {
            if (allocated < allocationSize) {
                return initialValue + allocated++;
            }
            initialValue = getNewInitialValue(jdbcManager, sqlLogger);
            allocated = 1;
            return initialValue;
        }

    }

}
