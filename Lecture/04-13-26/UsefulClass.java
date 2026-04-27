
public class UsefulClass {
    private final Object SEMAPHORE;
    public UsefulClass() {
        SEMAPHORE = "Useful Class SEMAPHORE";
    }
    public void doSomething() {
        synchronized (SEMAPHORE) {
            System.out.println("Doing something useful...");
        }
    }
    public void doSomethingelse() {
        synchronized (SEMAPHORE) {
            System.out.println("Doing something else useful...");
        }
    }

}
