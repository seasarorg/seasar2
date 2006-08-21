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
package org.seasar.extension.dxo.builder.impl;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.seasar.extension.dxo.command.DxoCommand;
import org.seasar.extension.dxo.command.impl.MapConversionDxoCommand;

/**
 * @author koichik
 * 
 */
public class MapConversionDxoCommandBuilder extends AbstractDxoCommandBuilder {

    protected Class[] ACCEPTABLE_DEST_CLASSES = new Class[] { Map.class,
            Map[].class, List.class };

    public DxoCommand createDxoCommand(final Class dxoClass, final Method method) {
        final String expression = getAnnotationReader().getMapConversion(
                dxoClass, method);
        if (expression == null) {
            return null;
        }
        return new MapConversionDxoCommand(expression, method);
    }

}
