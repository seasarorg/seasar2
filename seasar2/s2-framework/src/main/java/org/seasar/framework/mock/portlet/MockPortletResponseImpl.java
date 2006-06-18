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
package org.seasar.framework.mock.portlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shot
 * 
 */
public class MockPortletResponseImpl implements MockPortletResponse {

    private Map valueMap = new HashMap();
    
    private Map onlyValueMap = new HashMap();
    
    public void addProperty(String key, String value) {
        if(key == null) {
            throw new IllegalArgumentException();
        }
        if(valueMap.containsKey(key)) {
            List list = (List)valueMap.get(key);
            list.add(value);
        }else {
            List list = new ArrayList();
            list.add(value);
            valueMap.put(key, list);
        }
    }

    public void setProperty(String key, String value) {
        if(key == null) {
            throw new IllegalArgumentException();
        }
        onlyValueMap.put(key, value);
    }

    public String encodeURL(String url) {
        return url;
    }

}
