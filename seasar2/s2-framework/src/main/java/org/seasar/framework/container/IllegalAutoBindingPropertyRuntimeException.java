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
 * 自動バインディングの対象となるコンポーネントが見つからなかった場合にスローされます。
 * 
 * <p>
 * この例外がスローされるのは、 {@link BindingTypeDef バインディングタイプ定義}が<code>must</code>で自動バインディングの対象が見つからなかった時です。
 * </p>
 * 
 * @author higa
 * @author jundu
 * 
 * @see AutoBindingDef
 * @see BindingTypeDef
 */
public class IllegalAutoBindingPropertyRuntimeException extends
        SRuntimeException {

    private static final long serialVersionUID = -8464366695114050369L;

    private Class componentClass;

    private String propertyName;

    /**
     * <code>IllegalAutoBindingPropertyRuntimeException</code>を構築します。
     * 
     * @param componentClass
     *            自動バインディングに失敗したコンポーネント
     * @param propertyName
     *            自動バインディング対象が見つからなかったプロパティまたはフィールドの名称
     */
    public IllegalAutoBindingPropertyRuntimeException(Class componentClass,
            String propertyName) {
        super("ESSR0080",
                new Object[] { componentClass.getName(), propertyName });
        this.componentClass = componentClass;
        this.propertyName = propertyName;
    }

    /**
     * 自動バインディングに失敗したコンポーネントのクラスを返します。
     * 
     * @return 自動バインディングに失敗したコンポーネントのクラス
     */
    public Class getComponentClass() {
        return componentClass;
    }

    /**
     * 自動バインディング対象が見つからなかったプロパティまたはフィールドの名称を返します。
     * 
     * @return 自動バインディングに失敗したプロパティまたはフィールドの名称
     */
    public String getPropertyName() {
        return propertyName;
    }
}