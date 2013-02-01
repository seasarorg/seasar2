/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.exception;

import org.seasar.framework.exception.SRuntimeException;

/**
 * 永続ユニットの定義が存在しない場合にスローされる例外です。
 * 
 * @author koichik
 */
public class PersistenceUnitNodFoundException extends SRuntimeException {

    private static final long serialVersionUID = -862210393207237212L;

    /**
     * インスタンスを構築します。
     * 
     * @param unitName
     *            存在しない永続ユニットの名称
     */
    public PersistenceUnitNodFoundException(final String unitName) {
        super("ESSR0093", new Object[] { unitName });
    }

}
