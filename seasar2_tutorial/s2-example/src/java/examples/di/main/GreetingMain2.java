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
package examples.di.main;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

import examples.di.impl.GreetingClient;

public class GreetingMain2 {

    private static final String PATH = "examples/di/dicon/GreetingMain2.dicon";

    public static void main(String[] args) {
        S2Container container = S2ContainerFactory.create(PATH);
        GreetingClient greetingClient = (GreetingClient) container
                .getComponent("greetingClient");
        greetingClient.execute();
    }
}