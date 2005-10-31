package org.seasar.framework.container.impl;

import java.util.Map;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.TooManyRegistrationComponentDef;
import org.seasar.framework.container.TooManyRegistrationRuntimeException;

/**
 * @author koichik
 */
public class S2ContainerBehaviorTest extends S2TestCase {
    S2Container container;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(S2ContainerBehaviorTest.class);
    }

    public S2ContainerBehaviorTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        include("S2ContainerBehaviorTest.dicon");
    }

    public void testGetComponent() throws Exception {
        assertNotNull("1", container.getComponent("foo"));
        try {
            container.getComponent("not exists");
            fail("2");
        }
        catch (ComponentNotFoundRuntimeException expected) {
        }
        try {
            container.getComponent("bar");
            fail("3");
        }
        catch (TooManyRegistrationRuntimeException expected) {
            System.out.println(expected);
        }
    }

    public void testGetComponentDef() throws Exception {
        assertNotNull("1", container.getComponentDef("foo"));
        try {
            container.getComponentDef("not exists");
            fail("2");
        }
        catch (ComponentNotFoundRuntimeException expected) {
        }
        assertTrue("3", container.getComponentDef("bar") instanceof TooManyRegistrationComponentDef);
    }

    public void testHasComponentDef() throws Exception {
        assertTrue("1", container.hasComponentDef("foo"));
        assertFalse("2", container.hasComponentDef("not exists"));
        assertTrue("3", container.hasComponentDef("bar"));
    }

    public void testInjectDependency() throws Exception {
        Outer outer = new Outer();
        container.injectDependency(outer, "outerFoo");
        assertNotNull("1", outer.getMap());
        try {
            container.injectDependency(outer, "not exists");
            fail("2");
        }
        catch (ComponentNotFoundRuntimeException expected) {
        }
        try {
            container.injectDependency(outer, "outerBar");
            fail("3");
        }
        catch (UnsupportedOperationException expected) {
            System.out.println(expected);
        }
    }

    public static class Outer {
        Map map;

        public Map getMap() {
            return this.map;
        }

        public void setMap(Map map) {
            this.map = map;
        }
    }
}
