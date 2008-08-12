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
package org.seasar.extension.jdbc.gen.util;

import java.io.File;
import java.util.Comparator;

/**
 * @author taedium
 * 
 */
public class EnvAwareFileComparator implements Comparator<File> {

    protected String envSuffix;

    /**
     * @param env
     */
    public EnvAwareFileComparator(String env) {
        this.envSuffix = "_" + env;
    }

    public int compare(File file1, File file2) {
        String envIncludedName1 = removeExtension(file1.getName());
        String envIncludedName2 = removeExtension(file2.getName());
        String envExcludedName1 = removeEnv(envIncludedName1);
        String envExcludedName2 = removeEnv(envIncludedName2);

        int result = envExcludedName1.compareTo(envExcludedName2);
        if (result == 0) {
            if (envIncludedName1.endsWith(envSuffix)) {
                return envIncludedName2.endsWith(envSuffix) ? 0 : 1;
            }
            return envIncludedName2.endsWith(envSuffix) ? -1 : 0;
        }
        return result;
    }

    protected String removeExtension(String name) {
        int pos = name.lastIndexOf('.');
        if (pos > -1) {
            return name.substring(0, pos);
        }
        return name;
    }

    protected String removeEnv(String name) {
        if (name.endsWith(envSuffix)) {
            return name.substring(0, name.length() - envSuffix.length());
        }
        return name;
    }

}
