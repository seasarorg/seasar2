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
package org.seasar.framework.exception;

import javax.transaction.NotSupportedException;

import org.seasar.framework.message.MessageFormatter;

/**
 * Seasar2用の{@link NotSupportedException}を返します。
 * 
 * @author higa
 * 
 */
public class SNotSupportedException extends NotSupportedException {

    private static final long serialVersionUID = 2417177201914177474L;

    private String messageCode;

    private Object[] args;

    /**
     * {@link SNotSupportedException}を作成します。
     * 
     * @param messageCode
     * @param args
     */
    public SNotSupportedException(String messageCode, Object[] args) {
        super(MessageFormatter.getMessage(messageCode, args));
        this.messageCode = messageCode;
        this.args = args;
    }

    /**
     * メッセージコードを返します。
     * 
     * @return メッセージコード
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * 引数の配列を返します。
     * 
     * @return 引数の配列
     */
    public Object[] getArgs() {
        return args;
    }
}
