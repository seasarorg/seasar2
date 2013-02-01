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
package org.seasar.framework.container.external.portlet;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.portlet.PortletRequest;

import org.seasar.framework.container.external.AbstractUnmodifiableExternalContextMap;

/**
 * Portlet用のRequestHeaderValuesMapです。
 * 
 * @author <a href="mailto:shinsuke@yahoo.co.jp">Shinsuke Sugaya</a>
 */
public class PortletRequestHeaderValuesMap extends
        AbstractUnmodifiableExternalContextMap {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private final PortletRequest request;

    private final Set propertyNames = new HashSet();

    /**
     * {@link PortletRequestHeaderValuesMap}を作成します。
     * 
     * @param request
     */
    public PortletRequestHeaderValuesMap(final PortletRequest request) {
        this.request = request;
        for (final Enumeration names = request.getPropertyNames(); names
                .hasMoreElements();) {
            propertyNames.add(names.nextElement());
        }
    }

    protected Object getAttribute(String key) {
        if (propertyNames.contains(key)) {
            return toStringArray(request.getProperties(key));
        }
        return null;
    }

    protected Iterator getAttributeNames() {
        return propertyNames.iterator();
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
