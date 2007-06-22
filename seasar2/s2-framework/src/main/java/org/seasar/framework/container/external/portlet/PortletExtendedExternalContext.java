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

import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.seasar.framework.container.external.servlet.HttpServletExternalContext;

/**
 * 拡張された {@link PortletExternalContext}です。
 * 
 * @author shinsuke
 * 
 */
public class PortletExtendedExternalContext extends PortletExternalContext {
    private HttpServletExternalContext servletExternalContext;

    private ThreadLocal isPortlet = new ThreadLocal();

    /**
     * {@link PortletExtendedExternalContext}を作成します。
     */
    public PortletExtendedExternalContext() {
        super();
        servletExternalContext = new HttpServletExternalContext();
        setPortlet(false);
    }

    /**
     * Portletかどうか設定します。
     * 
     * @param value
     */
    protected void setPortlet(boolean value) {
        if (value) {
            isPortlet.set(Boolean.TRUE);
        } else {
            isPortlet.set(Boolean.FALSE);
        }
    }

    /**
     * Portletかどうか返します。
     * 
     * @return Portletかどうか
     */
    protected boolean isPortlet() {
        Boolean value = (Boolean) isPortlet.get();
        if (value != null && value.equals(Boolean.TRUE)) {
            return true;
        }
        return false;
    }

    public Object getRequest() {
        if (isPortlet()) {
            return super.getRequest();
        } else {
            return servletExternalContext.getRequest();
        }
    }

    public void setRequest(Object request) {
        if (request == null) {
            super.setRequest(null);
            servletExternalContext.setRequest(null);
            setPortlet(false);
        } else if (request instanceof PortletRequest) {
            super.setRequest(request);
            setPortlet(true);
        } else {
            servletExternalContext.setRequest(request);
            setPortlet(false);
        }
    }

    public Object getResponse() {
        if (isPortlet()) {
            return super.getResponse();
        } else {
            return servletExternalContext.getResponse();
        }
    }

    public void setResponse(Object response) {
        if (response == null) {
            super.setResponse(null);
            servletExternalContext.setResponse(null);
            setPortlet(false);
        } else if (response instanceof PortletResponse) {
            super.setResponse(response);
            setPortlet(true);
        } else {
            servletExternalContext.setResponse(response);
            setPortlet(false);
        }
    }

    public Object getSession() {
        if (isPortlet()) {
            return super.getSession();
        } else {
            return servletExternalContext.getSession();
        }
    }

    public Object getApplication() {
        if (isPortlet()) {
            return super.getApplication();
        } else {
            return servletExternalContext.getApplication();
        }
    }

    public void setApplication(Object application) {
        if (application == null) {
            super.setApplication(null);
            servletExternalContext.setApplication(null);
            setPortlet(false);
        } else if (application instanceof PortletContext) {
            super.setApplication(application);
            setPortlet(true);
        } else {
            servletExternalContext.setApplication(application);
            setPortlet(false);
        }
    }

    public Map getApplicationMap() {
        if (isPortlet()) {
            return super.getApplicationMap();
        } else {
            return servletExternalContext.getApplicationMap();
        }
    }

    public Map getInitParameterMap() {
        if (isPortlet()) {
            return super.getInitParameterMap();
        } else {
            return servletExternalContext.getInitParameterMap();
        }
    }

    public Map getSessionMap() {
        if (isPortlet()) {
            return super.getSessionMap();
        } else {
            return servletExternalContext.getSessionMap();
        }
    }

    public Map getRequestCookieMap() {
        if (isPortlet()) {
            return super.getRequestCookieMap();
        } else {
            return servletExternalContext.getRequestCookieMap();
        }
    }

    public Map getRequestHeaderMap() {
        if (isPortlet()) {
            return super.getRequestHeaderMap();
        } else {
            return servletExternalContext.getRequestHeaderMap();
        }
    }

    public Map getRequestHeaderValuesMap() {
        if (isPortlet()) {
            return super.getRequestHeaderValuesMap();
        } else {
            return servletExternalContext.getRequestHeaderValuesMap();
        }
    }

    public Map getRequestMap() {
        if (isPortlet()) {
            return super.getRequestMap();
        } else {
            return servletExternalContext.getRequestMap();
        }
    }

    public Map getRequestParameterMap() {
        if (isPortlet()) {
            return super.getRequestParameterMap();
        } else {
            return servletExternalContext.getRequestParameterMap();
        }
    }

    public Map getRequestParameterValuesMap() {
        if (isPortlet()) {
            return super.getRequestParameterValuesMap();
        } else {
            return servletExternalContext.getRequestParameterValuesMap();
        }
    }

}
