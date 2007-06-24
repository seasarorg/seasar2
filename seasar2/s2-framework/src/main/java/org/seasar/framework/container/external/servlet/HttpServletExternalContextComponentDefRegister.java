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

import org.seasar.framework.container.ExternalContextComponentDefRegister;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.external.ApplicationMapComponentDef;
import org.seasar.framework.container.external.InitParameterMapComponentDef;
import org.seasar.framework.container.external.RequestHeaderMapComponentDef;
import org.seasar.framework.container.external.RequestHeaderValuesMapComponentDef;
import org.seasar.framework.container.external.RequestMapComponentDef;
import org.seasar.framework.container.external.RequestParameterMapComponentDef;
import org.seasar.framework.container.external.RequestParameterValuesMapComponentDef;
import org.seasar.framework.container.external.SessionMapComponentDef;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * Servlet用の {@link ExternalContextComponentDefRegister}です。
 * 
 * @author higa
 * 
 */
public class HttpServletExternalContextComponentDefRegister implements
        ExternalContextComponentDefRegister {

    public void registerComponentDefs(S2Container container) {
        S2ContainerImpl impl = (S2ContainerImpl) container;
        impl.register0(new HttpServletRequestComponentDef());
        impl.register0(new HttpServletResponseComponentDef());
        impl.register0(new HttpSessionComponentDef());
        impl.register0(new ServletContextComponentDef());
        impl.register0(new ApplicationMapComponentDef());
        impl.register0(new InitParameterMapComponentDef());
        impl.register0(new SessionMapComponentDef());
        impl.register0(new RequestMapComponentDef());
        impl.register0(new CookieMapComponentDef());
        impl.register0(new RequestHeaderMapComponentDef());
        impl.register0(new RequestHeaderValuesMapComponentDef());
        impl.register0(new RequestParameterMapComponentDef());
        impl.register0(new RequestParameterValuesMapComponentDef());
    }
}
