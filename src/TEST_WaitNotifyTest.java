import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TEST_WaitNotifyTest {

    public static void main(String[] args) throws Exception {
        SerialThread msg = new SerialThread();
//
        Thread.sleep(2000);
//        
//        Waiter waiter = new Waiter(msg);
//        new Thread(waiter,"waiter").start();
//        
////        TEST_Waiter waiter1 = new TEST_Waiter(msg);
////        new Thread(waiter1, "waiter1").start();
//        
//        Thread.sleep(3000);
//        
//        Notifier notifier = new Notifier(msg);
//        new Thread(notifier, "notifier").start();
//        System.out.println("All the threads are started");
        
        

        CyclicBarrier barrier = new CyclicBarrier(2);
        Waiter receiver = new Waiter(msg, barrier);
        Notifier sender = new Notifier(msg, barrier);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(receiver);
        executor.submit(sender);
    }

}