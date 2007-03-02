package examples.di.impl;

import examples.di.impl.ConstructorSample;

public class ConstructorSampleImpl implements ConstructorSample {

	private String name;
	
	public ConstructorSampleImpl(){
	}
	
	public ConstructorSampleImpl(String name){
		this.name = name;
	}
	
	public String greet() {
		return this.name;
	}
}
