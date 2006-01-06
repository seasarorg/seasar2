/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

import org.seasar.framework.aop.intertype.PropertyInterType.PropertyAnnotationHandler;
import org.seasar.framework.container.annotation.tiger.Property;
import org.seasar.framework.container.annotation.tiger.PropertyType;

/**
 * @author y-komori
 * 
 */
public class TigerPropertyAnnotationHandler implements PropertyAnnotationHandler {
    public int getPropertyType(Class clazz) {
        return getPropertyTypeInternal(clazz);
    }

    public int getPropertyType(Field field) {
        return getPropertyTypeInternal(field);

    }

    public int getPropertyTypeInternal(AnnotatedElement element) {
        Property property = element.getAnnotation(Property.class);
        int propertyType = PropertyInterType.UNSPECIFIED;
        if (property != null) {
            PropertyType type = property.value();
            if (type == PropertyType.NONE) {
                propertyType = PropertyInterType.NONE;
            } else if (type == PropertyType.READ) {
                propertyType = PropertyInterType.READ;
            } else if (type == PropertyType.WRITE) {
                propertyType = PropertyInterType.WRITE;
            } else if (type == PropertyType.READWRITE) {
                propertyType = PropertyInterType.READWRITE;
            }
        }

        return propertyType;
    }
}
