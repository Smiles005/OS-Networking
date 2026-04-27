import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import csci1140.KeyboardReader;
/* 
import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import csci1140.*; */

public class Driver {
    //if 1k=1024, then 8k=8*1024=8192
    public final static int PORT = 8192;
    public final static String SERVER_IP = "10.120.82.35";
    public static void main(String[] args) {
        // Your code here to connect to the server, send your name, send the name of
        // this file, read the file into a string, send the string, and then read the
        // response from the server and write it to PartII.java
        Socket socket = part1();
        part2(socket);

    }

    public static Socket part1() {
        // Your code here to connect to the server
        Socket socket = null;
        // Connect to the server
        try {
            InetAddress server = InetAddress.getByName(SERVER_IP);
            socket = new Socket(server, PORT);
            KeyboardReader keyIn = new KeyboardReader();
            PrintWriter out = null;
            String username = null;

            try {
                username = keyIn.readLine("Enter your name: ");
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                out.println(username);
                out.flush();
                String fileName = "Driver.java";
                out.println(fileName);
                out.flush();
                
                String fileContent = new String(Files.readAllBytes(FileSystems.getDefault().getPath(fileName)));

                out.println(fileContent);
                out.flush();
                out.println("EOF");
                out.flush();

                
            } catch(IOException ioe) {
                ioe.printStackTrace(System.err);
            } finally {
                try {
                    // out.close();
                } catch(Exception e) {}
                
            }

            
        
        } catch(UnknownHostException uhe) {
            System.out.println("Sorry, I couldn't find the server "+SERVER_IP);
        } catch(IOException ioe) {
            ioe.printStackTrace(System.err);
        } finally {
        }
        return socket;
        // Send your name as a String to the server
        // Flush
        // Send the name of your Java file (yes, the one you are writing right now) as a
        // String to the server
        // Flush
        // Using the classes java.nio.file.FilesLinks to an external site., read the
        // entire file into a single String. (Yes, it's the one you are writing right
        // now. Stop asking that!) You will find the Path interface documentationLinks
        // to an external site. helpful.
        // Write the single String you read out to the server
        // Flush
        // Send the string EOF to the server (i.e., "EOF")
        // Flush
    }

    public static void part2(Socket socket) {
        // Your code here to read the response from the server and write it to
        // PartII.java, compile and run PartII.java
        BufferedReader in = null;
        
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                BufferedWriter fileOut = new BufferedWriter(new FileWriter("PartII.java"));
                String line;
                while((line = in.readLine()) != null) {
                    if(line.equals("EOF")) {
                        break;
                    }
                    fileOut.write(line);
                    fileOut.newLine();
                }
                fileOut.close();
                out.close();
                
            } catch(IOException ioe) {
                ioe.printStackTrace(System.err);
            } finally {
                
                try {
                    in.close();
                } catch(Exception e) {}
                try {
                    socket.close();
                } catch(Exception e){}
            }

    }
    /*
     * I have a server listening on port #8K. I will tell you the IP Address.
     * 
     * Write a Java class that carries out the following tasks:
     * 
     * Part I:
     * 
     * Connect to the server
     * Send your name as a String to the server
     * Flush
     * Send the name of your Java file (yes, the one you are writing right now) as a
     * String to the server
     * Flush
     * Using the classes java.nio.file.FilesLinks to an external site., read the
     * entire file into a single String. (Yes, it's the one you are writing right
     * now. Stop asking that!) You will find the Path interface documentationLinks
     * to an external site. helpful.
     * Write the single String you read out to the server
     * Flush
     * Send the string EOF to the server (i.e., "EOF")
     * Flush
     * Part II:
     * 
     * Next, the server will send you a Java file in response. The data will be in
     * strings, and after the last string is sent, the server will send the string
     * EOF.
     * Write all the strings the server sends you to a file called PartII.java,
     * except for the EOF string, of course.
     * When you receive EOF, close the output stream you are using for PartII.java
     * close the socket
     * Compile and run PartII.java
     * You will attach your code to this question, but only as a backup. I will use
     * the code you send to the server as your answer to the exam.
     */
}
