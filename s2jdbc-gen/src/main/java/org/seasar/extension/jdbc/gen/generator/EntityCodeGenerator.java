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

import javax.persistence.Entity;

import org.seasar.extension.jdbc.gen.model.EntityModel;

import freemarker.template.Configuration;

/**
 * @author taedium
 * 
 */
public class EntityCodeGenerator extends AbstractCodeGenerator {

    public EntityCodeGenerator(EntityModel entityModel, String entityClassName,
            String entityGapClassName, String templateName,
            Configuration configuration, String encoding, File destDir) {
        super(entityModel, entityClassName, entityGapClassName, templateName,
                configuration, encoding, destDir);
    }

    @Override
    protected Set<String> getImports() {
        Set<String> result = new TreeSet<String>();
        result.add(Entity.class.getName());
        result.add(entityGapClassName);
        return result;
    }

    @Override
    protected String getTargetClassName() {
        return entityClassName;
    }

}
