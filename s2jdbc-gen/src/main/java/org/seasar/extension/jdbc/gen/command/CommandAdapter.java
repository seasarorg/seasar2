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
package org.seasar.extension.jdbc.gen.command;

import org.seasar.extension.jdbc.gen.internal.arg.ArgumentsParser;
import org.seasar.extension.jdbc.gen.internal.exception.SystemPropertyNotFoundRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.ReflectUtil;

/**
 * {@link Command}のアダプタとなるクラスです。
 * <p>
 * コマンドラインの情報を元に{@link Command}を組み立て実行します。
 * </p>
 * 
 * @author taedium
 */
public class CommandAdapter {

    /** コマンドクラスのキー */
    public static String COMMAND_KEY = Command.class.getName();

    /** コマンド呼び出しクラスのキー */
    public static String COMMAND_INVOKER_KEY = CommandInvoker.class.getName();

    /**
     * システムプロパティに登録された{@link Command}をシステムプロパティに登録された{@link CommandInvoker}
     * で実行します。
     * 
     * @param args
     *            引数の配列
     */
    public static void main(String[] args) {
        String commandName = System.getProperty(COMMAND_KEY);
        if (commandName == null) {
            throw new SystemPropertyNotFoundRuntimeException(COMMAND_KEY);
        }
        String invokerName = System.getProperty(COMMAND_INVOKER_KEY);
        if (invokerName == null) {
            throw new SystemPropertyNotFoundRuntimeException(
                    COMMAND_INVOKER_KEY);
        }
        Command command = ReflectUtil.newInstance(Command.class, commandName);
        ArgumentsParser parser = new ArgumentsParser(command);
        parser.parse(args);

        CommandInvoker invoker = ReflectUtil.newInstance(CommandInvoker.class,
                invokerName);
        invoker.invoke(command);
    }
}
