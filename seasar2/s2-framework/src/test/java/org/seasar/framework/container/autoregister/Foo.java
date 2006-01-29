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
package org.seasar.framework.container.autoregister;

/**
 * @author higa
 */
public class Foo implements Greeting {

    public static final String COMPONENT =
        "name=foo, instance=prototype, autoBinding=property";
    
    public static final String foo2_BINDING = null;
    
    private Foo2 foo2;
    
    public Foo2 getFoo2() {
        return foo2;
    }

    public void setFoo2(Foo2 foo2) {
        this.foo2 = foo2;
    }
    
    public String greet() {
        return null;
    }
}