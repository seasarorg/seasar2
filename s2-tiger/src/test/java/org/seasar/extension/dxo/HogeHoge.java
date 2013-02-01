/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo;

/**
 * @author koichik
 * 
 */
public class HogeHoge {

    private String foo;

    private char[] bar;

    private int baz;

    /**
     * 
     */
    public HogeHoge() {
    }

    /**
     * @param foo
     * @param bar
     * @param baz
     */
    public HogeHoge(String foo, char[] bar, int baz) {
        this.foo = foo;
        this.bar = bar;
        this.baz = baz;
    }

    /**
     * @return
     */
    public String getFoo() {
        return foo;
    }

    /**
     * @param foo
     */
    public void setFoo(String foo) {
        this.foo = foo;
    }

    /**
     * @return
     */
    public char[] getBar() {
        return bar;
    }

    /**
     * @param bar
     */
    public void setBar(char[] bar) {
        this.bar = bar;
    }

    /**
     * @return
     */
    public int getBaz() {
        return baz;
    }

    /**
     * @param baz
     */
    public void setBaz(int baz) {
        this.baz = baz;
    }

}
