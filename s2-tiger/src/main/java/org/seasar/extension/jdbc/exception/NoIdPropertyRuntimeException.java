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
 * 識別子がないエンティティを更新または削除しようとした場合の例外です。
 * 
 * @author koichik
 */
public class NoIdPropertyRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    /** エンティティ名 */
    protected String entityName;

    /**
     * インスタンスを構築します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param entityName
     *            エンティティ名
     */
    public NoIdPropertyRuntimeException(final String messageCode,
            final String entityName) {
        super(messageCode, new Object[] { entityName });
        this.entityName = entityName;
    }

    /**
     * エンティティ名を返します。
     * 
     * @return エンティティ名
     */
    public String getEntityName() {
        return entityName;
    }

}
