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
import org.seasar.extension.jdbc.gen.GenCommand;
import org.seasar.extension.jdbc.gen.util.BeanUtil;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.ClassLoaderUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * コンストラクタで受け取る文字列があらわすコマンドクラスを新しいクラスローダーを使って呼び出すタスクです。
 * <p>
 * このタスクに設定されたプロパティはコマンドクラスのインスタンスにコピーされます。
 * </p>
 * 
 * @author taedium
 */
public abstract class AbstractGenTask extends Task {

    /** コマンドクラスのインタフェースの名前 */
    protected static String COMMAND_INTERFACE_NAME = "org.seasar.extension.jdbc.gen.GenCommand";

    /** このタスクから実行するコマンドクラスの名前 */
    protected String commandClassName;

    /** クラスパス */
    protected Path classpath;

    /**
     * 指定されたクラス名でインスタンスを構築します。
     * <p>
     * {@code commandClassName}に指定するクラスは{@link GenCommand}を実装している必要があります。
     * </p>
     * 
     * @param commandClassName
     *            このタスクから処理が委譲されるクラスの名前です。
     */
    public AbstractGenTask(String commandClassName) {
        if (commandClassName == null) {
            throw new NullPointerException(commandClassName);
        }
        this.commandClassName = commandClassName;
    }

    @Override
    public void execute() throws BuildException {
        if (classpath == null) {
            throw new BuildException("classpath is not specified for '"
                    + getTaskName() + "' task");
        }
        URL[] urls = toURLs(classpath.list());
        ClassLoader cl = createClassLoader(urls);
        Class<?> clazz = loadClass(commandClassName, cl);
        Object command = ClassUtil.newInstance(clazz);
        copy(this, command);
        execute(clazz, command, cl);
    }

    /**
     * パス文字列の配列を{@link URL}の配列に変換します。
     * 
     * @param paths
     *            パス文字列の配列
     * @return {@link URL}の配列
     */
    protected URL[] toURLs(String[] paths) {
        try {
            URL[] urls = new URL[paths.length];
            for (int i = 0; i < paths.length; ++i) {
                urls[i] = new File(paths[i]).toURL();
            }
            return urls;
        } catch (final MalformedURLException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * {@link URL}の配列からクラスをロードするクラスローダーを作成します。
     * 
     * @param urls
     *            {@link URL}の配列
     * @return クラスローダー
     */
    protected ClassLoader createClassLoader(URL[] urls) {
        return new URLClassLoader(urls);
    }

    /**
     * 指定されたクラスローダーでクラスをロードします。
     * 
     * @param className
     *            クラス名
     * @param classLoader
     *            クラスローダー
     * @return ロードされたクラス
     */
    protected Class<?> loadClass(String className, ClassLoader classLoader) {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try {
            return ClassUtil.forName(className);
        } finally {
            Thread.currentThread().setContextClassLoader(original);
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
    protected void copy(Object src, Object dest) {
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
    protected void execute(Class<?> clazz, Object command,
            ClassLoader classLoader) {
        Class<?> commandClass = ClassLoaderUtil.loadClass(classLoader,
                COMMAND_INTERFACE_NAME);
        if (!commandClass.isAssignableFrom(clazz)) {
            throw new BuildException("class '" + clazz.getName()
                    + "' must implement '" + COMMAND_INTERFACE_NAME
                    + "' interface.");
        }
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try {
            Method method = ClassUtil.getMethod(clazz, "execute", null);
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

}
