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
package org.seasar.framework.ejb.unit;

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author taedium
 * 
 */
public class PersistentStateNotFoundException extends SRuntimeException {
    private static final long serialVersionUID = 1L;

    private Class targetClass;

    private String persistentStateName;

    private boolean property;

    public PersistentStateNotFoundException(Class targetClass,
            String persistentStateName, boolean property) {
        super(property ? "ESSR0503" : "ESSR0502", new Object[] {
                targetClass.getName(), persistentStateName });
        this.targetClass = targetClass;
        this.persistentStateName = persistentStateName;
        this.property = property;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public String getPersistentStateName() {
        return persistentStateName;
    }

    public boolean isProperty() {
        return property;
    }
}
