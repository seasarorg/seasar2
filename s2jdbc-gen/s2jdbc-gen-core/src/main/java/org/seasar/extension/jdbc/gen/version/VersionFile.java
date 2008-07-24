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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;

import org.seasar.extension.jdbc.gen.exception.IllegalVersionValueRuntimeException;
import org.seasar.extension.jdbc.gen.util.CloseableUtil;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.FileOutputStreamUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.TextUtil;

/**
 * @author taedium
 * 
 */
public class VersionFile {

    protected File versionFile;

    protected String currentVersionName;

    protected String nextVersionName;

    protected int currentVersionNo;

    protected int nextVersionNo;

    /**
     * @param versionFile
     * @param versionNoPattern
     */
    public VersionFile(File versionFile, String versionNoPattern) {
        if (versionFile == null) {
            throw new NullPointerException("versionFile");
        }
        if (versionNoPattern == null) {
            throw new NullPointerException("versionNoPattern");
        }
        this.versionFile = versionFile;
        DecimalFormat versionNoFormat = new DecimalFormat(versionNoPattern);
        int currentVersion = 0;
        if (versionFile.exists()) {
            String value = TextUtil.readUTF8(versionFile).trim();
            if (!StringUtil.isNumber(value)) {
                throw new IllegalVersionValueRuntimeException(versionFile
                        .getPath(), value);
            }
            currentVersion = Integer.valueOf(value);
            currentVersionName = versionNoFormat.format(currentVersion);
        }
        nextVersionNo = currentVersion + 1;
        nextVersionName = versionNoFormat.format(nextVersionNo);
    }

    public String getCurrentVersionName() {
        return currentVersionName;
    }

    public String getNextVersionName() {
        return nextVersionName;
    }

    public int getCurrentVersionNo() {
        return currentVersionNo;
    }

    public int getNextVersionNo() {
        return nextVersionNo;
    }

    public String getPath() {
        return versionFile.getPath();
    }

    public void increment() {
        OutputStream os = FileOutputStreamUtil.create(versionFile);
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(os, "UTF-8");
            writer.write(String.valueOf(nextVersionNo));
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            CloseableUtil.close(writer);
        }
    }
}
