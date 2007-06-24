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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.ExternalContext;

/**
 * Servlet用の {@link ExternalContext}です。
 * 
 * @author higa
 * 
 */
public class HttpServletExternalContext implements ExternalContext {

    private ThreadLocal requests = new ThreadLocal();

    private ThreadLocal responses = new ThreadLocal();

    private ServletContext application;

    public Object getRequest() {
        return getHttpServletRequest();
    }

    /**
     * {@link HttpServletRequest}を返します。
     * 
     * @return {@link HttpServletRequest}
     */
    protected HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) requests.get();
    }

    public void setRequest(Object request) {
        requests.set(request);
    }

    public Object getResponse() {
        return responses.get();
    }

    public void setResponse(Object response) {
        responses.set(response);
    }

    public Object getSession() {
        return getHttpSession();
    }

    /**
     * {@link HttpSession}を返します。
     * 
     * @return {@link HttpSession}
     */
    protected HttpSession getHttpSession() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return null;
        }
        return request.getSession();
    }

    public Object getApplication() {
        return application;
    }

    public void setApplication(Object application) {
        if (!(application instanceof ServletContext)) {
            throw new IllegalArgumentException("application:" + application);
        }
        this.application = (ServletContext) application;
    }

    public Map getApplicationMap() {
        return new ServletApplicationMap(application);
    }

    public Map getInitParameterMap() {
        return new ServletInitParameterMap(application);
    }

    public Map getRequestCookieMap() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return Collections.EMPTY_MAP;
        }
        return new CookieMap(request);
    }

    public Map getRequestHeaderMap() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return Collections.EMPTY_MAP;
        }
        return new ServletRequestHeaderMap(request);
    }

    public Map getRequestHeaderValuesMap() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return Collections.EMPTY_MAP;
        }
        return new ServletRequestHeaderValuesMap(request);
    }

    public Map getRequestMap() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return new HashMap();
        }
        return new ServletRequestMap(request);
    }

    public Map getRequestParameterMap() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return Collections.EMPTY_MAP;
        }
        return new ServletRequestParameterMap(request);
    }

    public Map getRequestParameterValuesMap() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return Collections.EMPTY_MAP;
        }
        return new ServletRequestParameterValuesMap(request);
    }

    public Map getSessionMap() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            return new HashMap();
        }
        return new HttpSessionMap(request);
    }
}