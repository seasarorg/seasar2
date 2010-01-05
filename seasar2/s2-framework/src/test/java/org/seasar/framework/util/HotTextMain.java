/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.util;

/**
 * @author higa
 * 
 */
public class HotTextMain {

    private static final String PATH = StringUtil.replace(ClassUtil
            .getPackageName(HotTextTest.class), ".", "/")
            + "/HotTextMain.txt";

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        HotText text = new HotText(PATH);
        System.out.println(text.getValue());
        System.out.print("Please rewrite HotTextMain.txt and input something>");
        System.in.read();
        System.out.println(text.getValue());
    }

}
