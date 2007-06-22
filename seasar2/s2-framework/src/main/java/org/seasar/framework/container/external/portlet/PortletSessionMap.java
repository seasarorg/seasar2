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
package org.seasar.framework.container.external.portlet;

import java.util.Iterator;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.seasar.framework.container.external.AbstractExternalContextMap;
import org.seasar.framework.util.AssertionUtil;
import org.seasar.framework.util.EmptyIterator;
import org.seasar.framework.util.EnumerationIterator;

/**
 * Portlet用のSessionMapです。
 * 
 * @author <a href="mailto:shinsuke@yahoo.co.jp">Shinsuke Sugaya</a>
 */
public class PortletSessionMap extends AbstractExternalContextMap {

    private static final Iterator EMPTY_ITERATOR = new EmptyIterator();

    private PortletRequest request;

    /**
     * {@link PortletSessionMap}を作成します。
     * 
     * @param request
     */
    public PortletSessionMap(PortletRequest request) {
        AssertionUtil.assertNotNull("request is null.", request);
        this.request = request;
    }

    protected Object getAttribute(String key) {
        PortletSession session = getSession();
        return (session != null) ? session.getAttribute(key) : null;
    }

    protected void setAttribute(String key, Object value) {
        request.getPortletSession(true).setAttribute(key, value);
    }

    protected Iterator getAttributeNames() {
        PortletSession session = getSession();
        return (session != null) ? new EnumerationIterator(session
                .getAttributeNames()) : EMPTY_ITERATOR;
    }

    protected void removeAttribute(String key) {
        PortletSession session = getSession();
        if (session != null) {
            session.removeAttribute(key);
        }
    }

    private PortletSession getSession() {
        return request.getPortletSession(false);
    }

}
