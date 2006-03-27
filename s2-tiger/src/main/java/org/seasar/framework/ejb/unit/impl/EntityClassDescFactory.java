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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateDesc;

/**
 * @author taedium
 * 
 */
public final class EntityClassDescFactory {

    private static Map<Class<?>, EntityClassDesc> entityDescCache = new HashMap<Class<?>, EntityClassDesc>();

    private static Map<Class<?>, EntityClassDesc> innerCache = new HashMap<Class<?>, EntityClassDesc>();

    private EntityClassDescFactory() {
    }

    public static EntityClassDesc getEntityClassDesc(Class entityClass) {

        if (entityDescCache.containsKey(entityClass)) {
            return entityDescCache.get(entityClass);
        }

        EntityClassDesc entityDesc = getInstance(entityClass);
        setupRelationships(entityDesc, new HashSet<PersistentClassDesc>());
        setupForeignKeyColumns(entityDesc, new HashSet<PersistentClassDesc>());
        innerCache.clear();

        entityDescCache.put(entityClass, entityDesc);
        return entityDesc;
    }

    private static void setupRelationships(PersistentClassDesc me,
            Collection<PersistentClassDesc> processed) {

        if (processed.contains(me)) {
            return;
        }
        processed.add(me);

        for (int i = 0; i < me.getPersistentStateDescSize(); i++) {
            PersistentStateDesc stateDesc = me.getPersistentStateDesc(i);
            if (stateDesc.isToOneRelationship()
                    || stateDesc.isToManyRelationship()) {
                Class clazz = null;
                if (stateDesc.isCollection()) {
                    clazz = stateDesc.getCollectionType();
                } else {
                    clazz = stateDesc.getPersistentStateType();
                }
                EntityClassDesc you = getInstance(clazz);
                stateDesc.setRelationshipClassDesc(you);
                setupRelationships(you, processed);
            }
        }
    }

    private static void setupForeignKeyColumns(PersistentClassDesc me,
            Collection<PersistentClassDesc> processed) {

        if (processed.contains(me)) {
            return;
        }
        processed.add(me);

        for (int i = 0; i < me.getPersistentStateDescSize(); i++) {
            PersistentStateDesc stateDesc = me.getPersistentStateDesc(i);
            if (stateDesc.isToOneRelationship()
                    || stateDesc.isToManyRelationship()) {
                PersistentClassDesc relDesc = stateDesc
                        .getRelationshipClassDesc();
                setupForeignKeyColumns(relDesc, processed);
                if (stateDesc.isToOneRelationship() && stateDesc.isOwningSide()) {
                    stateDesc.setupForeignKeyColumns();
                }
            }
        }
    }

    private static EntityClassDesc getInstance(Class entityClass) {
        if (innerCache.containsKey(entityClass)) {
            return innerCache.get(entityClass);
        }
        EntityClassDesc entityDesc = new EntityClassDesc(entityClass);
        innerCache.put(entityClass, entityDesc);
        return entityDesc;
    }
}
