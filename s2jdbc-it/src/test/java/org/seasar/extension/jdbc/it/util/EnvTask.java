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
 * 環境名をプロパティに設定するタスクです。
 * 
 * @author taedium
 */
public class EnvTask extends Task {

    /** 環境名を表すプロパティ */
    protected String envValueProperty;

    /**
     * 環境名を表すプロパティを設定します。
     * 
     * @param envValueProperty
     *            環境名を表すプロパティ
     */
    public void setEnvValueProperty(String envValueProperty) {
        this.envValueProperty = envValueProperty;
    }

    @Override
    public void execute() throws BuildException {
        if (envValueProperty == null) {
            throw new BuildException("envValueProperty not specified.");
        }

        ClassLoader original = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(
                getClass().getClassLoader());
            Env.setFilePath("env_ut.txt");
            getProject().setNewProperty(envValueProperty, Env.getValue());
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }
}
