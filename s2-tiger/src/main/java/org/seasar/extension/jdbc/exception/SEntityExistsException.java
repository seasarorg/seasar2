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

import javax.persistence.EntityExistsException;

import org.seasar.framework.message.MessageFormatter;

/**
 * 一意制約違反のためにエンティティを永続化できなかった場合の例外です。
 * <p>
 * {@link EntityExistsException}のSeasar2拡張です。
 * </p>
 * 
 * @author koichik
 */
public class SEntityExistsException extends EntityExistsException {

    private static final long serialVersionUID = 0L;

    /** 一意制約違反となったSQL */
    protected final String sql;

    /**
     * インスタンスを構築します。
     * 
     * @param sql
     *            一意制約違反となったSQL
     * @param cause
     *            原因となった例外
     */
    public SEntityExistsException(final String sql, final Throwable cause) {
        super(MessageFormatter.getMessage("ESSR0744", new Object[] { sql }),
                cause);
        this.sql = sql;
    }

    /**
     * インスタンスを構築します。
     * 
     * @param entity
     *            エンティティ
     * @param sql
     *            一意制約違反となったSQL
     * @param cause
     *            原因となった例外
     */
    public SEntityExistsException(final Object entity, final String sql,
            final Throwable cause) {
        super(MessageFormatter.getMessage("ESSR0745", new Object[] { entity,
                sql }), cause);
        this.sql = sql;
    }

    /**
     * 一意制約違反となったSQLを返します。
     * 
     * @return 一意制約違反となったSQL
     */
    public String getSql() {
        return sql;
    }

}
