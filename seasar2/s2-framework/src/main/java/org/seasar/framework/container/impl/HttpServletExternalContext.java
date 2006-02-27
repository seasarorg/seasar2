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
package org.seasar.framework.container.impl;

import javax.servlet.http.HttpServletRequest;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * @author higa
 *
 */
public class HttpServletExternalContext implements ExternalContext {

    private ThreadLocal requests = new ThreadLocal();
    private ThreadLocal responses = new ThreadLocal();
    private Object application;
    
    public Object getRequest() {
        return requests.get();
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
        HttpServletRequest request = (HttpServletRequest) getRequest();
        if (request == null) {
            throw new EmptyRuntimeException("request");
        }
        return request.getSession();
    }

    public Object getApplication() {
        return application;
    }

    public void setApplication(Object application) {
        this.application = application;
    }
}