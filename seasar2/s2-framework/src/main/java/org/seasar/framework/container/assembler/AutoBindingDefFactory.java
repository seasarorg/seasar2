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
package org.seasar.framework.container.assembler;

import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.container.AutoBindingDef;
import org.seasar.framework.container.IllegalAutoBindingDefRuntimeException;

/**
 * 自動バンディング定義のファクトリです。
 * 
 * @author higa
 * 
 */
public class AutoBindingDefFactory {

    /**
     * 自動バインディング定義AUTOのフィールドです。
     */
    public static final AutoBindingDef AUTO = new AutoBindingAutoDef(
            AutoBindingDef.AUTO_NAME);

    /**
     * 自動バインディング定義CONSTRUCTORのフィールドです。
     */
    public static final AutoBindingDef CONSTRUCTOR = new AutoBindingConstructorDef(
            AutoBindingDef.CONSTRUCTOR_NAME);

    /**
     * 自動バインディング定義PROPERTYのフィールドです。
     */
    public static final AutoBindingDef PROPERTY = new AutoBindingPropertyDef(
            AutoBindingDef.PROPERTY_NAME);

    /**
     * 自動バインディング定義NONEのフィールドです。
     */
    public static final AutoBindingDef NONE = new AutoBindingNoneDef(
            AutoBindingDef.NONE_NAME);

    /**
     * 自動バインディング定義SEMIAUTOのフィールドです。
     */
    public static final AutoBindingDef SEMIAUTO = new AutoBindingSemiAutoDef(
            AutoBindingDef.SEMIAUTO_NAME);

    private static Map autoBindingDefs = new HashMap();

    static {
        addAutoBindingDef(AUTO);
        addAutoBindingDef(CONSTRUCTOR);
        addAutoBindingDef(PROPERTY);
        addAutoBindingDef(NONE);
        addAutoBindingDef(SEMIAUTO);
    }

    /**
     * インスタンスを構築します。
     */
    protected AutoBindingDefFactory() {
    }

    /**
     * 自動バインディング定義を追加します。
     * 
     * @param autoBindingDef
     */
    public static void addAutoBindingDef(AutoBindingDef autoBindingDef) {
        autoBindingDefs.put(autoBindingDef.getName(), autoBindingDef);
    }

    /**
     * 自動バインディング定義が存在するかどうかを返します。
     * 
     * @param name
     * @return
     */
    public static boolean existAutoBindingDef(String name) {
        return autoBindingDefs.containsKey(name);
    }

    /**
     * 自動バインディング定義を返します。
     * 
     * @param name
     * @return
     */
    public static AutoBindingDef getAutoBindingDef(String name) {
        if (!autoBindingDefs.containsKey(name)) {
            throw new IllegalAutoBindingDefRuntimeException(name);
        }
        return (AutoBindingDef) autoBindingDefs.get(name);
    }
}
