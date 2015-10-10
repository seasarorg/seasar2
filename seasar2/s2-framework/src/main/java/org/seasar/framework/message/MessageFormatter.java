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
package org.seasar.framework.message;

import java.text.MessageFormat;

/**
 * メッセージコードと引数からメッセージを組み立てるクラスです。
 * 
 * @author higa
 * 
 */

public class MessageFormatter {

    private static final String MESSAGES = "Messages";

    private MessageFormatter() {
    }

    /**
     * メッセージを返します。
     * 
     * @param messageCode
     * @param args
     * @return メッセージ
     */
    public static String getMessage(String messageCode, Object[] args) {
        if (messageCode == null) {
            messageCode = "";
        }
        return getFormattedMessage(messageCode, getSimpleMessage(messageCode,
                args));
    }

    /**
     * メッセージコードつきのメッセージを返します。
     * 
     * @param messageCode
     * @param simpleMessage
     * @return メッセージコードつきのメッセージ
     */
    public static String getFormattedMessage(String messageCode,
            String simpleMessage) {
        return "[" + messageCode + "]" + simpleMessage;
    }

    /**
     * メッセージコードなしの単純なメッセージを返します。
     * 
     * @param messageCode
     * @param arguments
     * @return メッセージコードなしの単純なメッセージ
     */
    public static String getSimpleMessage(String messageCode, Object[] arguments) {

        try {
            String pattern = getPattern(messageCode);
            if (pattern != null) {
                return MessageFormat.format(pattern, arguments);
            }
        } catch (Throwable ignore) {
        }
        return getNoPatternMessage(arguments);
    }

    private static String getPattern(String messageCode) {
        MessageResourceBundle resourceBundle = getMessages(getSystemName(messageCode));
        if (resourceBundle == null) {
            return null;
        }

        int length = messageCode.length();
        if (length > 8) {
            String key = messageCode.charAt(0)
                    + messageCode.substring(length - 4);
            String pattern = resourceBundle.get(key);
            if (pattern != null) {
                return pattern;
            }
        }
        return resourceBundle.get(messageCode);
    }

    private static String getSystemName(String messageCode) {
        return messageCode.substring(1, Math.max(1, messageCode.length() - 4));
    }

    private static MessageResourceBundle getMessages(String systemName) {
        return MessageResourceBundleFactory.getBundle(systemName + MESSAGES);
    }

    private static String getNoPatternMessage(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            buffer.append(args[i] + ", ");
        }
        buffer.setLength(buffer.length() - 2);
        return buffer.toString();
    }
}