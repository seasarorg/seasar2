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
package org.seasar.extension.jdbc.gen.desc;

import java.lang.reflect.Field;

import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.SequenceDesc;
import org.seasar.extension.jdbc.gen.SequenceDescFactory;
import org.seasar.extension.jdbc.gen.util.AnnotationUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class SequenceDescFactoryImpl implements SequenceDescFactory {

    protected GenDialect dialect;

    /**
     * @param dialect
     */
    public SequenceDescFactoryImpl(GenDialect dialect) {
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        this.dialect = dialect;
    }

    public SequenceDesc getSequenceDesc(EntityMeta entityMeta,
            PropertyMeta propertyMeta) {
        GenerationType generationType = propertyMeta.getGenerationType();
        if (generationType == GenerationType.AUTO) {
            generationType = dialect.getDefaultGenerationType();
        }
        if (generationType == GenerationType.SEQUENCE) {
            SequenceGenerator generator = getSequenceGenerator(propertyMeta);
            SequenceDesc sequenceDesc = new SequenceDesc();
            String sequenceName = getSequenceName(entityMeta, propertyMeta,
                    generator);
            sequenceDesc.setSequenceName(sequenceName);
            sequenceDesc.setInitialValue(generator.initialValue());
            sequenceDesc.setAllocationSize(generator.allocationSize());
            sequenceDesc.setDataType(getDataType(propertyMeta));
            return sequenceDesc;
        }
        return null;
    }

    protected SequenceGenerator getSequenceGenerator(PropertyMeta propertyMeta) {
        Field field = propertyMeta.getField();
        SequenceGenerator sequenceGenerator = field
                .getAnnotation(SequenceGenerator.class);
        return sequenceGenerator != null ? sequenceGenerator : AnnotationUtil
                .getDefaultSequenceGenerator();
    }

    protected String getSequenceName(EntityMeta entityMeta,
            PropertyMeta propertyMeta, SequenceGenerator sequenceGenerator) {
        String sequenceName = sequenceGenerator.sequenceName();
        if (!StringUtil.isEmpty(sequenceName)) {
            return sequenceName;
        }
        return entityMeta.getTableMeta().getName() + "_"
                + propertyMeta.getColumnMeta().getName();
    }

    protected String getDataType(PropertyMeta propertyMeta) {
        int sqlType = propertyMeta.getValueType().getSqlType();
        return dialect.getDbType(sqlType).getDefinition(0, 20, 0);
    }
}
