package examples.di.impl;

//import java.util.Date;

import java.util.Date;

public class GreetingClientSingletonImpl implements GreetingClientSingleton {

	private Greeting greeting;

	private Date date;

	public void setGreeting(Greeting greeting) {
		this.greeting = greeting;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void execute() {
		System.out.println(greeting.greet() + getDate().toString());
	}
}
