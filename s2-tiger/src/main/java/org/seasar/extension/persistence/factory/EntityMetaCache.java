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

import org.seasar.extension.persistence.EntityMeta;
import org.seasar.framework.util.AssertionUtil;

/**
 * @author higa
 * 
 */
public class EntityMetaCache {

    private File file;

    private long lastModified;

    private EntityMeta entityMeta;

    public EntityMetaCache(File file, EntityMeta entityMeta) {
        setFile(file);
        setEntityMeta(entityMeta);
    }

    protected void setFile(File file) {
        if (file == null) {
            return;
        }
        this.file = file;
        lastModified = file.lastModified();
    }

    public boolean isModified() {
        if (file == null) {
            return false;
        }
        return file.lastModified() > lastModified;
    }

    /**
     * @return Returns the entityMeta.
     */
    public EntityMeta getEntityMeta() {
        return entityMeta;
    }

    /**
     * @param entityMeta
     *            The entityMeta to set.
     */
    public void setEntityMeta(EntityMeta entityMeta) {
        AssertionUtil.assertNotNull("entityMeta", entityMeta);
        this.entityMeta = entityMeta;
    }

    /**
     * @return Returns the file.
     */
    public File getFile() {
        return file;
    }

    /**
     * @return Returns the lastModified.
     */
    public long getLastModified() {
        return lastModified;
    }
}