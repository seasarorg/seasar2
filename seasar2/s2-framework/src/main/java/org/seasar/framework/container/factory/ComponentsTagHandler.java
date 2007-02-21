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
package org.seasar.framework.container.factory;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.xml.TagHandler;
import org.seasar.framework.xml.TagHandlerContext;
import org.xml.sax.Attributes;

/**
 * @author higa
 * 
 */
public class ComponentsTagHandler extends TagHandler {
    private static final long serialVersionUID = 3182865184697069169L;

    protected Class containerImplClass = S2ContainerImpl.class;

    public Class getContainerImplClass() {
        return containerImplClass;
    }

    public void setContainerImplClass(Class containerImplClass) {
        this.containerImplClass = containerImplClass;
    }

    public void start(TagHandlerContext context, Attributes attributes) {
        S2Container container = createContainer();
        String path = (String) context.getParameter("path");
        container.setPath(path);
        String namespace = attributes.getValue("namespace");
        if (!StringUtil.isEmpty(namespace)) {
            container.setNamespace(namespace);
        }
        String initializeOnCreate = attributes.getValue("initializeOnCreate");
        if (!StringUtil.isEmpty(initializeOnCreate)) {
            container.setInitializeOnCreate(Boolean.valueOf(initializeOnCreate)
                    .booleanValue());
        }

        S2Container parent = (S2Container) context.getParameter("parent");
        if (parent != null) {
            container.setRoot(parent.getRoot());
        }
        context.push(container);
    }

    public void end(TagHandlerContext context, String body) {
        S2Container container = (S2Container) context.pop();
        if (container.isInitializeOnCreate()) {
            container.init();
        }
    }

    protected S2Container createContainer() {
        return (S2Container) ClassUtil.newInstance(containerImplClass);
    }
}
