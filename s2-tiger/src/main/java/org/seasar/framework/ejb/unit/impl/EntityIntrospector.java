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

import java.util.ArrayList;
import static org.seasar.framework.ejb.unit.PersistentStateType.TO_MANY;
import static org.seasar.framework.ejb.unit.PersistentStateType.TO_ONE;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.ejb.unit.PersistentStateType;
import org.seasar.framework.ejb.unit.ProxiedObjectResolver;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author taedium
 * 
 */
public class EntityIntrospector {

    private static final Object PRESENT = new Object();

    private final Map<Object, Object> processed = new IdentityHashMap<Object, Object>();

    private final Map<Class, PersistentClassDesc> classDescs = new HashMap<Class, PersistentClassDesc>();

    private final ProxiedObjectResolver resolver;

    protected EntityIntrospector(ProxiedObjectResolver resolver) {
        this.resolver = resolver;
    }

    public EntityIntrospector(Object entity, ProxiedObjectResolver resolver) {
        this(resolver);
        if (entity == null) {
            throw new EmptyRuntimeException("entity");
        }
        createClassDescs(entity);
        setupRelationships();
    }

    public PersistentClassDesc getPersistentClassDesc(Class<?> entityClass) {
        return classDescs.get(entityClass);
    }

    public List<PersistentClassDesc> getAllPersistentClassDescs() {
        return new ArrayList<PersistentClassDesc>(classDescs.values());
    }

    protected void createClassDescs(Object entity) {
        Object real = unproxy(entity);
        createClassDescsByClass(real.getClass());
        createClassDescsByInstance(real);
    }

    protected void createClassDescsByClass(Class entityClass) {
        if (classDescs.containsKey(entityClass)) {
            return;
        }

        EntityClassDesc entityDesc = new EntityClassDesc(entityClass);
        classDescs.put(entityClass, entityDesc);

        for (PersistentStateDesc stateDesc : entityDesc
                .getPersistentStateDescs()) {

            PersistentStateType stateType = stateDesc.getPersistentStateType();
            if (stateType == TO_MANY || stateType == TO_ONE) {
                createClassDescsByClass(stateDesc.getPersistenceTargetClass());
            }
        }
    }

    protected void createClassDescsByInstance(Object entity) {
        if (isProcessed(entity)) {
            return;
        }

        EntityClassDesc entityDesc = new EntityClassDesc(entity.getClass());
        classDescs.put(entity.getClass(), entityDesc);

        for (PersistentStateDesc stateDesc : entityDesc
                .getPersistentStateDescs()) {
            Object state = unproxy(stateDesc.getValue(entity));
            if (state == null) {
                continue;
            }

            PersistentStateType stateType = stateDesc.getPersistentStateType();
            if (stateType == TO_MANY) {
                for (Object element : getElements(state)) {
                    createClassDescsByInstance(unproxy(element));
                }
            } else if (stateType == TO_ONE) {
                createClassDescsByInstance(state);
            }
        }
    }

    protected void setupRelationships() {
        for (PersistentClassDesc classDesc : classDescs.values()) {

            for (PersistentStateDesc stateDesc : classDesc
                    .getPersistentStateDescs()) {

                PersistentStateType stateType = stateDesc
                        .getPersistentStateType();
                if (stateType == TO_ONE) {
                    PersistentClassDesc relationship = classDescs.get(stateDesc
                            .getPersistenceTargetClass());
                    stateDesc.setupForeignKeyColumns(relationship);
                }
            }
        }
    }

    protected boolean isProcessed(Object entity) {
        return entity == null || processed.put(entity, PRESENT) != null;
    }

    public Object unproxy(Object value) {
        if (resolver == null) {
            return value;
        }
        return resolver.unproxy(value);
    }

    public Collection getElements(Object toManyRelationship) {
        if (toManyRelationship instanceof Collection) {
            return (Collection) toManyRelationship;
        } else if (toManyRelationship instanceof Map) {
            return ((Map) toManyRelationship).values();
        } else {
            return Collections.EMPTY_LIST;
        }
    }
}
