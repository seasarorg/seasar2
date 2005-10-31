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

import javax.transaction.xa.XAException;

import org.seasar.framework.message.MessageFormatter;

/**
 * @author higa
 *
 */
public class SXAException extends XAException {

    private static final long serialVersionUID = 9069430381428399030L;

	private String messageCode_;
	private Object[] messageArgs_;
	
	public SXAException(Throwable t) {
		this("ESSR0017", new Object[]{t}, t);
	}

	public SXAException(String messageCode, Object[] messageArgs) {
		this(messageCode, messageArgs, null);
	}

	public SXAException(String messageCode, Object[] messageArgs,
		Throwable t) {

		super(MessageFormatter.getMessage(messageCode, messageArgs));
		messageCode_ = messageCode;
		messageArgs_ = messageArgs;
		initCause(t);
	}

	public String getMessageCode() {
		return messageCode_;
	}
	
	public Object[] getMessageArgs() {
		return messageArgs_;
	}
}
