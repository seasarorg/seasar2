package org.seasar.framework.util;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * @author higa
 * 
 */
public class MethodUtilTest extends TestCase {

    public void testIsEqualsMethod() {
        Method equalsMethod = ClassUtil.getMethod(getClass(), "equals",
                new Class[] { Object.class });
        assertTrue("1", MethodUtil.isEqualsMethod(equalsMethod));
        Method hashCodeMethod = ClassUtil.getMethod(getClass(), "hashCode",
                new Class[0]);
        assertFalse("2", MethodUtil.isEqualsMethod(hashCodeMethod));
    }

    public void testIsHashCodeMethod() {
        Method equalsMethod = ClassUtil.getMethod(getClass(), "equals",
                new Class[] { Object.class });
        assertFalse("1", MethodUtil.isHashCodeMethod(equalsMethod));
        Method hashCodeMethod = ClassUtil.getMethod(getClass(), "hashCode",
                new Class[0]);
        assertTrue("2", MethodUtil.isHashCodeMethod(hashCodeMethod));
    }
    
    public void testIsToStringMethod() {
        Method toStringMethod = ClassUtil.getMethod(getClass(), "toString",
                new Class[0]);
        assertTrue("1", MethodUtil.isToStringMethod(toStringMethod));
        Method hashCodeMethod = ClassUtil.getMethod(getClass(), "hashCode",
                new Class[0]);
        assertFalse("2", MethodUtil.isToStringMethod(hashCodeMethod));
    }
}
