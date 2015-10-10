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
 * <p>
 * ベースの結合が見つからない場合の例外です。
 * </p>
 * <p>
 * <code>aaa.bbb</code>という関連で結合する場合、<code>aaa</code>の結合を先に指定する必要があります。
 * </p>
 * 
 * @author higa
 * 
 */
public class BaseJoinNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String entityName;

    private String join;

    private String baseJoin;

    /**
     * {@link BaseJoinNotFoundRuntimeException}を作成します。
     * 
     * @param entityName
     *            エンティティ名
     * @param join
     *            結合名
     * @param baseJoin
     *            ベースの結合名
     */
    public BaseJoinNotFoundRuntimeException(String entityName, String join,
            String baseJoin) {
        super("ESSR0706", new Object[] { entityName, join, baseJoin });
        this.entityName = entityName;
        this.join = join;
        this.baseJoin = baseJoin;
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

    /**
     * ベースの結合名を返します。
     * 
     * @return ベースの結合名
     */
    public String getBaseJoin() {
        return baseJoin;
    }
}