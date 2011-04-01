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

    /** 遅延ロードするためのマーク */
    static final Map LAZY_MARK = new HashMap();

    ThreadLocal requests = new ThreadLocal();

    ThreadLocal responses = new ThreadLocal();

    ThreadLocal requestMaps = new ThreadLocal();

    ThreadLocal requestHeaderMaps = new ThreadLocal();

    ThreadLocal requestHeaderValuesMaps = new ThreadLocal();

    ThreadLocal requestParameterMaps = new ThreadLocal();

    ThreadLocal requestParameterValuesMaps = new ThreadLocal();

    ThreadLocal requestCookieMaps = new ThreadLocal();

    ThreadLocal sessionMaps = new ThreadLocal();

    ServletContext application;

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
        if (request == null) {
            requestMaps.set(new HashMap());
            requestHeaderMaps.set(Collections.EMPTY_MAP);
            requestHeaderValuesMaps.set(Collections.EMPTY_MAP);
            requestCookieMaps.set(Collections.EMPTY_MAP);
            requestParameterMaps.set(Collections.EMPTY_MAP);
            requestParameterValuesMaps.set(Collections.EMPTY_MAP);
            sessionMaps.set(new HashMap());
        } else {
            final HttpServletRequest req = (HttpServletRequest) request;
            requestMaps.set(new ServletRequestMap(req));
            requestHeaderMaps.set(new ServletRequestHeaderMap(req));
            requestHeaderValuesMaps.set(new ServletRequestHeaderValuesMap(req));
            requestCookieMaps.set(new CookieMap(req));
            requestParameterMaps.set(LAZY_MARK); // lazy initialize
            requestParameterValuesMaps.set(LAZY_MARK); // lazy initialize
            sessionMaps.set(LAZY_MARK); // lazy initialize
        }
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
        Map requestCookieMap = (Map) requestCookieMaps.get();
        if (requestCookieMap == null) {
            requestCookieMap = Collections.EMPTY_MAP;
            requestCookieMaps.set(requestCookieMap);
        }
        return requestCookieMap;
    }

    public Map getRequestHeaderMap() {
        Map requestHeaderMap = (Map) requestHeaderMaps.get();
        if (requestHeaderMap == null) {
            requestHeaderMap = Collections.EMPTY_MAP;
            requestCookieMaps.set(requestHeaderMap);
        }
        return requestHeaderMap;
    }

    public Map getRequestHeaderValuesMap() {
        Map requestHeaderMap = (Map) requestHeaderValuesMaps.get();
        if (requestHeaderMap == null) {
            requestHeaderMap = Collections.EMPTY_MAP;
            requestHeaderMaps.set(requestHeaderMap);
        }
        return requestHeaderMap;
    }

    public Map getRequestMap() {
        Map requestMap = (Map) requestMaps.get();
        if (requestMap == null) {
            requestMap = new HashMap();
            requestMaps.set(requestMap);
        }
        return requestMap;
    }

    public Map getRequestParameterMap() {
        Map requestParameterMap = (Map) requestParameterMaps.get();
        if (requestParameterMap == null) {
            requestParameterMap = Collections.EMPTY_MAP;
            requestParameterMaps.set(requestParameterMap);
        } else if (requestParameterMap == LAZY_MARK) {
            requestParameterMap = new ServletRequestParameterMap(
                    getHttpServletRequest());
            requestParameterMaps.set(requestParameterMap);
        }
        return requestParameterMap;
    }

    public Map getRequestParameterValuesMap() {
        Map requestParameterValuesMap = (Map) requestParameterValuesMaps.get();
        if (requestParameterValuesMap == null) {
            requestParameterValuesMap = Collections.EMPTY_MAP;
            requestParameterValuesMaps.set(requestParameterValuesMap);
        } else if (requestParameterValuesMap == LAZY_MARK) {
            requestParameterValuesMap = new ServletRequestParameterValuesMap(
                    getHttpServletRequest());
            requestParameterValuesMaps.set(requestParameterValuesMap);
        }
        return requestParameterValuesMap;
    }

    public Map getSessionMap() {
        Map sessionMap = (Map) sessionMaps.get();
        if (sessionMap == null) {
            sessionMap = new HashMap();
            sessionMaps.set(sessionMap);
        } else if (sessionMap == LAZY_MARK) {
            sessionMap = new HttpSessionMap(getHttpServletRequest());
            sessionMaps.set(sessionMap);
        }
        return sessionMap;
    }
}