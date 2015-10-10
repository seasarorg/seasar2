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

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.gen.desc.SequenceDesc;
import org.seasar.extension.jdbc.gen.desc.SequenceDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.internal.exception.UnsupportedGenerationTypeRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.AnnotationUtil;
import org.seasar.extension.jdbc.gen.provider.ValueTypeProvider;
import org.seasar.framework.log.Logger;
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

    /** {@link ValueType}の提供者 */
    protected ValueTypeProvider valueTypeProvider;

    /**
     * インスタンスを構築します。
     * 
     * @param dialect
     *            方言
     * @param valueTypeProvider
     *            {@link ValueType}の提供者
     */
    public SequenceDescFactoryImpl(GenDialect dialect,
            ValueTypeProvider valueTypeProvider) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (valueTypeProvider == null) {
            throw new NullPointerException("valueTypeResolver");
        }
        this.dialect = dialect;
        this.valueTypeProvider = valueTypeProvider;
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
            SequenceGenerator generator = getSequenceGenerator(entityMeta,
                    propertyMeta);
            SequenceDesc sequenceDesc = new SequenceDesc();
            String sequenceName = getSequenceName(entityMeta, propertyMeta,
                    generator);
            sequenceDesc.setSequenceName(sequenceName);
            sequenceDesc.setInitialValue(generator.initialValue());
            sequenceDesc.setAllocationSize(generator.allocationSize());
            sequenceDesc.setDataType(getDataType(propertyMeta));
            return sequenceDesc;
        }
        return null;
    }

    /**
     * シーケンスジェネレータを返します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMeta
     *            プロパティメタデータ
     * @return シーケンスジェネレータ
     */
    protected SequenceGenerator getSequenceGenerator(EntityMeta entityMeta,
            PropertyMeta propertyMeta) {
        Field field = propertyMeta.getField();
        GeneratedValue generatedValue = field
                .getAnnotation(GeneratedValue.class);
        if (generatedValue == null) {
            throw new IllegalStateException("@GeneratedValue not found.");
        }
        String name = generatedValue.generator();
        if (StringUtil.isEmpty(name)) {
            return AnnotationUtil.getDefaultSequenceGenerator();
        }
        SequenceGenerator sequenceGenerator = field
                .getAnnotation(SequenceGenerator.class);
        if (sequenceGenerator != null && name.equals(sequenceGenerator.name())) {
            return sequenceGenerator;
        }
        sequenceGenerator = entityMeta.getEntityClass().getAnnotation(
                SequenceGenerator.class);
        if (sequenceGenerator != null && name.equals(sequenceGenerator.name())) {
            return sequenceGenerator;
        }
        throw new IllegalStateException("@SequenceGenerator not found.");
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
        ValueType valueType = valueTypeProvider.provide(propertyMeta);
        int sqlType = valueType.getSqlType();
        Column column = getColumn(propertyMeta);
        return dialect.getSqlType(sqlType).getDataType(column.length(),
                column.precision(), column.scale(), false);
    }

    /**
     * カラムを返します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @return カラム
     */
    protected Column getColumn(PropertyMeta propertyMeta) {
        Field field = propertyMeta.getField();
        Column column = field.getAnnotation(Column.class);
        return column != null ? column : AnnotationUtil.getDefaultColumn();
    }

}
