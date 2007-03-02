package examples.unit;

import org.seasar.extension.unit.S2TestCase;

public class HelloMockTest extends S2TestCase {

	    private static String PATH = "examples/unit/HelloMock.dicon";

	    private Hello hello ;

	    public void testHello() throws Exception{
	    assertEquals("Hello", hello.greeting());
	    assertEquals("Hoge", hello.echo("test"));
	    }
	    protected void setUp() throws Exception {
		     include(PATH);
	    }
}
