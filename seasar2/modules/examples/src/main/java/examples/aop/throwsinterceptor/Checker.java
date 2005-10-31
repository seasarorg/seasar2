package examples.aop.throwsinterceptor;

public class Checker {
	public void check(String str) {
		if (str != null) {
			System.out.println(str);
		} else {
			throw new NullPointerException("null");
		}
	}
}