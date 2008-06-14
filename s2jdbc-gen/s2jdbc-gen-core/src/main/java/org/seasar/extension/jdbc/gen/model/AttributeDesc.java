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
package org.seasar.extension.jdbc.gen.model;

import javax.persistence.TemporalType;

import org.seasar.framework.util.ClassUtil;

/**
 * エンティティの属性記述です。
 * 
 * @author taedium
 */
public class AttributeDesc {

    /** 名前 */
    protected String name;

    /** 属性のクラス */
    protected Class<?> attributeClass;

    /** 識別子であれば{@code true} */
    protected boolean id;

    /** 時制の種別 */
    protected TemporalType temporalType;

    /** バージョンであれば{@code true} */
    protected boolean version;

    /** 一時的であれば{@code true} */
    protected boolean trnsient;

    /** {@code LOB}であれば{@code true} */
    protected boolean lob;

    /** カラムの名前 */
    protected String columnName;

    /** {@code null}可能ならば{@code true} */
    protected boolean nullable;

    /**
     * インスタンスを構築します。
     */
    public AttributeDesc() {
    }

    /**
     * 名前を返します。
     * 
     * @return 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定します。
     * 
     * @param name
     *            名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 属性のクラスを返します。
     * 
     * @return 属性のクラス
     */
    public Class<?> getAttributeClass() {
        return attributeClass;
    }

    /**
     * 属性のクラスを設定します。
     * 
     * @param attributeClass
     *            属性のクラス
     */
    public void setAttributeClass(Class<?> attributeClass) {
        this.attributeClass = attributeClass;
    }

    /**
     * 属性のクラスがプリミティブ型の場合に、参照型に変換して返します。
     * 
     * @return 属性のクラスに対応する参照型
     */
    public Class<?> getAttributeClassAsRefType() {
        return ClassUtil.getWrapperClassIfPrimitive(attributeClass);
    }

    /**
     * 識別子であれば{@code true}を返します。
     * 
     * @return 識別子であれば{@code true}、そうでなければ{@code false}
     */
    public boolean isId() {
        return id;
    }

    /**
     * 識別子であれば{@code true}を設定します。
     * 
     * @param id
     *            識別子であれば{@code true}
     */
    public void setId(boolean id) {
        this.id = id;
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
     *            時制の種別
     */
    public void setTemporalType(TemporalType temporalType) {
        this.temporalType = temporalType;
    }

    /**
     * バージョンであれば{@code true}を返します。
     * 
     * @return バージョンであれば{@code true}、そうでなければ{@code false}
     */
    public boolean isVersion() {
        return version;
    }

    /**
     * バージョンであれば{@code true}を設定します。
     * 
     * @param version
     *            バージョンであれば{@code true}
     */
    public void setVersion(boolean version) {
        this.version = version;
    }

    /**
     * 一時的であれば{@code true}を返します。
     * 
     * @return 一時的であれば{@code true}、そうでなければ{@code false}
     */
    public boolean isTransient() {
        return trnsient;
    }

    /**
     * 一時的であれば{@code true}を設定します。
     * 
     * @param trnsient
     *            一時的であれば{@code true}
     */
    public void setTransient(boolean trnsient) {
        this.trnsient = trnsient;
    }

    /**
     * {@code LOB}であれば{@code true}を返します。
     * 
     * @return {@code LOB}であれば{@code true}、そうでなければ{@code false}
     */
    public boolean isLob() {
        return lob;
    }

    /**
     * {@code LOB}であれば{@code true}を設定します。
     * 
     * @param lob
     *            {@code LOB}であれば{@code true}
     */
    public void setLob(boolean lob) {
        this.lob = lob;
    }

    /**
     * カラムの名前を返します。
     * 
     * @return カラムの名前
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * カラムの名前を設定します。
     * 
     * @param columnName
     *            カラムの名前
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * {@code null}可能の場合{@code true}を返します。
     * 
     * @return {@code null}可能の場合{@code true}、そうでない場合{@code false}
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * {@code null}可能の場合{@code true}を設定します。
     * 
     * @param nullable
     *            {@code null}可能の場合{@code true}
     */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

}
