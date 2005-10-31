package examples.aop.mockinterceptor;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.aop.interceptors.MockInterceptor;

public class HelloTest extends S2TestCase{
    //テストクラスと同じパッケージにあるのでパスが省略できる
    private static String PATH = "Hello.dicon";
	
    //変数の自動セット
    private Hello hello ;
	
    private MockInterceptor mi ;
	
    public void testHello() throws Exception{
		
		//diconファイルでインターフェイスのモックが正しく行われているか
		assertEquals("Hello", hello.greeting());
		assertEquals("Hoge", hello.echo("test"));
			
		hello.echo("Hello");
		//echo()メソッドが呼ばれたかどうか
		assertEquals(true, mi.isInvoked("echo"));
			
		//echo()メソッドの引数の値が"Hello"かどうか
		assertEquals("Hello", mi.getArgs("echo")[0]);
		
    }
	
    protected void setUp() throws Exception {
    	//S2Containerに対するinclude()メソッド
    	include(PATH);
    }

    protected void tearDown() throws Exception {
    }
	
    public HelloTest(String arg0) {
    	super(arg0);
    }

    public static void main(String[] args) {
    	junit.textui.TestRunner.run(HelloTest.class);
    }

}
