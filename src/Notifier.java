import java.io.IOException;
import java.util.concurrent.CyclicBarrier;

import javax.xml.bind.DatatypeConverter;

public class Notifier implements Runnable {

    private SerialThread msg;
    private CyclicBarrier barrier;
    
    public Notifier(SerialThread msg, CyclicBarrier barrier) {
        this.barrier = barrier;
        this.msg = msg;
    }

    @Override
    public void run() {
//        String name = Thread.currentThread().getName();
//        System.out.println(name+" started");
        try {
        	while (true){
//        		synchronized (msg) {
        			Thread.sleep(50);
	            	if (!SerialThread.hex_OutData.equals(SerialThread.ACK)){
	    				SerialThread.getMessage();
	            	}
 
	    			// Yatmislari oyat
	            	
//	                msg.notify(); 
	            	
	                // Then notify.
	                barrier.await();
//            	}
            }
        } catch (IllegalStateException e) { 
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		} catch (Exception e) { 
			e.printStackTrace();
		}
        
    }

}