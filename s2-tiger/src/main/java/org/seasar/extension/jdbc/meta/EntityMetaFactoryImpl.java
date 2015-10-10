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

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.JoinColumnMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.PropertyMetaFactory;
import org.seasar.extension.jdbc.RelationshipType;
import org.seasar.extension.jdbc.TableMeta;
import org.seasar.extension.jdbc.TableMetaFactory;
import org.seasar.extension.jdbc.exception.FieldDuplicatedRuntimeException;
import org.seasar.extension.jdbc.exception.JoinColumnAutoConfigurationRuntimeException;
import org.seasar.extension.jdbc.exception.JoinColumnNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.ManyToOneFKNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.MappedByNotIdenticalRuntimeException;
import org.seasar.extension.jdbc.exception.MappedByPropertyNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.NonEntityRuntimeException;
import org.seasar.extension.jdbc.exception.OneToOneFKNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.ReferencedColumnNameNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.UnsupportedInheritanceRuntimeException;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.util.ArrayMap;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;
import org.seasar.framework.util.ModifierUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 * 
 */
public class EntityMetaFactoryImpl implements EntityMetaFactory {

    /**
     * エンティティメタデータのマップです。
     */
    protected ConcurrentHashMap<String, EntityMeta> entityMetaMap = new ConcurrentHashMap<String, EntityMeta>(
            200);

    /**
     * テーブルメタデータのファクトリです。
     */
    protected TableMetaFactory tableMetaFactory;

    /**
     * プロパティメタデータのファクトリです。
     */
    protected PropertyMetaFactory propertyMetaFactory;

    /**
     * 初期化されたかどうかです。
     */
    protected volatile boolean initialized;

    /**
     * 永続化層の規約です。
     */
    protected PersistenceConvention persistenceConvention;

    public EntityMeta getEntityMeta(Class<?> entityClass) {
        if (entityClass == null) {
            throw new NullPointerException("entityClass");
        }
        if (!initialized) {
            initialize();
        }
        EntityMeta entityMeta = getEntityMetaInternal(entityClass);
        if (!entityMeta.isRelationshipResolved()) {
            synchronized (entityMeta) {
                if (!entityMeta.isRelationshipResolved()) {
                    resolveRelationship(entityMeta);
                }
            }
        }
        return entityMeta;
    }

    /**
     * 内部的に使われるエンティティメタデータを返すメソッドです。
     * 
     * @param entityClass
     *            エンティティクラス
     * @return エンティティメタデータ
     */
    protected EntityMeta getEntityMetaInternal(Class<?> entityClass) {
        String className = entityClass.getName();
        EntityMeta entityMeta = entityMetaMap.get(className);
        if (entityMeta != null) {
            return entityMeta;
        }
        entityMeta = createEntityMeta(entityClass);
        EntityMeta entityMeta2 = entityMetaMap.putIfAbsent(className,
                entityMeta);
        return entityMeta2 != null ? entityMeta2 : entityMeta;
    }

    /**
     * エンティティメタデータを作成します。
     * 
     * @param entityClass
     *            エンティティクラス
     * @return エンティティメタデータ
     * @throws NonEntityRuntimeException
     *             クラスがエンティティではない場合。
     */
    protected EntityMeta createEntityMeta(Class<?> entityClass)
            throws NonEntityRuntimeException {
        Entity entity = entityClass.getAnnotation(Entity.class);
        if (entity == null) {
            throw new NonEntityRuntimeException(entityClass);
        }
        EntityMeta entityMeta = new EntityMeta();
        doEntityClass(entityMeta, entityClass);
        doName(entityMeta, entityClass, entity);
        doTableMeta(entityMeta, entityClass);
        doPropertyMeta(entityMeta, entityClass);
        doCustomize(entityMeta, entityClass);
        return entityMeta;
    }

    /**
     * エンティティクラスを処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param entityClass
     *            エンティティクラス
     */
    protected void doEntityClass(EntityMeta entityMeta, Class<?> entityClass) {
        entityMeta.setEntityClass(entityClass);
    }

    /**
     * 名前を処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param entityClass
     *            エンティティクラス
     * @param entity
     *            エンティティアノテーション
     */
    protected void doName(EntityMeta entityMeta, Class<?> entityClass,
            Entity entity) {
        if (!StringUtil.isEmpty(entity.name())) {
            entityMeta.setName(entity.name());
        } else {
            String entityName = fromClassToEntityName(entityClass);
            entityMeta.setName(entityName);
        }
    }

    /**
     * クラスをエンティティ名に変換します。
     * 
     * @param entityClass
     *            エンティティクラス
     * @return エンティティ名
     */
    protected String fromClassToEntityName(Class<?> entityClass) {
        return ClassUtil.getShortClassName(entityClass);
    }

    /**
     * テーブルメタデータを処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param entityClass
     *            エンティティクラス
     */
    protected void doTableMeta(EntityMeta entityMeta, Class<?> entityClass) {
        TableMeta tableMeta = tableMetaFactory.createTableMeta(entityClass,
                entityMeta);
        entityMeta.setTableMeta(tableMeta);
    }

    /**
     * プロパティメタデータを処理します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param entityClass
     *            エンティティクラス
     */
    protected void doPropertyMeta(EntityMeta entityMeta, Class<?> entityClass) {
        Field[] fields = getFields(entityClass);
        for (Field f : fields) {
            f.setAccessible(true);
            entityMeta.addPropertyMeta(propertyMetaFactory.createPropertyMeta(
                    f, entityMeta));
        }
    }

    /**
     * フィールドの配列を返します。
     * 
     * @param entityClass
     *            エンティティクラス
     * @return フィールドの配列
     */
    protected Field[] getFields(Class<?> entityClass) {
        ArrayMap fields = new ArrayMap();
        for (Field f : ClassUtil.getDeclaredFields(entityClass)) {
            if (!ModifierUtil.isInstanceField(f)) {
                continue;
            }
            fields.put(f.getName(), f);
        }

        for (Class<?> clazz = entityClass.getSuperclass(); clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            if (clazz.isAnnotationPresent(Entity.class)) {
                throw new UnsupportedInheritanceRuntimeException(entityClass);
            }
            if (clazz.isAnnotationPresent(MappedSuperclass.class)) {
                for (Field f : ClassUtil.getDeclaredFields(clazz)) {
                    if (!ModifierUtil.isInstanceField(f)) {
                        continue;
                    }
                    String name = f.getName();
                    if (!fields.containsKey(name)) {
                        fields.put(name, f);
                    } else {
                        throw new FieldDuplicatedRuntimeException(f);
                    }
                }
            }
        }
        return (Field[]) fields.toArray(new Field[fields.size()]);
    }

    /**
     * カスタマイズします。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param entityClass
     *            エンティティクラス
     */
    @SuppressWarnings("unused")
    protected void doCustomize(EntityMeta entityMeta, Class<?> entityClass) {
    }

    /**
     * 関連を解決します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void resolveRelationship(EntityMeta entityMeta) {
        int size = entityMeta.getPropertyMetaSize();
        for (int i = 0; i < size; i++) {
            PropertyMeta propertyMeta = entityMeta.getPropertyMeta(i);
            if (!propertyMeta.isRelationship()) {
                continue;
            }
            if (propertyMeta.getMappedBy() != null) {
                checkMappedBy(propertyMeta, entityMeta);
            }
            if (propertyMeta.getRelationshipType() == RelationshipType.MANY_TO_ONE
                    || propertyMeta.getRelationshipType() == RelationshipType.ONE_TO_ONE
                    && propertyMeta.getMappedBy() == null) {
                resolveJoinColumn(entityMeta, propertyMeta);
            }
        }
        entityMeta.setRelationshipResolved(true);
    }

    /**
     * mappedByで指定されたプロパティが存在するかチェックします。
     * 
     * @param propertyMeta
     *            プロパティメタデータ
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void checkMappedBy(PropertyMeta propertyMeta,
            EntityMeta entityMeta) {
        String mappedBy = propertyMeta.getMappedBy();
        Class<?> relationshipClass = propertyMeta.getRelationshipClass();
        EntityMeta relationshipEntityMeta = getEntityMetaInternal(relationshipClass);
        if (relationshipEntityMeta.hasPropertyMeta(mappedBy)) {
            Field f = relationshipEntityMeta.getPropertyMeta(mappedBy)
                    .getField();
            if (entityMeta.getEntityClass() != f.getType()) {
                throw new MappedByNotIdenticalRuntimeException(entityMeta
                        .getName(), propertyMeta.getName(), propertyMeta
                        .getMappedBy(), entityMeta.getEntityClass(),
                        propertyMeta.getRelationshipClass(), propertyMeta
                                .getMappedBy(), f.getType());
            }
        } else {
            throw new MappedByPropertyNotFoundRuntimeException(entityMeta
                    .getName(), propertyMeta.getName(), propertyMeta
                    .getMappedBy(), propertyMeta.getRelationshipClass());
        }
    }

    /**
     * 関連のJoinColumnを解決します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param propertyMeta
     *            プロパティメタデータ
     */
    protected void resolveJoinColumn(EntityMeta entityMeta,
            PropertyMeta propertyMeta) {
        List<JoinColumnMeta> jcmList = propertyMeta.getJoinColumnMetaList();
        EntityMeta inverseEntityMeta = getEntityMetaInternal(propertyMeta
                .getRelationshipClass());
        if (jcmList.size() == 0) {
            if (inverseEntityMeta.getIdPropertyMetaList().size() != 1) {
                throw new JoinColumnNotFoundRuntimeException(entityMeta
                        .getName(), propertyMeta.getName());
            }
            propertyMeta.addJoinColumnMeta(new JoinColumnMeta());
        }
        if (jcmList.size() == 1) {
            JoinColumnMeta joinColumnMeta = jcmList.get(0);
            PropertyMeta inverseIdPropertyMeta = null;
            if (inverseEntityMeta.getIdPropertyMetaList().size() == 1) {
                inverseIdPropertyMeta = inverseEntityMeta
                        .getIdPropertyMetaList().get(0);
            }
            if (joinColumnMeta.getName() == null) {
                if (inverseIdPropertyMeta == null) {
                    throw new JoinColumnAutoConfigurationRuntimeException(
                            entityMeta.getName(), propertyMeta.getName(),
                            inverseEntityMeta.getName());
                }
                joinColumnMeta
                        .setName(persistenceConvention
                                .fromPropertyNameToColumnName(propertyMeta
                                        .getName())
                                + "_"
                                + inverseIdPropertyMeta.getColumnMeta()
                                        .getName());
            }
            if (joinColumnMeta.getReferencedColumnName() == null) {
                if (inverseIdPropertyMeta == null) {
                    throw new JoinColumnAutoConfigurationRuntimeException(
                            entityMeta.getName(), propertyMeta.getName(),
                            inverseEntityMeta.getName());
                }
                joinColumnMeta.setReferencedColumnName(inverseIdPropertyMeta
                        .getColumnMeta().getName());
            }
        }
        for (JoinColumnMeta jcm : jcmList) {
            if (!entityMeta.hasColumnPropertyMeta(jcm.getName())) {
                if (propertyMeta.getRelationshipType() == RelationshipType.MANY_TO_ONE) {
                    throw new ManyToOneFKNotFoundRuntimeException(entityMeta
                            .getName(), propertyMeta.getName(), jcm.getName());
                }
                throw new OneToOneFKNotFoundRuntimeException(entityMeta
                        .getName(), propertyMeta.getName(), jcm.getName());
            }
            if (!inverseEntityMeta.hasColumnPropertyMeta(jcm
                    .getReferencedColumnName())) {
                throw new ReferencedColumnNameNotFoundRuntimeException(
                        entityMeta.getName(), propertyMeta.getName(),
                        inverseEntityMeta.getName(), jcm
                                .getReferencedColumnName());
            }
        }
    }

    /**
     * テーブルメタデータファクトリを設定します。
     * 
     * @param tableMetaFactory
     *            テーブルメタデータファクトリ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setTableMetaFactory(TableMetaFactory tableMetaFactory) {
        this.tableMetaFactory = tableMetaFactory;
    }

    /**
     * プロパティメタデータファクトリを設定します。
     * 
     * @param propertyMetaFactory
     *            プロパティメタデータファクトリ
     */
    @Binding(bindingType = BindingType.MUST)
    public void setPropertyMetaFactory(PropertyMetaFactory propertyMetaFactory) {
        this.propertyMetaFactory = propertyMetaFactory;
    }

    /**
     * 永続化層の規約を設定します
     * 
     * @param persistenceConvention
     *            永続化層の規約
     */
    @Binding(bindingType = BindingType.MUST)
    public void setPersistenceConvention(
            PersistenceConvention persistenceConvention) {
        this.persistenceConvention = persistenceConvention;
    }

    /**
     * 初期化を行ないます。
     */
    @InitMethod
    public void initialize() {
        DisposableUtil.add(new Disposable() {

            public void dispose() {
                clear();
            }
        });
        initialized = true;
    }

    /**
     * キャッシュをクリアします。
     */
    public void clear() {
        entityMetaMap.clear();
        initialized = false;
    }
}