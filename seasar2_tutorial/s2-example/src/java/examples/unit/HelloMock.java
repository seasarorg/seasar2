package examples.unit;

public class HelloMock implements Hello {

	public String greeting() {
		return "Hello";
	}

	public String echo(String str) {
		return "Hoge";
	}
}
