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

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
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
    
    protected HttpSession getHttpSession() {
        HttpServletRequest request = getHttpServletRequest();
        if (request == null) {
            throw new EmptyRuntimeException("request");
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
        return new CookieMap(getHttpServletRequest());
    }

    public Map getRequestHeaderMap() {
        return new ServletRequestHeaderMap(getHttpServletRequest());
    }

    public Map getRequestHeaderValuesMap() {
        return new ServletRequestHeaderValuesMap(getHttpServletRequest());
    }

    public Map getRequestMap() {
        return new ServletRequestMap(getHttpServletRequest());
    }

    public Map getRequestParameterMap() {
        return new ServletRequestParameterMap(getHttpServletRequest());
    }

    public Map getRequestParameterValuesMap() {
        return new ServletRequestParameterValuesMap(getHttpServletRequest());
    }

    public Map getSessionMap() {
        return new HttpSessionMap(getHttpServletRequest());
    }
}