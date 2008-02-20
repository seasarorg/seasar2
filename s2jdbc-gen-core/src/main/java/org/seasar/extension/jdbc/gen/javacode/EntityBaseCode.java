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

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.model.PropertyModel;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class EntityBaseCode extends AbstractJavaCode {

    protected EntityModel entityModel;

    protected String packageName;

    protected Set<String> importPackageNames;

    protected String shortClassName;

    public EntityModel getEntityModel() {
        return entityModel;
    }

    public String getPackageName() {
        return packageName;
    }

    public Set<String> getImportPackageNames() {
        return Collections.unmodifiableSet(importPackageNames);
    }

    public String getShortClassName() {
        return shortClassName;
    }

    public EntityBaseCode(EntityModel entityModel, String className,
            String templateName) {
        super(className, templateName);
        this.entityModel = entityModel;
        this.packageName = getPackageName(className);
        this.importPackageNames = createImportPackageNames();
        this.shortClassName = getShortClassName(className);
    }

    protected String getPackageName(String className) {
        return ClassUtil.splitPackageAndShortClassName(className)[0];
    }

    protected String createPackageName(String className) {
        return ClassUtil.splitPackageAndShortClassName(className)[0];
    }

    protected Set<String> createImportPackageNames() {
        Set<String> packageNames = new TreeSet<String>();
        packageNames.add(MappedSuperclass.class.getName());
        for (PropertyModel pm : entityModel.getPropertyModelList()) {
            if (pm.isId()) {
                packageNames.add(Id.class.getName());
                packageNames.add(GeneratedValue.class.getName());
            } else if (pm.isLob()) {
                packageNames.add(Lob.class.getName());
            } else if (pm.getTemporalType() != null) {
                packageNames.add(Temporal.class.getName());
                packageNames.add(TemporalType.class.getName());
            } else if (pm.isTransient()) {
                packageNames.add(Transient.class.getName());
            } else if (pm.isVersion()) {
                packageNames.add(Version.class.getName());
            }

            String name = ClassUtil.getPackageName(pm.getPropertyClass());
            if (name != null && !"java.lang".equals(name)) {
                packageNames.add(pm.getPropertyClass().getName());
            }
        }
        return packageNames;
    }

}
