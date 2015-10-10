/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.task;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * @author koichik
 * 
 */
public class RefreshTask extends Task {

    /** ポート番号 */
    protected int port = 8386;

    /** プロジェクト名 */
    protected String projectName;

    /**
     * ポート番号を設定します。
     * 
     * @param port
     *            ポート番号
     */
    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }

    /**
     * プロジェクト名を設定します。
     * 
     * @param projectName
     *            プロジェクト名
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public void execute() throws BuildException {
        if (projectName == null) {
            throw new BuildException("projectName is not specified for '"
                    + getTaskName() + "' task");
        }

        try {
            final URL url = new URL("http://localhost:" + port + "/refresh?"
                    + projectName + "=INFINITE");
            final HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            con.connect();
            try {
                if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    log("refresh task failed. status=" + con.getResponseCode(),
                            Project.MSG_WARN);
                }
            } finally {
                con.disconnect();
            }
        } catch (final Exception e) {
            log("refresh task failed. cause=" + e, Project.MSG_WARN);
        }
    }

}
