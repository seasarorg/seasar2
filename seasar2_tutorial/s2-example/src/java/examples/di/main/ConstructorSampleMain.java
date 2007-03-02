package examples.di.main;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

import examples.di.impl.ConstructorSample;



public class ConstructorSampleMain {
	private static final String PATH = "examples/di/dicon/ConstructorSample.dicon";
	
    public static void main(String[] args) {
    	S2Container container = S2ContainerFactory.create(PATH);
    	ConstructorSample constructorSample = (ConstructorSample) container
                .getComponent("constructorSample");
    	System.out.println(constructorSample.greet());
    }
}
