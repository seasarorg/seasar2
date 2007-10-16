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
package org.seasar.extension.jdbc.meta;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.seasar.extension.jdbc.ColumnMetaFactory;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JoinColumnMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.PropertyMetaFactory;
import org.seasar.extension.jdbc.RelationshipType;
import org.seasar.extension.jdbc.exception.BothMappedByAndJoinColumnRuntimeException;
import org.seasar.extension.jdbc.exception.IdGeneratorNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.JoinColumnNameAndReferencedColumnNameMandatoryRuntimeException;
import org.seasar.extension.jdbc.exception.MappedByMandatoryRuntimeException;
import org.seasar.extension.jdbc.exception.MappedByNotIdenticalRuntimeException;
import org.seasar.extension.jdbc.exception.MappedByPropertyNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.NonRelationshipRuntimeException;
import org.seasar.extension.jdbc.exception.OneToManyNotGenericsRuntimeException;
import org.seasar.extension.jdbc.exception.OneToManyNotListRuntimeException;
import org.seasar.extension.jdbc.exception.RelationshipNotEntityRuntimeException;
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
            doColumnMeta(propertyMeta, field, entityMeta);
            if (propertyMeta.getColumnMeta() != null) {
                doId(propertyMeta, field, entityMeta);
                doVersion(propertyMeta, field, entityMeta);
                doLob(propertyMeta, field, entityMeta);
            } else {
                doRelationship(propertyMeta, field, entityMeta);
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
            @SuppressWarnings("unused")
            EntityMeta entityMeta) {
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
            @SuppressWarnings("unused")
            EntityMeta entityMeta) {
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
        if (ValueTypes.isSimpleType(propertyMeta.getPropertyClass())) {
            propertyMeta.setColumnMeta(columnMetaFactory.createColumnMeta(
                    field, entityMeta, propertyMeta));
        }
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
     * @return {@link GenerationType#SEQUENCE}方式で識別子の値を自動生成するIDジェネレータが存在した場合に<code>true</code>
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
                        || name.equals(sequenceGenerator.name())) {
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
     * @return {@link GenerationType#TABLE}方式で識別子の値を自動生成するIDジェネレータが存在した場合に<code>true</code>
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
                        || name.equals(tableGenerator.name())) {
                    return false;
                }
            }
        }
        propertyMeta.setTableIdGenerator(new TableIdGenerator(entityMeta,
                propertyMeta, tableGenerator));
        return true;
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
            @SuppressWarnings("unused")
            EntityMeta entityMeta) {
        if (field.getAnnotation(Version.class) == null) {
            return;
        }
        Class<?> clazz = ClassUtil.getWrapperClassIfPrimitive(field.getType());
        if (!Number.class.isAssignableFrom(clazz)) {
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
            @SuppressWarnings("unused")
            EntityMeta entityMeta) {
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
            @SuppressWarnings("unused")
            EntityMeta entityMeta) {
        propertyMeta.setLob(field.getAnnotation(Lob.class) != null);
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
     */
    protected void doRelationship(PropertyMeta propertyMeta, Field field,
            EntityMeta entityMeta) {
        doJoinColumn(propertyMeta, field, entityMeta);
        OneToOne oneToOne = field.getAnnotation(OneToOne.class);
        if (oneToOne != null) {
            doOneToOne(propertyMeta, field, entityMeta, oneToOne);
        } else {
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            if (oneToMany != null) {
                doOneToMany(propertyMeta, field, entityMeta, oneToMany);
            } else {
                ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
                if (manyToOne != null) {
                    doManyToOne(propertyMeta, field, entityMeta, manyToOne);
                } else {
                    throw new NonRelationshipRuntimeException(entityMeta
                            .getName(), propertyMeta.getName());
                }
            }
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
            checkMappedBy(propertyMeta, field, entityMeta);
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
            checkMappedBy(propertyMeta, field, entityMeta);
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
            EntityMeta entityMeta, @SuppressWarnings("unused")
            ManyToOne manyToOne) {
        propertyMeta.setRelationshipType(RelationshipType.MANY_TO_ONE);
        Class<?> relationshipClass = field.getType();
        if (relationshipClass.getAnnotation(Entity.class) == null) {
            throw new RelationshipNotEntityRuntimeException(entityMeta
                    .getName(), propertyMeta.getName(), relationshipClass);
        }
        propertyMeta.setRelationshipClass(relationshipClass);
    }

    /**
     * mappedByで指定されたプロパティが存在するかチェックします。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param field
     *            フィールド
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void checkMappedBy(PropertyMeta propertyMeta,
            @SuppressWarnings("unused")
            Field field, EntityMeta entityMeta) {
        String fName = persistenceConvention
                .fromPropertyNameToFieldName(propertyMeta.getMappedBy());
        try {
            Field f = propertyMeta.getRelationshipClass().getDeclaredField(
                    fName);
            if (entityMeta.getEntityClass() != f.getType()) {
                throw new MappedByNotIdenticalRuntimeException(entityMeta
                        .getName(), propertyMeta.getName(), propertyMeta
                        .getMappedBy(), entityMeta.getEntityClass(),
                        propertyMeta.getRelationshipClass(), propertyMeta
                                .getMappedBy(), f.getType());
            }
        } catch (NoSuchFieldException e) {
            throw new MappedByPropertyNotFoundRuntimeException(entityMeta
                    .getName(), propertyMeta.getName(), propertyMeta
                    .getMappedBy(), propertyMeta.getRelationshipClass());
        }
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