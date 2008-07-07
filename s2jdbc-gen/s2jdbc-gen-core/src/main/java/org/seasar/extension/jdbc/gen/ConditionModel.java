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
package org.seasar.extension.jdbc.gen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.seasar.extension.jdbc.EntityMeta;

/**
 * @author taedium
 * 
 */
public class ConditionModel {

    protected String className;

    protected String packageName;

    protected String shortClassName;

    protected EntityMeta entityMeta;

    protected SortedSet<String> importPackageNameSet = new TreeSet<String>();

    protected List<ConditionAttributeModel> conditionAttributeModelList = new ArrayList<ConditionAttributeModel>();

    protected List<ConditionMethodModel> conditionMethodModelList = new ArrayList<ConditionMethodModel>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getShortClassName() {
        return shortClassName;
    }

    public void setShortClassName(String shortClassName) {
        this.shortClassName = shortClassName;
    }

    public SortedSet<String> getImportPackageNameSet() {
        return Collections.unmodifiableSortedSet(importPackageNameSet);
    }

    public void addImportPackageName(String name) {
        importPackageNameSet.add(name);
    }

    public List<ConditionAttributeModel> getConditionAttributeModelList() {
        return Collections.unmodifiableList(conditionAttributeModelList);
    }

    public void addConditionAttributeModel(
            ConditionAttributeModel conditionAttributeModel) {
        conditionAttributeModelList.add(conditionAttributeModel);
    }

    public List<ConditionMethodModel> getConditionMethodModelList() {
        return Collections.unmodifiableList(conditionMethodModelList);
    }

    public void addConditionMethodModel(
            ConditionMethodModel conditionMethodModel) {
        conditionMethodModelList.add(conditionMethodModel);
    }

    public EntityMeta getEntityMeta() {
        return entityMeta;
    }

    public void setEntityMeta(EntityMeta entityMeta) {
        this.entityMeta = entityMeta;
    }

}
