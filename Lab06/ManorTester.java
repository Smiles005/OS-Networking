import java.io.*;
import java.util.*;

public class ManorTester {
    private static Object HAY = new Object(); // semaphore for haystack

    public static void main(String[] args) {
        ThreadManor manager = new ThreadManor(25);
        manager.start();
        Runnable[] jobs = new Runnable[10];
        jobs[0] = new Runnable() {
            public void run() {
                Random random = new Random();
                for (int i = 0; i < random.nextInt(10, 50); i++) {
                    try {
                        System.out.println("Manager Watching");
                        Thread.sleep(random.nextInt(50));
                    } catch (InterruptedException ie) {
                    }
                    manager.ordersFromLord(jobs[5] = new Runnable() {
                        public void run() {
                            Random random = new Random();
                            try {
                                Thread.sleep(random.nextInt(500));
                            } catch (InterruptedException ie) {
                            }
                        }
                    });
                    System.out.println("Queue size: " + manager.getQueueSize());
                }
            }

        };
        jobs[1] = new Runnable() {
            public void run() {
                Random random = new Random();
                for (int i = 0; i < 100; i++) {
                    try {
                        System.out.println("Tending the livestock...");
                        Thread.sleep(random.nextInt(100));
                    } catch (InterruptedException ie) {
                    }
                    manager.ordersFromLord(jobs[6] = new Runnable() {
                        public void run() {
                            Random random = new Random();
                            System.out.println("Tending the livestock...");
                            String[] animals = { "cow", "pig", "chicken", "sheep", "goat", "escaped" };
                            try {
                                Thread.sleep(random.nextInt(500));
                                String animal = animals[random.nextInt(animals.length - 1)];
                                if (animal.equals("escaped")) {
                                    System.out.println("A worker is chasing an escaped animal!");
                                }
                            } catch (InterruptedException ie) {
                            }
                        }
                    });
                }
            }

        };
        jobs[2] = new Runnable() {
            public void run() {
                Random random = new Random();
                for (int i = 0; i < random.nextInt(10, 50); i++) {
                    try {
                        System.out.println("Working in the fields...");
                        Thread.sleep(random.nextInt(50));
                    } catch (InterruptedException ie) {
                    }
                    manager.ordersFromLord(jobs[7] = new Runnable() {
                        public void run() {
                            Random random = new Random();
                            String[] crops = { "wheat", "corn", "barley", "oats", "hay", "needle" };
                            String fileName = "Haystack.txt";

                            // make haystack in Haystack.txt and throw a needle in there, then have the
                            // worker look for it
                            synchronized (HAY) {
                                PrintStream writer = null;
                                try {
                                    writer = new PrintStream(new BufferedOutputStream(new FileOutputStream(fileName)));
                                    for (int j = 0; j < random.nextInt(200); j++) {
                                        String crop = crops[random.nextInt(crops.length)];
                                        writer.println(crop);
                                    }
                                    System.out.println("Created " + fileName);
                                } catch (Exception e) {
                                    System.err.println("Error creating " + fileName + ": " + e.getMessage());
                                } finally {
                                    try {
                                        if (writer != null)
                                            writer.close();
                                    } catch (Exception e) {
                                        System.err.println("Error closing files: " + e.getMessage());
                                    }
                                }
                            }

                            for (int j = 0; j < random.nextInt(100); j++) {
                                // make haystack in Haystack.txt and throw a needle in there, then have the
                                // worker look for it
                                String crop = crops[random.nextInt(crops.length)];
                                if (crop.equals("needle")) {
                                    System.out.println("Found the needle in the feilds!");
                                }
                            }

                        }
                    });
                }
            }

        };
        jobs[3] = new Runnable() {
            public void run() {
                Random random = new Random();
                for (int i = 0; i < 100; i++) {
                    try {
                        System.out.println("Cooking in the kitchen...");
                        Thread.sleep(random.nextInt(50));
                    } catch (InterruptedException ie) {
                    }
                    manager.ordersFromLord(jobs[8] = new Runnable() {
                        public void run() {
                            Random random = new Random();
                            try {
                                Thread.sleep(random.nextInt(500));
                            } catch (InterruptedException ie) {
                            }
                        }
                    });
                }
            }

        };

        jobs[4] = new Runnable() {
            public void run() {
                Random random = new Random();
                for (int i = 0; i < 100; i++) {
                    try {
                        System.out.println("Looking for needle in haystack...");
                        Thread.sleep(random.nextInt(50));
                    } catch (InterruptedException ie) {
                    }
                    manager.ordersFromLord(jobs[9] = new Runnable() {
                        public void run() {
                            Random random = new Random();
                            synchronized (HAY) {
                                String fileName = "Haystack.txt";
                                BufferedReader reader = null;
                                try {
                                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
                                    String line;
                                    boolean foundNeedle = false;
                                    while ((line = reader.readLine()) != null) {
                                        if (line.equals("needle")) {
                                            foundNeedle = true;
                                            break;
                                        }
                                    }
                                    if (foundNeedle) {
                                        System.out.println("Found the needle in the haystack!");
                                    } else {
                                        System.out.println("Could not find the needle in the haystack.");
                                    }
                                } catch (Exception e) {
                                    System.err.println("Error reading " + fileName + ": " + e.getMessage());
                                } finally {
                                    try {
                                        if (reader != null)
                                            reader.close();
                                    } catch (Exception e) {
                                        System.err.println("Error closing files: " + e.getMessage());
                                    }
                                }
                            }
                        }
                    });
                }

            }

        };
        
        Random random = new Random();
        for (int i = 0; i < random.nextInt(100,5000); i++) {
            try {
                Thread.sleep(random.nextInt(500));
            } catch (InterruptedException ie) {
            }
            manager.ordersFromLord(jobs[random.nextInt(0, jobs.length-1)]);
            System.out.println("Queue size: " + manager.getQueueSize());
        }
        try {
                Thread.sleep(random.nextInt(500));
        } catch (InterruptedException ie) {
        }
        manager.halt();

    }
}

// Random random = new Random();
// while(true) {
// try {
// Thread.sleep(random.nextInt(500));
// } catch(InterruptedException ie) {}
// manager.addWork(new Runnable() {
// public void run() {
// Random random = new Random();
// try {
// Thread.sleep(random.nextInt(500));
// } catch(InterruptedException ie) {}
// }
// });
// }