/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
package org.seasar.framework.util;

import java.io.File;

import org.seasar.framework.exception.SIllegalStateException;

/**
 * @author higa
 * 
 */
public final class EclipseUtil {

    private EclipseUtil() {
    }

    public static File getProjectRoot(String projectName) {
        File dir = ResourceUtil.getResourceAsFile(".");
        while (dir != null) {
            if (dir.getName().equalsIgnoreCase(projectName)) {
                return dir;
            }
            File child = new File(dir, projectName);
            if (child.exists()) {
                return child;
            }
            dir = dir.getParentFile();
        }
        throw new SIllegalStateException("ESSR0001",
                new Object[] { projectName });
    }

    public static File getCurrentProjectRoot() {
        File dir = ResourceUtil.getResourceAsFile(".");
        while (dir != null) {
            File projectFile = new File(dir, ".project");
            if (projectFile.exists()) {
                return dir;
            }
            dir = dir.getParentFile();
        }
        throw new SIllegalStateException("ESSR0001",
                new Object[] { ".project" });
    }
}