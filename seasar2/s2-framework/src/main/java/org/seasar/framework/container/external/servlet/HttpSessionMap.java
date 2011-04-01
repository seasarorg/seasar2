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
package org.seasar.framework.container.external.servlet;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.external.AbstractExternalContextMap;
import org.seasar.framework.util.AssertionUtil;
import org.seasar.framework.util.EmptyIterator;
import org.seasar.framework.util.EnumerationIterator;

/**
 * {@link HttpSession}を {@link Map}としてアクセスするためのクラスです。
 * 
 * @author shot
 * @author higa
 */
public class HttpSessionMap extends AbstractExternalContextMap {

    private static final Iterator EMPTY_ITERATOR = new EmptyIterator();

    private HttpServletRequest request;

    /**
     * {@link HttpSessionMap}を作成します。
     * 
     * @param request
     */
    public HttpSessionMap(HttpServletRequest request) {
        AssertionUtil.assertNotNull("request is null.", request);
        this.request = request;
    }

    protected Object getAttribute(String key) {
        HttpSession session = getSession();
        return (session != null) ? session.getAttribute(key) : null;
    }

    protected void setAttribute(String key, Object value) {
        request.getSession(true).setAttribute(key, value);
    }

    protected Iterator getAttributeNames() {
        HttpSession session = getSession();
        return (session != null) ? new EnumerationIterator(session
                .getAttributeNames()) : EMPTY_ITERATOR;
    }

    protected void removeAttribute(String key) {
        HttpSession session = getSession();
        if (session != null) {
            session.removeAttribute(key);
        }
    }

    private HttpSession getSession() {
        return request.getSession(false);
    }

}
