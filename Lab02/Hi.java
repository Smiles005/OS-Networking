import java.io.*;
import java.net.URL;
import csci1140.*;
// In addition to the standard Java I/O classes, use the class java.net.URL to complete the following tasks:

public class Hi {
    public static void main(String[] args) {
        boolean valid = false;
        while (!valid) {
        // Prompt the user for a URL
        KeyboardReader k = new KeyboardReader();
        String userInput1 = k.readLine("Please enter the URL: ");
        System.out.println("You entered: " + userInput1);
        // Open a stream to the supplied URL
        BufferedReader in = null;
        BufferedWriter out = null;
        try {
            URL url = new URL(userInput1);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            out = new BufferedWriter(new FileWriter("output.txt"));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.write(inputLine);
                out.newLine();
            }
            System.out.println("Contents saved to output.txt");
            valid = true;
        } catch (Exception e) {
            System.out.println("Invalid URL. Please try again.");
        }finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                System.out.println("Error closing streams.");
            }
        }
        // Read the contents of that stream as strings and save them to a local file.
        // If the supplied URL is not valid, inform the user and prompt for another URL.
        }
    }    
}
