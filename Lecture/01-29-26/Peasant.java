public class Peasant extends Thread {
    private boolean isSuppressed;//halt flag
    private final Object power = new Object();//semaphore
    public void run() {
        isSuppressed = true;
        boolean localIsSuppressed=true;
        while (localIsSuppressed) {
            System.out.println("Working the land...");
            synchronized (power) {//be able to stop without giving up the lock
                localIsSuppressed = isSuppressed;
            }
        }

    }
    public void strike() {
        synchronized (power) {
            System.out.println("Peasant is on strike and has power!");
            isSuppressed = false;
        }
    }
}
