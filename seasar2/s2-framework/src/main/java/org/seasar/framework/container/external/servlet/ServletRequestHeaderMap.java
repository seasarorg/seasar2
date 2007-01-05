/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.external.AbstractUnmodifiableExternalContextMap;

/**
 * @author shot
 * @author higa
 */
public class ServletRequestHeaderMap extends
        AbstractUnmodifiableExternalContextMap {

    private final HttpServletRequest request;

    private final Set headerNames = new HashSet();

    public ServletRequestHeaderMap(final HttpServletRequest request) {
        this.request = request;
        for (final Enumeration names = request.getHeaderNames(); names
                .hasMoreElements();) {
            headerNames.add(names.nextElement());
        }
    }

    protected Object getAttribute(final String key) {
        if (headerNames.contains(key)) {
            return request.getHeader(key);
        }
        return null;
    }

    protected Iterator getAttributeNames() {
        return headerNames.iterator();
    }

}
