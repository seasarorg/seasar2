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

    public String fromFieldNameToPropertyName(String fieldName) {
        return fieldName;
    }

    public String fromPropertyNameToFieldName(String propertyName) {
        return propertyName;
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
}