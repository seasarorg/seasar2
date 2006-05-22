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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentColumn;
import org.seasar.framework.ejb.unit.PersistentJoinColumn;
import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.ejb.unit.ToManyRelationshipStateDesc;

/**
 * @author taedium
 * 
 */
public class ToManyRelationshipStateDescImpl extends
        AbstractPersistentStateDesc implements ToManyRelationshipStateDesc {

    public ToManyRelationshipStateDescImpl(
            PersistentClassDesc persistentClassDesc, String primaryTableName,
            PersistentStateAccessor accessor) {

        super(persistentClassDesc, primaryTableName, accessor);
        setColumn(new PersistentColumn(null, primaryTableName));
        introspect();
    }

    protected void introspect() {
        OneToMany oneToMany = annotatedElement.getAnnotation(OneToMany.class);
        ManyToMany manyToMany = annotatedElement
                .getAnnotation(ManyToMany.class);
        Class<?> targetEntity = null;

        if (oneToMany != null) {
            targetEntity = oneToMany.targetEntity();
        } else if (manyToMany != null) {
            targetEntity = manyToMany.targetEntity();
        }

        if (targetEntity == null || targetEntity == void.class) {
            targetEntity = extractTargetEntityFromCollection(accessor
                    .getGenericType());
        }
        setPersistenceTargetClass(targetEntity);

    }

    private Class extractTargetEntityFromCollection(Type t) {
        if (t != null && t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) t;
            Type[] genTypes = pt.getActualTypeArguments();
            if (genTypes.length == 1 && genTypes[0] instanceof Class) {
                return (Class) genTypes[0];
            } else if (genTypes.length == 2 && genTypes[1] instanceof Class) {
                return (Class) genTypes[1];
            }
        } else if (t instanceof Class) {
            return (Class) t;
        }
        return null;
    }

    public void adjustPrimaryKeyColumns(List<PersistentJoinColumn> pkJoinColumns) {
        throw new UnsupportedOperationException("adjustPrimaryKeyColumns");
    }

}
