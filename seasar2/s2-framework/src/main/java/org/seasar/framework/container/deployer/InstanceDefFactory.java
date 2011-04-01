/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.deployer;

import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.container.IllegalInstanceDefRuntimeException;
import org.seasar.framework.container.InstanceDef;

/**
 * {@link InstanceDef}を作成するためのクラスです。
 * 
 * @author higa
 * 
 */
public class InstanceDefFactory {

    /**
     * singleton用の{@link InstanceDef}。
     */
    public static final InstanceDef SINGLETON = new InstanceSingletonDef(
            InstanceDef.SINGLETON_NAME);

    /**
     * prototype用の{@link InstanceDef}。
     */
    public static final InstanceDef PROTOTYPE = new InstancePrototypeDef(
            InstanceDef.PROTOTYPE_NAME);

    /**
     * application用の{@link InstanceDef}。
     */
    public static final InstanceDef APPLICATION = new InstanceApplicationDef(
            InstanceDef.APPLICATION_NAME);

    /**
     * session用の{@link InstanceDef}。
     */
    public static final InstanceDef SESSION = new InstanceSessionDef(
            InstanceDef.SESSION_NAME);

    /**
     * request用の{@link InstanceDef}。
     */
    public static final InstanceDef REQUEST = new InstanceRequestDef(
            InstanceDef.REQUEST_NAME);

    /**
     * outer用の{@link InstanceDef}。
     */
    public static final InstanceDef OUTER = new InstanceOuterDef(
            InstanceDef.OUTER_NAME);

    private static Map instanceDefs = new HashMap();

    static {
        addInstanceDef(SINGLETON);
        addInstanceDef(PROTOTYPE);
        addInstanceDef(APPLICATION);
        addInstanceDef(SESSION);
        addInstanceDef(REQUEST);
        addInstanceDef(OUTER);
    }

    /**
     * {@link InstanceDefFactory}を作成します。
     */
    protected InstanceDefFactory() {
    }

    /**
     * {@link InstanceDef}を追加します。
     * 
     * @param instanceDef
     */
    public static void addInstanceDef(InstanceDef instanceDef) {
        instanceDefs.put(instanceDef.getName(), instanceDef);
    }

    /**
     * {@link InstanceDef}が存在するかどうかを返します。
     * 
     * @param name
     * @return
     */
    public static boolean existInstanceDef(String name) {
        return instanceDefs.containsKey(name);
    }

    /**
     * nameに応じた{@link InstanceDef}を返します。
     * 
     * @param name
     * @return {@link InstanceDef}
     */
    public static InstanceDef getInstanceDef(String name) {
        if (!instanceDefs.containsKey(name)) {
            throw new IllegalInstanceDefRuntimeException(name);
        }
        return (InstanceDef) instanceDefs.get(name);
    }
}
