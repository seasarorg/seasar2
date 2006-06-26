package org.seasar.framework.container.impl.portlet;

import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.seasar.framework.container.impl.servlet.HttpServletExternalContext;

/**
 * @author shinsuke
 * 
 */
public class PortletExtendedExternalContext extends PortletExternalContext {
    private HttpServletExternalContext servletExternalContext;

    private ThreadLocal isPortlet = new ThreadLocal();

    public PortletExtendedExternalContext() {
        super();
        servletExternalContext = new HttpServletExternalContext();
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
