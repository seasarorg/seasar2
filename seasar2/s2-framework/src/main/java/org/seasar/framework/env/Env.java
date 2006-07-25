/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.env;

import java.io.File;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.TextUtil;

/**
 * @author higa
 * 
 */
public class Env {

    public static final String PRODUCT = "product";

    public static final String UT = "ut";

    public static final String CT = "ct";

    public static final String IT = "it";

    public static final String DEFAULT_FILE_PATH = "env.text";

    private static String value;

    private static String filePath = DEFAULT_FILE_PATH;

    private static File file;

    private static long lastModified;

    static {
        initialize();
    }

    private Env() {
    }

    public static void initialize() {
        setFilePath(DEFAULT_FILE_PATH);
    }

    public static String getValue() {
        if (file != null && file.lastModified() > lastModified) {
            calcValue();
        }
        if (StringUtil.isEmpty(value)) {
            return PRODUCT;
        }
        return value;
    }

    public static String adjustPath(String path) {
        String env = getValue();
        if (PRODUCT.equals(env)) {
            return path;
        }
        int index = path.lastIndexOf('.');
        if (index < 0) {
            return path;
        }
        String p = path.substring(0, index);
        String ext = path.substring(index + 1);
        return p + "_" + env + "." + ext;
    }

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        if (filePath == null) {
            throw new EmptyRuntimeException("filePath");
        }
        Env.filePath = filePath;
        file = ResourceUtil.getResourceAsFileNoException(filePath);
        if (file != null) {
            calcValue();
        } else {
            clearValue();
        }
    }

    protected static void calcValue() {
        value = TextUtil.readText(file);
        lastModified = file.lastModified();
    }

    protected static void clearValue() {
        value = null;
        lastModified = 0;
    }
}
