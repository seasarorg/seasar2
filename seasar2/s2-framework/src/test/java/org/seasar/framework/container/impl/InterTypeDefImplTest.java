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
package org.seasar.framework.container.impl;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import junit.framework.TestCase;

import org.seasar.framework.aop.InterType;
import org.seasar.framework.container.InterTypeDef;
import org.seasar.framework.container.S2Container;

public class InterTypeDefImplTest extends TestCase {

    public void testSetExpression() throws Exception {
        S2Container container = new S2ContainerImpl();
        InterTypeDef id = new InterTypeDefImpl();
        id.setExpression("fieldInterType");
        id.setContainer(container);
        ComponentDefImpl cd = new ComponentDefImpl(FieldInterType.class,
                "fieldInterType");
        container.register(cd);
        assertEquals("1", FieldInterType.class, id.getInterType().getClass());
    }

    public static class FieldInterType implements InterType {
        public void introduce(Class targetClass, CtClass enhancedClass) {
            try {
                enhancedClass.addField(new CtField(CtClass.booleanType, "test",
                        enhancedClass));
            } catch (CannotCompileException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
