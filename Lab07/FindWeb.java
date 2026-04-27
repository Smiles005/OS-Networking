import java.io.*;
import java.util.*;

public class FindWeb {
    // Pull the hostname from the output.txt file that is in the format "Host: 10.120.138.112 - IP: 10.120.138.112" if the host is not an ip address, save it in a linked list and the ip in a separate linked list. print just those websites and ips. Then, for each hostname, try to connect to it on port 80 and print out the response code. If the response code is 200, print out the first 100 characters of the response body.
    public static void main(String[] args) {
        LinkedList<String> hostnames = new LinkedList<>();
        LinkedList<String> hostaddresses = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("output.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Host: ")) {
                    String[] parts = line.split(" - IP: ");
                    if (parts.length == 2) {
                        String hostname = parts[0].substring(6).trim();
                        String ip = parts[1].trim();
                        if (!hostname.equals(ip)) {
                            hostnames.add(hostname);
                            hostaddresses.add(ip);
                            System.out.println("Hostname: " + hostname + " - IP: " + ip);
                        }
                    }
                }
            }
            // Now, for each hostname, try to connect to it on port 80 and print out the response code. If the response code is 200, print out the first 100 characters of the response body.
            for (int i = 0; i < hostnames.size(); i++) {
                String hostname = hostnames.get(i);
                String ip = hostaddresses.get(i);
                try {
                    Socket socket = new Socket(hostname, 80);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    out.println("GET / HTTP/1.1");
                    out.println("Host: " + hostname);
                    out.println("Connection: Close");
                    out.println();
                    String responseLine;
                    StringBuilder response = new StringBuilder();
                    while ((responseLine = in.readLine()) != null) {
                        response.append(responseLine).append("\n");
                    }
                    String responseStr = response.toString();
                    if (responseStr.contains("200 OK")) {
                        System.out.println("Response code 200 for " + hostname);
                        int bodyIndex = responseStr.indexOf("\r\n\r\n");
                        if (bodyIndex != -1) {
                            String responseBody = responseStr.substring(bodyIndex + 4);
                            System.out.println("First 100 characters of response body: " + responseBody.substring(0, Math.min(100, responseBody.length())));
                        }
                    } else {
                        System.out.println("Response code not 200 for " + hostname);
                    }
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error connecting to " + hostname + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    } 

}