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
package org.seasar.framework.beans;

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author higa
 * 
 */
public class IllegalPropertyRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 3584516316082904020L;

    private Class componentClass_;

    private String propertyName_;

    public IllegalPropertyRuntimeException(Class componentClass,
            String propertyName, Throwable cause) {
        super("ESSR0059", new Object[] { componentClass.getName(),
                propertyName, cause }, cause);
        componentClass_ = componentClass;
        propertyName_ = propertyName;
    }

    public Class getComponentClass() {
        return componentClass_;
    }

    public String getPropertyName() {
        return propertyName_;
    }
}