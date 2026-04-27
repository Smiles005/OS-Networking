
import java.util.*;

public class ThreadManor extends Thread {//thread pool class
    //threads either report back after each task or run a queue of tasks and report when empty
    private final Object MANAGER = new Object();//semaphore
    private ArrayList<Runnable> taskQueue;
    private ArrayList<Peasant> idleWorkers;
    private ArrayList<Peasant> busyWorkers;
    private final int MAX_WORKERS;
    public boolean keepWorking;
    private Peasant[] workers;

    public ThreadManor(){
        this(100);
    }

    public ThreadManor(int nThreads){
        this.MAX_WORKERS = nThreads;
        this.workers = new Peasant[nThreads];
        this.taskQueue = new ArrayList<>();
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
            busyWorkers.remove(worker);
            idleWorkers.add(worker);
            MANAGER.notifyAll(); // notify that a worker is now idle
        }
    }


    public void halt(){
        synchronized(MANAGER){
            keepWorking = false;
        }
    }

    public void run() {
        keepWorking = true;
        boolean localKeepWorking = true;
        ArrayList<Runnable> localTaskQueue;
        while (localKeepWorking) {
            synchronized (MANAGER) {
                while (taskQueue.isEmpty() && keepWorking) {
                    try {
                        MANAGER.wait(); // wait for tasks to be added
                    } catch (InterruptedException ie) {}
                }
                localKeepWorking = keepWorking;
                localTaskQueue = addAll(taskQueue);
                taskQueue.clear();
            }
            if (localKeepWorking){
                boolean workerAvailable = (idleWorkers.size()+ busyWorkers.size()) < MAX_WORKERS || !idleWorkers.isEmpty();
                while (!localTaskQueue.isEmpty() && workerAvailable){ { 
                    //workerAvailable = (idleWorkers.size()+ busyWorkers.size()) < MAX_WORKERS || !idleWorkers.isEmpty();
                    
                }

                /* Runnable task = localTaskQueue.remove(0);
                Peasant bob = new Peasant(this);
                bob.assignWork(task); */
            }
        }
    }
}