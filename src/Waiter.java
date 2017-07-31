import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Waiter implements Runnable{
    
    private SerialThread msg;
    private CyclicBarrier barrier;
    
    public Waiter(SerialThread m, CyclicBarrier barrier) {
        this.barrier = barrier;
        this.msg=m;
    }

    @Override
    public void run() {
//        String name = Thread.currentThread().getName();
    	
        while(true){
//    		synchronized (msg) {
	            try{
	                Thread.sleep(50);
	            	SerialThread.sendMessage(randomMsg());
//	                msg.wait();
	                
	                // Then notify.
	                barrier.await();
	            } catch (Exception e) { 
					e.printStackTrace();
				} 
//        	}
        }
        	
    }
    
    private static String randomMsg(){
    	Random rnd = new Random();
		int rndInt = SerialThread.showRandomInteger(0, SerialThread.prtcl.length-1, rnd);
		return SerialThread.prtcl[rndInt];
    }

}