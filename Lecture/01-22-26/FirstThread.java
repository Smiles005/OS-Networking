public class FirstThread extends Thread {
    private int id;
    private static int idCount;
    public FirstThread() {
        id = idCount++;
    }

    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println("Hi From " + id );
            
        }
    }
    public static void main(String[] args) {
        System.out.println("About to create a threads!");
        FirstThread[] threads = new FirstThread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new FirstThread();
            
        }
        System.out.println("About to start threads!");
        for (int i = 0; i < 10; i++) {
            threads[i].start();
        }
        
        /*  for (int i = 0; i < 10; i++) {
            new FirstThread().start();
        } */
        System.out.println("Done");
    }
}