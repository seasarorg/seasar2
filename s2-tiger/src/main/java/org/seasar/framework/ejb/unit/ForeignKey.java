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

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author taedium
 * 
 */
public class ForeignKey {

    private final PersistentColumn foreignKeyColumn;

    private final PersistentStateDesc stateDesc;

    private EmbeddedStateDesc embeddedStateDesc;

    public ForeignKey(PersistentStateDesc stateDesc, PersistentColumn foreignKeyColumn) {
        if (stateDesc == null) {
            throw new EmptyRuntimeException("stateDesc");
        }
        if (foreignKeyColumn == null) {
            throw new EmptyRuntimeException("foreignKeyColumn");
        }
        this.stateDesc = stateDesc;
        this.foreignKeyColumn = foreignKeyColumn;
    }

    public Object getValue(Object relationship, ProxiedObjectResolver resolver) {
        if (embeddedStateDesc == null) {
            return stateDesc.getValue(relationship, resolver);
        }
        Object value = embeddedStateDesc.getValue(relationship, resolver);
        return stateDesc.getValue(value, resolver);
    }

    public PersistentColumn getColumn() {
        return foreignKeyColumn;
    }

    public void setEmbeddedStateDesc(EmbeddedStateDesc embeddedStateDesc) {
        this.embeddedStateDesc = embeddedStateDesc;
    }

    public Class<?> getPersistenceTargetClass() {
        if (embeddedStateDesc == null) {
            return stateDesc.getPersistenceTargetClass();
        }
        return embeddedStateDesc.getPersistenceTargetClass();
    }

}
