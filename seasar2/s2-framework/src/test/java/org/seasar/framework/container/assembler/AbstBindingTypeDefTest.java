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
package org.seasar.framework.container.assembler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.impl.S2ContainerImpl;

/**
 * @author higa
 * 
 */
public class AbstBindingTypeDefTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testBindingComponentDef() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register(ComponentDefAware.class);
        ComponentDef cd = container.getComponentDef(ComponentDefAware.class);
        ComponentDefAware cdAware = (ComponentDefAware) cd.getComponent();
        assertSame("1", cd, cdAware.getComponentDef());
    }

    /**
     * @throws Exception
     */
    public void testBindAutoForField() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Hoge.class);
        container.register(cd);
        container.register(new ArrayList());
        container.register(new ArrayList());
        Hoge hoge = (Hoge) container.getComponent(Hoge.class);
        assertNull("1", hoge.aaa);
    }

    /**
     * @throws Exception
     */
    public void testBindAutoForField2() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Hoge2.class);
        cd.setAutoBindingDef(AutoBindingDefFactory.SEMIAUTO);
        PropertyDef propDef = new PropertyDefImpl("aaa");
        propDef.setAccessTypeDef(AccessTypeDefFactory.FIELD);
        cd.addPropertyDef(propDef);
        container.register(cd);
        container.register(new Hoge3());
        Hoge2 hoge2 = (Hoge2) container.getComponent(Hoge2.class);
        assertNotNull("1", hoge2.aaa);
    }

    /**
     * @throws Exception
     */
    public void testBindAutoForProperty() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Hoge5.class);
        container.register(cd);
        container.register(new ArrayList());
        Hoge5 hoge = (Hoge5) container.getComponent(Hoge5.class);
        assertNotNull(hoge.list);
    }

    /**
     * @throws Exception
     */
    public void testBindAutoForPackagePrefix() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Hoge.class, "aaa_hoge");
        ComponentDefImpl cd2 = new ComponentDefImpl(Foo.class);
        container.register(cd);
        container.register(cd2);
        container.register(Foo3.class);
        Foo foo = (Foo) container.getComponent(Foo.class);
        assertNotNull(foo.getHoge());
        Foo3 foo3 = (Foo3) container.getComponent(Foo3.class);
        assertNotNull(foo3.getHoGe());
    }

    /**
     * @throws Exception
     */
    public void testBindAutoForArray() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register(Foo2.class);
        container.register(Hoge3.class);
        container.register(Hoge4.class);
        Foo2 foo2 = (Foo2) container.getComponent(Foo2.class);
        assertNotNull(foo2.getHoges());
    }

    /**
     * @throws Exception
     */
    public void testBindAutoForArray2() throws Exception {
        S2Container container0 = new S2ContainerImpl();
        container0.setPath("0");
        container0.register(MapArrayHolder.class);

        S2Container container1 = new S2ContainerImpl();
        container1.setPath("1");
        container1.register(MapArrayHolder.class);
        container1.register(HashMap.class);
        container0.include(container1);

        S2Container container2 = new S2ContainerImpl();
        container2.setPath("2");
        container2.register(MapArrayHolder.class);
        container2.register(HashMap.class);
        container2.register(HashMap.class);
        container1.include(container2);

        S2Container container3 = new S2ContainerImpl();
        container3.setPath("3");
        container3.register(MapArrayHolder.class);
        container3.register(HashMap.class);
        container3.register(HashMap.class);
        container3.register(HashMap.class);
        container2.include(container3);

        S2Container container4 = new S2ContainerImpl();
        container4.setPath("4");
        container4.register(MapArrayHolder.class);
        container4.register(HashMap.class);
        container4.register(HashMap.class);
        container4.register(HashMap.class);
        container4.register(HashMap.class);
        container3.include(container4);

        container0.init();

        MapArrayHolder holder = (MapArrayHolder) container0
                .getComponent(MapArrayHolder.class);
        assertEquals(10, holder.maps.length);

        holder = (MapArrayHolder) container1.getComponent(MapArrayHolder.class);
        assertEquals(10, holder.maps.length);

        holder = (MapArrayHolder) container2.getComponent(MapArrayHolder.class);
        assertEquals(9, holder.maps.length);

        holder = (MapArrayHolder) container3.getComponent(MapArrayHolder.class);
        assertEquals(7, holder.maps.length);

        holder = (MapArrayHolder) container4.getComponent(MapArrayHolder.class);
        assertEquals(4, holder.maps.length);
    }

    /**
     * @throws Exception
     */
    public void testBindAutoForArrayField() throws Exception {
        S2Container container = new S2ContainerImpl();
        container.register(Foo4.class);
        container.register(Hoge3.class);
        container.register(Hoge4.class);
        Foo4 foo4 = (Foo4) container.getComponent(Foo4.class);
        assertNull(foo4.hoges);
    }

    /**
     * @throws Exception
     */
    public void testBindAutoForArrayField2() throws Exception {
        S2Container container = new S2ContainerImpl();
        ComponentDefImpl cd = new ComponentDefImpl(Foo4.class);
        cd.setAutoBindingDef(AutoBindingDefFactory.SEMIAUTO);
        PropertyDef propDef = new PropertyDefImpl("hoges");
        propDef.setAccessTypeDef(AccessTypeDefFactory.FIELD);
        cd.addPropertyDef(propDef);
        container.register(cd);
        container.register(Hoge3.class);
        container.register(Hoge4.class);
        Foo4 foo4 = (Foo4) container.getComponent(Foo4.class);
        assertNotNull(foo4.hoges);
    }

    /**
     * 
     */
    public static class ComponentDefAware {
        private ComponentDef componentDef;

        /**
         * @return
         */
        public ComponentDef getComponentDef() {
            return componentDef;
        }

        /**
         * @param componentDef
         */
        public void setComponentDef(ComponentDef componentDef) {
            this.componentDef = componentDef;
        }
    }

    /**
     * 
     */
    public static class Hoge {
        private List aaa;

        /**
         * @return
         */
        public List getAaa() {
            return aaa;
        }
    }

    /**
     * 
     */
    public static class Hoge2 {
        private IHoge aaa;
    }

    /**
     * 
     */
    public static interface IHoge {
    }

    /**
     * 
     */
    public static class Hoge3 implements IHoge {
    }

    /**
     * 
     */
    public static class Hoge4 extends Hoge3 {
    }

    /**
     * 
     */
    public static class Hoge5 {
        /**
         * 
         */
        public ArrayList list;
    }

    /**
     * 
     */
    public static class Foo {

        private Hoge hoge;

        /**
         * @return
         */
        public Hoge getHoge() {
            return hoge;
        }

        /**
         * @param hoge
         */
        public void setHoge(Hoge hoge) {
            this.hoge = hoge;
        }
    }

    /**
     * 
     */
    public static class Foo2 {

        private IHoge[] hoges;

        /**
         * @return
         */
        public IHoge[] getHoges() {
            return hoges;
        }

        /**
         * @param hoges
         */
        public void setHoges(IHoge[] hoges) {
            this.hoges = hoges;
        }
    }

    /**
     * 
     */
    public static class Foo3 {

        private Hoge hoGe;

        /**
         * @return
         */
        public Hoge getHoGe() {
            return hoGe;
        }

        /**
         * @param hoGe
         */
        public void setHoGe(Hoge hoGe) {
            this.hoGe = hoGe;
        }
    }

    /**
     * 
     */
    public static class Foo4 {

        IHoge[] hoges;

    }

    /**
     * 
     */
    public static class MapArrayHolder {
        Map[] maps;

        /**
         * @param maps
         */
        public void setMaps(Map[] maps) {
            this.maps = maps;
        }
    }
}
