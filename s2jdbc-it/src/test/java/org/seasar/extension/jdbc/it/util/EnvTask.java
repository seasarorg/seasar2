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
package org.seasar.extension.jdbc.it.util;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.seasar.framework.env.Env;

/**
 * @author taedium
 * 
 */
public class EnvTask extends Task {

    protected String envFilePath = "env_ut.txt";

    protected String envProperty;

    /**
     * @return Returns the envFilePath.
     */
    public String getEnvFilePath() {
        return envFilePath;
    }

    /**
     * @param envFilePath
     *            The envFilePath to set.
     */
    public void setEnvFilePath(String envFilePath) {
        this.envFilePath = envFilePath;
    }

    /**
     * @return Returns the envProperty.
     */
    public String getEnvProperty() {
        return envProperty;
    }

    /**
     * @param envProperty
     *            The envProperty to set.
     */
    public void setEnvProperty(String envProperty) {
        this.envProperty = envProperty;
    }

    @Override
    public void execute() throws BuildException {
        if (envProperty == null) {
            throw new BuildException("envProperty not specified.");
        }

        ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                getClass().getClassLoader());
            Env.setFilePath(envFilePath);
            getProject().setNewProperty(envProperty, Env.getValue());
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }
}
