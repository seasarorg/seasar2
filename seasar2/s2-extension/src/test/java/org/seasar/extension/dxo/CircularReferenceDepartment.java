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
 * 
 */
public class CircularReferenceDepartment extends Department {
    private static final long serialVersionUID = 2842245523020553511L;

    private CircularReferenceEmployee[] emps;

    /**
     * @return
     */
    public CircularReferenceEmployee[] getEmps() {
        return emps;
    }

    /**
     * @param emps
     */
    public void setEmps(CircularReferenceEmployee[] emps) {
        this.emps = emps;
    }

}
