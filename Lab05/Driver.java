public class Driver {
    
    public static void main(String[] args) {
        java.io.File[] roots = java.io.File.listRoots();
        if (roots != null && roots.length > 0) {
            java.io.File root = roots[0];

            java.io.File[] directories = root.listFiles(java.io.File::isDirectory);
            int size=directories.length;
            if (directories != null) {
                for (java.io.File dir : directories) {
                    if (dir == directories[size-1]) {//last thread
                        System.out.println("Driver completed starting threads for directories in roots \nNow starting the subdirectory checking threads:\n");
                        try {
                            fileDirectThread.join();
                        } catch (InterruptedException e) {}
                        
                    }
                }

                System.out.println("Driver completed starting threads for directories in roots \nNow starting the subdirectory checking threads:\n");
                FileDirect fileDirectThread = new FileDirect(directories[0].getAbsolutePath());
                fileDirectThread.start();
            }
        }

    }
}
    // Write a driver class that gets a list of all the roots on the machine from
    // the File class. Select one of those roots, and get a list of all the
    // directories from that root (i.e. the top-level directories in the partition).
    // Create and start an instance of your thread class for each directory.

   