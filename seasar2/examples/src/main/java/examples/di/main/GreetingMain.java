package examples.di.main;

import examples.di.Greeting;
import examples.di.impl.GreetingClientImpl;
import examples.di.impl.GreetingImpl;

public class GreetingMain {

    public static void main(String[] args) {
        Greeting greeting = new GreetingImpl();
        GreetingClientImpl greetingClient = new GreetingClientImpl();
        greetingClient.setGreeting(greeting);
        greetingClient.execute();
    }
}