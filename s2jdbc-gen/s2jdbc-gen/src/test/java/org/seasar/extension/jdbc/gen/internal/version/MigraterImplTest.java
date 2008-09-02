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
package org.seasar.extension.jdbc.gen.internal.version;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.internal.sql.SqlUnitExecutorImpl;
import org.seasar.extension.jdbc.gen.sql.SqlExecutionContext;
import org.seasar.extension.jdbc.gen.version.Migrater;
import org.seasar.extension.jdbc.gen.version.SchemaInfoTable;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.util.ResourceUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class MigraterImplTest {

    private MigraterImpl migrater;

    private File baseDir;

    /**
     * 
     */
    @Before
    public void setUp() {
        SqlUnitExecutorImpl sqlUnitExecutor = new SqlUnitExecutorImpl(
                new MockDataSource(), null, false);
        SchemaInfoTable schemaInfoTable = new SchemaInfoTable() {

            public int getVersionNo() {
                return 10;
            }
        };
        String path = getClass().getPackage().getName().replace('.', '/')
                + "/migrate";
        baseDir = ResourceUtil.getResourceAsFile(path);
        File versionFile = new File(baseDir, "ddl-version.txt");

        DdlVersionDirectoryImpl directory = new DdlVersionDirectoryImpl(
                baseDir, versionFile, "v000");
        migrater = new MigraterImpl(sqlUnitExecutor, schemaInfoTable,
                directory, "latest", "ut");
    }

    /**
     * 
     */
    @Test
    public void testMigrate() {
        final List<File> dropFileList = new ArrayList<File>();
        final List<File> createFileList = new ArrayList<File>();

        migrater.migrate(new Migrater.Callback() {

            public void drop(SqlExecutionContext sqlExecutionContext, File file) {
                dropFileList.add(file);
            }

            public void create(SqlExecutionContext sqlExecutionContext,
                    File file) {
                createFileList.add(file);
            }
        });

        assertEquals(2, dropFileList.size());
        File v010 = new File(baseDir, "v010");
        File drop = new File(v010, "drop");
        assertEquals(new File(drop, "drop.sql"), dropFileList.get(0));
        assertEquals(new File(drop, "drop_ut.sql"), dropFileList.get(1));

        assertEquals(1, createFileList.size());
        File v011 = new File(baseDir, "v011");
        File create = new File(v011, "create");
        assertEquals(new File(create, "create.sql"), createFileList.get(0));
    }
}
