package examples.di.impl;


public class MethodSampleImpl implements MethodSample {

	private String name;

	public void init(String name) {
		this.name = name;
	}

	public String greet() {
		return this.name;
	}

}
