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
 * Seasar2の例外のベースクラスです。
 * 
 * @author higa
 * 
 */
public class SRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -4452607868694297329L;

    private String messageCode;

    private Object[] args;

    private String message;

    private String simpleMessage;

    /**
     * {@link SRuntimeException}を作成します。
     * 
     * @param messageCode
     */
    public SRuntimeException(String messageCode) {
        this(messageCode, null, null);
    }

    /**
     * {@link SRuntimeException}を作成します。
     * 
     * @param messageCode
     * @param args
     */
    public SRuntimeException(String messageCode, Object[] args) {
        this(messageCode, args, null);
    }

    /**
     * {@link SRuntimeException}を作成します。
     * 
     * @param messageCode
     * @param args
     * @param cause
     */
    public SRuntimeException(String messageCode, Object[] args, Throwable cause) {

        super(cause);
        this.messageCode = messageCode;
        this.args = args;
        simpleMessage = MessageFormatter.getSimpleMessage(messageCode, args);
        message = "[" + messageCode + "]" + simpleMessage;
    }

    /**
     * メッセージコードを返します。
     * 
     * @return メッセージコード
     */
    public final String getMessageCode() {
        return messageCode;
    }

    /**
     * 引数の配列を返します。
     * 
     * @return 引数の配列
     */
    public final Object[] getArgs() {
        return args;
    }

    public final String getMessage() {
        return message;
    }

    /**
     * メッセージを設定します。
     * 
     * @param message
     *            メッセージ
     */
    protected void setMessage(String message) {
        this.message = message;
    }

    /**
     * メッセージコードなしの単純なメッセージを返します。
     * 
     * @return メッセージコードなしの単純なメッセージ
     */
    public final String getSimpleMessage() {
        return simpleMessage;
    }
}