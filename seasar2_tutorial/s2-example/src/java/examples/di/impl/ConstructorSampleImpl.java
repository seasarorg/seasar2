package examples.di.impl;


public class ConstructorSampleImpl implements ConstructorSample {

	private String name;

	public ConstructorSampleImpl() {
	}

	public ConstructorSampleImpl(String name) {
		this.name = name;
	}

	public String greet() {
		return this.name;
	}
}
