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
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.Environment;
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

    /** JavaタスクのresultPropertyのサフィックス */
    protected static String RESULT_PROPERTY_NAME_SUFFIX = "_result";

    /** JavaタスクのerrorPropertyのサフィックス */
    protected static String ERROR_PROPERTY_NAME_SUFFIX = "_error";

    /** Javaタスク */
    protected Java java = new Java();

    /** JVMのコマンドライン */
    protected Commandline jvmCommandline = new Commandline();

    /** クラスパス */
    protected Path classpath;

    /**
     * JVMの引数を作成します。
     * 
     * @return JVMの引数
     */
    public Commandline.Argument createJvmarg() {
        return jvmCommandline.createArgument();
    }

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
        String commandName = command.getClass().getName();

        ArgumentsBuilder builder = new ArgumentsBuilder(command);
        List<String> args = builder.build();

        Environment.Variable sysproperty = new Environment.Variable();
        sysproperty.setKey(CommandAdapter.COMMAND_KEY);
        sysproperty.setValue(commandName);

        String resultPropertyName = commandName + RESULT_PROPERTY_NAME_SUFFIX;
        String errorPropertyName = commandName + ERROR_PROPERTY_NAME_SUFFIX;

        java.bindToOwner(this);
        java.addSysproperty(sysproperty);
        for (String arg : args) {
            java.createArg().setValue(arg);
        }
        for (String arg : jvmCommandline.getArguments()) {
            java.createJvmarg().setValue(arg);
        }
        java.setClasspath(classpath);
        java.setClassname(CommandAdapter.class.getName());
        java.setResultProperty(resultPropertyName);
        java.setErrorProperty(errorPropertyName);
        java.setFork(true);
        java.execute();

        String result = getProject().getProperty(resultPropertyName);
        if (!"0".equals(result)) {
            throw new BuildException(getProject()
                    .getProperty(errorPropertyName));
        }
    }

    /**
     * コマンドを返します。
     * 
     * @return コマンド
     */
    protected abstract Command getCommand();
}
