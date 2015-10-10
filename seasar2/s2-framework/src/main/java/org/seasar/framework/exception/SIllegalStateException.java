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

import org.seasar.framework.message.MessageFormatter;

/**
 * Seasar2用の{@link IllegalStateException}です。
 * 
 * @author higa
 * 
 */
public class SIllegalStateException extends IllegalStateException {

    private static final long serialVersionUID = 7321239007471022687L;

    private String messageCode;

    private Object[] args;

    /**
     * {@link SIllegalStateException}を作成します。
     * 
     * @param messageCode
     */
    public SIllegalStateException(String messageCode) {
        super(MessageFormatter.getMessage(messageCode, null));

        this.messageCode = messageCode;
    }

    /**
     * {@link SIllegalStateException}を作成します。
     * 
     * @param messageCode
     * @param args
     */
    public SIllegalStateException(String messageCode, Object[] args) {
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