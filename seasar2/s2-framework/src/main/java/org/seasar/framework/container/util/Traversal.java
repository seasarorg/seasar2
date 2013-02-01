/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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

import java.util.HashSet;
import java.util.Set;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;

/**
 * トラバースするためのクラスです。
 * 
 * @author koichik
 */
public class Traversal {

    /**
     * {@link S2Container}を処理するためのインターフェースです。
     * 
     */
    public static interface S2ContainerHandler {
        /**
         * {@link S2Container}を処理します。
         * 
         * @param container
         * @return 処理した結果
         */
        Object processContainer(S2Container container);
    }

    /**
     * {@link ComponentDef}を処理するためのインターフェースです。
     * 
     */
    public static interface ComponentDefHandler {
        /**
         * {@link ComponentDef}を処理します。
         * 
         * @param componentDef
         * @return 処理した結果
         */
        Object processComponent(ComponentDef componentDef);
    }

    /**
     * コンポーネントをトラバースします。
     * 
     * @param container
     * @param handler
     * @return 処理した結果
     * @see #forEachComponent(S2Container,
     *      org.seasar.framework.container.util.Traversal.ComponentDefHandler,
     *      boolean)
     */
    public static Object forEachComponent(final S2Container container,
            final ComponentDefHandler handler) {
        return forEachComponent(container, handler, true);
    }

    /**
     * コンポーネントをトラバースします。
     * 
     * @param container
     * @param handler
     * @param parentFirst
     * @return 処理した結果
     */
    public static Object forEachComponent(final S2Container container,
            final ComponentDefHandler handler, final boolean parentFirst) {
        return forEachContainer(container, new S2ContainerHandler() {
            public Object processContainer(final S2Container container) {
                for (int i = 0; i < container.getComponentDefSize(); ++i) {
                    final Object result = handler.processComponent(container
                            .getComponentDef(i));
                    if (result != null) {
                        return result;
                    }
                }
                return null;
            }
        }, parentFirst);
    }

    /**
     * {@link S2Container}をトラバースします。
     * 
     * @param container
     * @param handler
     * @return 処理した結果
     * @see #forEachContainer(S2Container,
     *      org.seasar.framework.container.util.Traversal.S2ContainerHandler,
     *      boolean, Set)
     */
    public static Object forEachContainer(final S2Container container,
            final S2ContainerHandler handler) {
        return forEachContainer(container, handler, true, new HashSet());
    }

    /**
     * {@link S2Container}をトラバースします。
     * 
     * @param container
     * @param handler
     * @param parentFirst
     * @return 処理した結果
     * @see #forEachContainer(S2Container,
     *      org.seasar.framework.container.util.Traversal.S2ContainerHandler,
     *      boolean, Set)
     */
    public static Object forEachContainer(final S2Container container,
            final S2ContainerHandler handler, final boolean parentFirst) {
        return forEachContainer(container, handler, parentFirst, new HashSet());
    }

    /**
     * {@link S2Container}をトラバースします。
     * 
     * @param container
     * @param handler
     * @param parentFirst
     * @param processed
     * @return 処理した結果
     */
    protected static Object forEachContainer(final S2Container container,
            final S2ContainerHandler handler, final boolean parentFirst,
            final Set processed) {
        if (parentFirst) {
            final Object result = handler.processContainer(container);
            if (result != null) {
                return result;
            }
        }
        for (int i = 0; i < container.getChildSize(); ++i) {
            final S2Container child = container.getChild(i);
            if (processed.contains(child)) {
                continue;
            }
            processed.add(child);
            final Object result = forEachContainer(child, handler, parentFirst,
                    processed);
            if (result != null) {
                return result;
            }
        }
        if (!parentFirst) {
            return handler.processContainer(container);
        }
        return null;
    }

    /**
     * 親の {@link S2Container}をトラバースします。
     * 
     * @param container
     * @param handler
     * @return 処理した結果
     * @see #forEachParentContainer(S2Container,
     *      org.seasar.framework.container.util.Traversal.S2ContainerHandler,
     *      boolean, Set)
     */
    public static Object forEachParentContainer(final S2Container container,
            final S2ContainerHandler handler) {
        return forEachParentContainer(container, handler, true, new HashSet());
    }

    /**
     * 親の {@link S2Container}をトラバースします。
     * 
     * @param container
     * @param handler
     * @param childFirst
     * @return 処理した結果
     * @see #forEachParentContainer(S2Container,
     *      org.seasar.framework.container.util.Traversal.S2ContainerHandler,
     *      boolean, Set)
     */
    public static Object forEachParentContainer(final S2Container container,
            final S2ContainerHandler handler, final boolean childFirst) {
        return forEachParentContainer(container, handler, childFirst,
                new HashSet());
    }

    /**
     * 親の {@link S2Container}をトラバースします。
     * 
     * @param container
     * @param handler
     * @param childFirst
     * @param processed
     * @return 処理した結果
     */
    protected static Object forEachParentContainer(final S2Container container,
            final S2ContainerHandler handler, final boolean childFirst,
            final Set processed) {
        if (childFirst) {
            final Object result = handler.processContainer(container);
            if (result != null) {
                return result;
            }
        }
        for (int i = 0; i < container.getParentSize(); ++i) {
            final S2Container parent = container.getParent(i);
            if (processed.contains(parent)) {
                continue;
            }
            processed.add(parent);
            final Object result = forEachParentContainer(parent, handler,
                    childFirst, processed);
            if (result != null) {
                return result;
            }
        }
        if (!childFirst) {
            return handler.processContainer(container);
        }
        return null;
    }
}
