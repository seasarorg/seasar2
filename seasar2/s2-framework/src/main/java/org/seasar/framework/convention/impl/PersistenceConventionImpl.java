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
 * @author higa
 * 
 */
public class PersistenceConventionImpl implements PersistenceConvention {

    private String ignoreTablePrefix;

    private String idName = "id";

    private String versionName = "version";

    private String insertedName = "inserted";

    private String updatedName = "updated";

    private String deletedName = "deleted";

    private boolean useEntityNameForId = true;

    private boolean useEntityNameForVersion = true;

    private boolean useEntityNameForInserted = true;

    private boolean useEntityNameForUpdated = true;

    private boolean useEntityNameForDeleted = true;

    private boolean noNameConversion = false;

    /**
     * @return Returns the ignoreTablePrefix.
     */
    public String getIgnoreTablePrefix() {
        return ignoreTablePrefix;
    }

    /**
     * @param ignoreTablePrefix
     *            The ignoreTablePrefix to set.
     */
    public void setIgnoreTablePrefix(String ignoreTablePrefix) {
        this.ignoreTablePrefix = ignoreTablePrefix;
    }

    /**
     * @return Returns the noNameConversion.
     */
    public boolean isNoNameConversion() {
        return noNameConversion;
    }

    /**
     * @param noNameConversion
     *            The noNameConversion to set.
     */
    public void setNoNameConversion(boolean noNameConversion) {
        this.noNameConversion = noNameConversion;
    }

    /**
     * @return Returns the deletedName.
     */
    public String getDeletedName() {
        return deletedName;
    }

    /**
     * @param deletedName
     *            The deletedName to set.
     */
    public void setDeletedName(String deletedName) {
        this.deletedName = deletedName;
    }

    /**
     * @return Returns the idName.
     */
    public String getIdName() {
        return idName;
    }

    /**
     * @param idName
     *            The idName to set.
     */
    public void setIdName(String idName) {
        this.idName = idName;
    }

    /**
     * @return Returns the insertedName.
     */
    public String getInsertedName() {
        return insertedName;
    }

    /**
     * @param insertedName
     *            The insertedName to set.
     */
    public void setInsertedName(String insertedName) {
        this.insertedName = insertedName;
    }

    /**
     * @return Returns the updatedName.
     */
    public String getUpdatedName() {
        return updatedName;
    }

    /**
     * @param updatedName
     *            The updatedName to set.
     */
    public void setUpdatedName(String updatedName) {
        this.updatedName = updatedName;
    }

    /**
     * @return Returns the versionName.
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * @param versionName
     *            The versionName to set.
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * @return Returns the useEntityNameForDeleted.
     */
    public boolean isUseEntityNameForDeleted() {
        return useEntityNameForDeleted;
    }

    /**
     * @param useEntityNameForDeleted
     *            The useEntityNameForDeleted to set.
     */
    public void setUseEntityNameForDeleted(boolean useEntityNameForDeleted) {
        this.useEntityNameForDeleted = useEntityNameForDeleted;
    }

    /**
     * @return Returns the useEntityNameForId.
     */
    public boolean isUseEntityNameForId() {
        return useEntityNameForId;
    }

    /**
     * @param useEntityNameForId
     *            The useEntityNameForId to set.
     */
    public void setUseEntityNameForId(boolean useEntityNameForId) {
        this.useEntityNameForId = useEntityNameForId;
    }

    /**
     * @return Returns the useEntityNameForInserted.
     */
    public boolean isUseEntityNameForInserted() {
        return useEntityNameForInserted;
    }

    /**
     * @param useEntityNameForInserted
     *            The useEntityNameForInserted to set.
     */
    public void setUseEntityNameForInserted(boolean useEntityNameForInserted) {
        this.useEntityNameForInserted = useEntityNameForInserted;
    }

    /**
     * @return Returns the useEntityNameForUpdated.
     */
    public boolean isUseEntityNameForUpdated() {
        return useEntityNameForUpdated;
    }

    /**
     * @param useEntityNameForUpdated
     *            The useEntityNameForUpdated to set.
     */
    public void setUseEntityNameForUpdated(boolean useEntityNameForUpdated) {
        this.useEntityNameForUpdated = useEntityNameForUpdated;
    }

    /**
     * @return Returns the useEntityNameForVersion.
     */
    public boolean isUseEntityNameForVersion() {
        return useEntityNameForVersion;
    }

    /**
     * @param useEntityNameForVersion
     *            The useEntityNameForVersion to set.
     */
    public void setUseEntityNameForVersion(boolean useEntityNameForVersion) {
        this.useEntityNameForVersion = useEntityNameForVersion;
    }

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

    public boolean isDeleted(String entityName, String propertyName) {
        AssertionUtil.assertNotNull("entityName", entityName);
        AssertionUtil.assertNotNull("propertyName", propertyName);
        if (useEntityNameForDeleted) {
            return propertyName.equals(StringUtil.decapitalize(entityName)
                    + StringUtil.capitalize(deletedName));
        }
        return propertyName.equals(deletedName);
    }

    public boolean isId(String entityName, String propertyName) {
        AssertionUtil.assertNotNull("entityName", entityName);
        AssertionUtil.assertNotNull("propertyName", propertyName);
        if (useEntityNameForId) {
            return propertyName.equals(StringUtil.decapitalize(entityName)
                    + StringUtil.capitalize(idName));
        }
        return propertyName.equals(idName);
    }

    public boolean isInserted(String entityName, String propertyName) {
        AssertionUtil.assertNotNull("entityName", entityName);
        AssertionUtil.assertNotNull("propertyName", propertyName);
        if (useEntityNameForInserted) {
            return propertyName.equals(StringUtil.decapitalize(entityName)
                    + StringUtil.capitalize(insertedName));
        }
        return propertyName.equals(insertedName);
    }

    public boolean isUpdated(String entityName, String propertyName) {
        AssertionUtil.assertNotNull("entityName", entityName);
        AssertionUtil.assertNotNull("propertyName", propertyName);
        if (useEntityNameForUpdated) {
            return propertyName.equals(StringUtil.decapitalize(entityName)
                    + StringUtil.capitalize(updatedName));
        }
        return propertyName.equals(updatedName);
    }

    public boolean isVersion(String entityName, String propertyName) {
        AssertionUtil.assertNotNull("entityName", entityName);
        AssertionUtil.assertNotNull("propertyName", propertyName);
        if (useEntityNameForVersion) {
            return propertyName.equals(StringUtil.decapitalize(entityName)
                    + StringUtil.capitalize(versionName));
        }
        return propertyName.equals(versionName);
    }
}
