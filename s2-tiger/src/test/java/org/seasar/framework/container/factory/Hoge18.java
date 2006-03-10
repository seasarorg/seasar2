package org.seasar.framework.container.factory;

import javax.ejb.PostConstruct;
import javax.ejb.Stateless;

@Stateless
public class Hoge18 implements IHoge18 {

    public Hoge18() {
    }

    public void hoge() {
    }

    @PostConstruct
    static void init() {
    }
}
