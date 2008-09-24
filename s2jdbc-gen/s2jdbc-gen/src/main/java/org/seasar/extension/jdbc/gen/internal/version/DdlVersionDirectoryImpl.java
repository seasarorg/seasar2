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

import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectory;
import org.seasar.extension.jdbc.gen.version.ManagedFile;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.StringConversionUtil;

/**
 * @author taedium
 * 
 */
public class DdlVersionDirectoryImpl implements DdlVersionDirectory {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(DdlVersionDirectoryImpl.class);

    /** createディレクトリの名前 */
    protected static String CREATE_DIR_NAME = "create";

    /** dropディレクトリの名前 */
    protected static String DROP_DIR_NAME = "drop";

    protected File versionDir;

    protected ManagedFile createDir;

    protected ManagedFile dropDir;

    protected int versionNo;

    protected String env;

    public DdlVersionDirectoryImpl(File baseDir, int versionNo,
            String versionNoPattern, String env) {
        if (baseDir == null) {
            throw new NullPointerException("baseDir");
        }
        if (versionNoPattern == null) {
            throw new NullPointerException("versionNoPattern");
        }
        this.versionNo = versionNo;
        this.env = env;
        String versionDirName = StringConversionUtil.toString(versionNo,
                versionNoPattern);
        versionDir = new File(baseDir, versionDirName);
        createDir = createManagedFile(CREATE_DIR_NAME);
        dropDir = createManagedFile(DROP_DIR_NAME);
    }

    public File asFile() {
        return versionDir;
    }

    public ManagedFile getCreateDirectory() {
        return createDir;
    }

    public ManagedFile getDropDirectory() {
        return dropDir;
    }

    public int getVersionNo() {
        return versionNo;
    }

    public boolean isFirstVersion() {
        return versionNo == 0;
    }

    protected ManagedFile createManagedFile(String path) {
        return new ManagedFileImpl(FileUtil.getCanonicalPath(versionDir), path,
                env);
    }

}
