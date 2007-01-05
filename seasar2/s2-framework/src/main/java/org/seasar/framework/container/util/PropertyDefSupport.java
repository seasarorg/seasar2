/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.CaseInsensitiveMap;

/**
 * @author higa
 * 
 */
public final class PropertyDefSupport {

    private CaseInsensitiveMap propertyDefs = new CaseInsensitiveMap();

    private S2Container container;

    public PropertyDefSupport() {
    }

    public synchronized void addPropertyDef(PropertyDef propertyDef) {
        if (container != null) {
            propertyDef.setContainer(container);
        }
        propertyDefs.put(propertyDef.getPropertyName(), propertyDef);
    }

    public synchronized int getPropertyDefSize() {
        return propertyDefs.size();
    }

    public synchronized PropertyDef getPropertyDef(int index) {
        return (PropertyDef) propertyDefs.get(index);
    }

    public synchronized PropertyDef getPropertyDef(String propertyName) {
        return (PropertyDef) propertyDefs.get(propertyName);
    }

    public synchronized boolean hasPropertyDef(String propertyName) {
        return propertyDefs.containsKey(propertyName);
    }

    public void setContainer(S2Container container) {
        this.container = container;
        for (int i = 0; i < getPropertyDefSize(); ++i) {
            getPropertyDef(i).setContainer(container);
        }
    }
}