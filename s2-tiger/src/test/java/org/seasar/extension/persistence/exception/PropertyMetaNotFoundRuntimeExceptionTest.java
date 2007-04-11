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
package org.seasar.extension.persistence.exception;

import junit.framework.TestCase;

import org.seasar.extension.persistence.dto.EmployeeDto;

/**
 * @author higa
 * 
 */
public class PropertyMetaNotFoundRuntimeExceptionTest extends TestCase {

    /**
     * 
     */
    public void testGetMessage() {
        NoEntityRuntimeException ex = new NoEntityRuntimeException(
                EmployeeDto.class);
        assertEquals(EmployeeDto.class, ex.getTargetClass());
        System.out.println(ex.getMessage());
    }
}