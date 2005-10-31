/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.util;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;

/**
 * @author koichik
 */
public class Traversal {
    public static interface S2ContainerHandler {
        Object processContainer(S2Container container);
    }

    public static interface ComponentDefHandler {
        Object processComponent(ComponentDef componentDef);
    }

    public static Object forEachContainer(final S2Container container, final S2ContainerHandler handler) {
        return forEachContainer(container, handler, true);
    }

    public static Object forEachContainer(final S2Container container, final S2ContainerHandler handler,
            final boolean parentFirst) {
        if (parentFirst) {
            final Object result = handler.processContainer(container);
            if (result != null) {
                return result;
            }
        }
        for (int i = 0; i < container.getChildSize(); ++i) {
            final Object result = forEachContainer(container.getChild(i), handler, parentFirst);
            if (result != null) {
                return result;
            }
        }
        if (!parentFirst) {
            return handler.processContainer(container);
        }
        return null;
    }

    public static Object forEachComponent(final S2Container container, final ComponentDefHandler handler) {
        return forEachComponent(container, handler, true);
    }

    public static Object forEachComponent(final S2Container container, final ComponentDefHandler handler,
            final boolean parentFirst) {
        return forEachContainer(container, new S2ContainerHandler() {
            public Object processContainer(final S2Container container) {
                for (int i = 0; i < container.getComponentDefSize(); ++i) {
                    final Object result = handler.processComponent(container.getComponentDef(i));
                    if (result != null) {
                        return result;
                    }
                }
                return null;
            }
        }, parentFirst);
    }
}
