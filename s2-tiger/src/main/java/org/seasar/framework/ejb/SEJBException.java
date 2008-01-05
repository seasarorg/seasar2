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
package org.seasar.framework.ejb;

import javax.ejb.EJBException;

import org.seasar.framework.message.MessageFormatter;

/**
 * {@link EJBException}をラップする例外です。
 * 
 * @author koichik
 */
public class SEJBException extends EJBException {

    private static final long serialVersionUID = 1L;

    private String messageCode;

    private Object[] args;

    /**
     * インスタンスを構築します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param args
     *            メッセージに埋め込まれる引数
     */
    public SEJBException(final String messageCode, final Object... args) {
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
     * メッセージに埋め込まれる引数を返します。
     * 
     * @return メッセージに埋め込まれる引数
     */
    public Object[] getArgs() {
        return args;
    }

}
