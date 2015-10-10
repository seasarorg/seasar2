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

import javax.persistence.OptimisticLockException;

import org.seasar.framework.message.MessageFormatter;

/**
 * 楽観的ロックで競合が発生した場合にスローされる例外です。
 * <p>
 * {@link OptimisticLockException}のSeasar2拡張です。
 * </p>
 * 
 * @author koichik
 */
public class SOptimisticLockException extends OptimisticLockException {

    private static final long serialVersionUID = 1L;

    /**
     * インスタンスを構築します．
     */
    public SOptimisticLockException() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param entity
     *            競合が発生したエンティティ
     */
    public SOptimisticLockException(final Object entity) {
        super(MessageFormatter.getMessage("ESSR0736", new Object[] { entity }),
                null, entity);
    }

}
