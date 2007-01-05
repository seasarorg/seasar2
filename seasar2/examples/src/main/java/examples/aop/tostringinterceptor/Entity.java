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
package examples.aop.tostringinterceptor;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    private int intValue = 1;

    private static int staticIntValue = 12;

    private double doubleValue = 12.3;

    private String stringValue = "1234";

    public static final int staticFinalIntValue = 12345;

    private Map mapValue = new HashMap();

    private InnerEntity innerEntity;

    public Entity(InnerEntity innerEntity) {
        this.innerEntity = innerEntity;
    }
}
