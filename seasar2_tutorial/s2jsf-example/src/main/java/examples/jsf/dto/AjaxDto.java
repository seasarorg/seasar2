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

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * @author yone
 */
public class AjaxDto {

    private List xxx;

    private int select1;
    
    public List getXxx() {
        return this.xxx;
    }
    
    public void setSelect1(int select1) {
        this.select1 = select1;
    }
    
    public int getSelect1() {
        return this.select1;
    }

    public Object test(String componentName, String arg2, int testFlg) {
        List items = null;
        if (testFlg == 1) {
            S2Container container = SingletonS2ContainerFactory.getContainer();
            items = (List) container.getComponent(componentName);
            items.clear();
        } else {
            items = new ArrayList();
        }
        javax.faces.model.SelectItem item1 = new javax.faces.model.SelectItem();
        item1.setValue("111");
        item1.setLabel(arg2 + "AA");
        items.add(item1);
        javax.faces.model.SelectItem item2 = new javax.faces.model.SelectItem();
        item2.setValue("222");
        item2.setLabel(arg2 + "BB");
        items.add(item2);
        javax.faces.model.SelectItem item3 = new javax.faces.model.SelectItem();
        item3.setValue("333");
        item3.setLabel(arg2 + "CC");
        items.add(item3);

        this.xxx = items;

        return this;
    }

}
