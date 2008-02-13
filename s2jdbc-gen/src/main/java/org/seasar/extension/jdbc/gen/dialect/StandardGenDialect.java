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
package org.seasar.extension.jdbc.gen.dialect;

import javax.persistence.TemporalType;

import org.seasar.extension.jdbc.gen.GenerationDialect;

/**
 * @author taedium
 * 
 */
public class StandardGenDialect implements GenerationDialect {

    public boolean isUserTable(String tableName) {
        return true;
    }

    public boolean isLobType(String typeName) {
        return "BLOB".equals(typeName) || "CLOB".equals(typeName);
    }

    public TemporalType getTemporalType(String typeName) {
        if ("DATE".equals(typeName)) {
            return TemporalType.DATE;
        } else if ("TIME".equals(typeName)) {
            return TemporalType.TIME;
        } else if ("TIMESTAMP".equals(typeName)) {
            return TemporalType.TIMESTAMP;
        }
        return null;
    }

    public String getDefaultSchema(String userName) {
        return null;
    }

}
