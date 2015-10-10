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
package org.seasar.extension.jdbc.gen.extension.svn;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.seasar.extension.jdbc.gen.event.GenDdlEvent;
import org.seasar.extension.jdbc.gen.internal.version.DdlVersionDirectoryImpl;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectory;
import org.seasar.framework.util.ResourceUtil;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import static junit.framework.Assert.*;

/**
 * @author koichik
 */
public class GenDdlSvnProcessorTest {

    static final String MIGRATE_PATH = "src/test/resources/org/seasar/extension/jdbc/gen/internal/version/migrate";

    File projectDir;

    DdlVersionDirectory currentVersion;

    DdlVersionDirectory nextVersion;

    /**
     * 
     */
    @Before
    public void setUp() {
        projectDir = ResourceUtil.getBuildDir(getClass());
        while (!projectDir.getName().equals("s2jdbc-gen")) {
            projectDir = projectDir.getParentFile();
        }

        currentVersion = new DdlVersionDirectoryImpl(new File(projectDir,
                MIGRATE_PATH), 20, "v000", null);

        nextVersion = new DdlVersionDirectoryImpl(new File(projectDir,
                MIGRATE_PATH), 30, "v000", null);
    }

    /**
     * 
     */
    @After
    public void tearDown() {
        SVNClientManager cm = SVNClientManager.newInstance();
        SVNWCClient wc = cm.getWCClient();
        try {
            wc.doDelete(nextVersion.asFile(), true, true, false);
        } catch (SVNException ignore) {
        }
    }

    /**
     * @throws Exception
     */
    @Test
    @Ignore("This test fails under subversion 1.7 or higher")
    public void test() throws Exception {
        // ACT preCreateCurrentVersionDir
        GenDdlEvent ev = new GenDdlEvent(this, currentVersion,
                nextVersion);
        GenDdlSvnProcessor svnProcessor = new GenDdlSvnProcessor();
        svnProcessor.preCreateNextVersionDir(ev);

        // ARRANGE
        File nextVersionDir = nextVersion.asFile();
        assertFalse(nextVersionDir.exists());
        nextVersionDir.mkdir();

        // ACT postCreateCurrentVersionDir
        svnProcessor.postCreateNextVersionDir(ev);

        // ASSERT
        SVNClientManager cm = SVNClientManager.newInstance();
        SVNStatusClient sc = cm.getStatusClient();
        SVNStatus st = sc.doStatus(nextVersionDir, false);
        assertNotNull(st);
        assertSame(SVNStatusType.STATUS_ADDED, st.getContentsStatus());

        // ACT preCreateTargetFile
        ev = new GenDdlEvent(this, currentVersion, nextVersion,
                "create");
        svnProcessor.preCreateTargetFile(ev);

        // ARRANGE
        File dir = nextVersion.getCreateDirectory().asFile();
        dir.mkdir();

        // ACT postCreateTargetFile
        svnProcessor.postCreateTargetFile(ev);

        // ASSERT
        st = sc.doStatus(dir, false);
        assertNotNull(st);
        assertSame(SVNStatusType.STATUS_ADDED, st.getContentsStatus());

        // ACT preCreateTargetFile
        ev = new GenDdlEvent(this, currentVersion, nextVersion,
                "create/aaa.txt");
        svnProcessor.preCreateTargetFile(ev);

        // ASSERT
        File target = new File(nextVersionDir, "create/aaa.txt");
        assertTrue(target.exists());
        st = sc.doStatus(target, false);
        assertNotNull(st);
        assertSame(SVNStatusType.STATUS_ADDED, st.getContentsStatus());

        // ACT postCreateTargetFile
        svnProcessor.postCreateTargetFile(ev);

        // ACT preRemoveCurrentVersionDir
        ev = new GenDdlEvent(this, currentVersion, nextVersion);
        svnProcessor.preRemoveNextVersionDir(ev);

        // ASSERT
        st = sc.doStatus(nextVersionDir, false);
        assertNotNull(st);
        assertSame(SVNStatusType.STATUS_UNVERSIONED, st.getContentsStatus());

        // ACT postRemoveCurrentVersionDir
        svnProcessor.postRemoveNextVersionDir(ev);
    }

}
