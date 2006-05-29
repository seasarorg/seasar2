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

import java.util.List;

import org.seasar.framework.ejb.unit.ProxiedObjectResolver;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author taedium
 * 
 */
public class EntityListIntrospector extends EntityIntrospector {

    public EntityListIntrospector(List<?> entities,
            boolean introspectsRelationships, ProxiedObjectResolver resolver) {
        super(introspectsRelationships, resolver);

        if (entities == null) {
            throw new EmptyRuntimeException("entities");
        }

        for (Object entity : entities) {
            if (entity == null) {
                continue;
            }
            createClassDescs(entity);
        }
        setupRelationships();
    }
}
