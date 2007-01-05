/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.container;

import org.seasar.framework.exception.SRuntimeException;

/**
 * <p>
 * 不正なアクセスタイプ定義が指定された場合にスローされます。
 * </p>
 * <p>
 * 有効な{@link AccessTypeDef アクセスタイプ定義}としては、
 * {@link org.seasar.framework.container.assembler.AccessTypePropertyDef PROPERTY}と
 * {@link org.seasar.framework.container.assembler.AccessTypeFieldDef FIELD}があります。
 * </p>
 * 
 * @author koichik
 * @author belltree
 * 
 * @see org.seasar.framework.container.assembler.AccessTypeDefFactory#getAccessTypeDef(String)
 */
public class IllegalAccessTypeDefRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1L;

    private String accessTypeName;

    /**
     * 不正なアクセスタイプ定義名を指定して、 <code>IllegalAccessTypeDefRuntimeException</code>を構築します。
     * 
     * @param accessTypeName
     *            不正なアクセスタイプ定義名
     */
    public IllegalAccessTypeDefRuntimeException(final String accessTypeName) {
        super("ESSR0083", new Object[] { accessTypeName });
        this.accessTypeName = accessTypeName;
    }

    /**
     * 不正なアクセスタイプ定義名を返します。
     * 
     * @return 不正なアクセスタイプ定義名
     * 
     * @see AccessTypeDef#PROPERTY_NAME
     * @see AccessTypeDef#FIELD_NAME
     */
    public String getAccessTypeName() {
        return accessTypeName;
    }
}
