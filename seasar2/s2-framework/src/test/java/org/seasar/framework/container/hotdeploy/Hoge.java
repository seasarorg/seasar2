package org.seasar.framework.container.hotdeploy;

public class Hoge {

    private Foo foo;

    public void setFoo(Foo foo) {
        this.foo = foo;
    }

    public String greet() {
        return foo.greet();
    }
}
