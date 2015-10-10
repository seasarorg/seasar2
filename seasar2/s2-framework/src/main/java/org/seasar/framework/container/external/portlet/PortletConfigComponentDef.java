/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import javax.portlet.PortletConfig;

import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.impl.SimpleComponentDef;
import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * This class is SimpleComponentDef implementation for PortletConfig.
 * 
 * @author <a href="mailto:shinsuke@yahoo.co.jp">Shinsuke Sugaya</a>
 * 
 */
public class PortletConfigComponentDef extends SimpleComponentDef {

    /**
     * {@link PortletConfigComponentDef}を作成します。
     */
    public PortletConfigComponentDef() {
        super(PortletConfig.class, ContainerConstants.CONFIG_NAME);
    }

    /**
     * @see org.seasar.framework.container.ComponentDef#getComponent()
     */
    public Object getComponent() {
        if (getContainer().getRoot().getExternalContext() instanceof PortletExternalContext) {
            return getContainer().getRoot().getExternalContext().getRequest();
        }
        throw new EmptyRuntimeException("config");
    }
}