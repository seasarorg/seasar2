package org.seasar.framework.container.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.util.Traversal;

/**
 * @author koichik
 */
public class TraversalTest extends TestCase {
    private S2Container container;

    public TraversalTest() {
    }

    public TraversalTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        container = S2ContainerFactory
                .create("org/seasar/framework/container/util/TraversalTest.dicon");
    }

    public void testForEachContainer() throws Exception {
        final List l = new ArrayList();
        Traversal.forEachContainer(container, new Traversal.S2ContainerHandler() {
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

    public void testForEachContainerChildLast() throws Exception {
        final List l = new ArrayList();
        Traversal.forEachContainer(container, new Traversal.S2ContainerHandler() {
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

    public void testForEachContainerFinding() throws Exception {
        String s = (String) Traversal.forEachContainer(container,
                new Traversal.S2ContainerHandler() {
                    public Object processContainer(S2Container container) {
                        return container.getNamespace();
                    }
                }, false);

        assertEquals("1", "1", s);
    }

    public void testForEachComponent() throws Exception {
        final List l = new ArrayList();
        Traversal.forEachComponent(container, new Traversal.ComponentDefHandler() {
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

    public void testForEachComponentChildFirst() throws Exception {
        final List l = new ArrayList();
        Traversal.forEachComponent(container, new Traversal.ComponentDefHandler() {
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

    public void testForEachComponentFinding() throws Exception {
        String s = (String)
        Traversal.forEachComponent(container, new Traversal.ComponentDefHandler() {
            public Object processComponent(ComponentDef componentDef) {
                return componentDef.getComponentName();
            }
        }, false);
        
        assertEquals("1", "1-1", s);
    }
}
