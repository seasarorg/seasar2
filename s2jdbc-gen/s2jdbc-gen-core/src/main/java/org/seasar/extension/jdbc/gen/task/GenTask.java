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
import org.seasar.extension.jdbc.gen.util.BeanUtil;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * @author taedium
 * 
 */
public class GenTask extends Task {

    protected String commandClassName;

    protected String classpathRef;

    public void setClasspathRef(String classpathRef) {
        this.classpathRef = classpathRef;
    }

    /**
     * インスタンスを構築します。
     */
    public GenTask() {
    }

    /**
     * 指定されたクラス名でインスタンスを構築します。
     * 
     * @param commandClassName
     *            このタスクから処理が委譲されるクラスの名前です。
     */
    public GenTask(String commandClassName) {
        this.commandClassName = commandClassName;
    }

    @Override
    public void execute() throws BuildException {
        Path path = (Path) getProject().getReference(classpathRef);
        URL[] urls = toURLs(path.list());
        ClassLoader cl = createClassLoader(urls);
        Class<?> clazz = loadClass(commandClassName, cl);
        Object command = ClassUtil.newInstance(clazz);
        copy(this, command);
        execute(clazz, command, cl);
    }

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

    protected ClassLoader createClassLoader(URL[] urls) {
        return new URLClassLoader(urls);
    }

    protected Class<?> loadClass(String className, ClassLoader classLoader) {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try {
            return ClassUtil.forName(className);
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

    protected void copy(Object src, Object dest) {
        BeanUtil.copy(this, dest);
    }

    protected void execute(Class<?> clazz, Object command,
            ClassLoader classLoader) {
        Method method = ClassUtil.getMethod(clazz, "execute", null);
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);
        try {
            MethodUtil.invoke(method, command, null);
        } finally {
            Thread.currentThread().setContextClassLoader(original);
        }
    }

}
