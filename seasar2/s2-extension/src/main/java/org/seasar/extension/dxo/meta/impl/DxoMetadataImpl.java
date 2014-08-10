/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo.meta.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.seasar.extension.dxo.builder.DxoCommandBuilder;
import org.seasar.extension.dxo.command.DxoCommand;
import org.seasar.extension.dxo.meta.DxoMetadata;
import org.seasar.framework.util.MethodUtil;

/**
 * Dxoのメタデータを管理するクラスです。
 * 
 * @author koichik
 */
public class DxoMetadataImpl implements DxoMetadata {

    /** Dxoのメソッドに対応した{@link DxoCommand}を保持するマップです。 */
    protected Map commands = new HashMap();

    /**
     * インスタンスを構築します。
     * 
     * @param dxoClass
     *            Dxoの型
     * @param builders
     *            コマンドを構築するビルダの配列
     */
    public DxoMetadataImpl(final Class dxoClass,
            final DxoCommandBuilder[] builders) {
        final Method[] methods = dxoClass.getMethods();
        for (int i = 0; i < methods.length; ++i) {
            final Method method = methods[i];
            if (MethodUtil.isBridgeMethod(method)
                    || MethodUtil.isSyntheticMethod(method)) {
                continue;
            }

            final int modifier = method.getModifiers();
            if (!Modifier.isPublic(modifier) || !Modifier.isAbstract(modifier)) {
                continue;
            }

            for (int j = 0; j < builders.length; ++j) {
                final DxoCommand command = builders[j].createDxoCommand(
                        dxoClass, method);
                if (command != null) {
                    commands.put(method, command);
                    break;
                }
            }
        }
    }

    public DxoCommand getCommand(final Method method) {
        return (DxoCommand) commands.get(method);
    }

}
