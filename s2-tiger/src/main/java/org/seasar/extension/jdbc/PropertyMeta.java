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
package org.seasar.extension.jdbc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GenerationType;
import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.exception.IdentityGeneratorNotSupportedRuntimeException;
import org.seasar.extension.jdbc.exception.SequenceGeneratorNotSupportedRuntimeException;
import org.seasar.framework.util.ArrayMap;

/**
 * プロパティ用のメタデータです。
 * 
 * @author higa
 * 
 */
public class PropertyMeta {

    /**
     * 名前です。
     */
    protected String name;

    /**
     * プロパティクラスです。
     */
    protected Class<?> propertyClass;

    /**
     * フィールドです。
     */
    protected Field field;

    /**
     * カラムメタデータです。
     */
    protected ColumnMeta columnMeta;

    /**
     * 値タイプです。
     */
    protected ValueType valueType;

    /**
     * 識別子かどうかです。
     */
    protected boolean id;

    /**
     * IDを自動生成する方法です。
     */
    protected GenerationType generationType;

    /**
     * {@link GenerationType#IDENTITY}で識別子を自動生成するIDジェネレータです。
     */
    protected IdGenerator identityIdGenerator;

    /**
     * {@link GenerationType#SEQUENCE}で識別子を自動生成するIDジェネレータです。
     */
    protected IdGenerator sequenceIdGenerator;

    /**
     * {@link GenerationType#TABLE}で識別子を自動生成するIDジェネレータです。
     */
    protected IdGenerator tableIdGenerator;

    /**
     * フェッチタイプです。
     */
    protected FetchType fetchType;

    /**
     * 時制の種別です。
     */
    protected TemporalType temporalType;

    /**
     * enumの種別です。
     */
    protected EnumType enumType;

    /**
     * バージョン用かどうかです。
     */
    protected boolean version;

    /**
     * 一時的かどうかです。
     */
    protected boolean trnsient;

    /**
     * <code>LOB</code>かどうかです。
     */
    protected boolean lob;

    /**
     * 結合カラムメタデータのリストです。
     */
    protected List<JoinColumnMeta> joinColumnMetaList = new ArrayList<JoinColumnMeta>();

    /**
     * 関連タイプです。
     */
    protected RelationshipType relationshipType;

    /**
     * 関連の所有者側のプロパティ名です。
     */
    protected String mappedBy;

    /**
     * 関連クラスです。
     */
    protected Class<?> relationshipClass;

    /**
     * 追加情報のマップです。
     */
    protected ArrayMap additionalInfoMap = new ArrayMap();

    /**
     * 名前を返します。
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定します。
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * プロパティクラスを返します。
     * 
     * @return プロパティクラス
     */
    public Class<?> getPropertyClass() {
        return propertyClass;
    }

    /**
     * フィールドを返します。
     * 
     * @return フィールド
     */
    public Field getField() {
        return field;
    }

    /**
     * フィールドを設定します。
     * 
     * @param field
     *            フィールド
     */
    public void setField(Field field) {
        this.field = field;
        propertyClass = field.getType();
    }

    /**
     * カラムメタデータを返します。
     * 
     * @return カラムメタデータ
     */
    public ColumnMeta getColumnMeta() {
        return columnMeta;
    }

    /**
     * カラムメタデータを設定します。
     * 
     * @param columnMeta
     *            カラムメタデータ
     */
    public void setColumnMeta(ColumnMeta columnMeta) {
        this.columnMeta = columnMeta;
    }

    /**
     * 値タイプを返します。
     * 
     * @return 値タイプ
     */
    public ValueType getValueType() {
        return valueType;
    }

    /**
     * 値タイプを設定します。
     * 
     * @param valueType
     *            値タイプ
     */
    public void setValueType(ValueType valueType) {
        this.valueType = valueType;
    }

    /**
     * 識別子かどうかを返します。
     * 
     * @return 識別子かどうか
     */
    public boolean isId() {
        return id;
    }

    /**
     * 識別子かどうかを設定します。
     * 
     * @param id
     *            識別子かどうか
     */
    public void setId(boolean id) {
        this.id = id;
    }

    /**
     * 識別子を自動生成する方法を返します。
     * 
     * @return 識別子を自動生成する方法
     */
    public GenerationType getGenerationType() {
        return generationType;
    }

    /**
     * 識別子を自動生成する方法
     * 
     * @param generationType
     *            識別子を自動生成する方法
     */
    public void setGenerationType(GenerationType generationType) {
        this.generationType = generationType;
    }

    /**
     * 識別子を自動生成するIDジェネレータを設定します。
     * 
     * @return 識別子を自動生成するIDジェネレータ
     */
    public boolean hasIdGenerator() {
        return generationType != null;
    }

    /**
     * 識別子を自動生成するIDジェネレータを返します。
     * 
     * @param entityMeta
     *            エンティティメタデータ
     * @param dialect
     *            データベースの方言
     * @return 識別子を自動生成するIDジェネレータ
     */
    public IdGenerator getIdGenerator(EntityMeta entityMeta, DbmsDialect dialect) {
        switch (generationType == GenerationType.AUTO ? dialect
                .getDefaultGenerationType() : generationType) {
        case IDENTITY:
            if (!dialect.supportsIdentity()) {
                throw new IdentityGeneratorNotSupportedRuntimeException(
                        entityMeta.getName(), getName(), dialect.getName());
            }
            return identityIdGenerator;
        case SEQUENCE:
            if (!dialect.supportsSequence()) {
                throw new SequenceGeneratorNotSupportedRuntimeException(
                        entityMeta.getName(), getName(), dialect.getName());
            }
            return sequenceIdGenerator;
        case TABLE:
            return tableIdGenerator;
        }
        return null; // unreachable
    }

    /**
     * {@link GenerationType#IDENTITY}で識別子を自動生成するIDジェネレータを設定します。
     * 
     * @param idGenerator
     *            {@link GenerationType#IDENTITY}で識別子を自動生成するIDジェネレータ
     */
    public void setIdentityIdGenerator(IdGenerator idGenerator) {
        identityIdGenerator = idGenerator;
    }

    /**
     * {@link GenerationType#SEQUENCE}で識別子を自動生成するIDジェネレータを設定します。
     * 
     * @param idGenerator
     *            {@link GenerationType#SEQUENCE}で識別子を自動生成するIDジェネレータ
     */
    public void setSequenceIdGenerator(IdGenerator idGenerator) {
        sequenceIdGenerator = idGenerator;
    }

    /**
     * {@link GenerationType#TABLE}で識別子を自動生成するIDジェネレータを設定します。
     * 
     * @param idGenerator
     *            {@link GenerationType#TABLE}で識別子を自動生成するIDジェネレータ
     */
    public void setTableIdGenerator(IdGenerator idGenerator) {
        tableIdGenerator = idGenerator;
    }

    /**
     * フェッチタイプを返します。
     * 
     * @return フェッチタイプ
     */
    public FetchType getFetchType() {
        return fetchType;
    }

    /**
     * フェッチタイプを設定します。
     * 
     * @param fetchType
     *            フェッチタイプ
     */
    public void setFetchType(final FetchType fetchType) {
        this.fetchType = fetchType;
    }

    /**
     * フェッチタイプがEAGERなら{@literal true}を返します。
     * 
     * @return フェッチタイプがEAGERなら{@literal true}
     */
    public boolean isEager() {
        return fetchType != FetchType.LAZY;
    }

    /**
     * フェッチタイプがLAZYなら{@literal true}を返します。
     * 
     * @return フェッチタイプがLAZYなら{@literal true}
     */
    public boolean isLazy() {
        return fetchType == FetchType.LAZY;
    }

    /**
     * 時制の種別を返します。
     * 
     * @return 時制の種別
     */
    public TemporalType getTemporalType() {
        return temporalType;
    }

    /**
     * 時制の種別を設定します。
     * 
     * @param temporalType
     *            The temporalType to set.
     */
    public void setTemporalType(TemporalType temporalType) {
        this.temporalType = temporalType;
    }

    /**
     * enumの種別を返します。
     * 
     * @return enumの種別
     */
    public EnumType getEnumType() {
        return enumType;
    }

    /**
     * enumの種別を設定します。
     * 
     * @param enumType
     *            enumの種別
     */
    public void setEnumType(EnumType enumType) {
        this.enumType = enumType;
    }

    /**
     * 一時的かどうかを返します。
     * 
     * @return 一時的かどうか
     */
    public boolean isTransient() {
        return trnsient;
    }

    /**
     * 一時的かどうかを設定します。
     * 
     * @param tran
     *            一時的かどうか
     */
    public void setTransient(boolean tran) {
        this.trnsient = tran;
    }

    /**
     * バージョンチェック用かどうかを返します。
     * 
     * @return バージョンチェック用かどうか
     */
    public boolean isVersion() {
        return version;
    }

    /**
     * バージョンチェック用かどうかを設定します。
     * 
     * @param version
     *            バージョンチェック用かどうか
     */
    public void setVersion(boolean version) {
        this.version = version;
    }

    /**
     * <code>LOB</code>かどうかを返します。
     * 
     * @return <code>LOB</code>かどうか
     */
    public boolean isLob() {
        return lob;
    }

    /**
     * <code>LOB</code>かどうかを設定します。
     * 
     * @param lob
     *            <code>LOB</code>
     */
    public void setLob(boolean lob) {
        this.lob = lob;
    }

    /**
     * 結合カラムメタデータのリストを返します。
     * 
     * @return 結合カラムメタデータのリスト
     */
    public List<JoinColumnMeta> getJoinColumnMetaList() {
        return joinColumnMetaList;
    }

    /**
     * 結合カラムメタデータを追加します。
     * 
     * @param joinColumnMeta
     *            結合カラムメタデータ
     */
    public void addJoinColumnMeta(JoinColumnMeta joinColumnMeta) {
        joinColumnMetaList.add(joinColumnMeta);
    }

    /**
     * 関連の所有者側のプロパティ名を返します。
     * 
     * @return 関連の所有者側のプロパティ名
     */
    public String getMappedBy() {
        return mappedBy;
    }

    /**
     * 関連の所有者側のプロパティ名を設定します。
     * 
     * @param mappedBy
     *            関連の所有者側のプロパティ名
     */
    public void setMappedBy(String mappedBy) {
        this.mappedBy = mappedBy;
    }

    /**
     * 関連かどうかを返します。
     * 
     * @return 関連かどうか
     */
    public boolean isRelationship() {
        return relationshipType != null;
    }

    /**
     * 関連タイプを返します。
     * 
     * @return 関連タイプ
     */
    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    /**
     * 関連タイプを設定します。
     * 
     * @param relationshipType
     *            関連タイプ
     */
    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    /**
     * 関連クラスを返します。
     * 
     * @return 関連クラス
     */
    public Class<?> getRelationshipClass() {
        return relationshipClass;
    }

    /**
     * 関連クラスを設定します。
     * 
     * @param relationshipClass
     *            関連クラス
     */
    public void setRelationshipClass(Class<?> relationshipClass) {
        this.relationshipClass = relationshipClass;
    }

    /**
     * 名前に対応した追加情報を返します。
     * 
     * @param name
     *            名前
     * 
     * @return 追加情報
     */
    public Object getAdditionalInfo(String name) {
        return additionalInfoMap.get(name);
    }

    /**
     * 位置に対応した追加情報を返します。
     * 
     * @param index
     *            位置
     * @return 追加情報
     */
    public Object getAdditionalInfo(int index) {
        return additionalInfoMap.get(index);
    }

    /**
     * 追加情報のサイズを返します。
     * 
     * @return 追加情報のサイズ
     */
    public int getAdditionalInfoSize() {
        return additionalInfoMap.size();
    }

    /**
     * 追加情報を追加します。
     * 
     * @param name
     *            名前
     * @param additionalInfo
     *            追加情報
     */
    public void addAdditionalInfo(String name, Object additionalInfo) {
        additionalInfoMap.put(name, additionalInfo);
    }
}