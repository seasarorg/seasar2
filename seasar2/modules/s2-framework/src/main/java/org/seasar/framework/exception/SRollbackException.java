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

import javax.transaction.RollbackException;

import org.seasar.framework.message.MessageFormatter;

/**
 * @author higa
 *
 */
public class SRollbackException extends RollbackException {

    private static final long serialVersionUID = 2987454536669276359L;

	private String messageCode_;
	private Object[] args_;
	
	public SRollbackException(String messageCode, Object[] args) {
		super(MessageFormatter.getMessage(messageCode, args));
		messageCode_ = messageCode;
		args_ = args;
	}

	public String getMessageCode() {
		return messageCode_;
	}
	
	public Object[] getArgs() {
		return args_;
	}
}
