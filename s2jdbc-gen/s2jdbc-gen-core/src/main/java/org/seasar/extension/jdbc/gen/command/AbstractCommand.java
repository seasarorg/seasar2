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
import org.seasar.extension.jdbc.gen.exception.CommandFailedRuntimeException;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.log.Logger;

/**
 * コマンドの抽象クラスです。
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
        String commandClassName = getClass().getName();
        getLogger().log("DS2JDBCGen0003", new Object[] { commandClassName });
        logWritableProperties();
        validate();
        init();
        try {
            doExecute();
        } catch (Throwable t) {
            throw new CommandFailedRuntimeException(t, commandClassName);
        } finally {
            destroy();
        }
        getLogger().log("DS2JDBCGen0008", new Object[] { commandClassName });
    }

    /**
     * サブクラスで実行します。
     * 
     * @throws Throwable
     */
    protected abstract void doExecute() throws Throwable;

    /**
     * 設定可能なプロパティの値をログ出力します。
     */
    protected void logWritableProperties() {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(getClass());
        for (int i = 0; i < beanDesc.getPropertyDescSize(); i++) {
            PropertyDesc propertyDesc = beanDesc.getPropertyDesc(i);
            if (propertyDesc.hasWriteMethod()) {
                getLogger().log(
                        "DS2JDBCGen0001",
                        new Object[] { propertyDesc.getPropertyName(),
                                propertyDesc.getValue(this) });
            }
        }
    }

    /**
     * 検証します。
     */
    protected final void validate() {
        doValidate();
    }

    /**
     * サブクラスで検証します。
     */
    protected abstract void doValidate();

    /**
     * 初期化します。
     */
    protected final void init() {
        doInit();
    }

    /**
     * サブクラスで初期化します。
     */
    protected abstract void doInit();

    /**
     * 破棄します。
     */
    protected final void destroy() {
        doDestroy();
    }

    /**
     * サブクラスで破棄します。
     */
    protected abstract void doDestroy();

    /**
     * ロガーを返します。
     * 
     * @return ロガー
     */
    protected abstract Logger getLogger();

}
