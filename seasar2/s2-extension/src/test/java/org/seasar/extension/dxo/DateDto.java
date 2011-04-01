/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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

import java.sql.Time;
import java.util.Date;

/**
 * @author Satsohi Kimura
 */
public class DateDto {

    private Date a = DateUtil.newDate(2100, 6, 22);

    private Time b;

    /**
     * @return
     */
    public Date getA() {
        return a;
    }

    /**
     * @param a
     */
    public void setA(Date a) {
        this.a = a;
    }

    /**
     * @return
     */
    public Time getB() {
        return b;
    }

    /**
     * @param b
     */
    public void setB(Time b) {
        this.b = b;
    }

}
