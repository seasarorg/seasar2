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

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JdbcContext;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.SqlLogger;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.ResultSetUtil;
import org.seasar.framework.util.StringUtil;

/**
 * {@link GenerationType#SEQUENCE}方式で識別子の値を自動生成するIDジェネレータです。
 * 
 * @author koichik
 */
public class SequenceIdGenerator extends AbstractPreAllocateIdGenerator {

    /** シーケンスの名前 */
    protected String sequenceName;

    /**
     * インスタンスを構築します。
     * 
     * @param entityMeta
     *            エンティティのメタデータ
     * @param propertyMeta
     *            識別子を表すプロパティのメタデータ
     * @param sequenceGenerator
     *            識別子に付けられたアノテーション
     */
    public SequenceIdGenerator(final EntityMeta entityMeta,
            final PropertyMeta propertyMeta,
            final SequenceGenerator sequenceGenerator) {
        super(entityMeta, propertyMeta, sequenceGenerator.allocationSize());
        sequenceName = getSequenceName(sequenceGenerator);
    }

    @Override
    protected long getNewInitialValue(final JdbcManagerImplementor jdbcManager,
            final SqlLogger sqlLogger) {
        final String sql = jdbcManager.getDialect().getSequenceNextValString(
                sequenceName, (int) allocationSize);
        sqlLogger.logSql(sql);
        final JdbcContext jdbcContext = jdbcManager.getJdbcContext();
        try {
            final PreparedStatement ps = jdbcContext.getPreparedStatement(sql);
            final ResultSet rs = PreparedStatementUtil.executeQuery(ps);
            try {
                return getGeneratedId(rs);
            } finally {
                ResultSetUtil.close(rs);
            }
        } finally {
            if (!jdbcContext.isTransactional()) {
                jdbcContext.destroy();
            }
        }
    }

    /**
     * シーケンスの名前を返します。
     * 
     * @param sequenceGenerator
     *            識別子に付けられたアノテーション
     * @return シーケンスの名前
     */
    protected String getSequenceName(final SequenceGenerator sequenceGenerator) {
        final String sequenceName = sequenceGenerator.sequenceName();
        if (!StringUtil.isEmpty(sequenceName)) {
            return sequenceName;
        }
        return entityMeta.getTableMeta().getName() + "_"
                + propertyMeta.getColumnMeta().getName();
    }

}
