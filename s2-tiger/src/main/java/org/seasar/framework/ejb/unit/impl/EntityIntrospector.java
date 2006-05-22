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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.seasar.framework.ejb.unit.EntityClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateDesc;
import org.seasar.framework.ejb.unit.ProxiedObjectResolver;
import org.seasar.framework.ejb.unit.ToManyRelationshipStateDesc;
import org.seasar.framework.ejb.unit.ToOneRelationshipStateDesc;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author taedium
 * 
 */
public class EntityIntrospector {

    private static final Object PRESENT = new Object();

    private final Map<Object, Object> processed = new IdentityHashMap<Object, Object>();

    private final Map<Class, EntityClassDesc> classDescs = new HashMap<Class, EntityClassDesc>();

    private final ProxiedObjectResolver resolver;

    private final boolean introspectsRelationships;

    private Stack<Object> stateDescStatck = new Stack<Object>();

    protected EntityIntrospector(boolean introspectsRelationships,
            ProxiedObjectResolver resolver) {
        this.introspectsRelationships = introspectsRelationships;
        this.resolver = resolver;
    }

    public EntityIntrospector(Object entity, boolean introspectsRelationships,
            ProxiedObjectResolver resolver) {
        this(introspectsRelationships, resolver);

        if (entity == null) {
            throw new EmptyRuntimeException("entity");
        }

        createClassDescs(entity);
        setupRelationships();
    }

    public EntityClassDesc getEntityClassDesc(Object entity) {
        return getEntityClassDesc(entity.getClass());
    }

    public EntityClassDesc getEntityClassDesc(Class<?> entityClass) {
        return classDescs.get(entityClass);
    }

    public List<EntityClassDesc> getAllEntityClassDescs() {
        return new ArrayList<EntityClassDesc>(classDescs.values());
    }

    protected void createClassDescs(Object entity) {
        Object real = resolver.unproxy(entity);
        createClassDescsByClass(real.getClass());
        if (introspectsRelationships) {
            createClassDescsByInstance(real);
        }
    }

    protected void createClassDescsByClass(Class entityClass) {
        if (classDescs.containsKey(entityClass)) {
            return;
        }

        EntityClassDescImpl entityDesc = new EntityClassDescImpl(entityClass);
        classDescs.put(entityClass, entityDesc);

        for (PersistentStateDesc stateDesc : entityDesc
                .getPersistentStateDescs()) {

            if (introspectsRelationships || stateDescStatck.isEmpty()) {
                stateDescStatck.push(stateDescStatck);
                if ((stateDesc instanceof ToOneRelationshipStateDesc)
                        || (stateDesc instanceof ToManyRelationshipStateDesc)) {
                    createClassDescsByClass(stateDesc
                            .getPersistenceTargetClass());
                }
                stateDescStatck.pop();
            }
        }
    }

    protected void createClassDescsByInstance(Object entity) {
        if (isProcessed(entity)) {
            return;
        }

        EntityClassDescImpl entityDesc = new EntityClassDescImpl(entity
                .getClass());
        classDescs.put(entity.getClass(), entityDesc);

        for (PersistentStateDesc stateDesc : entityDesc
                .getPersistentStateDescs()) {
            Object state = stateDesc.getValue(entity, resolver);
            if (state == null) {
                continue;
            }

            if (stateDesc instanceof ToManyRelationshipStateDesc) {
                for (Object element : getElements(state)) {
                    createClassDescsByInstance(resolver.unproxy(element));
                }
            } else if (stateDesc instanceof ToOneRelationshipStateDesc) {
                createClassDescsByInstance(state);
            }
        }
    }

    protected void setupRelationships() {
        for (EntityClassDesc classDesc : classDescs.values()) {
            for (PersistentStateDesc stateDesc : classDesc
                    .getPersistentStateDescs()) {

                if (stateDesc instanceof ToOneRelationshipStateDesc) {
                    ToOneRelationshipStateDesc toOne = (ToOneRelationshipStateDesc) stateDesc;
                    EntityClassDesc relationship = classDescs.get(toOne
                            .getPersistenceTargetClass());
                    toOne.setupForeignKeys(relationship);
                }
            }
        }
    }

    protected boolean isProcessed(Object entity) {
        return entity == null || processed.put(entity, PRESENT) != null;
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
