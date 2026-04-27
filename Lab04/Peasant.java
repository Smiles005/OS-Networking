public class Peasant extends Thread {
    private boolean isSuppressed;//halt flag
    private boolean festival;//suspend flag
    private final Object power = new Object();//semaphore
    private final Object rest = new Object();//semaphore

    public Peasant() {
        isSuppressed = true;
        festival = false;
    }   
    public void run() {
        isSuppressed = true;
        boolean localIsSuppressed=true;
        while (localIsSuppressed) {
            //print to file
            System.out.println("Working the land...");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}

            synchronized (power) {//be able to stop without giving up the lock
                localIsSuppressed = isSuppressed;
                
            }
            synchronized (rest) {//be able to suspend without giving up the lock
                if (festival) {
                    try {
                        rest.wait();
                    } catch (InterruptedException e) {}
                }
            }
        }

    }
    public void strike() { //stop
        synchronized (power) {
            System.out.println("Peasant is on strike and has power!");
            isSuppressed = false;
        }
    }

    
    
    public void holiday() { //suspend
        synchronized (rest) {
            System.out.println("Peasant is on holiday!");
            festival = true;
        
        }
        
    }
    
    public void whip() { //resume from suspend
        synchronized (rest) {
            System.out.println("Peasant has resumed work!");
            festival = false;
            rest.notifyAll();
        }
    }
}
