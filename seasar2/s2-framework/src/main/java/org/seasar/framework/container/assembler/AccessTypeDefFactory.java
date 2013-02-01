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

import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.IllegalAccessTypeDefRuntimeException;

/**
 * アクセスタイプ定義のファクトリです。
 * 
 * @author higa
 * 
 */
public class AccessTypeDefFactory {

    /**
     * アクセスタイプ定義がPROPERTYのフィールドです。
     */
    public static final AccessTypeDef PROPERTY = new AccessTypePropertyDef();

    /**
     * アクセスタイプ定義がFIELDのフィールドです。
     */
    public static final AccessTypeDef FIELD = new AccessTypeFieldDef();

    /**
     * インスタンスを構築します。
     */
    protected AccessTypeDefFactory() {
    }

    private static Map accessTypeDefs = new HashMap();

    static {
        addAccessTypeDef(PROPERTY);
        addAccessTypeDef(FIELD);
    }

    /**
     * アクセスタイプ定義を追加します。
     * 
     * @param accessTypeDef
     */
    public static void addAccessTypeDef(final AccessTypeDef accessTypeDef) {
        accessTypeDefs.put(accessTypeDef.getName(), accessTypeDef);
    }

    /**
     * アクセスタイプ定義が存在するかどうかを返します。
     * 
     * @param name
     * @return
     */
    public static boolean existAccessTypeDef(String name) {
        return accessTypeDefs.containsKey(name);
    }

    /**
     * アクセスタイプ定義を返します。
     * 
     * @param name
     * @return
     */
    public static AccessTypeDef getAccessTypeDef(String name) {
        if (!existAccessTypeDef(name)) {
            throw new IllegalAccessTypeDefRuntimeException(name);
        }
        return (AccessTypeDef) accessTypeDefs.get(name);
    }
}
