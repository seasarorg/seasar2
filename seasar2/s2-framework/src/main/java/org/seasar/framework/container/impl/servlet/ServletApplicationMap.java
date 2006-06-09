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
package org.seasar.framework.container.impl.servlet;

import java.util.Enumeration;

import javax.servlet.ServletContext;

import org.seasar.framework.container.impl.AbstractExternalContextMap;
import org.seasar.framework.util.AssertionUtil;

/**
 * @author Shinpei Ohtani
 * @author higa
 */
public class ServletApplicationMap extends AbstractExternalContextMap {

    private ServletContext context;

    public ServletApplicationMap(ServletContext context) {
        AssertionUtil.assertNotNull("context is null.", context);
        this.context = context;
    }

    protected Object getAttribute(String key) {
        return context.getAttribute(key);
    }

    protected Enumeration getAttributeNames() {
        return context.getAttributeNames();
    }

    protected void removeAttribute(String key) {
        context.removeAttribute(key);
    }

    protected void setAttribute(String key, Object value) {
        context.setAttribute(key, value);
    }
}