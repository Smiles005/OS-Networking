import java.io.*;
import java.lang.*;
import java.util.*;

public class ThreadManor extends Thread {// thread pool class
    // threads either report back after each task or run a queue of tasks and report
    // when empty
    private int currentWorkerCount = 0;
    private ArrayList<Peasant> idleWorkers;
    private ArrayList<Peasant> busyWorkers;
    private final Object MANAGER = new Object();// semaphore
    private ArrayList<Runnable> taskQueue;
    private ArrayList<Peasant> shack;
    private final int MAX_WORKERS;
    public boolean keepWorking;
    private Peasant[] workers;

    private static final String PROPERTIES_FILE = "threadmanor.properties";
    private static final String MAX_WORKERS_KEY = "max.workers";
    private static final String CAPABLE_WORKERS_KEY = "capable.workers";
    private static final int DEFAULT_MAX_WORKERS = 100;

    public ThreadManor() {
        this(loadMaxWorkersFromProperties());
    }

    private static int loadMaxWorkersFromProperties() {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE)) {
            prop.load(fis);
            int requested = -1;
            String v = prop.getProperty(MAX_WORKERS_KEY);
            if (v != null) {
                try {
                    requested = Integer.parseInt(v.trim());
                } catch (NumberFormatException nfe) {
                }
            }

            int capable = -1;
            String c = prop.getProperty(CAPABLE_WORKERS_KEY);
            if (c != null) {
                try {
                    capable = Integer.parseInt(c.trim());
                } catch (NumberFormatException nfe) {
                }
            }
            if (capable <= 0) {
                capable = Runtime.getRuntime().availableProcessors();
            }
            if (requested <= 0) {
                requested = DEFAULT_MAX_WORKERS;
            }
            return Math.min(requested, capable);
        } catch (IOException ioe) {
        }
        int capable = Runtime.getRuntime().availableProcessors();
        return Math.min(DEFAULT_MAX_WORKERS, capable);
    }

    public ThreadManor(int nThreads) {
        this.MAX_WORKERS = nThreads;
        this.workers = new Peasant[nThreads];
        this.taskQueue = new ArrayList<>();
        this.shack = new ArrayList<>();
        this.idleWorkers = new ArrayList<>();
        this.busyWorkers = new ArrayList<>();
    }

    public void ordersFromLord(Runnable task) {
        synchronized (MANAGER) {
            taskQueue.add(task);
            MANAGER.notifyAll(); // notify peasants that work is available
        }
    }

    public void imDoneLord(Peasant worker) {
        synchronized (MANAGER) {
            shack.add(worker);
            MANAGER.notifyAll(); // notify that a worker is now idle
        }
    }

    public void halt() {
        synchronized (MANAGER) {
            keepWorking = false;
        }
    }

    public int getQueueSize() {
        synchronized (MANAGER) {
            return taskQueue.size();
        }
    }

    public boolean workersAvailable() {
        return (currentWorkerCount < MAX_WORKERS || !idleWorkers.isEmpty() || !shack.isEmpty());
    }

    public boolean workToDo(ArrayList<Runnable> local) {
        return !local.isEmpty() || !taskQueue.isEmpty();
    }

    public void run() {
        keepWorking = true;
        boolean localKeepWorking = true;
        ArrayList<Runnable> localTaskQueue = new ArrayList<>();
        while (localKeepWorking) {
            synchronized (MANAGER) {
                while ((!workToDo(localTaskQueue) || !workersAvailable()) && keepWorking) {
                    // while (localTaskQueue.isEmpty() && taskQueue.isEmpty() && keepWorking) {
                    //
                    System.out.println("About to wait.");
                    try {
                        MANAGER.wait(); // wait for tasks to be added
                    } catch (InterruptedException ie) {
                    }

                }
                localKeepWorking = keepWorking;
                localTaskQueue.addAll(taskQueue);
                taskQueue.clear();
                idleWorkers.addAll(shack);
                busyWorkers.removeAll(shack);
                shack.clear();
            }
            if (localKeepWorking) {
                while (!localTaskQueue.isEmpty() && workersAvailable()) {
                    {
                        // workerAvailable = (idleWorkers.size()+ busyWorkers.size()) < MAX_WORKERS ||
                        // !idleWorkers.isEmpty();
                        Peasant worker = null;
                        if (!idleWorkers.isEmpty()) {
                            worker = idleWorkers.remove(0);
                        } else {
                            System.out.println("New worker");
                            worker = new Peasant(this);
                            worker.start();
                            currentWorkerCount++;
                        }
                        busyWorkers.add(worker);
                        System.out.println("Queue size: " + getQueueSize() + " cwc: " + currentWorkerCount);
                        worker.assignWork(localTaskQueue.remove(0));
                    }

                    /*
                     * Runnable task = localTaskQueue.remove(0);
                     * Peasant bob = new Peasant(this);
                     * bob.assignWork(task);
                     */
                }
            }
        }
        synchronized (MANAGER) {
            for (Peasant p : idleWorkers) {
                p.strike();
            }
            for (Peasant p : busyWorkers) {
                p.strike();
            }
            for (Peasant p : shack) {
                p.strike();
            }
        }
    }
}