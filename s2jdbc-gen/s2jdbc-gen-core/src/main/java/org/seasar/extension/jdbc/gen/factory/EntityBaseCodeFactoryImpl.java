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
package org.seasar.extension.jdbc.gen.factory;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.seasar.extension.jdbc.gen.EntityBaseCodeFactory;
import org.seasar.extension.jdbc.gen.model.AttributeDesc;
import org.seasar.extension.jdbc.gen.model.EntityBaseCode;
import org.seasar.extension.jdbc.gen.model.EntityDesc;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class EntityBaseCodeFactoryImpl implements EntityBaseCodeFactory {

    public EntityBaseCode getEntityBaseCode(EntityDesc entityDesc,
            String className) {
        EntityBaseCode code = new EntityBaseCode();
        code.setClassName(className);
        String[] elements = ClassUtil.splitPackageAndShortClassName(className);
        code.setPackageName(elements[0]);
        code.setShortClassName(elements[1]);
        code.setEntityDesc(entityDesc);
        doImportPackageNames(code, entityDesc);
        return code;
    }

    protected void doImportPackageNames(EntityBaseCode code,
            EntityDesc entityDesc) {
        code.addImportPackageName(MappedSuperclass.class.getName());
        for (AttributeDesc attr : entityDesc.getAttributeDescList()) {
            if (attr.isId()) {
                code.addImportPackageName(Id.class.getName());
                if (!entityDesc.hasCompositeId()) {
                    code.addImportPackageName(GeneratedValue.class.getName());
                }
            }
            if (attr.isLob()) {
                code.addImportPackageName(Lob.class.getName());
            }
            if (attr.getTemporalType() != null) {
                code.addImportPackageName(Temporal.class.getName());
                code.addImportPackageName(TemporalType.class.getName());
            }
            if (attr.isTransient()) {
                code.addImportPackageName(Transient.class.getName());
            }
            if (attr.isVersion()) {
                code.addImportPackageName(Version.class.getName());
            }

            String name = ClassUtil.getPackageName(attr.getAttributeClass());
            if (name != null && !"java.lang".equals(name)) {
                code.addImportPackageName(attr.getAttributeClass().getName());
            }
        }
    }
}
