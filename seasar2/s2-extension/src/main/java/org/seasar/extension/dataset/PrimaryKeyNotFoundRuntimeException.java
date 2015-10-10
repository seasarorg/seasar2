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
package org.seasar.extension.dataset;

import org.seasar.framework.exception.SRuntimeException;

/**
 * テーブルが見つからない場合の例外です。
 * 
 * @author higa
 * 
 */
public class PrimaryKeyNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = -8455516906109819288L;

    private String tableName;

    /**
     * {@link PrimaryKeyNotFoundRuntimeException}を作成します。
     * 
     * @param tableName
     *            テーブル名
     */
    public PrimaryKeyNotFoundRuntimeException(String tableName) {
        super("ESSR0101", new Object[] { tableName });
        this.tableName = tableName;
    }

    /**
     * テーブル名を返します。
     * 
     * @return テーブル名
     */
    public String getTableName() {
        return tableName;
    }
}