package org.seasar.framework.ejb.unit.impl;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.ejb.unit.AnnotationNotFoundRuntimeException;
import org.seasar.framework.ejb.unit.PersistentClass;

/**
 * @author taedium
 *
 */
public class EntityClassTest extends S2TestCase {

    public void testIsPropertyAccessed() {
        PersistentClass pc = new EntityClass(Hoge.class);
        assertFalse("1", pc.isPropertyAccessed());
        PersistentClass psh2 = new EntityClass(Hoge2.class);
        assertTrue("2", psh2.isPropertyAccessed());
    }

    public void testGetName() {
        PersistentClass pc = new EntityClass(Hoge.class);
        assertEquals("1", "Hoge", pc.getName());
        PersistentClass psh2 = new EntityClass(Hoge2.class);
        assertEquals("2", "Foo", psh2.getName());
    }

    public void testGetPersistentStateHolderType() {
        PersistentClass pc = new EntityClass(Hoge.class);
        assertEquals("1", Hoge.class, pc.getPersistentClassType());
    }

    public void testGetPersistentState() {
        PersistentClass pc = new EntityClass(Hoge.class);
        assertEquals("1", 3, pc.getPersistentStateSize());
    }

    public void testGetTableName() {
        PersistentClass pc = new EntityClass(Hoge3.class);
        assertEquals("1", 3, pc.getTableSize());
        assertEquals("2", "Foo1", pc.getTableName(0));
        assertEquals("3", "Foo2", pc.getTableName(1));
        assertEquals("4", "Foo3", pc.getTableName(2));
    }

    public void testInvalidClass() {
        try {
            new EntityClass(String.class);
            fail();
        } catch (AnnotationNotFoundRuntimeException e) {
        }
    }
}
