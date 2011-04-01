/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author koichik
 */
public class TraversalTest extends TestCase {
    private S2Container container;

    protected void setUp() throws Exception {
        container = S2ContainerFactory
                .create("org/seasar/framework/container/util/TraversalTest.dicon");
    }

    /**
     * @throws Exception
     */
    public void testForEachContainer() throws Exception {
        final List l = new ArrayList();
        Traversal.forEachContainer(container,
                new Traversal.S2ContainerHandler() {
                    public Object processContainer(S2Container container) {
                        l.add(container.getNamespace());
                        return null;
                    }
                });

        assertEquals("1", 4, l.size());
        assertEquals("2", "root", l.get(0));
        assertEquals("3", "1", l.get(1));
        assertEquals("4", "2", l.get(2));
        assertEquals("5", "3", l.get(3));
    }

    /**
     * @throws Exception
     */
    public void testForEachContainerChildLast() throws Exception {
        final List l = new ArrayList();
        Traversal.forEachContainer(container,
                new Traversal.S2ContainerHandler() {
                    public Object processContainer(S2Container container) {
                        l.add(container.getNamespace());
                        return null;
                    }
                }, false);

        assertEquals("1", 4, l.size());
        assertEquals("2", "1", l.get(0));
        assertEquals("3", "3", l.get(1));
        assertEquals("4", "2", l.get(2));
        assertEquals("5", "root", l.get(3));
    }

    /**
     * @throws Exception
     */
    public void testForEachContainerFinding() throws Exception {
        String s = (String) Traversal.forEachContainer(container,
                new Traversal.S2ContainerHandler() {
                    public Object processContainer(S2Container container) {
                        return container.getNamespace();
                    }
                }, false);

        assertEquals("1", "1", s);
    }

    /**
     * @throws Exception
     */
    public void testForEachComponent() throws Exception {
        final List l = new ArrayList();
        Traversal.forEachComponent(container,
                new Traversal.ComponentDefHandler() {
                    public Object processComponent(ComponentDef componentDef) {
                        l.add(componentDef.getComponentName());
                        return null;
                    }
                });

        assertEquals("1", 8, l.size());
        assertEquals("2", "1", l.get(0));
        assertEquals("3", "2", l.get(1));
        assertEquals("4", "1-1", l.get(2));
        assertEquals("5", "1-2", l.get(3));
        assertEquals("6", "2-1", l.get(4));
        assertEquals("7", "2-2", l.get(5));
        assertEquals("8", "3-1", l.get(6));
        assertEquals("9", "3-2", l.get(7));
    }

    /**
     * @throws Exception
     */
    public void testForEachComponentChildFirst() throws Exception {
        final List l = new ArrayList();
        Traversal.forEachComponent(container,
                new Traversal.ComponentDefHandler() {
                    public Object processComponent(ComponentDef componentDef) {
                        l.add(componentDef.getComponentName());
                        return null;
                    }
                }, false);

        assertEquals("1", 8, l.size());
        assertEquals("2", "1-1", l.get(0));
        assertEquals("3", "1-2", l.get(1));
        assertEquals("4", "3-1", l.get(2));
        assertEquals("5", "3-2", l.get(3));
        assertEquals("6", "2-1", l.get(4));
        assertEquals("7", "2-2", l.get(5));
        assertEquals("8", "1", l.get(6));
        assertEquals("9", "2", l.get(7));
    }

    /**
     * @throws Exception
     */
    public void testForEachComponentFinding() throws Exception {
        String s = (String) Traversal.forEachComponent(container,
                new Traversal.ComponentDefHandler() {
                    public Object processComponent(ComponentDef componentDef) {
                        return componentDef.getComponentName();
                    }
                }, false);

        assertEquals("1", "1-1", s);
    }

    /**
     * @throws Exception
     */
    public void testForEachParentContainer() throws Exception {
        final List l = new ArrayList();
        final S2Container container3 = container.getChild(2);
        Traversal.forEachParentContainer(container3,
                new Traversal.S2ContainerHandler() {
                    public Object processContainer(S2Container container) {
                        l.add(container.getNamespace());
                        return null;
                    }
                });

        assertEquals("1", 3, l.size());
        assertEquals("2", "3", l.get(0));
        assertEquals("3", "2", l.get(1));
        assertEquals("4", "root", l.get(2));
    }

    /**
     * @throws Exception
     */
    public void testForEachParentContainerChildLast() throws Exception {
        final List l = new ArrayList();
        final S2Container container3 = container.getChild(2);
        Traversal.forEachParentContainer(container3,
                new Traversal.S2ContainerHandler() {
                    public Object processContainer(S2Container container) {
                        l.add(container.getNamespace());
                        return null;
                    }
                }, false);

        assertEquals("1", 3, l.size());
        assertEquals("2", "root", l.get(0));
        assertEquals("3", "2", l.get(1));
        assertEquals("4", "3", l.get(2));
    }

    /**
     * @throws Exception
     */
    public void testForEachParentContainerFinding() throws Exception {
        final S2Container container3 = container.getChild(2);
        String s = (String) Traversal.forEachParentContainer(container3,
                new Traversal.S2ContainerHandler() {
                    public Object processContainer(S2Container container) {
                        return container.getNamespace();
                    }
                }, false);

        assertEquals("1", "root", s);
    }
}
