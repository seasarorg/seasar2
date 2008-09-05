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
package org.seasar.extension.jdbc.gen.internal.command;

import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.internal.arg.ArgumentsParser;
import org.seasar.extension.jdbc.gen.internal.exception.SystemPropertyNotFoundRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.ReflectUtil;

/**
 * {@link Command}のアダプタとなるクラスです。
 * 
 * @author taedium
 */
public class CommandAdapter {

    /** コマンドクラスのキー */
    public static String COMMAND_KEY = Command.class.getPackage().getName();

    /**
     * システムプロパティに登録された{@link Command}を実行します
     * 
     * @param args
     */
    public static void main(String[] args) {
        String className = System.getProperty(COMMAND_KEY);
        if (className == null) {
            throw new SystemPropertyNotFoundRuntimeException(COMMAND_KEY);
        }
        Command command = ReflectUtil.newInstance(Command.class, className);
        ArgumentsParser parser = new ArgumentsParser(command);
        parser.parse(args);
        command.execute();
    }
}
