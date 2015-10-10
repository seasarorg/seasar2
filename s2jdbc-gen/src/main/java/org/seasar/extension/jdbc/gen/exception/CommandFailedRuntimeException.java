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
package org.seasar.extension.jdbc.gen.exception;

import org.seasar.extension.jdbc.gen.ProductInfo;
import org.seasar.framework.exception.SRuntimeException;

/**
 * コマンドの実行が失敗した場合にスローされる例外です。
 * 
 * @author taedium
 */
public class CommandFailedRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** コマンドのクラス名 */
    protected String commandClassName;

    /**
     * インスタンスを構築します。
     * 
     * @param cause
     *            原因
     * @param commandClassName
     *            コマンドのクラス名
     */
    public CommandFailedRuntimeException(Throwable cause,
            String commandClassName) {
        super("ES2JDBCGen0005", new Object[] { commandClassName,
                ProductInfo.getInstance().getName(),
                ProductInfo.getInstance().getVersion(), cause }, cause);
        this.commandClassName = commandClassName;
    }

    /**
     * コマンドのクラス名を返します。
     * 
     * @return コマンドのクラス名
     */
    public String getCommandClassName() {
        return commandClassName;
    }

}
