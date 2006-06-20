package org.seasar.framework.container.impl.portlet;

import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.seasar.framework.container.ExternalContext;
import org.seasar.framework.container.impl.portlet.PortletExternalContext;
import org.seasar.framework.container.impl.servlet.HttpServletExternalContext;

/**
 * @author shinsuke
 * 
 */
public class PortletExtendedExternalContext implements ExternalContext {
    private HttpServletExternalContext servletExternalContext;

    private PortletExternalContext portletExternalContext;

    private ThreadLocal isPortlet = new ThreadLocal();

    public PortletExtendedExternalContext() {
        servletExternalContext = new HttpServletExternalContext();
        portletExternalContext = new PortletExternalContext();
        setPortlet(false);
    }

    protected void setPortlet(boolean value) {
        if (value) {
            isPortlet.set(Boolean.TRUE);
        } else {
            isPortlet.set(Boolean.FALSE);
        }
    }

    protected boolean isPortlet() {
        Boolean value = (Boolean) isPortlet.get();
        if (value != null && value.equals(Boolean.TRUE)) {
            return true;
        }
        return false;
    }

    public Object getRequest() {
        if (isPortlet()) {
            return portletExternalContext.getRequest();
        } else {
            return servletExternalContext.getRequest();
        }
    }

    public void setRequest(Object request) {
        if (request == null) {
            portletExternalContext.setRequest(null);
            servletExternalContext.setRequest(null);
            setPortlet(false);
        } else if (request instanceof PortletRequest) {
            portletExternalContext.setRequest(request);
            setPortlet(true);
        } else {
            servletExternalContext.setRequest(request);
            setPortlet(false);
        }
    }

    public Object getResponse() {
        if (isPortlet()) {
            return portletExternalContext.getResponse();
        } else {
            return servletExternalContext.getResponse();
        }
    }

    public void setResponse(Object response) {
        if (response == null) {
            portletExternalContext.setResponse(null);
            servletExternalContext.setResponse(null);
            setPortlet(false);
        } else if (response instanceof PortletResponse) {
            portletExternalContext.setResponse(response);
            setPortlet(true);
        } else {
            servletExternalContext.setResponse(response);
            setPortlet(false);
        }
    }

    public Object getSession() {
        if (isPortlet()) {
            return portletExternalContext.getSession();
        } else {
            return servletExternalContext.getSession();
        }
    }

    public Object getApplication() {
        if (isPortlet()) {
            return portletExternalContext.getApplication();
        } else {
            return servletExternalContext.getApplication();
        }
    }

    public void setApplication(Object application) {
        if (application == null) {
            portletExternalContext.setApplication(null);
            servletExternalContext.setApplication(null);
            setPortlet(false);
        } else if (application instanceof PortletContext) {
            portletExternalContext.setApplication(application);
            setPortlet(true);
        } else {
            servletExternalContext.setApplication(application);
            setPortlet(false);
        }
    }

    public Map getApplicationMap() {
        if (isPortlet()) {
            return portletExternalContext.getApplicationMap();
        } else {
            return servletExternalContext.getApplicationMap();
        }
    }

    public Map getInitParameterMap() {
        if (isPortlet()) {
            return portletExternalContext.getInitParameterMap();
        } else {
            return servletExternalContext.getInitParameterMap();
        }
    }

    public Map getSessionMap() {
        if (isPortlet()) {
            return portletExternalContext.getSessionMap();
        } else {
            return servletExternalContext.getSessionMap();
        }
    }

    public Map getRequestCookieMap() {
        if (isPortlet()) {
            return portletExternalContext.getRequestCookieMap();
        } else {
            return servletExternalContext.getRequestCookieMap();
        }
    }

    public Map getRequestHeaderMap() {
        if (isPortlet()) {
            return portletExternalContext.getRequestHeaderMap();
        } else {
            return servletExternalContext.getRequestHeaderMap();
        }
    }

    public Map getRequestHeaderValuesMap() {
        if (isPortlet()) {
            return portletExternalContext.getRequestHeaderValuesMap();
        } else {
            return servletExternalContext.getRequestHeaderValuesMap();
        }
    }

    public Map getRequestMap() {
        if (isPortlet()) {
            return portletExternalContext.getRequestMap();
        } else {
            return servletExternalContext.getRequestMap();
        }
    }

    public Map getRequestParameterMap() {
        if (isPortlet()) {
            return portletExternalContext.getRequestParameterMap();
        } else {
            return servletExternalContext.getRequestParameterMap();
        }
    }

    public Map getRequestParameterValuesMap() {
        if (isPortlet()) {
            return portletExternalContext.getRequestParameterValuesMap();
        } else {
            return servletExternalContext.getRequestParameterValuesMap();
        }
    }

}
