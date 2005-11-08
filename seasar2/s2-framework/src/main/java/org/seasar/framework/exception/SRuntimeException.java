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
package org.seasar.framework.exception;

import org.seasar.framework.message.MessageFormatter;

/**
 * @author higa
 *
 */
public class SRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -4452607868694297329L;

	private String messageCode_;
	private Object[] args_;
	private String message_;
	private String simpleMessage_;

	public SRuntimeException(String messageCode) {
		this(messageCode, null, null);
	}

	public SRuntimeException(String messageCode, Object[] args) {
		this(messageCode, args, null);
	}

	public SRuntimeException(
		String messageCode,
		Object[] args,
		Throwable cause) {

		super(cause);
		messageCode_ = messageCode;
		args_ = args;
		simpleMessage_ = MessageFormatter.getSimpleMessage(messageCode_, args_);
		message_ = "[" + messageCode + "]" + simpleMessage_;
	}

	public final String getMessageCode() {
		return messageCode_;
	}

	public final Object[] getArgs() {
		return args_;
	}

	public final String getMessage() {
		return message_;
	}
	
	protected void setMessage(String message) {
		message_ = message;
	}
	
	public final String getSimpleMessage() {
		return simpleMessage_;
	}
}