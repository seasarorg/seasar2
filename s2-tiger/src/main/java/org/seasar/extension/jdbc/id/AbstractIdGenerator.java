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

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.IdGenerator;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.exception.IdGenerationFailedRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.NumberConversionUtil;
import org.seasar.framework.util.ResultSetUtil;

/**
 * 識別子を自動生成するIDジェネレータの抽象クラスです。
 * 
 * @author koichik
 */
public abstract class AbstractIdGenerator implements IdGenerator {

    /** エンティティメタデータ */
    protected EntityMeta entityMeta;

    /** プロパティメタデータ */
    protected PropertyMeta propertyMeta;

    /**
     * インスタンスを構築します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMeta
     *            プロパティメタデータ
     */
    public AbstractIdGenerator(final EntityMeta entityMeta,
            final PropertyMeta propertyMeta) {
        super();
        this.entityMeta = entityMeta;
        this.propertyMeta = propertyMeta;
    }

    /**
     * エンティティの識別子に自動生成された値を設定します。
     * 
     * @param entity
     *            エンティティ
     * @param id
     *            自動生成された識別子の値
     */
    protected void setId(final Object entity, final long id) {
        final Field field = propertyMeta.getField();
        final Class<?> fieldType = ClassUtil.getWrapperClassIfPrimitive(field
                .getType());
        final Object value = NumberConversionUtil.convertNumber(fieldType, Long
                .valueOf(id));
        FieldUtil.set(field, entity, value);
    }

    /**
     * 結果セットから自動生成された識別子の値を取得して返します。
     * 
     * @param rs
     *            結果セット
     * @return 自動生成された識別子の値
     */
    protected long getGeneratedId(final ResultSet rs) {
        try {
            if (rs.next()) {
                return rs.getLong(1);
            }
            throw new IdGenerationFailedRuntimeException(entityMeta.getName(),
                    propertyMeta.getName());
        } catch (final SQLException e) {
            throw new IdGenerationFailedRuntimeException(entityMeta.getName(),
                    propertyMeta.getName(), e);
        } finally {
            ResultSetUtil.close(rs);
        }
    }

}
