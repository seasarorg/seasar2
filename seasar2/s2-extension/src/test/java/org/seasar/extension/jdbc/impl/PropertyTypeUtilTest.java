/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.seasar.extension.jdbc.PropertyType;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.util.ResultSetUtil;

/**
 * @author taedium
 */
public class PropertyTypeUtilTest extends S2TestCase {

    /**
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        include("j2ee.dicon");
    }

    
    protected void tearDown() throws Exception {
        PropertyTypeUtil.setPreserveUnderscore(false);
        super.tearDown();
    }


    /**
     * 
     * @throws Exception
     */
    public void testCreatePropertyTypes() throws Exception {
        PreparedStatement ps = ConnectionUtil.prepareStatement(getConnection(),
                "select d_name, active from dept3");
        try {
            ResultSet rs = ps.executeQuery();
            try {
                PropertyType[] propertyTypes = PropertyTypeUtil
                        .createPropertyTypes(rs.getMetaData());
                assertEquals(2, propertyTypes.length);
                PropertyType p = propertyTypes[0];
                assertEquals("dname", p.getPropertyName().toLowerCase());
                assertEquals("d_name", p.getColumnName().toLowerCase());
                p = propertyTypes[1];
                assertEquals("active", p.getPropertyName().toLowerCase());
                assertEquals("active", p.getColumnName().toLowerCase());
            } finally {
                ResultSetUtil.close(rs);
            }
        } finally {
            ps.close();
        }
    }

    /**
     * 
     * @throws Exception
     */
    public void testCreatePropertyTypes_preserveUnderscore() throws Exception {
        PropertyTypeUtil.setPreserveUnderscore(true);

        PreparedStatement ps = ConnectionUtil.prepareStatement(getConnection(),
                "select d_name, active from dept3");
        try {
            ResultSet rs = ps.executeQuery();
            try {
                PropertyType[] propertyTypes = PropertyTypeUtil
                        .createPropertyTypes(rs.getMetaData());
                assertEquals(2, propertyTypes.length);
                PropertyType p = propertyTypes[0];
                assertEquals("d_name", p.getPropertyName().toLowerCase());
                assertEquals("d_name", p.getColumnName().toLowerCase());
                p = propertyTypes[1];
                assertEquals("active", p.getPropertyName().toLowerCase());
                assertEquals("active", p.getColumnName().toLowerCase());
            } finally {
                ResultSetUtil.close(rs);
            }
        } finally {
            ps.close();
        }
    }

}
