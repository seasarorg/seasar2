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
package org.seasar.framework.container.assembler;

import org.seasar.framework.container.AutoBindingDef;

/**
 * 自動バインディング定義の抽象クラスです。
 * 
 * @author higa
 * 
 */
public abstract class AbstractAutoBindingDef implements AutoBindingDef {

    private String name;

    /**
     * {@link AbstractAutoBindingDef}のコンストラクタです。
     * 
     * @param name
     */
    protected AbstractAutoBindingDef(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AutoBindingDef)) {
            return false;
        }
        AutoBindingDef other = (AutoBindingDef) o;
        return name == null ? other.getName() == null : name.equals(other
                .getName());
    }

    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }
}