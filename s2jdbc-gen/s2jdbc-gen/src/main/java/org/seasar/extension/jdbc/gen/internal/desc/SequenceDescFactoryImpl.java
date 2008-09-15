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
package org.seasar.extension.jdbc.gen.internal.desc;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import javax.sql.DataSource;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.desc.SequenceDesc;
import org.seasar.extension.jdbc.gen.desc.SequenceDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.exception.SequenceNextValFailedRuntimeException;
import org.seasar.extension.jdbc.gen.internal.exception.UnsupportedGenerationTypeRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.AnnotationUtil;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.PreparedStatementUtil;
import org.seasar.framework.util.ResultSetUtil;
import org.seasar.framework.util.StatementUtil;
import org.seasar.framework.util.StringUtil;

/**
 * {@link SequenceDescFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class SequenceDescFactoryImpl implements SequenceDescFactory {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(SequenceDescFactoryImpl.class);

    /** 方言 */
    protected GenDialect dialect;

    /** データソース */
    protected DataSource dataSource;

    /**
     * インスタンスを構築します。
     * 
     * @param dialect
     *            方言
     * @param dataSource
     *            データソース
     */
    public SequenceDescFactoryImpl(GenDialect dialect, DataSource dataSource) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (dataSource == null) {
            throw new NullPointerException("dataSource");
        }
        this.dialect = dialect;
        this.dataSource = dataSource;
    }

    public SequenceDesc getSequenceDesc(EntityMeta entityMeta,
            PropertyMeta propertyMeta) {
        GenerationType generationType = propertyMeta.getGenerationType();
        if (generationType == GenerationType.AUTO) {
            generationType = dialect.getDefaultGenerationType();
        }
        if (generationType == GenerationType.SEQUENCE) {
            if (!dialect.supportsSequence()) {
                throw new UnsupportedGenerationTypeRuntimeException(
                        GenerationType.SEQUENCE, entityMeta.getName(),
                        propertyMeta.getName());
            }
            SequenceGenerator generator = getSequenceGenerator(propertyMeta);
            SequenceDesc sequenceDesc = new SequenceDesc();
            String sequenceName = getSequenceName(entityMeta, propertyMeta,
                    generator);
            sequenceDesc.setSequenceName(sequenceName);
            Long nextValue = getNextValue(sequenceName, generator
                    .allocationSize());
            long initialValue = nextValue != null ? Math.max(nextValue,
                    generator.initialValue()) : generator.initialValue();
            sequenceDesc.setInitialValue(initialValue);
            sequenceDesc.setAllocationSize(generator.allocationSize());
            sequenceDesc.setDataType(getDataType(propertyMeta));
            return sequenceDesc;
        }
        return null;
    }

    /**
     * シーケンスジェネレータを返します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @return シーケンスジェネレータ
     */
    protected SequenceGenerator getSequenceGenerator(PropertyMeta propertyMeta) {
        Field field = propertyMeta.getField();
        SequenceGenerator sequenceGenerator = field
                .getAnnotation(SequenceGenerator.class);
        return sequenceGenerator != null ? sequenceGenerator : AnnotationUtil
                .getDefaultSequenceGenerator();
    }

    /**
     * シーケンスの名前を返します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMeta
     *            プロパティメタデータ
     * @param sequenceGenerator
     *            シーケンスジェネレータ
     * @return シーケンスの名前
     */
    protected String getSequenceName(EntityMeta entityMeta,
            PropertyMeta propertyMeta, SequenceGenerator sequenceGenerator) {
        String sequenceName = sequenceGenerator.sequenceName();
        if (!StringUtil.isEmpty(sequenceName)) {
            return sequenceName;
        }
        return entityMeta.getTableMeta().getName() + "_"
                + propertyMeta.getColumnMeta().getName();
    }

    /**
     * シーケンスのデータ型を返します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @return シーケンスのデータ型
     */
    protected String getDataType(PropertyMeta propertyMeta) {
        int sqlType = propertyMeta.getValueType().getSqlType();
        return dialect.getSqlType(sqlType).getDataType(0, 20, 0, false);
    }

    /**
     * シーケンスの次の値を返します。
     * 
     * @param sequenceName
     *            シーケンス名
     * @param allocationSize
     *            割り当てサイズ
     * @return シーケンスの次の値、シーケンスが存在しない場合は{@code null}
     */
    protected Long getNextValue(String sequenceName, int allocationSize) {
        String sql = dialect.getSequenceNextValString(sequenceName,
                allocationSize);
        logger.debug(sql);
        Connection connection = DataSourceUtil.getConnection(dataSource);
        try {
            PreparedStatement ps = ConnectionUtil.prepareStatement(connection,
                    sql);
            try {
                ResultSet rs = PreparedStatementUtil.executeQuery(ps);
                try {
                    if (rs.next()) {
                        return rs.getLong(1);
                    }
                    throw new SequenceNextValFailedRuntimeException(
                            sequenceName);
                } finally {
                    ResultSetUtil.close(rs);
                }
            } finally {
                StatementUtil.close(ps);
            }
        } catch (SequenceNextValFailedRuntimeException e) {
            throw e;
        } catch (Exception e) {
            if (dialect.isSequenceNotFound(e)) {
                logger.log("DS2JDBCGen0017", new Object[] { sequenceName });
                return null;
            }
            throw new SequenceNextValFailedRuntimeException(sequenceName, e);
        } finally {
            ConnectionUtil.close(connection);
        }
    }
}
