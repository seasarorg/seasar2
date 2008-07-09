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

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.util.BeanUtil;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * 
 * @author taedium
 */
public abstract class AbstractTask extends Task {

    /** クラスパス */
    protected Path classpath;

    /**
     * インスタンスを構築します。
     */
    public AbstractTask() {
    }

    @Override
    public void execute() throws BuildException {
        validate();
        Command originalCommand = getCommand();
        ClassLoader classLoader = createClassLoader();
        Object newCommand = createCommand(originalCommand, classLoader);
        copyProperties(originalCommand, newCommand);
        executeCommand(classLoader, newCommand);
    }

    protected void validate() {
        if (classpath == null) {
            throw new BuildException("classpath is not specified for '"
                    + getTaskName() + "' task");
        }
        if (classpath.list().length == 0) {
            throw new BuildException("classpath is empty for '" + getTaskName()
                    + "' task");
        }
    }

    protected ClassLoader createClassLoader() {
        URL[] urls = null;
        try {
            urls = new URL[classpath.list().length];
            for (int i = 0; i < classpath.list().length; ++i) {
                urls[i] = new File(classpath.list()[i]).toURL();
            }
        } catch (final MalformedURLException e) {
            throw new IORuntimeException(e);
        }
        return new URLClassLoader(urls);
    }

    /**
     * 指定されたクラスローダーでコマンドのインスタンスを生成します。
     * 
     * @param classLoader
     *            クラスローダー
     * @return コマンドのインスタンス
     */
    protected Object createCommand(Command originalCommand,
            ClassLoader classLoader) {
        ClassLoader originalLoader = Thread.currentThread()
                .getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try {
            Class<?> clazz = ClassLoaderUtil.loadClass(classLoader,
                    originalCommand.getClass().getName());
            return ClassUtil.newInstance(clazz);
        } finally {
            Thread.currentThread().setContextClassLoader(originalLoader);
        }
    }

    /**
     * JavaBeans形式のオブジェクトのプロパティをコピーします。
     * 
     * @param src
     *            コピー元
     * @param dest
     *            コピー先
     */
    protected void copyProperties(Object src, Object dest) {
        BeanUtil.copy(this, dest);
    }

    /**
     * コマンドを実行します。
     * 
     * @param clazz
     *            コマンドクラス
     * @param command
     *            コマンドのインスタンス
     * @param classLoader
     *            コマンドを実行するクラスローダー
     */
    protected void executeCommand(ClassLoader classLoader, Object command) {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try {
            Method method = ClassUtil.getMethod(command.getClass(), "execute",
                    null);
            MethodUtil.invoke(method, command, null);
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
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

    protected abstract Command getCommand();
}
