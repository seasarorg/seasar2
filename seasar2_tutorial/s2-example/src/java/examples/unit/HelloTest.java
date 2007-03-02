package examples.unit;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.aop.interceptors.MockInterceptor;

public class HelloTest extends S2TestCase{
    private static String PATH = "examples/unit/Hello.dicon";
    private Hello hello;
    private MockInterceptor mi;
    public void testHello() throws Exception{
assertEquals("Hello", hello.greeting());
assertEquals("Hoge", hello.echo("test"));

hello.echo("Hello");
assertEquals(true, mi.isInvoked("echo"));
assertEquals("Hello", mi.getArgs("echo")[0]);
    }
    protected void setUp() throws Exception {
	  include(PATH);
    }
    protected void tearDown() throws Exception {
    }
}
