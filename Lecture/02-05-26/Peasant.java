public class Peasant extends Thread {
    
    private boolean keepWorking;//halt flag
    private final Object POWER = new Object();//semaphore
    private Runnable workToBeDone;
    private ThreadManor lord;

    public Peasant(ThreadManor lord){
        this.lord = lord;
    }

    public void assignWork(Runnable work) {
        //assign work to this peasant
        synchronized (POWER) {
            this.workToBeDone = work;
            POWER.notifyAll(); // wake up the peasant if waiting
        }

    }
    
    
    public void run() {
        keepWorking = true;
        boolean localKeepWorking=true;
        Runnable localWorktoBeDone=null;
        while (localKeepWorking) {
            System.out.println("Working the land...");
            synchronized (POWER) {//be able to stop without giving up the lock
                while (workToBeDone == null && keepWorking) {
                    try {
                        POWER.wait(); // wait for work to be assigned
                    } catch (InterruptedException ie) {}
                }
                localKeepWorking = keepWorking;
                localWorktoBeDone = workToBeDone;
                workToBeDone = null; // clear the work after assigning
            }
            
            if (localWorktoBeDone != null && localKeepWorking) {
                localWorktoBeDone.run();
                //tell ThreadManor I'm free now
                localWorktoBeDone = null; // clear the work after done
                lord.imDoneLord(this);
            }
            
        }

    }
    public void strike() {
        synchronized (POWER) {
            System.out.println("Peasant is on strike and has power!");
            keepWorking = false;
            POWER.notifyAll(); // wake up the peasant if waiting
        }
    }
}
