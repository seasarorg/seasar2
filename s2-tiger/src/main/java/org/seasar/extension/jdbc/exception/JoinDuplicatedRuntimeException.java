/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.exception;

import org.seasar.framework.exception.SRuntimeException;

/**
 * 結合が重複している場合の例外です。
 * 
 * @author higa
 * 
 */
public class JoinDuplicatedRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String entityName;

    private String join;

    /**
     * {@link JoinDuplicatedRuntimeException}を作成します。
     * 
     * @param entityName
     *            エンティティ名
     * @param join
     *            結合名
     */
    public JoinDuplicatedRuntimeException(String entityName, String join) {
        super("ESSR0707", new Object[] { entityName, join });
        this.entityName = entityName;
        this.join = join;
    }

    /**
     * エンティティ名を返します。
     * 
     * @return エンティティ名
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * 結合名を返します。
     * 
     * @return 結合名
     */
    public String getJoin() {
        return join;
    }
}