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
package examples.jsf.dto;

import java.io.Serializable;

public class SelectManyCheckboxDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String[] aaa = { "2" };

    private int[] bbb = { 3 };

    private int[] ccc;

    private int[] ddd;

    public String[] getAaa() {
        return aaa;
    }

    public void setAaa(String[] aaa) {
        this.aaa = aaa;
    }

    public int[] getBbb() {
        return bbb;
    }

    public void setBbb(int[] bbb) {
        this.bbb = bbb;
    }

    public int[] getCcc() {
        return ccc;
    }

    public void setCcc(int[] ccc) {
        this.ccc = ccc;
    }

    public int[] getDdd() {
        return ddd;
    }

    public void setDdd(int[] ddd) {
        this.ddd = ddd;
    }

}
