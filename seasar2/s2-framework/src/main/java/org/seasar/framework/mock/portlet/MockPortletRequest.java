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
package org.seasar.framework.mock.portlet;

import java.util.Locale;

import javax.portlet.PortletRequest;

/**
 * @author shinsuke
 * 
 */
public interface MockPortletRequest extends PortletRequest {
    public void addProperty(String name, Object value);

    public void setAuthType(String authType);

    public void addParameter(String name, String value);

    public void addParameter(String name, String[] values);

    public void setParameter(String name, String value);

    public void setParameter(String name, String[] values);

    public void addResponseContentType(String contentType);

    public void removeResponseContentType(String contentType);

    public void addLocale(Locale locale);

    public void removeLocale(Locale locale);

    public void setScheme(String scheme);

    public void setServerName(String serverName);

    public void setServerPort(int serverPort);
}
