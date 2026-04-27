import java.util.*;
import java.io.*;

public class MonitorDir extends Thread {
    private File directory;
    private ArrayList<String> previousContents;
    private boolean keepRunning;
    private final Object LOCK = new Object();//semophore

    public MonitorDir(String directoryPath) {
        try {
            this.directory = new File(directoryPath);
            if (!directory.isDirectory()) {
                throw new IllegalArgumentException("Provided path is not a directory.");
            }
        } catch (Exception e) {}
        this.previousContents = new ArrayList<>();
        this.keepRunning = true;
    }

    public void run() {
        boolean localKeepRunning = keepRunning;
        while (localKeepRunning) {
            synchronized (LOCK) {
                localKeepRunning = keepRunning;
            }
            ArrayList<String> currentContents = new ArrayList<>();
            String[] files = directory.list();
            if (files != null) {
                for (String file : files) {
                    currentContents.add(file);
                }
            }

            if (!previousContents.equals(currentContents)) {
                System.out.println("\nDirectory contents changed:");
                for (String file : currentContents) {
                    System.out.println(file);
                }
                previousContents = currentContents;
            }

            try {
                Thread.sleep(1000); // Check every second
            } catch (InterruptedException e) {}
        }

    }

    public void halt() {
        synchronized (LOCK) {
            keepRunning = false;
        }
    }
    
}
