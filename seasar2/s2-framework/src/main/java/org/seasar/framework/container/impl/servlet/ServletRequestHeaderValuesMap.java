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
package org.seasar.framework.container.impl.servlet;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.impl.AbstractUnmodifiableExternalContextMap;

/**
 * @author Shinpei Ohtani
 * @author higa
 */
public class ServletRequestHeaderValuesMap extends
        AbstractUnmodifiableExternalContextMap {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private final HttpServletRequest request;

    public ServletRequestHeaderValuesMap(final HttpServletRequest request) {
        this.request = request;
    }

    protected Object getAttribute(String key) {
        Enumeration e = request.getHeaders(key);
        return toStringArray(e);
    }

    protected Enumeration getAttributeNames() {
        return request.getHeaderNames();
    }

    private String[] toStringArray(Enumeration e) {
        if (e == null) {
            return EMPTY_STRING_ARRAY;
        }
        List list = new ArrayList();
        while (e.hasMoreElements()) {
            list.add(e.nextElement());
        }
        return (String[]) list.toArray(new String[list.size()]);
    }
}
