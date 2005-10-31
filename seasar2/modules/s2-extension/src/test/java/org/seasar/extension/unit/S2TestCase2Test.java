package org.seasar.extension.unit;

import java.util.Date;

import org.seasar.extension.unit.S2TestCase;

public class S2TestCase2Test extends S2TestCase {

	private static final String PATH = "bbb.dicon";
	private Date bbb_;
	
	public S2TestCase2Test(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(S2TestCase2Test.class);
	}

	public void setUp() {
		include(PATH);
	}
	
	public void testBindField() {
		assertNotNull("1", bbb_);
	}
	
	public void testEmptyComponent() {
		include("empty.dicon");
	}
}