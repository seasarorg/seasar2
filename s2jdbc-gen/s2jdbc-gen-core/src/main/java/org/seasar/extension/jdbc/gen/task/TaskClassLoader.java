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

import org.apache.tools.ant.AntClassLoader;
import org.seasar.extension.jdbc.gen.command.Command;

/**
 * Antタスクから{@link Command}を実行するために使用するクラスローダです。
 * 
 * @author taedium
 */
public class TaskClassLoader extends ClassLoader {

    /**
     * 親クラスローダを指定してインスタンスを構築します。
     * 
     * @param parent
     *            親クラスローダ
     */
    public TaskClassLoader(AntClassLoader parent) {
        super(parent);
    }

    /**
     * 強制的にクラスをロードします。
     * 
     * @param className
     *            クラス名
     * @return クラス
     * @throws ClassNotFoundException
     *             クラスが見つからなかった場合
     */
    public Class<?> forceLoadClass(String className)
            throws ClassNotFoundException {
        return ((AntClassLoader) getParent()).forceLoadClass(className);
    }

}
