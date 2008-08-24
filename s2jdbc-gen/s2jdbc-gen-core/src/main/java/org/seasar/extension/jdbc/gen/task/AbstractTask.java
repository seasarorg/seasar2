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
package org.seasar.extension.jdbc.gen.task;

import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.internal.arg.ArgumentsBuilder;
import org.seasar.extension.jdbc.gen.internal.command.CommandAdapter;

/**
 * {@link Task}の抽象クラスです。
 * 
 * @author taedium
 */
public abstract class AbstractTask extends Task {

    /** クラスパス */
    protected Path classpath;

    /**
     * クラスパスを設定します。
     * 
     * @param classpath
     *            クラスパス
     */
    public void setClasspath(Path classpath) {
        createClasspath().append(classpath);
    }

    /**
     * クラスパスの参照を設定します。
     * 
     * @param reference
     *            クラスパスの参照
     */
    public void setClasspathRef(Reference reference) {
        createClasspath().setRefid(reference);
    }

    /**
     * クラスパスを作成します。
     * 
     * @return クラスパス
     */
    public Path createClasspath() {
        if (classpath == null) {
            classpath = new Path(getProject());
        }
        return classpath.createPath();
    }

    @Override
    public void execute() throws BuildException {
        if (classpath == null) {
            throw new BuildException("classpath is not specified for '"
                    + getTaskName() + "' task");
        }
        if (classpath.list().length == 0) {
            throw new BuildException("classpath is empty for '" + getTaskName()
                    + "' task");
        }
        executeCommand();
    }

    /**
     * コマンドを実行します。
     */
    protected void executeCommand() {
        Command command = getCommand();
        String jvmArg = "-D" + CommandAdapter.COMMAND_KEY + "="
                + command.getClass().getName();
        ArgumentsBuilder builder = new ArgumentsBuilder(command);
        List<String> args = builder.build();

        Java java = new Java(this);
        java.createJvmarg().setValue(jvmArg);
        for (String arg : args) {
            java.createArg().setValue(arg);
        }
        java.setClasspath(classpath);
        java.setClassname(CommandAdapter.class.getName());
        java.setFailonerror(true);
        java.setFork(true);
        java.execute();
    }

    /**
     * コマンドを返します。
     * 
     * @return コマンド
     */
    protected abstract Command getCommand();
}
