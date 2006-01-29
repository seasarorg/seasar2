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
package examples.hotswap.main;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

import examples.hotswap.Greeting;

public class GreetingMain {

    private static final String CONFIGURE_PATH = "examples/hotswap/dicon/s2container.dicon";

    private static final String PATH = "examples/hotswap/dicon/hotswap.dicon";

    public static void main(String[] args) throws Exception {
        S2ContainerFactory.configure(CONFIGURE_PATH);
        S2Container container = S2ContainerFactory.create(PATH);
        System.out.println("hotswapMode:" + container.isHotswapMode());
        container.init();
        try {
            Greeting greeting = (Greeting) container
                    .getComponent(Greeting.class);
            System.out.println(greeting.greet());
            System.out.println("Let's modify GreetingImpl, then press ENTER.");
            System.in.read();
            System.out.println("after modify");
            System.out.println(greeting.greet());
        } finally {
            container.destroy();
        }
    }
}