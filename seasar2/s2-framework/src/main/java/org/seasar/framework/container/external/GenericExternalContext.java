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
package org.seasar.framework.container.external;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.util.MapUtil;

/**
 * Genericな {@link ExternalContext}です。
 * 
 * @author koichik
 */
public class GenericExternalContext implements ExternalContext {

    /**
     * 空の {@link Map}です。
     */
    protected static final Map EMPTY_MAP = Collections
            .unmodifiableMap(new HashMap());

    /**
     * applicationスコープ用の {@link Map}です。
     */
    protected Map application = MapUtil.createHashMap();

    /**
     * requestを管理するための {@link ThreadLocal}です。
     */
    protected ThreadLocal requests = new ThreadLocal();

    public Object getApplication() {
        return application;
    }

    public Map getApplicationMap() {
        return application;
    }

    public Map getInitParameterMap() {
        return EMPTY_MAP;
    }

    public Object getRequest() {
        return requests.get();
    }

    public Map getRequestCookieMap() {
        return EMPTY_MAP;
    }

    public Map getRequestHeaderMap() {
        return EMPTY_MAP;
    }

    public Map getRequestHeaderValuesMap() {
        return EMPTY_MAP;
    }

    public Map getRequestMap() {
        return (Map) requests.get();
    }

    public Map getRequestParameterMap() {
        return EMPTY_MAP;
    }

    public Map getRequestParameterValuesMap() {
        return EMPTY_MAP;
    }

    public Object getResponse() {
        return null;
    }

    public Object getSession() {
        return null;
    }

    public Map getSessionMap() {
        return EMPTY_MAP;
    }

    public void setApplication(final Object application) {
    }

    public void setRequest(final Object request) {
        requests.set(request);
    }

    public void setResponse(final Object response) {
    }

}
