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

    public ThreadManor() {
        this(100);
    }

    public ThreadManor(int nThreads) {
        this.MAX_WORKERS = nThreads;
        this.workers = new Peasant[nThreads];
        this.taskQueue = new ArrayList<>();
        this.shack = new ArrayList<>();
        this.idleWorkers = new ArrayList<>();
        this.busyWorkers = new ArrayList<>();
    }

    public ThreadManor(int nThreads) {
        this.maxWorkers = nThreads;
        this.threads = new Peasant[nThreads];
        this.taskQueue = new ArrayList<>();
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
    
    public boolean workersAvailable() {
        return (currentWorkerCount < MAX_WORKERS || !idleWorkers.isEmpty());
    }
    public boolean workToDo(ArrayList<Runnable> local) {
        return !local.isEmpty() && !taskQueue.isEmpty();
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
                    try {
                        MANAGER.wait(); // wait for tasks to be added
                    } catch (InterruptedException ie) {}
                    
                }
                localKeepWorking = keepWorking;
                localTaskQueue.addAll(taskQueue);
                taskQueue.clear();
                idleWorkers.addAll(shack);
                busyWorkers.removeAll(shack);
                shack.clear();
            }
            if (localKeepWorking) {
                while (!localTaskQueue.isEmpty() && workerAvailable) {
                    {
                        // workerAvailable = (idleWorkers.size()+ busyWorkers.size()) < MAX_WORKERS ||
                        // !idleWorkers.isEmpty();
                        Peasant worker = null;
                        if (!idleWorkers.isEmpty()) {
                            worker = idleWorkers.remove(0);
                        } else {
                            worker = new Peasant(this);
                            worker.start();
                            currentWorkerCount++;
                        }
                        busyWorkers.add(worker);
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