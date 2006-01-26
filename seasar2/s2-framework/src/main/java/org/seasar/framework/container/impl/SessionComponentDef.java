/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.impl;

import javax.servlet.http.HttpSession;

import org.seasar.framework.container.S2Container;

/**
 * @author higa
 *  
 */
public class SessionComponentDef extends SimpleComponentDef {

	private S2Container container;

	public SessionComponentDef(S2Container container) {
		super(HttpSession.class);
		this.container = container;
	}

	public S2Container getRoot() {
		return container.getRoot();
	}

	/**
	 * @see org.seasar.framework.container.ComponentDef#getComponent()
	 */
	public Object getComponent() {
		return getRoot().getSession();
	}
}