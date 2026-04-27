//Find a system log file in your profile that you can open and read - you do not need write access.
// Windows Users: Open Windows Explorer and type in %AppData% in the address bar.  This will open your profile.
// Unix/Linux users can find log files in ~/Library/Log, /Library/Log, or in /var/log
// Write a program that will read that log file and then print only the last 10 entries of the file to the screen.
// The program should handle situations where the log file contains fewer than 10 entries without crashing.

import java.io.*;
import java.util.*;

public class ReadLastTenLines {
    public static void main(String[] args) {

        String path = "C:\\Users\\isabe\\AppData\\Roaming\\Code\\logs\\20260122T101041\\sharedprocess.log";

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String line;
            Deque<String> tail = new ArrayDeque<>();
            while ((line = reader.readLine()) != null) {
                tail.addLast(line);
                if (tail.size() > 10) {
                    tail.removeFirst();
                }
            }
            for (String l : tail) {
                System.out.println(l);
            }
            System.out.println("Read last 10 lines from log file: " + path);
        } catch (Exception e) {
            System.err.println("Error reading from log file " + path + ": " + e.getMessage());
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
