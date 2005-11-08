/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.seasar.framework.message.MessageFormatter;

public final class Logger {

	private static Map loggerMap_ = Collections.synchronizedMap(new HashMap());

	private Log log_;

	private Logger(Class clazz) {
		log_ = LogFactory.getLog(clazz);
	}

	public static final Logger getLogger(Class clazz) {
		Logger logger = (Logger) loggerMap_.get(clazz);
		if (logger == null) {
			logger = new Logger(clazz);
			loggerMap_.put(clazz, logger);
		}
		return logger;
	}

	public final boolean isDebugEnabled() {
		return log_.isDebugEnabled();
	}

	public final void debug(Object message, Throwable throwable) {
		if (isDebugEnabled()) {
			log_.debug(message, throwable);
		}
	}

	public final void debug(Object message) {
		if (isDebugEnabled()) {
			log_.debug(message);
		}
	}

	public final boolean isInfoEnabled() {
		return log_.isInfoEnabled();
	}

	public final void info(Object message, Throwable throwable) {
		if (isInfoEnabled()) {
			log_.info(message, throwable);
		}
	}

	public final void info(Object message) {
		if (isInfoEnabled()) {
			log_.info(message);
		}
	}

	public final void warn(Object message, Throwable throwable) {
		log_.warn(message, throwable);
	}

	public final void warn(Object message) {
		log_.warn(message);
	}

	public final void error(Object message, Throwable throwable) {
		log_.error(message, throwable);
	}

	public final void error(Object message) {
		log_.error(message);
	}

	public final void fatal(Object message, Throwable throwable) {
		log_.fatal(message, throwable);
	}

	public final void fatal(Object message) {
		log_.fatal(message);
	}

	public final void log(Throwable throwable) {
		error(throwable.getMessage(), throwable);
	}

	public final void log(String messageCode, Object[] args) {
		log(messageCode, args, null);
	}

	public final void log(String messageCode, Object[] args, Throwable throwable) {
		char messageType = messageCode.charAt(0);
		if (isEnabledFor(messageType)) {
			String message = MessageFormatter.getSimpleMessage(messageCode,
					args);
			switch (messageType) {
			case 'D':
				log_.debug(message, throwable);
				break;
			case 'I':
				log_.info(message, throwable);
				break;
			case 'W':
				log_.warn(message, throwable);
				break;
			case 'E':
				log_.error(message, throwable);
				break;
			case 'F':
				log_.fatal(message, throwable);
				break;
			default:
				throw new IllegalArgumentException(String.valueOf(messageType));
			}
		}
	}

	private boolean isEnabledFor(final char messageType) {
		switch (messageType) {
		case 'D':
			return log_.isDebugEnabled();
		case 'I':
			return log_.isInfoEnabled();
		case 'W':
			return log_.isWarnEnabled();
		case 'E':
			return log_.isErrorEnabled();
		case 'F':
			return log_.isFatalEnabled();
		default:
			throw new IllegalArgumentException(String.valueOf(messageType));
		}
	}
}