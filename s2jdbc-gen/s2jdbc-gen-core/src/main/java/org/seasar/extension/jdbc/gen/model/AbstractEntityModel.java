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

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.seasar.extension.jdbc.gen.EntityDesc;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public abstract class AbstractEntityModel {

    protected SortedSet<String> importPackageNameSet = new TreeSet<String>();

    protected String className;

    protected String baseClassName;

    protected EntityDesc entityDesc;

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

    public EntityDesc getEntityDesc() {
        return entityDesc;
    }

    public void setEntityDesc(EntityDesc entityDesc) {
        this.entityDesc = entityDesc;
    }

    public String getPackageName() {
        return ClassUtil.splitPackageAndShortClassName(className)[0];
    }

    public String getShortClassName() {
        return ClassUtil.splitPackageAndShortClassName(className)[1];
    }

    public String getShortBaseClassName() {
        return ClassUtil.splitPackageAndShortClassName(baseClassName)[1];
    }

}
