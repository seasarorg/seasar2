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
package org.seasar.extension.dxo;

/**
 * @author Satsohi Kimura
 */
public class DateStringDto {
    private String a_yyyy = "2000";

    private String a_MM = "10";

    private String a_dd = "30";

    private String b_$$$$;

    /**
     * @return
     */
    public String getA_dd() {
        return a_dd;
    }

    /**
     * @param a_dd
     */
    public void setA_dd(String a_dd) {
        this.a_dd = a_dd;
    }

    /**
     * @return
     */
    public String getA_MM() {
        return a_MM;
    }

    /**
     * @param a_mm
     */
    public void setA_MM(String a_mm) {
        a_MM = a_mm;
    }

    /**
     * @return
     */
    public String getA_yyyy() {
        return a_yyyy;
    }

    /**
     * @param a_yyyy
     */
    public void setA_yyyy(String a_yyyy) {
        this.a_yyyy = a_yyyy;
    }

    /**
     * @return
     */
    public String getB_$$$$() {
        return b_$$$$;
    }

    /**
     * @param b_$$$$
     */
    public void setB_$$$$(String b_$$$$) {
        this.b_$$$$ = b_$$$$;
    }

}
