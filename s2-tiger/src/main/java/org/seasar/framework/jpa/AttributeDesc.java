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
package org.seasar.framework.jpa;

import javax.persistence.TemporalType;

/**
 * @author koichik
 * 
 */
public interface AttributeDesc {

    String getName();

    Class<?> getType();

    Class<?> getElementType();

    int getSqlType();

    TemporalType getTemporalType();

    boolean isId();

    boolean isAssociation();

    boolean isCollection();

    boolean isComponent();

    boolean isVersion();

    Object getValue(Object entity);

    void setValue(Object entity, Object value);

}
