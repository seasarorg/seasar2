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

import java.lang.reflect.Method;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.internal.util.BeanUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

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

        TaskClassLoader taskClassLoader = createTaskClassLoader();
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(taskClassLoader);
        try {
            Class<?> commandClass = null;
            try {
                commandClass = taskClassLoader.forceLoadClass(getCommand()
                        .getClass().getName());
            } catch (ClassNotFoundException e) {
                throw new BuildException(getCommand().getClass().getName()
                        + " not found.", e);
            }
            Object command = ClassUtil.newInstance(commandClass);
            copyProperties(getCommand(), command);
            Method method = ClassUtil.getMethod(commandClass, "execute", null);
            MethodUtil.invoke(method, command, null);
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    /**
     * {@link TaskClassLoader}を作成します。
     * 
     * @param path
     *            パス
     * @return クラスローダ
     */
    protected TaskClassLoader createTaskClassLoader() {
        AntClassLoader classLoader = getProject().createClassLoader(
                ClassLoader.getSystemClassLoader(), classpath);
        classLoader.setParentFirst(false);
        classLoader.addJavaLibraries();
        classLoader.setIsolated(true);
        return new TaskClassLoader(classLoader);
    }

    /**
     * JavaBeans形式のプロパティをコピーします。
     * 
     * @param src
     *            コピー元のコマンド
     * @param dest
     *            コピー先のコマンド
     */
    protected void copyProperties(Command src, Object dest) {
        BeanUtil.copy(src, dest);
    }

    /**
     * コマンドを返します。
     * 
     * @return コマンド
     */
    protected abstract Command getCommand();
}
