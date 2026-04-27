import java.io.*;
public class Driver {
//Write a thread class that stores two file names as instance data.  The names will indicate source and destination files.  The run method should copy the contents of the source file to the destination file. When the copy process is finished, print an appropriate message.
// Next, write a driver that creates at least three of the threads, using file names you provide.  After all the threads have been created, start all of them (we want the copies to be started simultaneously). Print a message from main, indicating that the copies are in progress.
// After all the copy threads have finished, print a message indicating that the copies are complete.
    public static void main(String[] args) {
        FileThread thread1 = new FileThread("source1.txt", "dest1.txt");
        FileThread thread2 = new FileThread("source2.txt", "dest2.txt");
        FileThread thread3 = new FileThread("source3.txt", "dest3.txt");

        thread1.start();
        thread2.start();
        thread3.start();

        System.out.println("File copy operations are in progress...");

        try {
        thread1.join();
        } catch (InterruptedException e) {}
        try {
            thread2.join();
        } catch (InterruptedException e) {}
        try {
            thread3.join();
        } catch (InterruptedException e) {}


        // use  after all threads are finished
        // while (thread1.isAlive() || thread2.isAlive() || thread3.isAlive()) {
        //     // wait for all threads to finish   
        //     try{
        //         Thread.sleep(100);
        //     }catch(InterruptedException e){}
            
        // }
        System.out.println("All file copy operations are complete.");
    }


}
