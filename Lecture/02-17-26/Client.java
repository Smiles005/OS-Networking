import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String hostname = "10.120.82.35"; //"10.120.80.63";
        String localHost = "127.0.0.1";
        int port = 12345;
        try  {
            Socket socket = new Socket(hostname, port);
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.println("Hello from client!");
            out.println("This is a second message from client.");
            out.println("Die.");
            out.println("Frog.");
            out.println("This is a fifth message from client.");
            out.println("The quick brown fox jumps over the lazy dog.");
            out.println("How far can a dog run into the woods?  Halfway, because after that it is running out of the woods.");
            out.println("What client.");
            out.println("How are you server.");
            out.println("This is a tenth message from client.");
            
            out.println("We the people in order to form a more perfect union establish justice insure domestic tranquility provide for the common defense promote the general welfare and secure the blessings of liberty to ourselves and our posterity do ordain and establish this constitution for the United States of America.");
            out.println("In principium, Deus creavit caelum et terram.");
            out.println("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
            out.println("In the beginning, God created the heavens and the earth.");
            out.println("In the beginning was the Word, and the Word was with God, and the Word was God.");
            out.println("In principio erat Verbum, et Verbum erat apud Deum, et Deus erat Verbum.");
            out.println("Erat in principio apud Deum. Omnia per ipsum facta sunt, et sine ipso factum est nihil quod factum est. In ipso vita erat, et vita erat lux hominum. Et lux in tenebris lucet, et tenebrae eam non comprehenderunt.");
            out.flush();
            socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
            System.err.println("Error connecting to server: " + ioe.getMessage());
        }
    }
    
}
