package examples.di.impl;


public class NameSampleImpl implements NameSample {

	private String name;

	public NameSampleImpl() {
	}

	public NameSampleImpl(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
