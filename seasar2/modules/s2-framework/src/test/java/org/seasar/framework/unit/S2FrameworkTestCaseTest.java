package org.seasar.framework.unit;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.seasar.framework.container.TooManyRegistrationRuntimeException;

public class S2FrameworkTestCaseTest extends S2FrameworkTestCase {

	private static final String J2EE_PATH = "S2FrameworkTestCaseTest_j2ee.dicon";
	private boolean testAaaSetUpInvoked_ = false;
	private Date date_;
	private String ccc_;
	private HashMap bbb_;
    private Date ddd_;
    private List list1;

	public void setUpAaa() {
		testAaaSetUpInvoked_ = true;
	}
	
	public void testAaa() {
		assertEquals("1", true, testAaaSetUpInvoked_);
	}
	
	public void tearDownAaa() {
		System.out.println("tearDownAaa");
	}
	
	public void setUpBbbTx() {
		include(J2EE_PATH);
	}

	public void testBbbTx() {
		System.out.println("testBbbTx");
	}
	
	public void setUpGetDataSource() {
		include(J2EE_PATH);
	}

	public void testGetDataSource() {
		assertNotNull("1", getDataSource());
	}
	
	public void setUpBindField() {
		include(J2EE_PATH);
		register(Date.class);
	}

	public void testBindField() {
		assertNotNull("1", date_);
	}
	
	public void setUpBindField2() {
		include("bbb.dicon");
	}
	
	public void testBindField2() {
		assertNotNull("1", bbb_);
        assertNotNull("2", ddd_);
	}
   
    public void setUpBindField3() {
        include("ccc.dicon");
    }
    
    public void testBindField3() {
        assertNotNull("1", list1);
    }
	
	public void testInclude() {
		include("aaa.dicon");
		try {
			getComponent(Date.class);
			fail("1");
		} catch (TooManyRegistrationRuntimeException ex) {
			System.out.println(ex);
		}
	}
	
	public void setUpIsAssignableFrom() {
		include("bbb.dicon");
	}
	
	public void testIsAssignableFrom() {
		assertEquals("1", "hoge", ccc_);
	}
    
    public void testEmptyComponent() {
        include("empty.dicon");
    }

	public static class Hoge {
		
		private String aaa;
		
		/**
		 * @return Returns the aaa.
		 */
		public String getAaa() {
			return aaa;
		}
		/**
		 * @param aaa The aaa to set.
		 */
		public void setAaa(String aaa) {
			this.aaa = aaa;
		}
	}
}
