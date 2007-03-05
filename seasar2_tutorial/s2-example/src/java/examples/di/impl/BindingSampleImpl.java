package examples.di.impl;


public class BindingSampleImpl implements BindingSample {

	private NameSample name;

	public BindingSampleImpl() {
		name = new NameSampleImpl("hogehoge");
	}

	public void setName(NameSample name) {
		this.name = name;
	}

	public String greet() {
		return name.getName();
	}
}
