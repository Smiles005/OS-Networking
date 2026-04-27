
public class DeadLock {
    public static void main(String[] args) {
        FirstThread firstThread = new FirstThread();
        NinethThread ninethThread = new NinethThread();
        firstThread.setFoe(ninethThread);
        ninethThread.setFoe(firstThread);
        firstThread.start();
        ninethThread.start();
    }
}
