package org.seasar.framework.container.factory;

import org.seasar.framework.container.annotation.tiger.AutoBindingType;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Component(name="aaa", instance=InstanceType.PROTOTYPE,
        autoBinding=AutoBindingType.PROPERTY)
public class Hoge2 {    
    
    @Binding("aaa2")
    public void setAaa(String aaa) {
    }
    
    @Binding(bindingType=BindingType.NONE)
    public void setBbb(String bbb) {
    }
    
    @Binding
    public void setCcc(String ccc) {
    }
}
