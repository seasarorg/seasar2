/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.util;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.util.CaseInsensitiveMap;
import org.seasar.framework.util.CaseInsensitiveSet;

/**
 * @author higa
 * @author manhole
 */
public final class DatabaseMetaDataUtil {

    private DatabaseMetaDataUtil() {
    }

    public static String[] getPrimaryKeys(DatabaseMetaData dbMetaData,
            String tableName) {

        Set set = getPrimaryKeySet(dbMetaData, tableName);
        return (String[]) set.toArray(new String[set.size()]);
    }

    public static Set getPrimaryKeySet(DatabaseMetaData dbMetaData,
            String tableName) {
        final String schema;
        int index = tableName.indexOf('.');
        if (index >= 0) {
            schema = tableName.substring(0, index);
            tableName = tableName.substring(index + 1);
        } else {
            schema = getUserName(dbMetaData);
        }
        final String convertedTableName = convertIdentifier(dbMetaData,
                tableName);
        Set set = new CaseInsensitiveSet();
        addPrimaryKeys(dbMetaData, convertIdentifier(dbMetaData, schema),
                convertedTableName, set);
        if (set.size() == 0) {
            addPrimaryKeys(dbMetaData, schema, tableName, set);
        }
        if (set.size() == 0 && schema != null) {
            addPrimaryKeys(dbMetaData, null, convertedTableName, set);
            if (set.size() == 0) {
                addPrimaryKeys(dbMetaData, null, tableName, set);
            }
        }
        return set;
    }

    public static Set getPrimaryKeySet(DatabaseMetaData dbMetaData,
            String schema, String tableName) {
        Set set = new CaseInsensitiveSet();
        addPrimaryKeys(dbMetaData, schema, tableName, set);
        return set;
    }

    public static String getUserName(DatabaseMetaData dbMetaData) {
        try {
            return dbMetaData.getUserName();
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    private static void addPrimaryKeys(DatabaseMetaData dbMetaData,
            String schema, String tableName, Set set) {
        try {
            ResultSet rs = dbMetaData.getPrimaryKeys(null, schema, tableName);
            while (rs.next()) {
                set.add(rs.getString(4));
            }
            rs.close();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    public static String[] getColumns(DatabaseMetaData dbMetaData,
            String tableName) {

        Map map = getColumnMap(dbMetaData, tableName);
        String[] ret = new String[map.size()];
        int index = 0;
        for (Iterator i = map.values().iterator(); i.hasNext();) {
            ColumnDesc cd = (ColumnDesc) i.next();
            ret[index++] = cd.getName();
        }
        return ret;
    }

    public static CaseInsensitiveMap getColumnMap(DatabaseMetaData dbMetaData,
            String tableName) {
        final String schema;
        int index = tableName.indexOf('.');
        if (index >= 0) {
            schema = tableName.substring(0, index);
            tableName = tableName.substring(index + 1);
        } else {
            schema = getUserName(dbMetaData);
        }
        final String convertedTableName = convertIdentifier(dbMetaData,
                tableName);
        CaseInsensitiveMap map = new CaseInsensitiveMap();
        addColumns(dbMetaData, convertIdentifier(dbMetaData, schema),
                convertedTableName, map);
        if (map.size() == 0) {
            addColumns(dbMetaData, schema, tableName, map);
        }
        if (map.size() == 0 && schema != null) {
            addColumns(dbMetaData, null, convertedTableName, map);
            if (map.size() == 0) {
                addColumns(dbMetaData, null, tableName, map);
            }
        }
        return map;
    }

    public static CaseInsensitiveMap getColumnMap(DatabaseMetaData dbMetaData,
            String schema, String tableName) {
        CaseInsensitiveMap map = new CaseInsensitiveMap();
        addColumns(dbMetaData, schema, tableName, map);
        return map;
    }

    private static void addColumns(DatabaseMetaData dbMetaData, String schema,
            String tableName, Map map) {
        try {
            ResultSet rs = dbMetaData.getColumns(null, schema, tableName, null);
            while (rs.next()) {
                ColumnDesc cd = new ColumnDesc();
                cd.setName(rs.getString(4));
                cd.setSqlType(rs.getInt(5));
                map.put(cd.getName(), cd);
            }
            rs.close();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    public static String convertIdentifier(DatabaseMetaData dbMetaData,
            String identifier) {

        if (identifier == null) {
            return null;
        }
        if (!supportsMixedCaseIdentifiers(dbMetaData)) {
            if (storesUpperCaseIdentifiers(dbMetaData)) {
                return identifier.toUpperCase();
            }
            return identifier.toLowerCase();
        }
        return identifier;
    }

    public static boolean supportsMixedCaseIdentifiers(
            DatabaseMetaData dbMetaData) {

        try {
            return dbMetaData.supportsMixedCaseIdentifiers();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    public static boolean storesUpperCaseIdentifiers(DatabaseMetaData dbMetaData) {
        try {
            return dbMetaData.storesUpperCaseIdentifiers();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    public static boolean storesLowerCaseIdentifiers(DatabaseMetaData dbMetaData) {
        try {
            return dbMetaData.storesLowerCaseIdentifiers();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    public static boolean supportsSchemasInTableDefinitions(
            DatabaseMetaData dbMetaData) {
        try {
            return dbMetaData.supportsSchemasInTableDefinitions();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    public static String getDatabaseProductName(DatabaseMetaData dbMetaData) {
        try {
            return dbMetaData.getDatabaseProductName();
        } catch (SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }
}