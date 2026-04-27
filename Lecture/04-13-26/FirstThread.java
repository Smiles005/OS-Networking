public class FirstThread extends Thread {
    private final Object SEMAPHORE;
    private NinethThread foe;
    public void run() {
        System.out.println("Hello from the first thread!");
        while(true){
            synchronized (SEMAPHORE) {
                System.out.println("First thread is doing something... \n #1 is tired");
                try {

                    Thread.sleep(9000);
                    foe.steal();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public FirstThread() {
        SEMAPHORE = "First Thread SEMAPHORE";
    }
    public void setFoe(NinethThread foe) {
        this.foe = foe;
    }
    public static void main(String[] args) {
        FirstThread thread = new FirstThread();
        thread.start();
    }
    public void steal() throws InterruptedException{
        synchronized (SEMAPHORE)  {
            System.out.println("First thread is doing something for someone else...");
            Thread.sleep(7000);
        }
    }
}