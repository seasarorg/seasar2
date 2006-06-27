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
package org.seasar.framework.container.external.portlet;

import java.util.Iterator;

import javax.portlet.PortletRequest;

import org.seasar.framework.container.external.AbstractUnmodifiableExternalContextMap;
import org.seasar.framework.util.EnumerationIterator;

/**
 * @author <a href="mailto:shinsuke@yahoo.co.jp">Shinsuke Sugaya</a>
 */
public class PortletRequestParameterMap extends
        AbstractUnmodifiableExternalContextMap {

    private final PortletRequest request;

    public PortletRequestParameterMap(final PortletRequest request) {
        this.request = request;
    }

    protected Object getAttribute(String key) {
        return request.getParameter(key);
    }

    protected Iterator getAttributeNames() {
        return new EnumerationIterator(request.getParameterNames());
    }

}
