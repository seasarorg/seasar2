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

import javax.persistence.DiscriminatorType;


/**
 * @author nakamura
 * 
 */
public class PersistentDiscriminatorColumn extends PersistentColumn {

    private final static String DEFAULT_NAME = "DTYPE";

    private DiscriminatorType type = DiscriminatorType.STRING;

    private Object value;

    private Class<?> persistenceTargetClass;

    public PersistentDiscriminatorColumn() {
        setName(DEFAULT_NAME);
    }
    
    public DiscriminatorType getType() {
        return type;
    }

    public void setType(DiscriminatorType type) {
        this.type = type;
        switch (type) {
        case STRING:
            persistenceTargetClass = String.class;
            break;
        case CHAR:
            persistenceTargetClass = Character.class;
            break;
        case INTEGER:
            persistenceTargetClass = Integer.class;
            break;
        default:
            persistenceTargetClass = String.class;
        }

    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Class<?> getPersistenceTargetClass() {
        return persistenceTargetClass;
    }
}
