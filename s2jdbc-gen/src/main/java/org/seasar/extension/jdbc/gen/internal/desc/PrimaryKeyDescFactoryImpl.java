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
package org.seasar.extension.jdbc.gen.internal.desc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seasar.extension.jdbc.ColumnMeta;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDesc;
import org.seasar.extension.jdbc.gen.desc.PrimaryKeyDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;

/**
 * {@link PrimaryKeyDescFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class PrimaryKeyDescFactoryImpl implements PrimaryKeyDescFactory {

    /** 方言 */
    protected GenDialect dialect;

    /**
     * インスタンスを構築します。
     * 
     * @param dialect
     *            方言
     */
    public PrimaryKeyDescFactoryImpl(GenDialect dialect) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        this.dialect = dialect;
    }

    public PrimaryKeyDesc getPrimaryKeyDesc(EntityMeta entityMeta) {
        if (entityMeta.getIdPropertyMetaList().isEmpty()) {
            return null;
        }
        PrimaryKeyDesc primaryKeyDesc = new PrimaryKeyDesc();
        doColumnName(entityMeta, primaryKeyDesc);
        return primaryKeyDesc;
    }

    /**
     * カラムの名前を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param idPropertyMetaList
     *            識別子のプロパティメタデータのリスト
     * @param primaryKeyDesc
     *            主キー記述
     */
    protected void doColumnName(EntityMeta entityMeta,
            PrimaryKeyDesc primaryKeyDesc) {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        for (Class<?> clazz = entityMeta.getEntityClass(); clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            classList.add(clazz);
        }
        Collections.reverse(classList);
        for (Class<?> clazz : classList) {
            for (PropertyMeta propertyMeta : entityMeta.getIdPropertyMetaList()) {
                if (clazz == propertyMeta.getField().getDeclaringClass()) {
                    ColumnMeta columnMeta = propertyMeta.getColumnMeta();
                    primaryKeyDesc.addColumnName(columnMeta.getName());
                }
            }
        }
    }
}
