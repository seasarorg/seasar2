package org.seasar.framework.container.deployer;

import java.io.Serializable;

public class GreetingImpl implements Greeting, Serializable {

    final static long serialVersionUID = 0L;

    public String greet() {
        return "Hello";
    }
    
    public String toString() {
        return "hoge";
    }
}
