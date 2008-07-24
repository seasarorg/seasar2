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
package org.seasar.extension.jdbc.gen.version;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.seasar.extension.jdbc.gen.VersionManager;
import org.seasar.extension.jdbc.gen.exception.NextVersionDirExistsRuntimeException;
import org.seasar.extension.jdbc.gen.util.FileUtil;

/**
 * @author taedium
 * 
 */
public class VersionManagerImpl implements VersionManager {

    protected File versionManagementDir;

    protected VersionFile versionFile;

    public VersionManagerImpl(File destDir, String versionFileName,
            String versionNoPattern) {
        if (destDir == null) {
            throw new NullPointerException("versionManagementDir");
        }
        if (versionFileName == null) {
            throw new NullPointerException("versionFileName");
        }
        if (versionNoPattern == null) {
            throw new NullPointerException("versionNoPattern");
        }
        this.versionManagementDir = destDir;
        this.versionFile = new VersionFile(new File(destDir, versionFileName),
                versionNoPattern);
    }

    public void increment(VersionUnit versionUnit) {
        File currentVerDir = getCurrentVersionDir();
        File nextVerDir = getNextVersionDir();
        try {
            if (currentVerDir != null && currentVerDir.exists()) {
                FileUtil.copyDirectory(currentVerDir, nextVerDir, new Filter());
            }
            versionUnit.execute(nextVerDir, versionFile.getNextVersionNo());
            versionFile.increment();
        } catch (RuntimeException e) {
            if (nextVerDir.exists()) {
                FileUtil.deleteDirectory(nextVerDir);
            }
            throw e;
        }
    }

    protected File getCurrentVersionDir() {
        String name = versionFile.getCurrentVersionName();
        if (name != null) {
            return new File(versionManagementDir, name);
        }
        return null;
    }

    protected File getNextVersionDir() {
        String name = versionFile.getNextVersionName();
        File nextVerDir = new File(versionManagementDir, name);
        if (nextVerDir.exists()) {
            throw new NextVersionDirExistsRuntimeException(
                    nextVerDir.getPath(), versionFile.getPath());
        }
        return nextVerDir;
    }

    protected static class Filter implements FilenameFilter {

        protected static Pattern excludePattern;

        static {
            List<String> excules = new ArrayList<String>();
            excules.add(".*~");
            excules.add("#.*#");
            excules.add("\\.#.*");
            excules.add("%*%");
            excules.add("\\._.*");
            excules.add("CVS");
            excules.add("\\.cvsignore");
            excules.add("SCCS");
            excules.add("vssver.scc");
            excules.add("\\.svn");
            excules.add("\\.DS_Store");
            StringBuilder buf = new StringBuilder();
            buf.append("(");
            for (String s : excules) {
                buf.append(s);
                buf.append("|");
            }
            buf.setLength(buf.length() - 1);
            buf.append(")");
            excludePattern = Pattern.compile(buf.toString());
        }

        public boolean accept(File dir, String name) {
            if (excludePattern.matcher(name).matches()) {
                return false;
            }
            return true;
        }
    }
}
