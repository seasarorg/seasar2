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

import org.seasar.framework.container.ExternalContextComponentDefRegister;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * This class is ExternalContextDefRegister implementation for Portlet
 * environment.
 * 
 * @author <a href="mailto:shinsuke@yahoo.co.jp">Shinsuke Sugaya</a>
 * 
 */
public class PortletExternalContextComponentDefRegister implements
        ExternalContextComponentDefRegister {

    public void registerComponentDefs(S2Container container) {
        S2ContainerImpl impl = (S2ContainerImpl) container;
        impl.register0(new PortletRequestComponentDef());
        impl.register0(new PortletResponseComponentDef());
        impl.register0(new PortletSessionComponentDef());
        impl.register0(new PortletContextComponentDef());
    }
}
