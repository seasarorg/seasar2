package examples.aop.syncinterceptor;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class AopSyncClient {
	private String PATH = "examples/aop/syncinterceptor/SyncCalc.dicon";
	private Count _count = null;
	
	public void init() {
		S2Container container = S2ContainerFactory.create(PATH);
		_count = (Count) container.getComponent(Count.class);
	}
	
	public void start() {
		System.out.println("count: " + _count.get());

		Runnable r = new Runnable() {
			public void run() {
				_count.add();
			}
		};
		Thread[] thres = new Thread[5];
		for (int i=0; i<5; i++) {
			thres[i] = new Thread(r);
			thres[i].start();	
		}
		for (int i=0; i<5; i++) {
			try {
				thres[i].join();				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("count: " + _count.get());
	}
	
	public static void main(String[] args) {
		AopSyncClient asc = new AopSyncClient();
		asc.init();
		asc.start();
	}
}
