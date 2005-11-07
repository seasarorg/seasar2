package examples.aop.originalinterceptor;

public class HeavyProcess {
    public void heavy(){
    	try{
    		Thread.sleep(5000);
    	} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
    }
}