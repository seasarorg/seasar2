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
package org.seasar.extension.jdbc.gen;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.seasar.framework.util.ClassUtil;

/**
 * エンティティクラスのモデルです。
 * 
 * @author taedium
 */
public class EntityModel {

    /** インポートパッケージ名のソートされたセット */
    protected SortedSet<String> importPackageNameSet = new TreeSet<String>();

    /** パッケージ名 */
    protected String packageName;

    /** クラスの単純名 */
    protected String shortClassName;

    /** エンティティ記述 */
    protected EntityDesc entityDesc;

    /** テーブルがカタログやスキーマで修飾されている場合{@code true} */
    protected boolean tableQualified;

    /**
     * パッケージ名を返します。
     * 
     * @return パッケージ名
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * パッケージ名を設定します。
     * 
     * @param packageName
     *            パッケージ名
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * クラスの単純名を返します。
     * 
     * @return クラスの単純名
     */
    public String getShortClassName() {
        return shortClassName;
    }

    /**
     * クラスの単純名を設定します。
     * 
     * @param shortClassName
     *            クラスの単純名
     */
    public void setShortClassName(String shortClassName) {
        this.shortClassName = shortClassName;
    }

    /**
     * インポートパッケージ名のソートされたセットを返します。
     * 
     * @return インポートパッケージ名のソートされたセット
     */
    public SortedSet<String> getImportPackageNameSet() {
        return Collections.unmodifiableSortedSet(importPackageNameSet);
    }

    /**
     * パッケージ名を追加します。
     * 
     * @param name
     *            パッケージ名
     */
    public void addImportPackageName(String name) {
        importPackageNameSet.add(name);
    }

    /**
     * エンティティ記述を返します。
     * 
     * @return エンティティ記述
     */
    public EntityDesc getEntityDesc() {
        return entityDesc;
    }

    /**
     * エンティティ記述を設定します。
     * 
     * @param entityDesc
     *            エンティティ記述
     */
    public void setEntityDesc(EntityDesc entityDesc) {
        this.entityDesc = entityDesc;
    }

    /**
     * テーブルがカタログやスキーマで修飾されている場合{@code true}を返します。
     * 
     * @return テーブルがカタログやスキーマで修飾されている場合{@code true}
     */
    public boolean isTableQualified() {
        return tableQualified;
    }

    /**
     * テーブルがカタログやスキーマで修飾されている場合{@code true}を設定します。
     * 
     * @param tableQualified
     *            テーブルがカタログやスキーマで修飾されている場合{@code true}
     */
    public void setTableQualified(boolean tableQualified) {
        this.tableQualified = tableQualified;
    }

    /**
     * 長さが有効の場合{@code true}を返します。
     * 
     * @param attributeDesc
     *            属性記述
     * @return 長さが有効の場合{@code true}を
     */
    public boolean isLengthAvailable(AttributeDesc attributeDesc) {
        return !isNumber(attributeDesc) && attributeDesc.getLength() > 0;
    }

    /**
     * 精度が有効の場合{@code true}を返します。
     * 
     * @param attributeDesc
     *            属性記述
     * @return 精度が有効の場合{@code true}
     */
    public boolean isPrecisionAvailable(AttributeDesc attributeDesc) {
        return isNumber(attributeDesc) && attributeDesc.getPrecision() > 0;
    }

    /**
     * スケールが有効の場合{@code true}を返します。
     * 
     * @param attributeDesc
     *            属性記述
     * @return スケールが有効の場合{@code true}
     */
    public boolean isScaleAvailable(AttributeDesc attributeDesc) {
        Class<?> clazz = ClassUtil.getWrapperClassIfPrimitive(attributeDesc
                .getAttributeClass());
        return clazz == BigDecimal.class || clazz == Float.class
                || clazz == Double.class;
    }

    /**
     * 数値を表す場合{@code true}を返します。
     * 
     * @param attributeDesc
     *            属性記述
     * @return 数値を表す場合{@code true}
     */
    protected boolean isNumber(AttributeDesc attributeDesc) {
        Class<?> clazz = ClassUtil.getWrapperClassIfPrimitive(attributeDesc
                .getAttributeClass());
        return Number.class.isAssignableFrom(clazz);
    }
}