public class SecondThread extends Thread {
    private int id;
    private static int idCount;
    private boolean yes;
    public SecondThread() {
        this.id = ++idCount;
        setDaemon(true);
        //never use for resource management as they will be terminated when all User threads finish
    }

    public void run() {
        yes = true;
        while (yes) {
            System.out.println(id );
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
        }
    }
    public void NO() {
        yes = false;
    }

    public static void main(String[] args) {
        System.out.println("Hi from Main and my priority is " + Thread.currentThread().getPriority());
        SecondThread[] threads = new SecondThread[100];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new SecondThread();
            threads[i].start();
        }

        Thread mine = Thread.currentThread();
        try {
            mine.sleep(1000);
        } catch (InterruptedException e) {}
        for (int i = 0; i < threads.length; i++) {
            threads[i].NO();
        }
        System.out.println("Ending Main");

    }
    
}
