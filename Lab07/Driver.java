import java.net.*;
import java.io.*;
import java.util.*;

public class Driver {
    public static void main(String[] args) {
        String localHost = null;
        LinkedList<byte[]> hostBytes = new LinkedList<>();
        long startTime = System.currentTimeMillis();

        try {
            localHost = InetAddress.getLocalHost().getHostAddress();
            hostBytes.add(InetAddress.getByName(localHost).getAddress());
        } catch (UnknownHostException e) {
            System.out.println("Error resolving local host address: " + e.getMessage());
        }
        System.out.println("Local Host Address: " + localHost+"\n"+"The bytes of the local host address are: " + hostBytes.getFirst().length + " bytes"+ "\n\n"+"Scanning the local subnet for hostnames...");

   
        LinkedList<String> lvl1hostnames = new LinkedList<>();
        LinkedList<String> lvl1hostaddresses = new LinkedList<>();

        byte[] skipper = hostBytes.getFirst().clone();
        int skip = (int)skipper[3];
        for (int i = 1; i <= 255; i++) {
            // String hostAddress = localHost.substring(0, localHost.lastIndexOf('.')) + "." + i;
            byte[] hostAddressBytes = hostBytes.getFirst().clone();
            hostAddressBytes[3] = (byte) i; // Modify the last byte of the IP address
            try {
                String hostAddress = InetAddress.getByAddress(hostAddressBytes).getHostAddress();
                InetAddress inetAddress = InetAddress.getByName(hostAddress);
                if (inetAddress.isReachable(100)) { // Check if the host is reachable
                    lvl1hostnames.add(inetAddress.getHostName());
                    lvl1hostaddresses.add(hostAddress);
                    System.out.println("Host: " + lvl1hostnames.getLast() + " - IP: " + lvl1hostaddresses.getLast());
                }
            } catch (IOException e) {}
            if (i == (skip-1)){i++;}
        }


        long endTime = System.currentTimeMillis();
        System.out.println("Time taken lv1: " + (endTime - startTime) + " milliseconds \n \n"+"Scanning the subnet one level up for hostnames...");


        startTime = System.currentTimeMillis();
        LinkedList<String> lvl2hostnames = new LinkedList<>();
        LinkedList<String> lvl2hostaddresses = new LinkedList<>();
        
        skip = (int)skipper[2];
            
        for (int i = 1; i <= 255; i++) {
            // String hostAddress = localHost.substring(0, localHost.lastIndexOf('.')) + "." + i;
            byte[] hostAddressBytes = hostBytes.getFirst().clone();
            hostAddressBytes[2] = (byte) i; // Modify the last byte of the IP address
            for (int j = 1; j <= 255; j++) {
                hostAddressBytes[3] = (byte) j; // Modify the last byte of the IP address
                try {
                    String hostAddress = InetAddress.getByAddress(hostAddressBytes).getHostAddress();
                    InetAddress inetAddress = InetAddress.getByName(hostAddress);
                    if (inetAddress.isReachable(100)) { // Check if the host is reachable
                        lvl2hostnames.add(inetAddress.getHostName());
                        lvl2hostaddresses.add(hostAddress);
                        System.out.println("Host: " + lvl2hostnames.getLast() + " - IP: " + lvl2hostaddresses.getLast());
                    }
                } catch (IOException e) {}
            }
            if (i == (skip-1)){i++;}
        }
        
        
        endTime = System.currentTimeMillis();
        System.out.println("Time taken lv2: " + (endTime - startTime) + " milliseconds\\n\\nScanning the subnet one level up for hostnames...");
        
        startTime = System.currentTimeMillis();
        LinkedList<String> lvl3hostnames = new LinkedList<>();
        LinkedList<String> lvl3hostaddresses = new LinkedList<>();
        
        
        skip = (int)skipper[1];
        for (int i = 1; i <= 255; i++) {
            // String hostAddress = localHost.substring(0, localHost.lastIndexOf('.')) + "." + i;
            byte[] hostAddressBytes = hostBytes.getFirst().clone();
            hostAddressBytes[1] = (byte) i; // Modify the last byte of the IP address
            for (int j = 1; j <= 255; j++) {
                hostAddressBytes[2] = (byte) j; // Modify the last byte of the IP address
                for (int k = 1; k <= 255; k++) {
                    hostAddressBytes[3] = (byte) k; // Modify the last byte of the IP address
                    try {
                        String hostAddress = InetAddress.getByAddress(hostAddressBytes).getHostAddress();
                        InetAddress inetAddress = InetAddress.getByName(hostAddress);
                        if (inetAddress.isReachable(100)) { // Check if the host is reachable
                            lvl3hostnames.add(inetAddress.getHostName());
                            lvl3hostaddresses.add(hostAddress);
                            System.out.println("Host: " + lvl3hostnames.getLast() + " - IP: " + lvl3hostaddresses.getLast());
                        }
                    } catch (IOException e) {}
                }
            }
            if (i == (skip-1)){i++;}
        }


        endTime = System.currentTimeMillis();
        System.out.println("Time taken lv3: " + (endTime - startTime) + " milliseconds"+"\n\n"+"Scanning the subnet one level up for hostnames...");
        
    
        // LinkedList<String> lvl2hostnames = new LinkedList<>();
        // LinkedList<String> lvl2hostaddresses = new LinkedList<>();
        

        // for (int i = 1; i <= 255; i++) {
        //     String hostAddress = localHost.substring(0, localHost.lastIndexOf('.')) + "." + i;
        //     try {
        //         InetAddress inetAddress = InetAddress.getByName(hostAddress);
        //         if (inetAddress.isReachable(100)) { // Check if the host is reachable
        //             lvl2hostnames.add(inetAddress.getHostName());
        //             lvl2hostaddresses.add(hostAddress);
        //             System.out.println("Host: " + lvl2hostnames.getLast() + " - IP: " + lvl2hostaddresses.getLast());
        //         }
        //     } catch (IOException e) {}
        // }


        // long endTime = System.currentTimeMillis();
        // System.out.println("Time taken: " + (endTime - startTime) + " milliseconds");
        // System.out.println("Local Host Address: " + localHost);
    }
}
//For this lab, we will attempt to resolve hostnames for machines on our local networks. Start by discovering your assigned IPv4 address. Then, using that address as the starting point, write a program which will iterate through all 255 addresses on the local subnet, and print out the hostname for each machine that has one.Time the running of the application.  Do you think it would be feasible to discover hostnames on the subnet one level up from yours?  How about 2 levels up?  Try them out and see how long it takes.