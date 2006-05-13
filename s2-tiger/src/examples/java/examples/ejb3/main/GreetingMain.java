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
package examples.ejb3.main;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

import examples.ejb3.GreetingClient;

public class GreetingMain {

    private static final String PATH = "examples/ejb3/dicon/GreetingMain.dicon";

    public static void main(String[] args) throws Exception {
        SingletonS2ContainerFactory.setConfigPath(PATH);
        SingletonS2ContainerFactory.init();
        try {
            doMain(args);
        } finally {
            SingletonS2ContainerFactory.destroy();
        }
    }

    public static void doMain(String[] args) throws Exception {
        Context ctx = new InitialContext();
        GreetingClient greetingClient = (GreetingClient)
            ctx.lookup("greetingClient");
        greetingClient.execute();
    }
}