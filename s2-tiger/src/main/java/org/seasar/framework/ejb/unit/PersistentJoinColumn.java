/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.ejb.unit;

import javax.persistence.JoinColumn;
import javax.persistence.PrimaryKeyJoinColumn;

import org.seasar.framework.util.StringUtil;

/**
 * @author nakamura
 * 
 */
public class PersistentJoinColumn extends PersistentColumn {

    private String referencedColumnName;

    public PersistentJoinColumn() {
    }

    public PersistentJoinColumn(JoinColumn column) {
        this(column.name(), column.table(), column.referencedColumnName());
    }

    public PersistentJoinColumn(PrimaryKeyJoinColumn column) {
        this(column.name(), null, column.referencedColumnName());
    }

    public PersistentJoinColumn(PersistentJoinColumn column) {
        this(column.getName(), column.getTable(), column
                .getReferencedColumnName());
    }

    public PersistentJoinColumn(String name, String tableName,
            String referencedColumnName) {
        super(name, tableName);
        setReferencedColumnName(referencedColumnName);
    }

    public String getReferencedColumnName() {
        return referencedColumnName;
    }

    public void setReferencedColumnName(String referencedColumnName) {
        this.referencedColumnName = StringUtil.isEmpty(referencedColumnName) ? null
                : referencedColumnName;
    }

}
