import java.util.*;

public class Driver {
    public static void main(String[] args) {
        final Random rand = new Random();
        ThreadManor manor = new ThreadManor(12);
        manor.start();
        for (int i = 0; i < 500; i++) {
            int taskNum = i;
            manor.ordersFromLord(new Runnable(){
                int jobNum = 0;
                public void run() {
                    System.out.println("Doing task " + taskNum + " job " + jobNum);
                    
                    try {
                        Thread.sleep(rand.nextInt(100)); // Simulate work
                    } catch (InterruptedException e) {}
                }
            });
            

        }
    }
}
