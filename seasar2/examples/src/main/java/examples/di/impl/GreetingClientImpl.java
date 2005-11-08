package examples.di.impl;

import examples.di.Greeting;
import examples.di.GreetingClient;

public class GreetingClientImpl implements GreetingClient {

    private Greeting greeting;

    public void setGreeting(Greeting greeting) {
        this.greeting = greeting;
    }
    
    public void execute() {
        System.out.println(greeting.greet());
    }
}