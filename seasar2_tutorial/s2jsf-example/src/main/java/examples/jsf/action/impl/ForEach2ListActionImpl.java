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
package examples.jsf.action.impl;

import java.util.Iterator;
import java.util.List;

import examples.jsf.action.ForEach2ListAction;
import examples.jsf.dto.ForEach2Dto;

public class ForEach2ListActionImpl implements ForEach2ListAction {

    private List forEach2DtoList;

    public void setForEach2DtoList(List forEach2DtoList) {
        this.forEach2DtoList = forEach2DtoList;
    }

    public String update() {
        for (Iterator i = forEach2DtoList.iterator(); i.hasNext();) {
            ForEach2Dto dto = (ForEach2Dto) i.next();
            if (dto.isDelete()) {
                i.remove();
            }
        }
        return null;
    }

    public String addRow() {
        forEach2DtoList.add(new ForEach2Dto());
        return null;
    }
}