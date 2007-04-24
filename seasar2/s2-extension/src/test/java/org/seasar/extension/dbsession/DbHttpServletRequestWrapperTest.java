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
package org.seasar.extension.dbsession;

import javax.servlet.http.HttpSession;

import org.seasar.framework.mock.servlet.MockHttpServletRequestImpl;
import org.seasar.framework.mock.servlet.MockServletContextImpl;
import org.seasar.framework.unit.S2FrameworkTestCase;

/**
 * @author higa
 * 
 */
public class DbHttpServletRequestWrapperTest extends S2FrameworkTestCase {

    /**
     * @throws Exception
     */
    public void testGetSession() throws Exception {
        MockServletContextImpl servletContext = new MockServletContextImpl(
                "hoge");
        MockHttpServletRequestImpl request = new MockHttpServletRequestImpl(
                servletContext, "foo");
        DbSessionStateManager sessionStateManager = new DbSessionStateManager() {
            public DbSessionState loadState(String sessionId) {
                return null;
            }

            public void updateState(String sessionId,
                    DbSessionState sessionState) {
            }

            public void removeState(String sessionId) {
            }
        };
        DbHttpServletRequestWrapper requestWrapper = new DbHttpServletRequestWrapper(
                request, sessionStateManager);
        HttpSession session = requestWrapper.getSession();
        assertNotNull(session);
        assertTrue(session instanceof DbHttpSession);
        assertSame(session, requestWrapper.getSession());
        System.out.println(session.getId());
        assertNotNull(session.getId());
    }
}
