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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.gen.internal.util.DefaultExcludesFilenameFilter;
import org.seasar.extension.jdbc.gen.internal.util.FileComparetor;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.version.ManagedFile;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class ManagedFileImpl implements ManagedFile {

    protected FileHolder fileHolder;

    protected FileHolder envNamedFileHolder;

    /**
     * 
     */
    public ManagedFileImpl(String basePath, String path, String env) {
        if (basePath == null) {
            throw new NullPointerException("basePath");
        }
        if (path == null) {
            throw new NullPointerException("path");
        }
        fileHolder = new FileHolder(basePath, basePath, path, env);
        if (env != null) {
            envNamedFileHolder = new FileHolder(basePath + "#" + env, basePath,
                    path, env);
        }
    }

    protected FileHolder getFileHolder() {
        return envNamedFileHolder != null ? envNamedFileHolder : fileHolder;
    }

    protected File getFile() {
        return getFileHolder().file;
    }

    public File asFile() {
        return getFile();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#getFileList()
     */
    public List<File> listFiles() {
        final Map<String, File> fileMap = new LinkedHashMap<String, File>();
        if (envNamedFileHolder != null) {
            traverseDirectory(envNamedFileHolder, fileMap);
        }
        traverseDirectory(fileHolder, fileMap);
        File[] files = fileMap.values().toArray(new File[fileMap.size()]);
        return Arrays.asList(files);
    }

    protected void traverseDirectory(final FileHolder fileHolder,
            final Map<String, File> fileMap) {

        FileUtil.traverseDirectory(fileHolder.file,
                new DefaultExcludesFilenameFilter(), new FileComparetor(),
                new FileUtil.FileHandler() {

                    public void handle(File file) {
                        String canonicalPath = FileUtil.getCanonicalPath(file);
                        String path = StringUtil.trimPrefix(canonicalPath,
                                fileHolder.actualBasePath + File.separator);
                        if (!fileMap.containsKey(path)) {
                            fileMap.put(path, file);
                        }
                    }
                });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#getChild(java
     * .lang.String)
     */
    public ManagedFile createChild(String path) {
        FileHolder holder = getFileHolder();
        return new ManagedFileImpl(holder.logicalBasePath, holder.path
                + File.separator + path, holder.env);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.extension.jdbc.gen.internal.version.ManagedFile#canRead()
     */
    public boolean canRead() {
        return getFile().canRead();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#canWrite()
     */
    public boolean canWrite() {
        return getFile().canWrite();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#compareTo(
     * java.io.File)
     */
    public int compareTo(File pathname) {
        return getFile().compareTo(pathname);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#createNewFile
     * ()
     */
    public boolean createNewFile() throws IOException {
        return getFile().createNewFile();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.extension.jdbc.gen.internal.version.ManagedFile#delete()
     */
    public boolean delete() {
        return getFile().delete();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#deleteOnExit()
     */
    public void deleteOnExit() {
        getFile().deleteOnExit();
    }

    /**
     * @param obj
     * @return
     * @see java.io.File#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return getFile().equals(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.extension.jdbc.gen.internal.version.ManagedFile#exists()
     */
    public boolean exists() {
        return getFile().exists();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#getAbsoluteFile
     * ()
     */
    public File getAbsoluteFile() {
        return getFile().getAbsoluteFile();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#getAbsolutePath
     * ()
     */
    public String getAbsolutePath() {
        return getFile().getAbsolutePath();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#getCanonicalFile
     * ()
     */
    public File getCanonicalFile() throws IOException {
        return getFile().getCanonicalFile();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#getCanonicalPath
     * ()
     */
    public String getCanonicalPath() throws IOException {
        return getFile().getCanonicalPath();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.extension.jdbc.gen.internal.version.ManagedFile#getName()
     */
    public String getName() {
        return getFile().getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#getParent()
     */
    public String getParent() {
        return getFile().getParent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#getParentFile
     * ()
     */
    public File getParentFile() {
        return getFile().getParentFile();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.extension.jdbc.gen.internal.version.ManagedFile#getPath()
     */
    public String getPath() {
        return getFile().getPath();
    }

    /**
     * @return
     * @see java.io.File#hashCode()
     */
    public int hashCode() {
        return getFile().hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#isAbsolute()
     */
    public boolean isAbsolute() {
        return getFile().isAbsolute();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#isDirectory()
     */
    public boolean isDirectory() {
        return getFile().isDirectory();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.extension.jdbc.gen.internal.version.ManagedFile#isFile()
     */
    public boolean isFile() {
        return getFile().isFile();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#isHidden()
     */
    public boolean isHidden() {
        return getFile().isHidden();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#lastModified()
     */
    public long lastModified() {
        return getFile().lastModified();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.extension.jdbc.gen.internal.version.ManagedFile#length()
     */
    public long length() {
        return getFile().length();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.extension.jdbc.gen.internal.version.ManagedFile#mkdir()
     */
    public boolean mkdir() {
        return getFile().mkdir();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.extension.jdbc.gen.internal.version.ManagedFile#mkdirs()
     */
    public boolean mkdirs() {
        return getFile().mkdirs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#renameTo(java
     * .io.File)
     */
    public boolean renameTo(File dest) {
        return getFile().renameTo(dest);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#setLastModified
     * (long)
     */
    public boolean setLastModified(long time) {
        return getFile().setLastModified(time);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.seasar.extension.jdbc.gen.internal.version.ManagedFile#setReadOnly()
     */
    public boolean setReadOnly() {
        return getFile().setReadOnly();
    }

    /**
     * @return
     * @see java.io.File#toString()
     */
    public String toString() {
        return getFile().toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.extension.jdbc.gen.internal.version.ManagedFile#toURI()
     */
    public URI toURI() {
        return getFile().toURI();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.extension.jdbc.gen.internal.version.ManagedFile#toURL()
     */
    public URL toURL() throws MalformedURLException {
        return getFile().toURL();
    }

    protected static class FileHolder {

        protected String actualBasePath;

        protected String logicalBasePath;

        protected String path;

        protected String env;

        protected File file;

        public FileHolder(String actualBasePath, String logicalBasePath,
                String path, String env) {
            this.actualBasePath = actualBasePath;
            this.logicalBasePath = logicalBasePath;
            this.path = path;
            this.env = env;
            file = new File(actualBasePath, path);
        }
    }
}
