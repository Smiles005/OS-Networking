public class Driver {
    //test MonitorDir class
    public static void main(String[] args) {
        MonitorDir monitor = new MonitorDir("."); // specify your directory path here
        monitor.start();
        try {
            Thread.sleep(10000); // Let it monitor for 60 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        monitor.halt();
    }
}
//Write a Thread that monitors the contents of a given directory, checking once each second.  Each time the contents are changed, display the contents to the terminal.