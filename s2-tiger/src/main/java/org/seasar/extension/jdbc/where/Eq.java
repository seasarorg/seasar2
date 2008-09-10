/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.where;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.name.PropertyName;

/**
 * EQUALをあらわすクラスです。
 * 
 * @author higa
 * 
 */
public class Eq implements Where {

    /**
     * プロパティ名です。
     */
    protected String name;

    /**
     * パラメータです。
     */
    protected Object param;

    /**
     * コンストラクタです。
     * 
     * @param name
     *            プロパティ名
     * @param param
     *            パラメータ
     */
    public Eq(String name, Object param) {
        this.name = name;
        this.param = param;
    }

    /**
     * コンストラクタです。
     * 
     * @param name
     *            プロパティ名
     * @param param
     *            パラメータ
     */
    public Eq(PropertyName name, Object param) {
        this(name.toPropertyName(), param);
    }

    public String getCriteria() {
        return name + " = ?";
    }

    public Object[] getParams() {
        return new Object[] { param };
    }

    public String[] getPropertyNames() {
        return new String[] { name };
    }

}
