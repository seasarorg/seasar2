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
package org.seasar.framework.convention.impl;

import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.util.AssertionUtil;
import org.seasar.framework.util.StringUtil;

/**
 * {@link PersistenceConvention}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class PersistenceConventionImpl implements PersistenceConvention {

    private String ignoreTablePrefix;

    private boolean noNameConversion = false;

    private String primaryKeySuffix = "Id";

    private String versionSuffix = "Version";

    public String fromTableNameToEntityName(String tableName) {
        AssertionUtil.assertNotNull("tableName", tableName);
        if (noNameConversion) {
            return tableName;
        }
        return StringUtil.camelize(StringUtil.trimPrefix(tableName,
                ignoreTablePrefix));
    }

    public String fromEntityNameToTableName(String entityName) {
        AssertionUtil.assertNotNull("entityName", entityName);
        if (noNameConversion) {
            return entityName;
        }
        String tableName = StringUtil.decamelize(entityName);
        if (ignoreTablePrefix != null) {
            tableName = ignoreTablePrefix + tableName;
        }
        return tableName;
    }

    public String fromColumnNameToPropertyName(String columnName) {
        AssertionUtil.assertNotNull("columnName", columnName);
        if (noNameConversion) {
            return columnName;
        }
        return StringUtil.decapitalize(StringUtil.camelize(columnName));
    }

    public String fromPropertyNameToColumnName(String propertyName) {
        AssertionUtil.assertNotNull("propertyName", propertyName);
        if (noNameConversion) {
            return propertyName;
        }
        return StringUtil.decamelize(propertyName);
    }

    public boolean isPrimaryKey(String entityName, String propertyName) {
        AssertionUtil.assertNotNull("entityName", entityName);
        AssertionUtil.assertNotNull("propertyName", propertyName);
        return propertyName.equalsIgnoreCase(entityName + primaryKeySuffix)
                || propertyName.equalsIgnoreCase(primaryKeySuffix);
    }

    public boolean isVersion(String entityName, String propertyName) {
        AssertionUtil.assertNotNull("entityName", entityName);
        AssertionUtil.assertNotNull("propertyName", propertyName);
        return propertyName.equalsIgnoreCase(entityName + versionSuffix)
                || propertyName.equalsIgnoreCase(versionSuffix);
    }

    /**
     * 無視するテーブルの<code>prefix</code>を返します。
     * 
     * @return 無視するテーブルの<code>prefix</code>
     */
    public String getIgnoreTablePrefix() {
        return ignoreTablePrefix;
    }

    /**
     * 無視するテーブルの<code>prefix</code>を設定します。
     * 
     * @param ignoreTablePrefix
     */
    public void setIgnoreTablePrefix(String ignoreTablePrefix) {
        this.ignoreTablePrefix = ignoreTablePrefix;
    }

    /**
     * 名前を変換しないかどうかを返します。
     * 
     * @return 名前を変換しないかどうか
     */
    public boolean isNoNameConversion() {
        return noNameConversion;
    }

    /**
     * 名前を変換しないかどうかを設定します。
     * 
     * @param noNameConversion
     */
    public void setNoNameConversion(boolean noNameConversion) {
        this.noNameConversion = noNameConversion;
    }

    /**
     * プライマリキーのサフィックスを返します。
     * 
     * @return プライマリキーのサフィックス
     */
    public String getPrimaryKeySuffix() {
        return primaryKeySuffix;
    }

    /**
     * プライマリキーのサフィックスを設定します。
     * 
     * @param primaryKeySuffix
     *            プライマリキーのサフィックス
     */
    public void setPrimaryKeySuffix(String primaryKeySuffix) {
        this.primaryKeySuffix = primaryKeySuffix;
    }

    /**
     * バージョン用プロパティのサフィックスを返します。
     * 
     * @return バージョン用プロパティのサフィックス
     */
    public String getVersionSuffix() {
        return versionSuffix;
    }

    /**
     * バージョン用プロパティのサフィックスを設定します。
     * 
     * @param versionSuffix
     *            バージョン用プロパティのサフィックス
     */
    public void setVersionSuffix(String versionSuffix) {
        this.versionSuffix = versionSuffix;
    }
}