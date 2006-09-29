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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.unit.S2TestCase;

public class DatabaseMetaDataTemplateTest extends S2TestCase {

    protected void setUp() {
        include("jdbc.dicon");
    }

    public void testDoExecute() throws Exception {
        MyTemplate template = new MyTemplate(getDataSource());
        System.out.println(template.execute());
    }

    private static class MyTemplate extends DatabaseMetaDataTemplate {

        public MyTemplate(DataSource dataSource) {
            super(dataSource);
        }

        protected Object doExecute(DatabaseMetaData dbMetaData)
                throws SQLException {
            List result = new ArrayList();
            ResultSet rs = dbMetaData.getSchemas();
            ResultSetMetaData rsMetaData = rs.getMetaData();
            while (rs.next()) {
                List row = new ArrayList();
                for (int i = 1; i <= rsMetaData.getColumnCount(); ++i) {
                    row.add(rs.getObject(i));
                }
                result.add(row);
            }
            return result;
        }
    }
}