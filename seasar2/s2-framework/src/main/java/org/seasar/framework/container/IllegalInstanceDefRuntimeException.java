/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
 * 不正なコンポーネントインスタンス定義が指定された場合にスローされます。
 * 
 * @author higa
 * @author jundu (Javadoc)
 * 
 * @see InstanceDef
 * @see org.seasar.framework.container.deployer.InstanceDefFactory
 */
public class IllegalInstanceDefRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = -3505265879782048528L;

    private String instanceName;

    /**
     * <code>IllegalInstanceDefRuntimeException</code>を構築します。
     * 
     * @param instanceName
     *            指定された不正なコンポーネントインスタンス定義名
     */
    public IllegalInstanceDefRuntimeException(String instanceName) {
        super("ESSR0078", new Object[] { instanceName });
        this.instanceName = instanceName;
    }

    /**
     * 例外の原因となったコンポーネントインスタンス定義名を返します。
     * 
     * @return コンポーネントインスタンス定義名
     */
    public String getInstanceName() {
        return instanceName;
    }
}