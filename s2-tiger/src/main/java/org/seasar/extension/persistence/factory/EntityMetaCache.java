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

/**
 * @author higa
 * 
 */
public class EntityMetaCache {

    private File file;

    private long lastModified;

    private EntityMeta entityMeta;

    /**
     * <code>EntityMetaCache</code>を作成します。
     * 
     * @param file
     * @param entityMeta
     */
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

    /**
     * キャッシュしている<code>EntityMeta</code>が更新されているかどうかを返します。
     * 
     * @return
     */
    public boolean isModified() {
        if (file == null) {
            return false;
        }
        return file.lastModified() > lastModified;
    }

    /**
     * <code>EntityMeta</code>を返します。
     * 
     * @return entityMeta.
     */
    public EntityMeta getEntityMeta() {
        return entityMeta;
    }

    /**
     * <code>EntityMeta</code>を設定します。
     * 
     * @param entityMeta
     */
    public void setEntityMeta(EntityMeta entityMeta) {
        this.entityMeta = entityMeta;
    }

    /**
     * キャッシュしている.classの<code>File</code>を返します。
     * 
     * @return file.
     */
    public File getFile() {
        return file;
    }

    /**
     * キャッシュしている.classが最後に更新された時間を返します。
     * 
     * @return Returns the lastModified.
     */
    public long getLastModified() {
        return lastModified;
    }
}