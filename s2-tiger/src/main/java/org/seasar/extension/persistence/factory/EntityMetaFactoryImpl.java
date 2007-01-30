/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.persistence.factory;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.seasar.extension.persistence.EntityMeta;
import org.seasar.extension.persistence.EntityMetaFactory;
import org.seasar.extension.persistence.TableMeta;
import org.seasar.extension.persistence.TableMetaFactory;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.hotdeploy.HotdeployUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;

/**
 * @author higa
 * 
 */
public class EntityMetaFactoryImpl implements EntityMetaFactory {

    private static final Class BYTE_ARRAY_CLASS = new Byte[0].getClass();

    private ConcurrentHashMap<String, EntityMetaCache> entityMetaCacheMap = new ConcurrentHashMap<String, EntityMetaCache>(
            200);

    private TableMetaFactory tableMetaFactory;

    /**
     * @return Returns the tableMetaFactory.
     */
    public TableMetaFactory getTableMetaFactory() {
        return tableMetaFactory;
    }

    /**
     * @param tableMetaFactory
     *            The tableMetaFactory to set.
     */
    @Binding(bindingType = BindingType.MUST)
    public void setTableMetaFactory(TableMetaFactory tableMetaFactory) {
        this.tableMetaFactory = tableMetaFactory;
    }

    public EntityMeta getEntityMeta(Class entityClass) {
        EntityMetaCache cache = entityMetaCacheMap.get(entityClass.getName());
        if (cache == null) {
            cache = createEntityMetaCache(entityClass);
            entityMetaCacheMap.put(entityClass.getName(), cache);
            return cache.getEntityMeta();
        }
        if (cache.isModified()) {
            synchronized (cache) {
                EntityMeta meta = createEntityMeta(entityClass);
                cache.setEntityMeta(meta);
            }
        }
        return cache.getEntityMeta();
    }

    protected EntityMetaCache createEntityMetaCache(Class entityClass) {
        File file = null;
        if (HotdeployUtil.isHotdeploy()) {
            file = ResourceUtil.getResourceAsFileNoException(entityClass);
        }
        EntityMeta entityMeta = createEntityMeta(entityClass);
        return new EntityMetaCache(file, entityMeta);
    }

    protected EntityMeta createEntityMeta(Class entityClass) {
        EntityMeta entityMeta = new EntityMeta();
        String entityName = fromClassToEntityName(entityClass);
        entityMeta.setName(entityName);
        TableMeta tableMeta = tableMetaFactory.createTableMeta(entityClass,
                entityName);
        entityMeta.setTableMeta(tableMeta);
        Field[] fields = entityClass.getDeclaredFields();
        for (Field f : fields) {
            if (!isInstanceField(f)) {
                continue;
            }
        }
        return entityMeta;
    }

    protected String fromClassToEntityName(Class entityClass) {
        return ClassUtil.getShortClassName(entityClass);
    }

    protected boolean isInstanceField(Field field) {
        int m = field.getModifiers();
        return !Modifier.isStatic(m) && !Modifier.isFinal(m);
    }

    protected boolean isSimpleValueType(Class type) {
        return type == String.class || type == Boolean.class
                || Number.class.isAssignableFrom(type)
                || Date.class.isAssignableFrom(type)
                || Calendar.class.isAssignableFrom(type)
                || type == BYTE_ARRAY_CLASS;
    }
}
