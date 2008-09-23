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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.gen.internal.util.DefaultExcludesFilenameFilter;
import org.seasar.extension.jdbc.gen.internal.util.FileComparetor;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.version.DdlVersionOpDirectory;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class DdlVersionOpDirectoryImpl implements DdlVersionOpDirectory {

    protected File dir;

    protected File envDir;

    public DdlVersionOpDirectoryImpl(File parent, String operationName,
            String env) {
        if (parent == null) {
            throw new NullPointerException("parent");
        }
        if (operationName == null) {
            throw new NullPointerException("operationName");
        }
        String parentPath = FileUtil.getCanonicalPath(parent);
        this.dir = new File(parentPath, operationName);
        if (env != null) {
            this.envDir = new File(parentPath + "#" + env, operationName);
        }
    }

    public File asFile() {
        return dir;
    }

    public File getChildFile(String name) {
        File parent = envDir != null ? envDir : dir;
        return new File(parent, name);
    }

    public List<File> list() {
        final Map<String, File> fileMap = new LinkedHashMap<String, File>();
        traverseDirectory(envDir, fileMap);
        traverseDirectory(dir, fileMap);
        File[] files = fileMap.values().toArray(new File[fileMap.size()]);
        return Arrays.asList(files);
    }

    protected void traverseDirectory(final File dir,
            final Map<String, File> fileMap) {
        if (dir == null) {
            return;
        }
        FileUtil.traverseDirectory(dir, new DefaultExcludesFilenameFilter(),
                new FileComparetor(), new FileUtil.FileHandler() {

                    String prefix = FileUtil.getCanonicalPath(dir);

                    public void handle(File file) {
                        String filePath = FileUtil.getCanonicalPath(file);
                        String key = StringUtil.trimPrefix(filePath, prefix);
                        if (!fileMap.containsKey(key)) {
                            fileMap.put(key, file);
                        }
                    }
                });
    }

}
