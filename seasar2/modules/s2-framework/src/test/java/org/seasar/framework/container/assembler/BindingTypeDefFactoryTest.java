package org.seasar.framework.container.assembler;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.container.IllegalBindingTypeDefRuntimeException;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;

/**
 * @author higa
 */
public class BindingTypeDefFactoryTest extends S2TestCase {

    public void testGetBindingTypeDef() throws Exception {
        assertEquals("1", BindingTypeDefFactory.MUST, BindingTypeDefFactory
                .getBindingTypeDef("must"));
        assertEquals("2", BindingTypeDefFactory.SHOULD, BindingTypeDefFactory
                .getBindingTypeDef("should"));
        assertEquals("3", BindingTypeDefFactory.MAY, BindingTypeDefFactory
                .getBindingTypeDef("may"));
        assertEquals("4", BindingTypeDefFactory.NONE, BindingTypeDefFactory
                .getBindingTypeDef("none"));
        try {
            BindingTypeDefFactory.getBindingTypeDef("hoge");
            fail("5");
        } catch (IllegalBindingTypeDefRuntimeException ex) {
            System.out.println(ex);
        }
    }
}