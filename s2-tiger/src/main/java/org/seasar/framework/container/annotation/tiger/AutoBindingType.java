/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.annotation.tiger;

/**
 * 自動バインディングの列挙型です。
 * 
 * @author higa
 */
public enum AutoBindingType {

    /**
     * コンストラクタ・プロパティともに自動バインディングを行います。
     */
    AUTO,

    /**
     * コンストラクタのみ自動バインディングを行います。
     */
    CONSTRUCTOR,

    /**
     * プロパティのみ自動バインディングを行います。
     */
    PROPERTY,

    /**
     * 自動バインディグを行いません。
     */
    NONE;

    /**
     * 自動バインディングタイプの名前を返します。
     * 
     * @return 自動バインディングタイプの名前
     */
    public String getName() {
        return toString().toLowerCase();
    }

}
