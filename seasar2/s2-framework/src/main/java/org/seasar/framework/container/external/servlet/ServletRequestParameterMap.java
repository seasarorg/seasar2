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
package org.seasar.framework.container.external.servlet;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.seasar.framework.container.external.AbstractUnmodifiableExternalContextMap;

/**
 * ServletRequestParameterに {@link Map}としてアクセスするためのクラスです。
 * 
 * @author shot
 * @author higa
 */
public class ServletRequestParameterMap extends
        AbstractUnmodifiableExternalContextMap {

    private static final String NULL = "null";

    private final ServletRequest request;

    private final Set parameterNames;

    /**
     * {@link ServletRequestParameterMap}を作成します。
     * 
     * @param request
     */
    public ServletRequestParameterMap(final ServletRequest request) {
        this.request = request;
        parameterNames = request.getParameterMap().keySet();
    }

    protected Object getAttribute(final String key) {
        if (parameterNames.contains(key)) {
            final String value = request.getParameter(key);
            if (NULL.equals(value)) {
                return "";
            }
            return value;
        }
        return null;
    }

    protected Iterator getAttributeNames() {
        return parameterNames.iterator();
    }

}
