
public class NinethThread extends Thread {
    private final Object SEMAPHORE;
    private FirstThread foe;
    public NinethThread() {
        SEMAPHORE = "Nineth Thread SEMAPHORE";
    }
    public void setFoe(FirstThread foe) {
        this.foe = foe;
    }
    public void run() {
        System.out.println("Hello from the nineth thread!");
        while(true){
            synchronized (SEMAPHORE) {
                System.out.println("Nineth thread is doing something... \n #9 is tired");
                try {

                    Thread.sleep(1000);
                    foe.steal();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void steal() throws InterruptedException{
        synchronized (SEMAPHORE)  {
            System.out.println("Nineth thread's semaphore is stolen by the first thread...");
            Thread.sleep(1000);
        }
    }

    public static void main(String[] args) {
        NinethThread thread = new NinethThread();
        thread.start();
    }
}
