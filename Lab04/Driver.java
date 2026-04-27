
public class Driver {
    //In the Thread class, the methods stop, suspend, and resume are deprecated.  You can read the documentation hereLinks to an external site. to see why. In class, we developed a simple replacement for the stop method but did not pay attention to the suspend and resume methods.  Therefore, your task is to create a suitable replacement strategy for these methods that you think you can implement in most threads you might need to write.  In other words, it should be generic enough that you could implement it either through inheritance or copy & paste.
    

    //test peasant class
    public static void main(String[] args) {
        Peasant p = new Peasant();
        System.out.println("Peasant thread starting work...");
        p.start();
        try {
            Thread.sleep(50);                
        } catch (InterruptedException ie) {}
        System.out.println("Peasant will be on holiday for 2 seconds...");
        p.holiday();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
        p.whip();
        System.out.println("Peasant will work for a bit");
        try {
            Thread.sleep(50);                
        } catch (InterruptedException ie) {}
        p.strike();
        System.out.println("Peasant is on strike.");
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
        }
        System.err.println("Main thread is ending.");
    }
    

}
