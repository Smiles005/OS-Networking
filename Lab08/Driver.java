import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import csci1140.KeyboardReader;

public class Driver {

    public static void main(String[] args) {

        KeyboardReader kb = new KeyboardReader();

        // 1. Print all roots
        File[] roots = File.listRoots();
        System.out.println("Available roots:");
        for (File r : roots) {
            System.out.println(" - " + r.getAbsolutePath());
        }

        // 2. Prompt user for a valid root
        File currentDir = null;
        while (currentDir == null) {
            String selectedRootPath = kb.readLine("Enter the path of the selected root: ");
            File f = new File(selectedRootPath);
            if (f.exists() && f.isDirectory()) {
                currentDir = f;
            } else {
                System.out.println("Invalid root. Try again.");
            }
        }

        // 3. Main navigation loop
        boolean quit = false;

        while (!quit) {

            System.out.println("\nCurrent directory: " + currentDir.getAbsolutePath());

            // List subdirectories
            File[] subdirs = currentDir.listFiles(File::isDirectory);
            System.out.println("\nSubdirectories:");
            if (subdirs != null && subdirs.length > 0) {
                for (File d : subdirs) {
                    System.out.println(" - " + d.getName());
                }
            } else {
                System.out.println(" (none)");
            }

            // List executable files
            File[] files = currentDir.listFiles(File::isFile);
            System.out.println("\nExecutable files:");
            boolean foundExec = false;
            if (files != null) {
                for (File f : files) {
                    if (f.canExecute()) {
                        System.out.println(" * " + f.getName());
                        foundExec = true;
                    }
                }
            }
            if (!foundExec) {
                System.out.println(" (none)");
            }

            // 4. Prompt user for next action
            System.out.println("\nOptions:");
            System.out.println(" 1. Enter a subdirectory name");
            System.out.println(" 2. Go back one level");
            System.out.println(" 3. Execute a file");
            System.out.println(" 4. Find all PDFs in this directory and subdirectories");
            System.out.println(" 5. Follow a folder path");
            System.out.println(" 6. Quit");

            int choice = kb.readInt("Choose an option: ");

            switch (choice) {

                case 1: // enter subdirectory
                    String sub = kb.readLine("Enter subdirectory name: ");
                    File newDir = new File(currentDir, sub);
                    if (newDir.exists() && newDir.isDirectory()) {
                        currentDir = newDir;
                    } else {
                        System.out.println("Invalid subdirectory.");
                    }
                    break;

                case 2: // go back
                    File parent = currentDir.getParentFile();
                    if (parent != null) {
                        currentDir = parent;
                    } else {
                        System.out.println("Already at root; cannot go back.");
                    }
                    break;

                case 3: // execute file
                    String fname = kb.readLine("Enter executable file name: ");
                    File exec = new File(currentDir, fname);

                    if (!exec.exists()) {
                        System.out.println("File does not exist.");
                    } else if (!exec.canExecute()) {
                        System.out.println("File is not executable.");
                    } else {
                        try {
                            System.out.println("Running: " + exec.getAbsolutePath());
                            Process p = Runtime.getRuntime().exec(exec.getAbsolutePath());
                        } catch (IOException e) {
                            System.out.println("Error executing file: " + e.getMessage());
                        }
                    }
                    break;

                case 4: // find all PDFs
                    System.out.println("\nSearching for PDFs under: " + currentDir.getAbsolutePath());
                    findAllPdfs(currentDir, 0);
                    break;

                case 5: // follow a folder path
                    String path = kb.readLine("Enter full folder path: ");
                    File jump = new File(path);

                    if (jump.exists() && jump.isDirectory()) {
                        currentDir = jump;
                        System.out.println("Moved to: " + currentDir.getAbsolutePath());
                    } else {
                        System.out.println("Invalid path. Directory does not exist.");
                    }
                    break;

                case 6: // quit
                    quit = true;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }

        System.out.println("Goodbye.");
    }

    // ---------------- PDF SEARCH ----------------

    public static boolean findAllPdfs(File dir, int depth) {
        if (dir == null || !dir.exists()) return false;

        File[] files = dir.listFiles();
        if (files == null) return false;

        boolean containsPdf = false;

        // Check for PDFs in this folder
        for (File f : files) {
            if (f.isFile() && f.getName().toLowerCase().endsWith(".pdf")) {
                containsPdf = true;
            }
        }

        // Check subdirectories
        List<File> subdirs = new ArrayList<>();
        for (File f : files) {
            if (f.isDirectory()) {
                if (findAllPdfs(f, depth + 1)) {
                    containsPdf = true;
                    subdirs.add(f);
                }
            }
        }

        // Print this directory only if it or its subdirectories contain PDFs
        if (containsPdf) {
            printIndent(depth);
            System.out.println(dir.getName() + "  (depth " + depth + ")");

            // Print PDFs in this folder
            for (File f : files) {
                if (f.isFile() && f.getName().toLowerCase().endsWith(".pdf")) {
                    printIndent(depth + 1);
                    System.out.println(f.getName() + "  (depth " + (depth + 1) + ")");
                }
            }
        }

        return containsPdf;
    }

    private static void printIndent(int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("    ");
        }
    }
}
