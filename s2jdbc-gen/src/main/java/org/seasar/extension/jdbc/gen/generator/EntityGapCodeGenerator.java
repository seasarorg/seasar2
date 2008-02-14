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
package org.seasar.extension.jdbc.gen.generator;

import java.io.File;
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

import freemarker.template.Configuration;

/**
 * @author taedium
 * 
 */
public class EntityGapCodeGenerator extends AbstractCodeGenerator {

    public EntityGapCodeGenerator(EntityModel entityModel,
            String entityClassName, String entityGapClassName,
            String templateName, Configuration configuration, String encoding,
            File destDir) {
        super(entityModel, entityClassName, entityGapClassName, templateName,
                configuration, encoding, destDir);
    }

    protected Set<String> getImports() {
        Set<String> result = new TreeSet<String>();
        result.add(MappedSuperclass.class.getName());
        for (PropertyModel pm : entityModel.getPropertyModelList()) {
            if (pm.isId()) {
                result.add(Id.class.getName());
                result.add(GeneratedValue.class.getName());
            } else if (pm.isLob()) {
                result.add(Lob.class.getName());
            } else if (pm.getTemporalType() != null) {
                result.add(Temporal.class.getName());
                result.add(TemporalType.class.getName());
            } else if (pm.isTransient()) {
                result.add(Transient.class.getName());
            } else if (pm.isVersion()) {
                result.add(Version.class.getName());
            }

            String pkgName = ClassUtil.getPackageName(pm.getPropertyClass());
            if (pkgName != null && !"java.lang".equals(pkgName)) {
                result.add(pm.getPropertyClass().getName());
            }
        }
        return result;
    }

    @Override
    protected String getTargetClassName() {
        return entityGapClassName;
    }

}
