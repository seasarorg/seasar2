package org.seasar.framework.container.factory;

public interface Greeting {

    String ASPECT = "greetingInterceptor";

    String greet();
}
