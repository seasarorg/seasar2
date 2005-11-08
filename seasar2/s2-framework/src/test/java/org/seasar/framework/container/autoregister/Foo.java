package org.seasar.framework.container.autoregister;

/**
 * @author higa
 */
public class Foo implements Greeting {

    public static final String COMPONENT =
        "name=foo, instance=prototype, autoBinding=property";
    
    public static final String foo2_BINDING = null;
    
    private Foo2 foo2;
    
    public Foo2 getFoo2() {
        return foo2;
    }

    public void setFoo2(Foo2 foo2) {
        this.foo2 = foo2;
    }
    
    public String greet() {
        return null;
    }
}