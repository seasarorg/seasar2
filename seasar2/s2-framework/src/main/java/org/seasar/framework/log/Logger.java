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
package org.seasar.framework.log;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.seasar.framework.message.MessageFormatter;

/**
 * ログ出力を提供するクラスです。
 * 
 * @author higa
 * 
 */
public class Logger {

    private static final Map loggers = new HashMap();

    private static boolean initialized;

    private final Log log;

    /**
     * {@link Logger}を返します。
     * 
     * @param clazz
     * @return {@link Logger}
     */
    public static synchronized Logger getLogger(final Class clazz) {
        if (!initialized) {
            initialize();
        }
        Logger logger = (Logger) loggers.get(clazz);
        if (logger == null) {
            logger = new Logger(clazz);
            loggers.put(clazz, logger);
        }
        return logger;
    }

    /**
     * {@link Logger}を初期化します。
     */
    public static synchronized void initialize() {
        initialized = true;
    }

    /**
     * リソースを開放します。
     */
    public synchronized static void dispose() {
        LogFactory.releaseAll();
        loggers.clear();
        initialized = false;
    }

    private Logger(final Class clazz) {
        log = LogFactory.getLog(clazz);
    }

    /**
     * DEBUG情報が出力されるかどうかを返します。
     * 
     * @return DEBUG情報が出力されるかどうか
     */
    public final boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * DEBUG情報を出力します。
     * 
     * @param message
     * @param throwable
     */
    public final void debug(Object message, Throwable throwable) {
        if (isDebugEnabled()) {
            log.debug(message, throwable);
        }
    }

    /**
     * DEBUG情報を出力します。
     * 
     * @param message
     */
    public final void debug(Object message) {
        if (isDebugEnabled()) {
            log.debug(message);
        }
    }

    /**
     * INFO情報が出力されるかどうかを返します。
     * 
     * @return INFO情報が出力されるかどうか
     */
    public final boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    /**
     * INFO情報を出力します。
     * 
     * @param message
     * @param throwable
     */
    public final void info(Object message, Throwable throwable) {
        if (isInfoEnabled()) {
            log.info(message, throwable);
        }
    }

    /**
     * INFO情報を出力します。
     * 
     * @param message
     */
    public final void info(Object message) {
        if (isInfoEnabled()) {
            log.info(message);
        }
    }

    /**
     * WARN情報を出力します。
     * 
     * @param message
     * @param throwable
     */
    public final void warn(Object message, Throwable throwable) {
        log.warn(message, throwable);
    }

    /**
     * WARN情報を出力します。
     * 
     * @param message
     */
    public final void warn(Object message) {
        log.warn(message);
    }

    /**
     * ERROR情報を出力します。
     * 
     * @param message
     * @param throwable
     */
    public final void error(Object message, Throwable throwable) {
        log.error(message, throwable);
    }

    /**
     * ERROR情報を出力します。
     * 
     * @param message
     */
    public final void error(Object message) {
        log.error(message);
    }

    /**
     * FATAL情報を出力します。
     * 
     * @param message
     * @param throwable
     */
    public final void fatal(Object message, Throwable throwable) {
        log.fatal(message, throwable);
    }

    /**
     * FATAL情報を出力します。
     * 
     * @param message
     */
    public final void fatal(Object message) {
        log.fatal(message);
    }

    /**
     * ログを出力します。
     * 
     * @param throwable
     */
    public final void log(Throwable throwable) {
        error(throwable.getMessage(), throwable);
    }

    /**
     * ログを出力します。
     * 
     * @param messageCode
     * @param args
     */
    public final void log(String messageCode, Object[] args) {
        log(messageCode, args, null);
    }

    /**
     * ログを出力します。
     * 
     * @param messageCode
     * @param args
     * @param throwable
     */
    public final void log(String messageCode, Object[] args, Throwable throwable) {
        char messageType = messageCode.charAt(0);
        if (isEnabledFor(messageType)) {
            String message = MessageFormatter.getSimpleMessage(messageCode,
                    args);
            switch (messageType) {
            case 'D':
                log.debug(message, throwable);
                break;
            case 'I':
                log.info(message, throwable);
                break;
            case 'W':
                log.warn(message, throwable);
                break;
            case 'E':
                log.error(message, throwable);
                break;
            case 'F':
                log.fatal(message, throwable);
                break;
            default:
                throw new IllegalArgumentException(String.valueOf(messageType));
            }
        }
    }

    private boolean isEnabledFor(final char messageType) {
        switch (messageType) {
        case 'D':
            return log.isDebugEnabled();
        case 'I':
            return log.isInfoEnabled();
        case 'W':
            return log.isWarnEnabled();
        case 'E':
            return log.isErrorEnabled();
        case 'F':
            return log.isFatalEnabled();
        default:
            throw new IllegalArgumentException(String.valueOf(messageType));
        }
    }
}