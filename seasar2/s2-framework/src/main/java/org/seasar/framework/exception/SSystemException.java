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

import javax.transaction.SystemException;

import org.seasar.framework.message.MessageFormatter;

/**
 * Seasar2用の{@link SystemException}です。
 * 
 * @author higa
 * 
 */
public class SSystemException extends SystemException {

    private static final long serialVersionUID = -404340896339687907L;

    private String messageCode;

    private Object[] args;

    /**
     * {@link SSystemException}を作成します。
     * 
     * @param messageCode
     * @param args
     */
    public SSystemException(String messageCode, Object[] args) {
        this(messageCode, args, null);
    }

    /**
     * {@link SSystemException}を作成します。
     * 
     * @param messageCode
     * @param args
     * @param cause
     */
    public SSystemException(String messageCode, Object[] args, Throwable cause) {
        super(MessageFormatter.getMessage(messageCode, args));
        this.messageCode = messageCode;
        this.args = args;
        initCause(cause);
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