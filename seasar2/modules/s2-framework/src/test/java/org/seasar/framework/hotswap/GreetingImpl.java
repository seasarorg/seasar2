package org.seasar.framework.hotswap;

import java.io.Serializable;

public class GreetingImpl implements Greeting, Serializable {

    final static long serialVersionUID = 0L;

    public String greet() {
        return "Hello2";
    }
    
    public String toString() {
        return "hoge";
    }
}
