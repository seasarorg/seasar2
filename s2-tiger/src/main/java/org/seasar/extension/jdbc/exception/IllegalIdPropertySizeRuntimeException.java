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
 * IDプロパティの数が不正な場合の例外です。
 * </p>
 * 
 * @author koichik
 */
public class IllegalIdPropertySizeRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String entityName;

    private final int expectedSize;

    private final int actualSize;

    /**
     * {@link IllegalIdPropertySizeRuntimeException}を作成します。
     * 
     * @param entityName
     *            エンティティ名
     * @param collectSize
     *            IDプロパティの実際の数
     * @param specifiedSize
     *            IDプロパティに指定された数
     */
    public IllegalIdPropertySizeRuntimeException(final String entityName,
            final int collectSize, final int specifiedSize) {
        super("ESSR0756", new Object[] { entityName,
                String.valueOf(collectSize), String.valueOf(specifiedSize) });
        this.entityName = entityName;
        this.expectedSize = collectSize;
        this.actualSize = specifiedSize;
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
     * IDプロパティの実際の数を返します。
     * 
     * @return IDプロパティの実際の数
     */
    public int getExpectedSize() {
        return expectedSize;
    }

    /**
     * IDプロパティに指定された数を返します。
     * 
     * @return IDプロパティに指定された数
     */
    public int getActualSize() {
        return actualSize;
    }

}
