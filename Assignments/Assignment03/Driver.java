// Begin by prompting the user for a hostname.  If the hostname is not valid, re-prompt the user until a valid hostname is provided.
// Once you have a valid hostname, get all the IP Addresses that are associated with that host.
// Next, attempt to reach each of the addresses from each of the network interfaces on the machine.
// Finally, for each address, print a list of the network interfaces that could reach the server.
import java.io.*;
import java.net.*;
import java.util.*;
import csci1140.*;

public class Driver {
    public static void main(String[] args) {
        KeyboardReader scanner = new KeyboardReader();
        String hostname = "";
        InetAddress[] addresses = null;

        // Prompt the user for a valid hostname
        while (true) {
            
            hostname = scanner.readLine("Enter a hostname: ");
            try {
                addresses = InetAddress.getAllByName(hostname);
                break; // Exit loop if hostname is valid
            } catch (UnknownHostException e) {
                System.out.println("Invalid hostname. Please try again.");
            }
        }

        // Get all network interfaces on the machine
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            Map<InetAddress, List<String>> reachableInterfaces = new HashMap<>();

            // Check each address against each network interface
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();

                while (inetAddresses.hasMoreElements()) {
                    InetAddress localAddress = inetAddresses.nextElement();

                    for (InetAddress address : addresses) {
                        if (localAddress.isReachable(1000)) { // Check if the address is reachable
                            reachableInterfaces.computeIfAbsent(address, k -> new ArrayList<>()).add(networkInterface.getName());
                        }
                    }
                }
            }

            // Print the results
            for (InetAddress address : addresses) {
                List<String> interfacesList = reachableInterfaces.getOrDefault(address, Collections.emptyList());
                System.out.println("IP Address: " + address.getHostAddress());
                if (interfacesList.isEmpty()) {
                    System.out.println("  No network interfaces can reach this address.");
                } else {
                    System.out.println("  Reachable from interfaces: " + String.join(", ", interfacesList));
                }
            }
        } catch (SocketException e) {
            System.out.println("Error retrieving network interfaces: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error checking reachability: " + e.getMessage());
        }
    }
}