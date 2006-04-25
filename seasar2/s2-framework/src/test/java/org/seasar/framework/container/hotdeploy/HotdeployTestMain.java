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
package org.seasar.framework.container.hotdeploy;

import java.lang.reflect.Method;


public class HotdeployTestMain {

    private static String PACKAGE_NAME = HotdeployTestMain.class.getPackage().getName() + ".sub";
    private static String AAA_NAME = PACKAGE_NAME + ".Aaa";
    private static String EXECUTE_NAME = "execute";
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        ClassLoader original = Thread.currentThread().getContextClassLoader();
        printAaa(original);
        System.out.println("Let's modify sub.Bbb#execute()");
        System.in.read();
        System.out.println("after modify");
        printAaa(original);
    }

    protected static void printAaa(ClassLoader original) throws Exception {
        HotdeployClassLoader loader = new HotdeployClassLoader(original);
        loader.setPackageName(PACKAGE_NAME);
        Thread.currentThread().setContextClassLoader(loader);
        Class clazz = loader.loadClass(AAA_NAME);
        Object o = clazz.newInstance();
        Method m = clazz.getMethod(EXECUTE_NAME, null);
        System.out.println(m.invoke(o, null));
    }
}
