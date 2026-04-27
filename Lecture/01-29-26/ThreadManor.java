public class ThreadManor {//thread pool class
    private final int nThreads;
    private final Peasant[] threads;
    private final TaskQueue taskQueue;

    public ThreadManor(int nThreads) {
        this.nThreads = nThreads;
        this.threads = new WorkerThread[nThreads];
        this.taskQueue = new TaskQueue();
    }
}