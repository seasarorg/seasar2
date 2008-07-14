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
import org.apache.tools.ant.types.Path;
import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.util.BeanUtil;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * @author taedium
 * 
 */
public class CommandExecutor {

    /** クラスパス */
    protected Path classpath;

    protected String taskName;

    public CommandExecutor(Path classpath, String taskName) {
        if (classpath == null) {
            throw new BuildException("classpath is not specified for '"
                    + taskName + "' task");
        }
        if (classpath.list().length == 0) {
            throw new BuildException("classpath is empty for '" + taskName
                    + "' task");
        }
        this.classpath = classpath;
        this.taskName = taskName;
    }

    public void execute(Command originalCommand) {
        ClassLoader classLoader = createClassLoader();
        Object newCommand = createCommand(originalCommand, classLoader);
        copyProperties(originalCommand, newCommand);
        executeCommand(classLoader, newCommand);
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
        BeanUtil.copy(src, dest);
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

}
