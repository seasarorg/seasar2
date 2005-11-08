package examples.aop.syncinterceptor;

public class CountImpl implements Count {
	private int _count = 0;

	public void add() {
		int a = _count;

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		_count = a + 1;
		System.out.println(a);
	}

	public int get() {
		return _count;
	}

}
