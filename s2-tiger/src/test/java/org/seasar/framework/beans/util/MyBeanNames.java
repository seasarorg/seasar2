/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.beans.util;

/**
 * MyBean用のBeanNamesクラスです。
 * 
 * @author kato
 * 
 */
public class MyBeanNames {

    /**
     * CharSequenceを作成します。
     * 
     * @param name
     * @return
     */
    protected static CharSequence createCharSequence(final String name) {
        return new CharSequence() {

            @Override
            public String toString() {
                return name;
            }

            public char charAt(int index) {
                return name.charAt(index);
            }

            public int length() {
                return name.length();
            }

            public CharSequence subSequence(int start, int end) {
                return name.subSequence(start, end);
            }

        };
    }

    public static CharSequence aaa() {
        return createCharSequence("aaa");
    }

    public static CharSequence bbb() {
        return createCharSequence("bbb");
    }

    public static CharSequence ccc() {
        return createCharSequence("ccc");
    }

}
