// import java.rmi.registry.*;
// import csci1140.*;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import csci1140.KeyboardReader;

public class Client {

    private Client() {}

    public static void main(String[] args) {

        String host = (args.length < 1) ? null : args[0];
        try {
            Registry registry = LocateRegistry.getRegistry( );
            // Registry registry = LocateRegistry.getRegistry("127.0.0.1", 80);
            System.out.println(registry);
            CacheHandler stub = (CacheHandler) registry.lookup("CacheHandler");
            KeyboardReader keyin = new KeyboardReader();


            String myName = keyin.readLine("Enter your name: ");

            // Partner you SEND to
            // String sendName = keyin.readLine("Enter the name of the person you send TO: ");
            // String sendIP = keyin.readLine("Enter their IP address: ");
            String test = keyin.readLine("Enter the URL you want to receive: ");
            //check for https:// and www. in test and add if not present
            if (!test.contains("www.")) {
                test = "www." + test;
            }
            if (!test.startsWith("http://") && !test.startsWith("https://")) {
                test = "http://" + test;
            }
            System.err.println("Requesting URL: " + test);
            URL web = new URL(test);

            /* String response = stub.sayHello();
            System.out.println("Response received: " + response); */
            //String response = stub.sendMessage(message);
            String response = stub.getURL(web);
            System.out.println("Response received: " + response);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
}
//modify the code so that you have a simple message-passing application.  The client should read a message from the keyboard and pass it to the server.  The server will then print the message, read a response from its keyboard, and return it to the client, which then displays the response.