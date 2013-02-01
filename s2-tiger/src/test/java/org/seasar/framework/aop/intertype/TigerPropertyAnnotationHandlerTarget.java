/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.intertype;

import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Property;
import org.seasar.framework.container.annotation.tiger.PropertyType;

/**
 * 
 */
@Property(PropertyType.NONE)
public class TigerPropertyAnnotationHandlerTarget implements
        PropertyInterTypeTarget {

    @Property(PropertyType.READ)
    int intReadField_ = 123;

    @Property(PropertyType.WRITE)
    int intWriteField_;

    @Property(PropertyType.READWRITE)
    int intReadWriteField_;

    @Property(PropertyType.NONE)
    int intNoneField_;

    /**
     * 
     */
    @Property(PropertyType.READWRITE)
    public int publicField_;

    /**
     * 
     */
    @Property(PropertyType.READWRITE)
    protected int protectedField_;

    @SuppressWarnings("unused")
    @Property(PropertyType.READWRITE)
    private int privateField_;

    int nonAnnotatedField_;

    @Property(PropertyType.READWRITE)
    Object objectField_;

    @Property(PropertyType.READWRITE)
    String[] stringArrayField_;

    @Property(PropertyType.READWRITE)
    long[][] longArrayField_;

    @Property(PropertyType.READWRITE)
    @Binding(bindingType = BindingType.MUST)
    Object testObject;

    public int getIntWriteField() {
        return intWriteField_;
    }
}
