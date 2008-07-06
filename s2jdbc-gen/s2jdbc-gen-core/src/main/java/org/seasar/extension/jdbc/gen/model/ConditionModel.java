/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.model;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * @author taedium
 * 
 */
public class ConditionModel {

    /** デフォルトのカラム */
    @Column
    protected static final Column DEFAULT_COLUMN = ReflectionUtil
            .getDeclaredField(ConditionModel.class, "DEFAULT_COLUMN")
            .getAnnotation(Column.class);

    protected SortedSet<String> importPackageNameSet = new TreeSet<String>();

    protected String className;

    protected String baseClassName;

    protected EntityMeta entityMeta;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getBaseClassName() {
        return baseClassName;
    }

    public void setBaseClassName(String baseClassName) {
        this.baseClassName = baseClassName;
    }

    public SortedSet<String> getImportPackageNameSet() {
        return Collections.unmodifiableSortedSet(importPackageNameSet);
    }

    public void addImportPackageName(String name) {
        if (!importPackageNameSet.contains(name)) {
            importPackageNameSet.add(name);
        }
    }

    public EntityMeta getEntityMeta() {
        return entityMeta;
    }

    public void setEntityMeta(EntityMeta entityMeta) {
        this.entityMeta = entityMeta;
    }

    public String getPackageName(String className) {
        if (className == null) {
            return null;
        }
        return ClassUtil.splitPackageAndShortClassName(className)[0];
    }

    public String getShortClassName(String className) {
        if (className == null) {
            return null;
        }
        return ClassUtil.splitPackageAndShortClassName(className)[1];
    }

    public String getWrapperShortClassName(PropertyMeta propertyMeta) {
        Class<?> clazz = ClassUtil.getWrapperClassIfPrimitive(propertyMeta
                .getPropertyClass());
        return clazz.getSimpleName();
    }

    public boolean isNullable(PropertyMeta propertyMeta) {
        Column column = getColumn(propertyMeta);
        return column.nullable();
    }

    protected Column getColumn(PropertyMeta propertyMeta) {
        Field field = propertyMeta.getField();
        Column column = field.getAnnotation(Column.class);
        return column != null ? column : DEFAULT_COLUMN;
    }
}
