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

    private SqlUnitExecutorImpl sqlUnitExecutor;

    private DdlVersionDirectoryTreeImpl directory;

    private File baseDir;

    /**
     * 
     */
    @Before
    public void setUp() {
        sqlUnitExecutor = new SqlUnitExecutorImpl(new MockDataSource(), null,
                false);

        String path = getClass().getPackage().getName().replace('.', '/')
                + "/migrate";
        baseDir = ResourceUtil.getResourceAsFile(path);
        File versionFile = new File(baseDir, "ddl-version.txt");
        directory = new DdlVersionDirectoryTreeImpl(baseDir, versionFile,
                "v000", null);
    }

    /**
     * 
     */
    @Test
    public void testMigrate_latestVersion() {
        SchemaInfoTable schemaInfoTable = new SchemaInfoTable() {

            public int getVersionNo() {
                return 9;
            }

            public void setVersionNo(int versionNo) {
            }
        };
        MigraterImpl migrater = new MigraterImpl(sqlUnitExecutor,
                schemaInfoTable, directory, "latest", "ut");
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

        assertEquals(3, dropFileList.size());
        File v009 = new File(baseDir, "v009");
        File drop = new File(v009, "drop");
        assertEquals(new File(drop, "aaa.sql"), dropFileList.get(0));
        assertEquals(new File(drop, "bbb.sql"), dropFileList.get(1));
        assertEquals(new File(drop, "ccc.sql"), dropFileList.get(2));

        assertEquals(1, createFileList.size());
        File v011 = new File(baseDir, "v011");
        File create = new File(v011, "create");
        assertEquals(new File(create, "aaa.sql"), createFileList.get(0));
    }

    /**
     * 
     */
    @Test
    public void testMigrate_specificVersion() {
        SchemaInfoTable schemaInfoTable = new SchemaInfoTable() {

            public int getVersionNo() {
                return 9;
            }

            public void setVersionNo(int versionNo) {
            }
        };
        MigraterImpl migrater = new MigraterImpl(sqlUnitExecutor,
                schemaInfoTable, directory, "11", "ut");
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

        assertEquals(3, dropFileList.size());
        File v009 = new File(baseDir, "v009");
        File drop = new File(v009, "drop");
        assertEquals(new File(drop, "aaa.sql"), dropFileList.get(0));
        assertEquals(new File(drop, "bbb.sql"), dropFileList.get(1));
        assertEquals(new File(drop, "ccc.sql"), dropFileList.get(2));

        assertEquals(1, createFileList.size());
        File v011 = new File(baseDir, "v011");
        File create = new File(v011, "create");
        assertEquals(new File(create, "aaa.sql"), createFileList.get(0));
    }

    /**
     * 
     */
    @Test
    public void testMigrate_nextVersion() {
        SchemaInfoTable schemaInfoTable = new SchemaInfoTable() {

            public int getVersionNo() {
                return 9;
            }

            public void setVersionNo(int versionNo) {
            }
        };
        MigraterImpl migrater = new MigraterImpl(sqlUnitExecutor,
                schemaInfoTable, directory, "next", "ut");
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

        assertEquals(3, dropFileList.size());
        File v009 = new File(baseDir, "v009");
        File drop = new File(v009, "drop");
        assertEquals(new File(drop, "aaa.sql"), dropFileList.get(0));
        assertEquals(new File(drop, "bbb.sql"), dropFileList.get(1));
        assertEquals(new File(drop, "ccc.sql"), dropFileList.get(2));

        assertEquals(1, createFileList.size());
        File v010 = new File(baseDir, "v010");
        File create = new File(v010, "create");
        assertEquals(new File(create, "aaa.sql"), createFileList.get(0));
    }

    /**
     * 
     */
    @Test
    public void testMigrate_nextVersion_GE_LatestVersion() {
        SchemaInfoTable schemaInfoTable = new SchemaInfoTable() {

            public int getVersionNo() {
                return 11;
            }

            public void setVersionNo(int versionNo) {
            }
        };
        MigraterImpl migrater = new MigraterImpl(sqlUnitExecutor,
                schemaInfoTable, directory, "next", "ut");
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

        assertEquals(3, dropFileList.size());
        File v011 = new File(baseDir, "v011");
        File drop = new File(v011, "drop");
        assertEquals(new File(drop, "aaa.sql"), dropFileList.get(0));
        assertEquals(new File(drop, "bbb.sql"), dropFileList.get(1));
        assertEquals(new File(drop, "ccc.sql"), dropFileList.get(2));

        assertEquals(1, createFileList.size());
        File create = new File(v011, "create");
        assertEquals(new File(create, "aaa.sql"), createFileList.get(0));
    }

    /**
     * 
     */
    @Test
    public void testMigrate_previousVersion() {
        SchemaInfoTable schemaInfoTable = new SchemaInfoTable() {

            public int getVersionNo() {
                return 9;
            }

            public void setVersionNo(int versionNo) {
            }
        };
        MigraterImpl migrater = new MigraterImpl(sqlUnitExecutor,
                schemaInfoTable, directory, "previous", "ut");
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

        assertEquals(3, dropFileList.size());
        File v009 = new File(baseDir, "v009");
        File drop = new File(v009, "drop");
        assertEquals(new File(drop, "aaa.sql"), dropFileList.get(0));
        assertEquals(new File(drop, "bbb.sql"), dropFileList.get(1));
        assertEquals(new File(drop, "ccc.sql"), dropFileList.get(2));

        assertEquals(1, createFileList.size());
        File v008 = new File(baseDir, "v008");
        File create = new File(v008, "create");
        assertEquals(new File(create, "aaa.sql"), createFileList.get(0));
    }

    /**
     * 
     */
    @Test
    public void testMigrate_previousVersion_LE_zero() {
        SchemaInfoTable schemaInfoTable = new SchemaInfoTable() {

            public int getVersionNo() {
                return 0;
            }

            public void setVersionNo(int versionNo) {
            }
        };
        MigraterImpl migrater = new MigraterImpl(sqlUnitExecutor,
                schemaInfoTable, directory, "previous", "ut");
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

        assertEquals(3, dropFileList.size());
        File v000 = new File(baseDir, "v000");
        File drop = new File(v000, "drop");
        assertEquals(new File(drop, "aaa.sql"), dropFileList.get(0));
        assertEquals(new File(drop, "bbb.sql"), dropFileList.get(1));
        assertEquals(new File(drop, "ccc.sql"), dropFileList.get(2));

        assertEquals(0, createFileList.size());
    }
}
