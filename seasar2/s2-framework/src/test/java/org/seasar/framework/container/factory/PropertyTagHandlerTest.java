package org.seasar.framework.container.factory;

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;
import org.seasar.framework.container.factory.S2ContainerFactory;

/**
 * @author higa
 * 
 */
public class PropertyTagHandlerTest extends TestCase {

    private static final String PATH = "org/seasar/framework/container/factory/PropertyTagHandlerTest.dicon";

    public void testProperty() throws Exception {
        S2Container container = S2ContainerFactory.create(PATH);
        assertEquals("1", new Date(0), container.getComponent("date"));
        ComponentDef cd = container.getComponentDef("date");
        PropertyDef pd = cd.getPropertyDef("time");
        assertEquals("2", BindingTypeDefFactory.NONE.getName(), pd.getBindingTypeDef().getName());
    }
}
