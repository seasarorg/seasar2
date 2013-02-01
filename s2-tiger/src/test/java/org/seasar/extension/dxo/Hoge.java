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

import java.math.BigDecimal;

/**
 *
 */
public class Hoge {
    private int foo;

    private String bar;

    private BigDecimal baz;

    /**
     * 
     */
    public Hoge() {
    }

    /**
     * @param foo
     * @param bar
     * @param baz
     */
    public Hoge(int foo, String bar, BigDecimal baz) {
        this.foo = foo;
        this.bar = bar;
        this.baz = baz;
    }

    /**
     * @return
     */
    public String getBar() {
        return bar;
    }

    /**
     * @param bar
     */
    public void setBar(String bar) {
        this.bar = bar;
    }

    /**
     * @return
     */
    public BigDecimal getBaz() {
        return baz;
    }

    /**
     * @param baz
     */
    public void setBaz(BigDecimal baz) {
        this.baz = baz;
    }

    /**
     * @return
     */
    public int getFoo() {
        return foo;
    }

    /**
     * @param foo
     */
    public void setFoo(int foo) {
        this.foo = foo;
    }
}
