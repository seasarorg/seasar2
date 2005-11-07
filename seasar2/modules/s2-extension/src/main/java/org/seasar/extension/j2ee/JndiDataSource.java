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
package org.seasar.extension.j2ee;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.seasar.framework.util.InitialContextUtil;

/**
 * @author higa
 *
 */
public final class JndiDataSource extends DataSourceWrapper {

	private InitialContext initialContext_;
	private String jndiName_;

	public JndiDataSource(InitialContext initialContext, String jndiName) {
		initialContext_ = initialContext;
		jndiName_ = jndiName;
		setPhysicalDataSource(
			(DataSource) InitialContextUtil.lookup(initialContext, jndiName));
	}
	
	public InitialContext getInitialContext() {
		return initialContext_;
	}

	public String getJndiName() {
		return jndiName_;
	}
}