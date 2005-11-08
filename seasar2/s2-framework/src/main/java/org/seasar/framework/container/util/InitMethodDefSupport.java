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
package org.seasar.framework.container.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.seasar.framework.container.InitMethodDef;
import org.seasar.framework.container.S2Container;

/**
 * @author higa
 *
 */
public final class InitMethodDefSupport {

	private List methodDefs_ = Collections.synchronizedList(new ArrayList());
	private S2Container container_;

	public InitMethodDefSupport() {
	}

	public void addInitMethodDef(InitMethodDef methodDef) {
		if (container_ != null) {
			methodDef.setContainer(container_);
		}
		methodDefs_.add(methodDef);
	}

	public int getInitMethodDefSize() {
		return methodDefs_.size();
	}

	public InitMethodDef getInitMethodDef(int index) {
		return (InitMethodDef) methodDefs_.get(index);
	}

	public void setContainer(S2Container container) {
		container_ = container;
		for (int i = 0; i < getInitMethodDefSize(); ++i) {
			getInitMethodDef(i).setContainer(container);
		}
	}
}