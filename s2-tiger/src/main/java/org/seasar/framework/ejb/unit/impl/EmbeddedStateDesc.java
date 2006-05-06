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
package org.seasar.framework.ejb.unit.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentJoinColumn;
import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.ejb.unit.PersistentStateType;

/**
 * @author taedium
 * 
 */
public class EmbeddedStateDesc extends AbstractPersistentStateDesc {

    private EmbeddableClassDesc embeddedClassDesc;

    public EmbeddedStateDesc(PersistentClassDesc persistentClassDesc,
            String primaryTableName, PersistentStateAccessor accessor) {

        super(persistentClassDesc, primaryTableName, accessor);
        setColumn(new PersistentColumn(null, primaryTableName));
        introspect();
    }

    private void introspect() {
        if(annotatedElement.isAnnotationPresent(Id.class)
                || annotatedElement.isAnnotationPresent(EmbeddedId.class)) {
            setIdentifier(true);            
        }
        detectAttributeOverrides();
        setupEmbeddedClassDesc();
    }

    private void setupEmbeddedClassDesc() {
        embeddedClassDesc = new EmbeddableClassDesc(persistentStateClass,
                primaryTableName, accessor.isPropertyAccessor(), identifier);
        embeddedClassDesc.overrideAttributes(detectAttributeOverrides());
    }

    private Map<String, PersistentColumn> detectAttributeOverrides() {
        AttributeOverride ao = annotatedElement
                .getAnnotation(AttributeOverride.class);
        AttributeOverrides aos = annotatedElement
                .getAnnotation(AttributeOverrides.class);

        Map<String, PersistentColumn> attribOverrides = new HashMap<String, PersistentColumn>();

        if (ao != null) {
            attribOverrides.put(ao.name(), new PersistentColumn(ao.column()));
        } else if (aos != null) {
            for (AttributeOverride each : aos.value()) {
                attribOverrides.put(each.name(), new PersistentColumn(each
                        .column()));
            }
        }

        return attribOverrides;
    }

    public PersistentStateType getPersistentStateType() {
        return PersistentStateType.EMBEDDED;
    }

    @Override
    public PersistentClassDesc getEmbeddedClassDesc() {
        return embeddedClassDesc;
    }

    @Override
    public List<PersistentStateDesc> getEmbeddedStateDescs() {
        return embeddedClassDesc.getPersistentStateDescs();
    }

    @Override
    protected void adjustPkColumnsByReferencedColumnName(
            List<PersistentJoinColumn> pkJoinColumns) {
        List<PersistentStateDesc> ids = getEmbeddedStateDescs();

        for (PersistentStateDesc each : ids) {
            for (PersistentJoinColumn pk : pkJoinColumns) {
                if (each.hasColumn(pk.getReferencedColumnName())) {
                    if (pk.getName() != null) {
                        each.getColumn().setName(pk.getName());
                    }
                }
            }
        }
    }

    @Override
    protected void adjustPkColumnsByIndex(
            List<PersistentJoinColumn> pkJoinColumns) {
        List<PersistentStateDesc> ids = getEmbeddedStateDescs();

        for (PersistentStateDesc each : ids) {
            int index = getEmbeddedClassDesc().getIdentifiers().indexOf(each);
            if (index < 0) {
                return;
            }
            PersistentJoinColumn pk = pkJoinColumns.get(index);
            if (pk.getName() != null) {
                each.getColumn().setName(pk.getName());
            }
        }
    }

}
