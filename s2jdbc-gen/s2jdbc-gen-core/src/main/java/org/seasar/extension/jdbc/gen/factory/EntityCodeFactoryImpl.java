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

import javax.persistence.Entity;

import org.seasar.extension.jdbc.gen.EntityCodeFactory;
import org.seasar.extension.jdbc.gen.model.EntityCode;
import org.seasar.extension.jdbc.gen.model.EntityDesc;
import org.seasar.framework.util.ClassUtil;

/**
 * @author taedium
 * 
 */
public class EntityCodeFactoryImpl implements EntityCodeFactory {

    public EntityCode getEntityCode(EntityDesc entityDesc, String className,
            String baseClassName) {
        EntityCode code = new EntityCode();
        code.setClassName(className);
        String[] elements = ClassUtil.splitPackageAndShortClassName(className);
        code.setPackageName(elements[0]);
        code.setShortClassName(elements[1]);
        code.setBaseClassName(baseClassName);
        String[] elements2 = ClassUtil
                .splitPackageAndShortClassName(baseClassName);
        code.setShortBaseClassName(elements2[1]);
        code.setEntityDesc(entityDesc);
        doImportPackageNames(code, entityDesc);
        return code;
    }

    protected void doImportPackageNames(EntityCode code, EntityDesc entityDesc) {
        code.addImportPackageName(Entity.class.getName());
        code.addImportPackageName(code.getBaseClassName());
    }
}
