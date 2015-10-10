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
package org.seasar.extension.jdbc.meta;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.seasar.extension.jdbc.ColumnMetaFactory;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JoinColumnMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.PropertyMetaFactory;
import org.seasar.extension.jdbc.RelationshipType;
import org.seasar.extension.jdbc.ValueType;
import org.seasar.extension.jdbc.exception.BothMappedByAndJoinColumnRuntimeException;
import org.seasar.extension.jdbc.exception.IdGeneratorNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.JoinColumnNameAndReferencedColumnNameMandatoryRuntimeException;
import org.seasar.extension.jdbc.exception.LazyFetchSpecifiedRuntimeException;
import org.seasar.extension.jdbc.exception.MappedByMandatoryRuntimeException;
import org.seasar.extension.jdbc.exception.OneToManyNotGenericsRuntimeException;
import org.seasar.extension.jdbc.exception.OneToManyNotListRuntimeException;
import org.seasar.extension.jdbc.exception.RelationshipNotEntityRuntimeException;
import org.seasar.extension.jdbc.exception.TemporalTypeNotSpecifiedRuntimeException;
import org.seasar.extension.jdbc.exception.UnsupportedPropertyTypeRuntimeException;
import org.seasar.extension.jdbc.exception.UnsupportedRelationshipRuntimeException;
import org.seasar.extension.jdbc.exception.VersionPropertyNotNumberRuntimeException;
import org.seasar.extension.jdbc.id.IdentityIdGenerator;
import org.seasar.extension.jdbc.id.SequenceIdGenerator;
import org.seasar.extension.jdbc.id.TableIdGenerator;
import org.seasar.extension.jdbc.types.ValueTypes;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ModifierUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * {@link PropertyMetaFactory}の実装クラスです。
 * 
 * @author higa
 * 
 */
@SequenceGenerator(name = "default")
@TableGenerator(name = "default")
public class PropertyMetaFactoryImpl implements PropertyMetaFactory {

    /**
     * デフォルトの{@link SequenceGenerator}です。
     */
    protected static final SequenceGenerator DEFAULT_SEQUENCE_GENERATOR = PropertyMetaFactoryImpl.class
            .getAnnotation(SequenceGenerator.class);

    /**
     * デフォルトの{@link TableGenerator}です。
     */
    protected static final TableGenerator DEFAULT_TABLE_GENERATOR = PropertyMetaFactoryImpl.class
            .getAnnotation(TableGenerator.class);

    /**
     * フィールドの型に対応する値タイプのマップです。
     */
    protected static final Map<Class<?>, ValueType> valueTypes = CollectionsUtil
            .newHashMap();
    static {
        valueTypes.put(boolean.class, ValueTypes.BOOLEAN);
        valueTypes.put(Boolean.class, ValueTypes.BOOLEAN);
        valueTypes.put(char.class, ValueTypes.CHARACTER);
        valueTypes.put(Character.class, ValueTypes.CHARACTER);
        valueTypes.put(byte.class, ValueTypes.BYTE);
        valueTypes.put(Byte.class, ValueTypes.BYTE);
        valueTypes.put(short.class, ValueTypes.SHORT);
        valueTypes.put(Short.class, ValueTypes.SHORT);
        valueTypes.put(int.class, ValueTypes.INTEGER);
        valueTypes.put(Integer.class, ValueTypes.INTEGER);
        valueTypes.put(long.class, ValueTypes.LONG);
        valueTypes.put(Long.class, ValueTypes.LONG);
        valueTypes.put(float.class, ValueTypes.FLOAT);
        valueTypes.put(Float.class, ValueTypes.FLOAT);
        valueTypes.put(double.class, ValueTypes.DOUBLE);
        valueTypes.put(Double.class, ValueTypes.DOUBLE);
        valueTypes.put(BigDecimal.class, ValueTypes.BIGDECIMAL);
        valueTypes.put(BigInteger.class, ValueTypes.BIGINTEGER);
        valueTypes.put(java.sql.Date.class, ValueTypes.SQLDATE);
        valueTypes.put(java.sql.Time.class, ValueTypes.TIME);
        valueTypes.put(java.sql.Timestamp.class, ValueTypes.TIMESTAMP);
    }

    /**
     * カラムメタデータファクトリです。
     */
    protected ColumnMetaFactory columnMetaFactory;

    /**
     * 永続化層の命名規約です。
     */
    protected PersistenceConvention persistenceConvention;

    public PropertyMeta createPropertyMeta(Field field, EntityMeta entityMeta) {
        PropertyMeta propertyMeta = new PropertyMeta();
        doField(propertyMeta, field, entityMeta);
        doName(propertyMeta, field, entityMeta);
        doTransient(propertyMeta, field, entityMeta);
        if (!propertyMeta.isTransient()) {
            Object relationshipAnnotation = getRelationshipAnnotation(field);
            if (relationshipAnnotation == null) {
                doColumnMeta(propertyMeta, field, entityMeta);
                doId(propertyMeta, field, entityMeta);
                doFetchType(propertyMeta, field, entityMeta);
                doTemporal(propertyMeta, field, entityMeta);
                doEnum(propertyMeta, field, entityMeta);
                doVersion(propertyMeta, field, entityMeta);
                doLob(propertyMeta, field, entityMeta);
                doValueType(propertyMeta, entityMeta);
            } else {
                doRelationship(propertyMeta, field, entityMeta,
                        relationshipAnnotation);
            }
        }
        doCustomize(propertyMeta, field, entityMeta);
        return propertyMeta;
    }

    /**
     * フィールドを処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doField(PropertyMeta propertyMeta, Field field,
            @SuppressWarnings("unused") EntityMeta entityMeta) {
        propertyMeta.setField(field);
    }

    /**
     * 名前を処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doName(PropertyMeta propertyMeta, Field field,
            @SuppressWarnings("unused") EntityMeta entityMeta) {
        propertyMeta.setName(persistenceConvention
                .fromFieldNameToPropertyName(field.getName()));
    }

    /**
     * カラムメタデータを処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doColumnMeta(PropertyMeta propertyMeta, Field field,
            EntityMeta entityMeta) {
        propertyMeta.setColumnMeta(columnMetaFactory.createColumnMeta(field,
                entityMeta, propertyMeta));
    }

    /**
     * 識別子メタデータを処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doId(PropertyMeta propertyMeta, Field field,
            EntityMeta entityMeta) {
        propertyMeta.setId(field.getAnnotation(Id.class) != null);
        GeneratedValue generatedValue = field
                .getAnnotation(GeneratedValue.class);
        if (generatedValue == null) {
            return;
        }
        GenerationType generationType = generatedValue.strategy();
        propertyMeta.setGenerationType(generationType);
        switch (generationType) {
        case AUTO:
            doIdentityIdGenerator(propertyMeta, entityMeta);
            doSequenceIdGenerator(propertyMeta, generatedValue, entityMeta);
            doTableIdGenerator(propertyMeta, generatedValue, entityMeta);
            break;
        case IDENTITY:
            doIdentityIdGenerator(propertyMeta, entityMeta);
            break;
        case SEQUENCE:
            if (!doSequenceIdGenerator(propertyMeta, generatedValue, entityMeta)) {
                throw new IdGeneratorNotFoundRuntimeException(entityMeta
                        .getName(), propertyMeta.getName(), generatedValue
                        .generator());
            }
            break;
        case TABLE:
            if (!doTableIdGenerator(propertyMeta, generatedValue, entityMeta)) {
                throw new IdGeneratorNotFoundRuntimeException(entityMeta
                        .getName(), propertyMeta.getName(), generatedValue
                        .generator());
            }
            break;
        }
    }

    /**
     * {@link GenerationType#IDENTITY}方式で識別子の値を自動生成するIDジェネレータを処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param entityMeta
     *            エンティティのメタデータ
     */
    protected void doIdentityIdGenerator(PropertyMeta propertyMeta,
            EntityMeta entityMeta) {
        propertyMeta.setIdentityIdGenerator(new IdentityIdGenerator(entityMeta,
                propertyMeta));
    }

    /**
     * {@link GenerationType#SEQUENCE}方式で識別子の値を自動生成するIDジェネレータを処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param generatedValue
     *            識別子に付けられた{@link GeneratedValue}アノテーション
     * @param entityMeta
     *            エンティティのメタデータ
     * @return {@link GenerationType#SEQUENCE}方式で識別子の値を自動生成するIDジェネレータが存在した場合に
     *         <code>true</code>
     */
    protected boolean doSequenceIdGenerator(PropertyMeta propertyMeta,
            GeneratedValue generatedValue, EntityMeta entityMeta) {
        String name = generatedValue.generator();
        SequenceGenerator sequenceGenerator;
        if (StringUtil.isEmpty(name)) {
            sequenceGenerator = DEFAULT_SEQUENCE_GENERATOR;
        } else {
            sequenceGenerator = propertyMeta.getField().getAnnotation(
                    SequenceGenerator.class);
            if (sequenceGenerator == null
                    || !name.equals(sequenceGenerator.name())) {
                sequenceGenerator = entityMeta.getEntityClass().getAnnotation(
                        SequenceGenerator.class);
                if (sequenceGenerator == null
                        || !name.equals(sequenceGenerator.name())) {
                    return false;
                }
            }
        }
        propertyMeta.setSequenceIdGenerator(new SequenceIdGenerator(entityMeta,
                propertyMeta, sequenceGenerator));
        return true;
    }

    /**
     * {@link GenerationType#TABLE}方式で識別子の値を自動生成するIDジェネレータを処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param generatedValue
     *            識別子に付けられた{@link GeneratedValue}アノテーション
     * @param entityMeta
     *            エンティティのメタデータ
     * @return {@link GenerationType#TABLE}方式で識別子の値を自動生成するIDジェネレータが存在した場合に
     *         <code>true</code>
     */
    protected boolean doTableIdGenerator(PropertyMeta propertyMeta,
            GeneratedValue generatedValue, EntityMeta entityMeta) {
        String name = generatedValue.generator();
        TableGenerator tableGenerator;
        if (StringUtil.isEmpty(name)) {
            tableGenerator = DEFAULT_TABLE_GENERATOR;
        } else {
            tableGenerator = propertyMeta.getField().getAnnotation(
                    TableGenerator.class);
            if (tableGenerator == null || !name.equals(tableGenerator.name())) {
                tableGenerator = entityMeta.getEntityClass().getAnnotation(
                        TableGenerator.class);
                if (tableGenerator == null
                        || !name.equals(tableGenerator.name())) {
                    return false;
                }
            }
        }
        propertyMeta.setTableIdGenerator(new TableIdGenerator(entityMeta,
                propertyMeta, tableGenerator));
        return true;
    }

    /**
     * フェッチタイプを処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doFetchType(final PropertyMeta propertyMeta,
            final Field field, final EntityMeta entityMeta) {
        final Basic basic = field.getAnnotation(Basic.class);
        if (basic == null) {
            propertyMeta.setFetchType(FetchType.EAGER);
            return;
        }
        if (propertyMeta.isId() && basic.fetch() == FetchType.LAZY) {
            throw new LazyFetchSpecifiedRuntimeException(entityMeta.getName(),
                    propertyMeta.getName());
        }
        propertyMeta.setFetchType(basic.fetch());
    }

    /**
     * 時制の種別を処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doTemporal(PropertyMeta propertyMeta, Field field,
            EntityMeta entityMeta) {
        if (propertyMeta.getPropertyClass() != java.util.Date.class
                && propertyMeta.getPropertyClass() != Calendar.class) {
            return;
        }
        Temporal temporal = field.getAnnotation(Temporal.class);
        if (temporal == null) {
            throw new TemporalTypeNotSpecifiedRuntimeException(entityMeta
                    .getName(), propertyMeta.getName());
        }
        propertyMeta.setTemporalType(temporal.value());
    }

    /**
     * enumの種別を処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doEnum(PropertyMeta propertyMeta, Field field,
            EntityMeta entityMeta) {
        if (!propertyMeta.getPropertyClass().isEnum()) {
            return;
        }
        Enumerated enumerated = field.getAnnotation(Enumerated.class);
        if (enumerated == null) {
            return;
        }
        propertyMeta.setEnumType(enumerated.value());
    }

    /**
     * バージョンチェック用かどうかを処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doVersion(PropertyMeta propertyMeta, Field field,
            @SuppressWarnings("unused") EntityMeta entityMeta) {
        if (field.getAnnotation(Version.class) == null) {
            return;
        }
        Class<?> clazz = ClassUtil.getWrapperClassIfPrimitive(field.getType());
        if (clazz != Integer.class && clazz != Long.class && clazz != int.class
                && clazz != long.class) {
            throw new VersionPropertyNotNumberRuntimeException(entityMeta
                    .getName(), propertyMeta.getName());
        }
        propertyMeta.setVersion(true);
    }

    /**
     * 一時的かどうかを処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doTransient(PropertyMeta propertyMeta, Field field,
            @SuppressWarnings("unused") EntityMeta entityMeta) {
        propertyMeta.setTransient(field.getAnnotation(Transient.class) != null
                || ModifierUtil.isTransient(field));
    }

    /**
     * <code>LOB</code>かどうかを処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doLob(PropertyMeta propertyMeta, Field field,
            @SuppressWarnings("unused") EntityMeta entityMeta) {
        propertyMeta.setLob(field.getAnnotation(Lob.class) != null);
    }

    /**
     * {@link ValueType}を処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param entityMeta
     *            エンティティメタデータ
     */
    @SuppressWarnings("unchecked")
    protected void doValueType(final PropertyMeta propertyMeta,
            final EntityMeta entityMeta) {
        final Class<?> propertyClass = propertyMeta.getPropertyClass();
        final ValueType valueType = valueTypes.get(propertyClass);
        if (valueType != null) {
            propertyMeta.setValueType(valueType);
            return;
        }

        if (propertyClass == String.class) {
            if (propertyMeta.isLob()) {
                propertyMeta.setValueType(ValueTypes.CLOB);
            } else {
                propertyMeta.setValueType(ValueTypes.STRING);
            }
            return;
        }

        if (propertyClass == byte[].class) {
            if (propertyMeta.isLob()) {
                propertyMeta.setValueType(ValueTypes.BLOB);
            } else {
                propertyMeta.setValueType(ValueTypes.BYTE_ARRAY);
            }
            return;
        }

        if (propertyClass == Date.class) {
            switch (propertyMeta.getTemporalType()) {
            case DATE:
                propertyMeta.setValueType(ValueTypes.DATE_SQLDATE);
                return;
            case TIME:
                propertyMeta.setValueType(ValueTypes.DATE_TIME);
                return;
            case TIMESTAMP:
                propertyMeta.setValueType(ValueTypes.DATE_TIMESTAMP);
                return;
            }
        }

        if (propertyClass == Calendar.class) {
            switch (propertyMeta.getTemporalType()) {
            case DATE:
                propertyMeta.setValueType(ValueTypes.CALENDAR_SQLDATE);
                return;
            case TIME:
                propertyMeta.setValueType(ValueTypes.CALENDAR_TIME);
                return;
            case TIMESTAMP:
                propertyMeta.setValueType(ValueTypes.CALENDAR_TIMESTAMP);
                return;
            }
        }

        if (propertyClass.isEnum()) {
            if (propertyMeta.getEnumType() == null) {
                propertyMeta.setValueType(ValueTypes
                        .getValueType(propertyClass));
                return;
            }
            switch (propertyMeta.getEnumType()) {
            case ORDINAL:
                propertyMeta.setValueType(ValueTypes
                        .getEnumOrdinalValueType(propertyClass));
                return;
            case STRING:
                propertyMeta.setValueType(ValueTypes
                        .getEnumStringValueType(propertyClass));
                return;
            }
        }

        final ValueType userDefinedValueType = ValueTypes
                .createUserDefineValueType(propertyClass);
        if (userDefinedValueType != null) {
            propertyMeta.setValueType(userDefinedValueType);
            return;
        }

        if (Serializable.class.isAssignableFrom(propertyClass)) {
            if (propertyMeta.isLob()) {
                propertyMeta.setValueType(ValueTypes.SERIALIZABLE_BLOB);
            } else {
                propertyMeta.setValueType(ValueTypes.SERIALIZABLE_BYTE_ARRAY);
            }
            return;
        }

        throw new UnsupportedPropertyTypeRuntimeException(entityMeta.getName(),
                propertyMeta.getName(), propertyMeta.getPropertyClass());
    }

    /**
     * フィールドに関連のアノテーションが指定されていればそれを返します。
     * 
     * @param field
     *            フィールド
     * @return 関連のアノテーションまたは<code>null</code>
     */
    protected Object getRelationshipAnnotation(final Field field) {
        final OneToOne oneToOne = field.getAnnotation(OneToOne.class);
        if (oneToOne != null) {
            return oneToOne;
        }
        final OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        if (oneToMany != null) {
            return oneToMany;
        }
        final ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
        if (manyToOne != null) {
            return manyToOne;
        }
        final ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
        if (manyToMany != null) {
            return manyToMany;
        }
        return null;
    }

    /**
     * 関連を処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     * @param annotation
     *            関連のアノテーション
     */
    protected void doRelationship(PropertyMeta propertyMeta, Field field,
            EntityMeta entityMeta, Object annotation) {
        doJoinColumn(propertyMeta, field, entityMeta);
        if (OneToOne.class.isInstance(annotation)) {
            doOneToOne(propertyMeta, field, entityMeta, OneToOne.class
                    .cast(annotation));
        } else if (OneToMany.class.isInstance(annotation)) {
            doOneToMany(propertyMeta, field, entityMeta, OneToMany.class
                    .cast(annotation));
        } else if (ManyToOne.class.isInstance(annotation)) {
            doManyToOne(propertyMeta, field, entityMeta, ManyToOne.class
                    .cast(annotation));
        } else {
            throw new UnsupportedRelationshipRuntimeException(entityMeta
                    .getName(), propertyMeta.getName());
        }
    }

    /**
     * JoinColumnを処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doJoinColumn(PropertyMeta propertyMeta, Field field,
            EntityMeta entityMeta) {
        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
        if (joinColumn != null) {
            JoinColumnMeta meta = new JoinColumnMeta(joinColumn.name(),
                    joinColumn.referencedColumnName());
            propertyMeta.addJoinColumnMeta(meta);
        } else {
            JoinColumns joinColumns = field.getAnnotation(JoinColumns.class);
            if (joinColumns != null) {
                JoinColumn[] array = joinColumns.value();
                for (int i = 0; i < array.length; i++) {
                    JoinColumn jc = array[i];
                    JoinColumnMeta meta = new JoinColumnMeta(jc.name(), jc
                            .referencedColumnName());
                    if (i > 0
                            && (meta.getName() == null || meta
                                    .getReferencedColumnName() == null)) {
                        throw new JoinColumnNameAndReferencedColumnNameMandatoryRuntimeException(
                                entityMeta.getName(), propertyMeta.getName(),
                                i + 1);
                    }
                    propertyMeta.addJoinColumnMeta(meta);
                }
            }
        }
    }

    /**
     * 一対一の関連を処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     * @param oneToOne
     *            一対一関連
     */
    protected void doOneToOne(PropertyMeta propertyMeta, Field field,
            EntityMeta entityMeta, OneToOne oneToOne) {
        propertyMeta.setRelationshipType(RelationshipType.ONE_TO_ONE);
        Class<?> relationshipClass = field.getType();
        if (relationshipClass.getAnnotation(Entity.class) == null) {
            throw new RelationshipNotEntityRuntimeException(entityMeta
                    .getName(), propertyMeta.getName(), relationshipClass);
        }
        propertyMeta.setRelationshipClass(relationshipClass);
        String mappedBy = oneToOne.mappedBy();
        if (!StringUtil.isEmpty(mappedBy)) {
            if (propertyMeta.getJoinColumnMetaList().size() > 0) {
                throw new BothMappedByAndJoinColumnRuntimeException(entityMeta
                        .getName(), propertyMeta.getName());
            }
            propertyMeta.setMappedBy(mappedBy);
        }
    }

    /**
     * 一対多の関連を処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     * @param oneToMany
     *            一対多関連
     */
    protected void doOneToMany(PropertyMeta propertyMeta, Field field,
            EntityMeta entityMeta, OneToMany oneToMany) {
        propertyMeta.setRelationshipType(RelationshipType.ONE_TO_MANY);
        if (!List.class.isAssignableFrom(field.getType())) {
            throw new OneToManyNotListRuntimeException(entityMeta.getName(),
                    propertyMeta.getName());
        }
        Class<?> relationshipClass = ReflectionUtil.getElementTypeOfList(field
                .getGenericType());
        if (relationshipClass == null) {
            throw new OneToManyNotGenericsRuntimeException(
                    entityMeta.getName(), propertyMeta.getName());
        }
        if (relationshipClass.getAnnotation(Entity.class) == null) {
            throw new RelationshipNotEntityRuntimeException(entityMeta
                    .getName(), propertyMeta.getName(), relationshipClass);
        }
        propertyMeta.setRelationshipClass(relationshipClass);
        String mappedBy = oneToMany.mappedBy();
        if (!StringUtil.isEmpty(mappedBy)) {
            if (propertyMeta.getJoinColumnMetaList().size() > 0) {
                throw new BothMappedByAndJoinColumnRuntimeException(entityMeta
                        .getName(), propertyMeta.getName());
            }
            propertyMeta.setMappedBy(mappedBy);
        } else {
            throw new MappedByMandatoryRuntimeException(entityMeta.getName(),
                    propertyMeta.getName());
        }
    }

    /**
     * 多対一の関連を処理します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     * @param manyToOne
     *            多対一関連
     */
    protected void doManyToOne(PropertyMeta propertyMeta, Field field,
            EntityMeta entityMeta,
            @SuppressWarnings("unused") ManyToOne manyToOne) {
        propertyMeta.setRelationshipType(RelationshipType.MANY_TO_ONE);
        Class<?> relationshipClass = field.getType();
        if (relationshipClass.getAnnotation(Entity.class) == null) {
            throw new RelationshipNotEntityRuntimeException(entityMeta
                    .getName(), propertyMeta.getName(), relationshipClass);
        }
        propertyMeta.setRelationshipClass(relationshipClass);
    }

    /**
     * 関連用のクラスを返します。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     * @return 関連用のクラス
     * @throws OneToManyNotGenericsRuntimeException
     *             一対多の関連がジェネリクスのリストではない場合。
     */
    protected Class<?> getRelationshipClass(PropertyMeta propertyMeta,
            Field field, EntityMeta entityMeta)
            throws OneToManyNotGenericsRuntimeException {
        Class<?> clazz = field.getType();
        if (List.class.isAssignableFrom(clazz)) {
            clazz = ReflectionUtil.getElementTypeOfList(field.getGenericType());
            if (clazz == null) {
                throw new OneToManyNotGenericsRuntimeException(entityMeta
                        .getName(), propertyMeta.getName());
            }
        }
        return clazz;
    }

    /**
     * カスタマイズします。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     */
    @SuppressWarnings("unused")
    protected void doCustomize(PropertyMeta propertyMeta, Field field,
            EntityMeta entityMeta) {
    }

    /**
     * カラムメタデータファクトリを返します。
     * 
     * @return カラムメタデータファクトリ
     */
    public ColumnMetaFactory getColumnMetaFactory() {
        return columnMetaFactory;
    }

    /**
     * カラムメタデータファクトリを設定します。
     * 
     * @param columnMetaFactory
     *            カラムメタデータファクトリ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setColumnMetaFactory(ColumnMetaFactory columnMetaFactory) {
        this.columnMetaFactory = columnMetaFactory;
    }

    /**
     * 永続化層の命名規約を返します。
     * 
     * @return 永続化層の命名規約
     */
    public PersistenceConvention getPersistenceConvention() {
        return persistenceConvention;
    }

    /**
     * 永続化層の命名規約を設定します。
     * 
     * @param persistenceConvention
     *            永続化層の命名規約
     */
    public void setPersistenceConvention(
            PersistenceConvention persistenceConvention) {
        this.persistenceConvention = persistenceConvention;
    }
}