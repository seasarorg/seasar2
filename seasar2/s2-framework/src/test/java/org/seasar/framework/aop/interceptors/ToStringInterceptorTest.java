/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.interceptors;

import java.util.Date;

import junit.framework.TestCase;

import org.seasar.framework.aop.Aspect;
import org.seasar.framework.aop.Pointcut;
import org.seasar.framework.aop.impl.AspectImpl;
import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.aop.proxy.AopProxy;

/**
 * @author eriguchi
 * 
 */
public class ToStringInterceptorTest extends TestCase {

    public void testIntercept() throws Exception {
        ToStringInterceptor interceptor = new ToStringInterceptor();
        Pointcut pointcut = new PointcutImpl(new String[] { "toString" });
        Aspect aspect = new AspectImpl(interceptor, pointcut);
        AopProxy aopProxy = new AopProxy(FooEntity.class,
                new Aspect[] { aspect });
        FooEntity proxy = (FooEntity) aopProxy.create();

        String identityHashCode = Integer.toHexString(System
                .identityHashCode(proxy));
        System.out.println(proxy.toString());
        assertEquals(
                "org.seasar.framework.aop.interceptors.ToStringInterceptorTest$FooEntity@"
                        + identityHashCode
                        + "[intVal=12,longVal=123,charVal='a',doubleVal=1234.5"
                        + ",dateVal=70/01/01 9:00,nullVal=null,longArray={1,2,3},intArray={1,2,3},shortArray={1,2,3}"
                        + ",byteArray={1,2,3},charArray={'1','2','3'},doubleArray={1.0,2.0,3.0},floatArray={1.0,2.0,3.0}"
                        + ",booleanArray={true,false,true},booleanClassArray={true,false,false},"
                        + "stringArray={\"1\",\"2\",\"3\"}]", proxy.toString());
    }

    public void testInterceptExtendsField() throws Exception {
        ToStringInterceptor interceptor = new ToStringInterceptor();
        interceptor.setDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        Pointcut pointcut = new PointcutImpl(new String[] { "toString" });
        Aspect aspect = new AspectImpl(interceptor, pointcut);
        AopProxy aopProxy = new AopProxy(BarEntity.class,
                new Aspect[] { aspect });
        BarEntity proxy = (BarEntity) aopProxy.create();

        String identityHashCode = Integer.toHexString(System
                .identityHashCode(proxy));
        System.out.println(proxy.toString());
        assertEquals(
                "org.seasar.framework.aop.interceptors.ToStringInterceptorTest$BarEntity@"
                        + identityHashCode
                        + "[exIntVal=123456,exLongVal=1234567,exDoubleVal=1234567.8"
                        + ",exDateVal=1970/01/01 09:00:00.001"
                        + ",intVal=12,longVal=123,charVal='a',doubleVal=1234.5"
                        + ",dateVal=1970/01/01 09:00:00.000,nullVal=null,longArray={1,2,3},intArray={1,2,3},shortArray={1,2,3}"
                        + ",byteArray={1,2,3},charArray={'1','2','3'},doubleArray={1.0,2.0,3.0},floatArray={1.0,2.0,3.0}"
                        + ",booleanArray={true,false,true},booleanClassArray={true,false,false},"
                        + "stringArray={\"1\",\"2\",\"3\"}]", proxy.toString());
    }

    static class FooEntity {
        private int intVal = 12;

        protected long longVal = 123;

        private char charVal = 'a';

        double doubleVal = 1234.5;

        public Date dateVal = new Date(0);

        public Date nullVal = null;

        public long[] longArray = new long[] { 1, 2, 3 };

        public int[] intArray = new int[] { 1, 2, 3 };

        public short[] shortArray = new short[] { 1, 2, 3 };

        public byte[] byteArray = new byte[] { 1, 2, 3 };

        public char[] charArray = new char[] { '1', '2', '3' };

        public double[] doubleArray = new double[] { 1, 2, 3 };

        public float[] floatArray = new float[] { 1, 2, 3 };

        public boolean[] booleanArray = new boolean[] { true, false, true };

        public Boolean[] booleanClassArray = new Boolean[] { Boolean.TRUE,
                Boolean.FALSE, Boolean.FALSE };

        public String[] stringArray = new String[] { "1", "2", "3" };
    }

    static class BarEntity extends FooEntity {
        private int exIntVal = 123456;

        protected long exLongVal = 1234567;

        double exDoubleVal = 1234567.8;

        public Date exDateVal = new Date(1);
    }
}
