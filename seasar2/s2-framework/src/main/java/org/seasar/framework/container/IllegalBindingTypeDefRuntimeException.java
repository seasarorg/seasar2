/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
 * 不正なバインディングタイプ定義が指定された場合にスローされます。
 * 
 * @author higa
 * @author jundu
 * 
 * @see BindingTypeDef
 * @see org.seasar.framework.container.assembler.BindingTypeDefFactory
 */
public class IllegalBindingTypeDefRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 9557906947127294L;

    private String bindingTypeName;

    /**
     * <code>IllegalBindingTypeDefRuntimeException</code>を構築します。
     * 
     * @param bindingTypeName
     *            指定された不正なバインディングタイプ定義名
     */
    public IllegalBindingTypeDefRuntimeException(String bindingTypeName) {
        super("ESSR0079", new Object[] { bindingTypeName });
        this.bindingTypeName = bindingTypeName;
    }

    /**
     * 例外の原因となった不正なバインディングタイプ定義名を返します。
     * 
     * @return バインディングタイプ定義名
     */
    public String getBindingTypeName() {
        return bindingTypeName;
    }
}