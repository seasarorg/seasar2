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
//package org.seasar.framework.container.impl.servlet;
package org.seasar.framework.container.impl.portlet;

import org.seasar.framework.container.ExternalContextComponentDefRegister;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.container.impl.portlet.PortletContextComponentDef;
import org.seasar.framework.container.impl.portlet.PortletRequestComponentDef;
import org.seasar.framework.container.impl.portlet.PortletResponseComponentDef;
import org.seasar.framework.container.impl.portlet.PortletSessionComponentDef;
import org.seasar.framework.container.impl.servlet.CookieMapComponentDef;
import org.seasar.framework.container.impl.servlet.HttpServletRequestComponentDef;
import org.seasar.framework.container.impl.servlet.HttpServletResponseComponentDef;
import org.seasar.framework.container.impl.servlet.HttpSessionComponentDef;
import org.seasar.framework.container.impl.servlet.HttpSessionMapComponentDef;
import org.seasar.framework.container.impl.servlet.ServletApplicationMapComponentDef;
import org.seasar.framework.container.impl.servlet.ServletContextComponentDef;
import org.seasar.framework.container.impl.servlet.ServletInitParameterMapComponentDef;
import org.seasar.framework.container.impl.servlet.ServletRequestHeaderMapComponentDef;
import org.seasar.framework.container.impl.servlet.ServletRequestHeaderValuesMapComponentDef;
import org.seasar.framework.container.impl.servlet.ServletRequestMapComponentDef;
import org.seasar.framework.container.impl.servlet.ServletRequestParameterMapComponentDef;
import org.seasar.framework.container.impl.servlet.ServletRequestParameterValuesMapComponentDef;

/**
 * @author shinsuke
 * 
 */
public class PortletExtendedExternalContextComponentDefRegister implements
        ExternalContextComponentDefRegister {

    public void registerComponentDefs(S2Container container) {
        S2ContainerImpl impl = (S2ContainerImpl) container;

        // for Servlet
        impl.register0(new HttpServletRequestComponentDef());
        impl.register0(new HttpServletResponseComponentDef());
        impl.register0(new HttpSessionComponentDef());
        impl.register0(new ServletContextComponentDef());
        impl.register0(new ServletApplicationMapComponentDef());
        impl.register0(new ServletInitParameterMapComponentDef());
        impl.register0(new HttpSessionMapComponentDef());
        impl.register0(new ServletRequestMapComponentDef());
        impl.register0(new CookieMapComponentDef());
        impl.register0(new ServletRequestHeaderMapComponentDef());
        impl.register0(new ServletRequestHeaderValuesMapComponentDef());
        impl.register0(new ServletRequestParameterMapComponentDef());
        impl.register0(new ServletRequestParameterValuesMapComponentDef());

        // for Portlet
        impl.register(new PortletRequestComponentDef());
        impl.register(new PortletResponseComponentDef());
        impl.register(new PortletSessionComponentDef());
        impl.register(new PortletContextComponentDef());

    }
}
