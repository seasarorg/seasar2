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
package org.seasar.extension.jdbc.gen.javacode;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;

import org.seasar.extension.jdbc.gen.model.EntityModel;

/**
 * @author taedium
 * 
 */
public class EntityCode extends AbstractJavaCode {

    protected EntityModel entityModel;

    protected Set<String> importPackageNames;

    protected String shortClassName;

    protected String shortBaseClassName;

    public EntityCode(EntityModel entityModel, String className,
            String baseClassName, String templateName) {
        super(className, templateName);
        this.entityModel = entityModel;
        this.importPackageNames = createImportPackageNames(baseClassName);
        this.shortClassName = getShortClassName(className);
        this.shortBaseClassName = getShortClassName(baseClassName);
    }

    public EntityModel getEntityModel() {
        return entityModel;
    }

    public Set<String> getImportPackageNames() {
        return importPackageNames;
    }

    public String getShortClassName() {
        return shortClassName;
    }

    public String getShortBaseClassName() {
        return shortBaseClassName;
    }

    protected Set<String> createImportPackageNames(String baseClassName) {
        Set<String> packageNames = new TreeSet<String>();
        packageNames.add(Entity.class.getName());
        packageNames.add(baseClassName);
        return packageNames;
    }

}
