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
package org.seasar.extension.jdbc.gen.command;

import org.seasar.extension.jdbc.gen.Command;

/**
 * エンティティのを生成するコマンドの抽象クラスです。
 * 
 * @author taedium
 */
public abstract class AbstractCommand implements Command {

    /**
     * インスタンスを構築します。
     */
    public AbstractCommand() {
    }

    public final void execute() {
        validate();
        init();
        try {
            doExecute();
        } finally {
            destroy();
        }
    }

    protected abstract void doExecute();

    protected final void validate() {
        doValidate();
    }

    protected abstract void doValidate();

    /**
     * 初期化します。
     */
    protected final void init() {
        doInit();
    }

    protected abstract void doInit();

    /**
     * 破棄します。
     */
    protected final void destroy() {
        doDestroy();
    }

    protected abstract void doDestroy();

}
