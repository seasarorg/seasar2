package org.seasar.framework.container.factory;

import javax.ejb.Stateless;

import org.seasar.framework.container.annotation.tiger.AutoBindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

@Stateless(name="hoge7")
@Component(name="hoge7x", instance=InstanceType.SINGLETON,
        autoBinding=AutoBindingType.PROPERTY)
public class Hoge8 {
}
