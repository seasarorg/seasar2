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

import org.seasar.framework.container.annotation.tiger.InterType;
import org.seasar.framework.container.annotation.tiger.Property;
import org.seasar.framework.container.annotation.tiger.PropertyType;

/**
 * 
 */
@InterType("aop.propertyInterType")
public class TigerPropertyAnnotationHandlerTarget2 {

    int defaultField;

    @Property(PropertyType.READ)
    int readField;

    @Property(PropertyType.WRITE)
    int writeField;

    @Property(PropertyType.READWRITE)
    int readWriteField;

    @Property(PropertyType.NONE)
    int noneField;

    int hasGetter;

    int hasSetter;

    int hasGetterSetter;

    /**
     * 
     */
    public TigerPropertyAnnotationHandlerTarget2() {
    }

    /**
     * @return
     */
    public int getHasGetter() {
        return hasGetter;
    }

    /**
     * @param hasSetter
     */
    public void setHasSetter(int hasSetter) {
        this.hasSetter = hasSetter;
    }

    /**
     * @return
     */
    public int getHasGetterSetter() {
        return hasGetterSetter;
    }

    /**
     * @param hasGetterSetter
     */
    public void setHasGetterSetter(int hasGetterSetter) {
        this.hasGetterSetter = hasGetterSetter;
    }
}
