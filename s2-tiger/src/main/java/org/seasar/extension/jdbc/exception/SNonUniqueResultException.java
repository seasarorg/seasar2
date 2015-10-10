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

import javax.persistence.NonUniqueResultException;

import org.seasar.framework.message.MessageFormatter;

/**
 * <p>
 * クエリの結果がユニークでない場合の例外です。 {@link NonUniqueResultException}のSeasar2拡張です。
 * </p>
 * 
 * @author higa
 * 
 */
public class SNonUniqueResultException extends NonUniqueResultException {

    private static final long serialVersionUID = 0L;

    private String sql;

    /**
     * {@link SNonUniqueResultException}を作成します。
     * 
     * @param sql
     *            SQL
     */
    public SNonUniqueResultException(String sql) {
        super(MessageFormatter.getMessage("ESSR0326", new Object[] { sql }));
        this.sql = sql;
    }

    /**
     * SQLを返します。
     * 
     * @return SQL
     */
    public String getSql() {
        return sql;
    }
}