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
package org.seasar.extension.jdbc.gen.exception;

import java.io.File;

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author taedium
 * 
 */
public class EntityClassNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    protected File classpathDir;

    protected String packageName;

    protected String entityNamePattern;

    protected String ignoreEntityNamePattern;

    public EntityClassNotFoundRuntimeException(File classpathDir,
            String packageName, String entityNamePattern,
            String ignoreEntityNamePattern) {
        super("ES2JDBCGen0014", new Object[] { classpathDir.getPath(),
                packageName, entityNamePattern, ignoreEntityNamePattern });
        this.classpathDir = classpathDir;
        this.packageName = packageName;
        this.entityNamePattern = entityNamePattern;
        this.ignoreEntityNamePattern = ignoreEntityNamePattern;
    }

    /**
     * @return Returns the classpathDir.
     */
    public File getClasspathDir() {
        return classpathDir;
    }

    /**
     * @return Returns the packageName.
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @return Returns the entityNamePattern.
     */
    public String getEntityNamePattern() {
        return entityNamePattern;
    }

    /**
     * @return Returns the ignoreEntityNamePattern.
     */
    public String getIgnoreEntityNamePattern() {
        return ignoreEntityNamePattern;
    }

}
