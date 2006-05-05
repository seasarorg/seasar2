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

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.seasar.framework.ejb.unit.PersistentClassDesc;
import org.seasar.framework.ejb.unit.PersistentStateAccessor;
import org.seasar.framework.ejb.unit.PersistentStateDesc;

/**
 * @author taedium
 * 
 */
public class PersistentStateDescFactory {

    private static final Set<Class<?>> BASICS = new HashSet<Class<?>>();

    static {
        BASICS.add(byte.class);
        BASICS.add(short.class);
        BASICS.add(int.class);
        BASICS.add(long.class);
        BASICS.add(double.class);
        BASICS.add(float.class);
        BASICS.add(Byte.class);
        BASICS.add(Short.class);
        BASICS.add(Integer.class);
        BASICS.add(Long.class);
        BASICS.add(Double.class);
        BASICS.add(Float.class);
        BASICS.add(String.class);
        BASICS.add(BigInteger.class);
        BASICS.add(BigDecimal.class);
        BASICS.add(java.util.Date.class);
        BASICS.add(Calendar.class);
        BASICS.add(java.sql.Date.class);
        BASICS.add(java.sql.Time.class);
        BASICS.add(Timestamp.class);
        BASICS.add(byte[].class);
        BASICS.add(Byte[].class);
        BASICS.add(char[].class);
        BASICS.add(Character[].class);
        BASICS.add(Enum.class);
        BASICS.add(Serializable.class);
    }

    private PersistentStateDescFactory() {
    }

    public static PersistentStateDesc getPersistentStateDesc(
            PersistentClassDesc persistentClassDesc, String primaryTableName,
            PersistentStateAccessor accessor) {

        if (!accessor.isPersisteceAccessor()) {
            return null;
        }

        AnnotatedElement element = accessor.getAnnotatedElement();

        if (isToManyRelationship(element)) {
            return new ToManyRelationshipStateDesc(persistentClassDesc,
                    primaryTableName, accessor);
        }
        if (isToOneRelationship(element)) {
            return new ToOneRelationshipStateDesc(persistentClassDesc,
                    primaryTableName, accessor);
        }
        if (isEmbedded(element)) {
            return new EmbeddedStateDesc(persistentClassDesc, primaryTableName,
                    accessor);
        }
        if (isBasic(element)) {
            return new BasicStateDesc(persistentClassDesc, primaryTableName,
                    accessor);
        }

        Class<?> persistentStateClass = accessor.getPersistentStateClass();

        if (isEmbeddableClass(persistentStateClass)) {
            return new EmbeddedStateDesc(persistentClassDesc, primaryTableName,
                    accessor);
        }
        if (isBasicClass(persistentStateClass)) {
            return new BasicStateDesc(persistentClassDesc, primaryTableName,
                    accessor);
        }

        return null;
    }

    private static boolean isToManyRelationship(AnnotatedElement element) {
        return element.isAnnotationPresent(OneToMany.class)
                || element.isAnnotationPresent(ManyToMany.class);
    }

    private static boolean isToOneRelationship(AnnotatedElement element) {
        return element.isAnnotationPresent(OneToOne.class)
                || element.isAnnotationPresent(ManyToOne.class);
    }

    private static boolean isEmbedded(AnnotatedElement element) {
        return element.isAnnotationPresent(Embedded.class)
                || element.isAnnotationPresent(EmbeddedId.class);
    }

    private static boolean isBasic(AnnotatedElement element) {
        return element.isAnnotationPresent(Basic.class);
    }

    private static boolean isEmbeddableClass(Class<?> clazz) {
        return clazz.isAnnotationPresent(Embeddable.class);
    }

    private static boolean isBasicClass(Class<?> clazz) {
        if (BASICS.contains(clazz)) {
            return true;
        }
        for (Class<?> basicClass : BASICS) {
            if (basicClass.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }
}
