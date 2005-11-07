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

import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.S2Container;

/**
 * @author higa
 *
 */
public final class MetaDefSupport {

	private List metaDefs_ = Collections.synchronizedList(new ArrayList());

	private S2Container container_;

	public MetaDefSupport() {
	}
	
	public MetaDefSupport(S2Container container) {
		setContainer(container);
	}

	public void addMetaDef(MetaDef metaDef) {
		if (container_ != null) {
			metaDef.setContainer(container_);
		}
		metaDefs_.add(metaDef);
	}

	public int getMetaDefSize() {
		return metaDefs_.size();
	}

	public MetaDef getMetaDef(int index) {
		return (MetaDef) metaDefs_.get(index);
	}

	public MetaDef getMetaDef(String name) {
		for (int i = 0; i < getMetaDefSize(); ++i) {
			MetaDef metaDef = getMetaDef(i);
			if (name == null && metaDef.getName() == null || name != null
					&& name.equalsIgnoreCase(metaDef.getName())) {
				return metaDef;
			}
		}
		return null;
	}

	public MetaDef[] getMetaDefs(String name) {
		List defs = new ArrayList();
		for (int i = 0; i < getMetaDefSize(); ++i) {
			MetaDef metaDef = getMetaDef(i);
			if (name == null && metaDef.getName() == null || name != null
					&& name.equalsIgnoreCase(metaDef.getName())) {
				defs.add(metaDef);
			}
		}
		return (MetaDef[]) defs.toArray(new MetaDef[defs.size()]);
	}

	public void setContainer(S2Container container) {
		container_ = container;
		for (int i = 0; i < getMetaDefSize(); ++i) {
			getMetaDef(i).setContainer(container);
		}
	}
}