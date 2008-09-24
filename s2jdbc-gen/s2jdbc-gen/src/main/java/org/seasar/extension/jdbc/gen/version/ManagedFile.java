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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

/**
 * @author taedium
 * 
 */
public interface ManagedFile {

    File asFile();

    List<File> listFiles();

    ManagedFile createChild(String path);

    /**
     * @return
     * @see java.io.File#canRead()
     */
    boolean canRead();

    /**
     * @return
     * @see java.io.File#canWrite()
     */
    boolean canWrite();

    /**
     * @param pathname
     * @return
     * @see java.io.File#compareTo(java.io.File)
     */
    int compareTo(File pathname);

    /**
     * @return
     * @throws IOException
     * @see java.io.File#createNewFile()
     */
    boolean createNewFile() throws IOException;

    /**
     * @return
     * @see java.io.File#delete()
     */
    boolean delete();

    /**
     * 
     * @see java.io.File#deleteOnExit()
     */
    void deleteOnExit();

    /**
     * @return
     * @see java.io.File#exists()
     */
    boolean exists();

    /**
     * @return
     * @see java.io.File#getAbsoluteFile()
     */
    File getAbsoluteFile();

    /**
     * @return
     * @see java.io.File#getAbsolutePath()
     */
    String getAbsolutePath();

    /**
     * @return
     * @throws IOException
     * @see java.io.File#getCanonicalFile()
     */
    File getCanonicalFile() throws IOException;

    /**
     * @return
     * @throws IOException
     * @see java.io.File#getCanonicalPath()
     */
    String getCanonicalPath() throws IOException;

    /**
     * @return
     * @see java.io.File#getName()
     */
    String getName();

    /**
     * @return
     * @see java.io.File#getParent()
     */
    String getParent();

    /**
     * @return
     * @see java.io.File#getParentFile()
     */
    File getParentFile();

    /**
     * @return
     * @see java.io.File#getPath()
     */
    String getPath();

    /**
     * @return
     * @see java.io.File#isAbsolute()
     */
    boolean isAbsolute();

    /**
     * @return
     * @see java.io.File#isDirectory()
     */
    boolean isDirectory();

    /**
     * @return
     * @see java.io.File#isFile()
     */
    boolean isFile();

    /**
     * @return
     * @see java.io.File#isHidden()
     */
    boolean isHidden();

    /**
     * @return
     * @see java.io.File#lastModified()
     */
    long lastModified();

    /**
     * @return
     * @see java.io.File#length()
     */
    long length();

    /**
     * @return
     * @see java.io.File#mkdir()
     */
    boolean mkdir();

    /**
     * @return
     * @see java.io.File#mkdirs()
     */
    boolean mkdirs();

    /**
     * @param dest
     * @return
     * @see java.io.File#renameTo(java.io.File)
     */
    boolean renameTo(File dest);

    /**
     * @param time
     * @return
     * @see java.io.File#setLastModified(long)
     */
    boolean setLastModified(long time);

    /**
     * @return
     * @see java.io.File#setReadOnly()
     */
    boolean setReadOnly();

    /**
     * @return
     * @see java.io.File#toURI()
     */
    URI toURI();

    /**
     * @return
     * @throws MalformedURLException
     * @see java.io.File#toURL()
     */
    URL toURL() throws MalformedURLException;

}