import java.io.*;

public class FileThread extends Thread {
    private String sourceFileName;
    private String destinationFileName;

    public FileThread(String sourceFileName, String destinationFileName) {
        this.sourceFileName = sourceFileName;
        this.destinationFileName = destinationFileName;
    }
    public void run() {
        BufferedReader reader = null;
        PrintStream writer = null; 
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFileName)));
            writer = new PrintStream(new BufferedOutputStream(new FileOutputStream(destinationFileName)));
            String line;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
            }
            System.out.println("Copy from " + sourceFileName + " to " + destinationFileName + " completed.");
        } catch (Exception e) {
            System.err.println("Error copying from " + sourceFileName + " to " + destinationFileName + ": " + e.getMessage());
        }finally {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
            } catch (Exception e) {
                System.err.println("Error closing files: " + e.getMessage());
            }
        }
    }
    //Write a thread class that stores two file names as instance data.
    // The names will indicate source and destination files. 
    // The run method should copy the contents of the source file to the destination file and be able to use multiple threads to do so. 
    // When the copy process is finished, print an appropriate message.


    
}