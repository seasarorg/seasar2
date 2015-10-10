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
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.internal.dialect.StandardGenDialect;
import org.seasar.extension.jdbc.gen.internal.event.GenDdlListenerImpl;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectory;
import org.seasar.extension.jdbc.gen.version.DdlVersionIncrementer;
import org.seasar.framework.mock.sql.MockDataSource;
import org.seasar.framework.util.ResourceUtil;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class DdlVersionIncrementerImplTest {

    private DdlVersionIncrementerImpl incrementer;

    private File baseDir;

    /**
     * 
     */
    @Before
    public void setUp() {
        String path = getClass().getPackage().getName().replace('.', '/')
                + "/migrate";
        baseDir = ResourceUtil.getResourceAsFile(path);
        File versionFile = new File(baseDir, "ddl-version.txt");
        DdlVersionDirectoryTreeImpl directory = new DdlVersionDirectoryTreeImpl(
                baseDir, versionFile, "v000", null);
        incrementer = new DdlVersionIncrementerImpl(directory,
                new GenDdlListenerImpl(), new StandardGenDialect(),
                new MockDataSource(), Collections.<String> emptyList(),
                Collections.<String> emptyList()) {

            @Override
            protected void makeDirectory(DdlVersionDirectory versionDir) {
            }

            @Override
            protected void copyDirectory(DdlVersionDirectory current,
                    DdlVersionDirectory next) {
            }

            @Override
            protected void copyDropDirectory(DdlVersionDirectory current,
                    DdlVersionDirectory next) {
            }

            @Override
            protected void incrementVersionNo(String changeLog) {
            }
        };
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testIncrement() throws Exception {
        final Object[] values = new Object[3];

        incrementer.increment("changeLog",
                new DdlVersionIncrementer.Callback() {

                    public void execute(DdlVersionDirectory versionDirectory) {
                        values[0] = versionDirectory.getCreateDirectory()
                                .asFile();
                        values[1] = versionDirectory.getDropDirectory()
                                .asFile();
                        values[2] = versionDirectory.getVersionNo();
                    }
                });

        File v012 = new File(baseDir, "v012");
        assertEquals(new File(v012, "create"), values[0]);
        assertEquals(new File(v012, "drop"), values[1]);
        assertEquals(new Integer(12), values[2]);
    }
}
