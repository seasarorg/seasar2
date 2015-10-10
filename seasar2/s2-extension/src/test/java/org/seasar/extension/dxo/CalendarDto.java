/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import java.util.Calendar;

/**
 * @author Satsohi Kimura
 */
public class CalendarDto {
    private Calendar a = Calendar.getInstance();

    {
        a.set(Calendar.YEAR, 2100);
        a.set(Calendar.MONTH, 5);
        a.set(Calendar.DAY_OF_MONTH, 22);
    }

    /**
     * @return
     */
    public Calendar getA() {
        return a;
    }

    /**
     * @param a
     */
    public void setA(Calendar a) {
        this.a = a;
    }

}
