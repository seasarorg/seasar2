package org.seasar.extension.unit;

import java.util.List;

import org.seasar.extension.unit.S2TestCase;

public class S2TestCase3Test extends S2TestCase {

	private static final String PATH = "ccc.dicon";
	private List list1;
	
	public S2TestCase3Test(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(S2TestCase3Test.class);
	}

	public void setUp() {
		include(PATH);
	}
	
	public void testBindField() {
		assertNotNull("1", list1);
	}
}